package client.logic;

import client.Client;
import client.packet.Packet;
import client.packet.PacketBuilder;
import client.screen.Lobby;

import javax.swing.*;
import java.util.Vector;


public class LobbyLogic extends Logic {
    private final Lobby lobby;
    private final Vector<Packet.NotifyLobby.Client> clients;
    private final Vector<Packet.NotifyLobby.Room> rooms;
    private final Vector<String> chats;

    public LobbyLogic(Client client, Packet.NotifyLobby packet) {
        super(client);
        this.lobby = new Lobby();
        this.clients = new Vector<>(packet.clients);
        this.rooms = new Vector<>(packet.rooms);
        this.chats = new Vector<>();

        this.client.screenManager.setContentPane(this.lobby);
        this.clients.add(new Packet.NotifyLobby.Client(this.client.clientId(), this.client.clientName()));
        this.chats.add(String.format("[SYSTEM] Your name is '%s'.", this.client.clientName()));
        this.chats.add("[SYSTEM] You are now in the lobby.");
        this.lobby.roomList().setListData(this.rooms);
        this.lobby.playerList().setListData(this.clients);
        this.lobby.chatList().setListData(this.chats);

        this.lobby.onClickEnter(index -> {
            this.lobby.lock();
            this.client.send(PacketBuilder.buildRequestEnterRoom(this.rooms.get(index).roomId));
        });
        this.lobby.onClickCreate(() -> {
            this.lobby.lock();
            String roomName = JOptionPane.showInputDialog("Type a name of a new room.");

            if (roomName == null) {
                this.lobby.unlock();
                return;
            }

            roomName = roomName.trim();

            if (roomName.isEmpty()) {
                this.lobby.unlock();
                return;
            }

            this.client.send(PacketBuilder.buildRequestCreateRoom(roomName));
        });
        this.lobby.onClickPlayer(index -> {
            this.lobby.playerList().clearSelection();
            this.lobby.msgField().setText(String.format("/w %d ", this.clients.get(index).clientId));
            this.lobby.msgField().grabFocus();
        });
        this.lobby.onChat(msg -> {
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
    public void handle(Packet.NotifyLobbyEnterLobby packet) {
        this.clients.add(new Packet.NotifyLobby.Client(packet.clientId, packet.clientName));
        this.lobby.playerList().setListData(this.clients);
    }

    @Override
    public void handle(Packet.NotifyLobbyLeaveLobby packet) {
        this.clients.remove(new Packet.NotifyLobby.Client(packet.clientId, null));
        this.lobby.playerList().setListData(this.clients);
    }

    @Override
    public void handle(Packet.NotifyLobbyRoomCreated packet) {
        this.rooms.add(new Packet.NotifyLobby.Room(packet.roomId, packet.roomName));
        this.lobby.roomList().setListData(this.rooms);
    }

    @Override
    public void handle(Packet.NotifyLobbyRoomRenamed packet) {
        int index = this.rooms.indexOf(new Packet.NotifyLobby.Room(packet.roomId, null));

        if (index == -1)
            return;

        this.rooms.remove(index);
        this.rooms.add(index, new Packet.NotifyLobby.Room(packet.roomId, packet.roomName));
        this.lobby.roomList().setListData(this.rooms);
    }

    @Override
    public void handle(Packet.NotifyLobbyRoomRemoved packet) {
        this.rooms.remove(new Packet.NotifyLobby.Room(packet.roomId, null));
        this.lobby.roomList().setListData(this.rooms);
    }

    @Override
    public void handle(Packet.NotifyRoom packet) {
        this.client.setLogic(new RoomLogic(this.client, packet));
    }

    @Override
    public void handle(Packet.BroadcastChatNormal packet) {
        this.chats.addElement(String.format("[%s] %s", packet.clientName, packet.message));
        this.lobby.chatList().setListData(this.chats);
        this.lobby.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }

    @Override
    public void handle(Packet.BroadcastChatWhisper packet) {
        this.chats.addElement(String.format("(from %s) %s", packet.clientName, packet.message));
        this.lobby.chatList().setListData(this.chats);
        this.lobby.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }

    @Override
    public void handle(Packet.RejectEnterRoomNotFound packet) {
        this.lobby.unlock();
    }

    @Override
    public void handle(Packet.RejectEnterRoomNotInRoom packet) {
        // Do nothing.
    }
}
