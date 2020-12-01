package client.logic;

import client.Client;
import client.packet.Packet;
import client.packet.PacketBuilder;

public class ServerHelloLogic extends Logic {
    public ServerHelloLogic(Client client, String name) {
        super(client);
        this.client.send(PacketBuilder.buildClientHello(name));
    }

    @Override
    public void handleEvent(String type) {
        // Do nothing.
    }

    @Override
    public void handle(Packet.ServerHello packet) {
        this.client.setInfo(packet);
    }

    @Override
    public void handle(Packet.NotifyLobby packet) {
        this.client.setLogic(new LobbyLogic(this.client, packet));
    }
}
