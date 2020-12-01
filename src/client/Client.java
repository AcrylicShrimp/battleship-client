package client;

import client.logic.Logic;
import client.logic.ServerHelloLogic;
import client.packet.Packet;
import client.packet.PacketBodyReader;
import client.packet.PacketHandler;
import client.screen.ScreenManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;

public class Client {
    public final ScreenManager screenManager;
    private Selector selector;
    private SocketChannel channel;
    private ArrayDeque<ByteBuffer> sendQueue;
    private PacketBodyReader reader;
    private Logic logic;
    private int id;
    private String name;

    public Client(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public SocketChannel channel() {
        return this.channel;
    }

    public String name() {
        return this.name;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public void setInfo(Packet.ServerHello packet) {
        this.id = packet.clientId;
        this.name = packet.clientName;
    }

    public int clientId() {
        return this.id;
    }

    public String clientName() {
        return this.name;
    }

    public void run(String host, int port, String name) {
        try {
            this.channel = SocketChannel.open();
            this.channel.configureBlocking(false);

            if (!this.channel.connect(new InetSocketAddress(host, port)))
                for (; ; ) {
                    if (this.channel.finishConnect())
                        break;

                    Thread.sleep(5);
                }

            this.sendQueue = new ArrayDeque<>();
            this.reader = new PacketBodyReader();
            this.logic = new ServerHelloLogic(this, name);
            this.selector = Selector.open();
            this.channel.register(this.selector, SelectionKey.OP_READ);

            for (; ; ) {
                if (this.selector.selectNow() != 0) {
                    Iterator<SelectionKey> iterator = this.selector.selectedKeys().iterator();

                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        if (key.isReadable()) {
                            PacketBodyReader.Result result = this.reader.read(this.channel);

                            if (result != null)
                                PacketHandler.dispatch(result.type, result.buffer, this.logic);
                        }

                        iterator.remove();
                    }
                }

                this.push();
                Thread.yield();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.disconnect();
        }
    }

    public void send(ByteBuffer buffer) {
        this.sendQueue.addLast(buffer.slice());
    }

    public void disconnect() {
        try {
            if (this.selector != null)
                this.selector.close();
            this.channel.close();
        } catch (IOException e) {
            // Do nothing.
        }
    }

    private void push() {
        try {
            while (!this.sendQueue.isEmpty()) {
                ByteBuffer buffer = this.sendQueue.getFirst();
                this.channel.write(buffer);

                if (buffer.hasRemaining())
                    break;

                this.sendQueue.removeFirst();
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.disconnect();
        }
    }
}
