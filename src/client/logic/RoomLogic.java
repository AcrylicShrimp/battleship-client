package client.logic;

import client.Client;
import client.packet.Packet;
import client.packet.PacketBuilder;
import client.screen.Room;

import javax.swing.*;
import java.util.Vector;

public class RoomLogic extends Logic {
    private final Room room;
    private final Vector<Packet.NotifyRoom.Client> clients;
    private final Vector<String> chats;

    public RoomLogic(Client client, Packet.NotifyRoom packet) {
        super(client);
        this.room = new Room();
        this.clients = new Vector<>(packet.clients);
        this.chats = new Vector<>();

        this.client.screenManager.setContentPane(this.room);
        this.clients.add(new Packet.NotifyRoom.Client(this.client.clientId(), this.client.clientName()));
        this.chats.add(String.format("[SYSTEM] Your name is '%s'.", this.client.clientName()));
        this.chats.add(String.format("[SYSTEM] You are now in the room '%s'.", packet.roomString));
        this.room.playerList().setListData(this.clients);
        this.room.chatList().setListData(this.chats);

        this.room.onClickLeave(() -> {
            this.room.lock();
            this.client.send(PacketBuilder.buildRequestLeaveRoom());
        });
        this.room.onClickRename(() -> {
            this.room.lock();
            String roomName = JOptionPane.showInputDialog("Type a new name of this room.");

            if (roomName == null) {
                this.room.unlock();
                return;
            }

            roomName = roomName.trim();

            if (roomName.isEmpty()) {
                this.room.unlock();
                return;
            }

            this.client.send(PacketBuilder.buildRequestRenameRoom(roomName));
            this.room.unlock();
        });
        this.room.onClickStart(() -> {
//            this.room.lock();
//            this.client.send(PacketBuilder.buildRequestLeaveRoom());
        });
        this.room.onClickPlayer(index -> {
            this.room.playerList().clearSelection();
            this.room.msgField().setText(String.format("/w %d ", this.clients.get(index).clientId));
            this.room.msgField().grabFocus();
        });
        this.room.onChat(msg -> {
            msg = msg.trim();

            if (msg.isEmpty())
                return;

            if (msg.matches("^\\/w [+-]?\\d+ [\\s\\S]+$")) {
                String[] split = msg.split(" ", 3);

                if (split.length != 3) {
                    this.client.send(PacketBuilder.buildChatNormal(msg));
                    return;
                }

                int to = Integer.parseInt(split[1]);
                String splittedMsg = split[2].trim();

                if (splittedMsg.isEmpty())
                    this.client.send(PacketBuilder.buildChatNormal(msg));
                else
                    this.client.send(PacketBuilder.buildChatWhisper(to, splittedMsg));
            } else
                this.client.send(PacketBuilder.buildChatNormal(msg));
        });
    }

    @Override
    public void handleEvent(String type) {
        // Do nothing.
    }

    @Override
    public void handle(Packet.NotifyRoomEnterRoom packet) {
        this.clients.add(new Packet.NotifyRoom.Client(packet.clientId, packet.clientName));
        this.room.playerList().setListData(this.clients);
    }

    @Override
    public void handle(Packet.NotifyRoomLeaveRoom packet) {
        this.clients.remove(new Packet.NotifyRoom.Client(packet.clientId, null));
        this.room.playerList().setListData(this.clients);
    }

    @Override
    public void handle(Packet.NotifyRoomRoomRenamed packet) {
        this.chats.addElement(String.format("[SYSTEM] The room name is now '%s'.", packet.roomName));
        this.room.chatList().setListData(this.chats);
        this.room.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }

    @Override
    public void handle(Packet.NotifyLobby packet) {
        this.client.setLogic(new LobbyLogic(this.client, packet));
    }

    @Override
    public void handle(Packet.BroadcastChatNormal packet) {
        this.chats.addElement(String.format("[%s] %s", packet.clientName, packet.message));
        this.room.chatList().setListData(this.chats);
        this.room.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }

    @Override
    public void handle(Packet.BroadcastChatWhisper packet) {
        this.chats.addElement(String.format("(from %s) %s", packet.clientName, packet.message));
        this.room.chatList().setListData(this.chats);
        this.room.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }
}
