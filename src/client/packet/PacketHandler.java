package client.packet;

import java.nio.ByteBuffer;

public class PacketHandler {
    public static void dispatch(int type, ByteBuffer buffer, PacketHandler handler) throws Exception {
        switch (type) {
            case PacketType.SERVER_HELLO:
                handler.handle(new Packet.ServerHello(buffer));
                break;
            case PacketType.NOTIFY_LOBBY_ENTER_LOBBY:
                handler.handle(new Packet.NotifyLobbyEnterLobby(buffer));
                break;
            case PacketType.NOTIFY_LOBBY_LEAVE_LOBBY:
                handler.handle(new Packet.NotifyLobbyLeaveLobby(buffer));
                break;
            case PacketType.NOTIFY_LOBBY_ROOM_CREATED:
                handler.handle(new Packet.NotifyLobbyRoomCreated(buffer));
                break;
            case PacketType.NOTIFY_LOBBY_ROOM_RENAMED:
                handler.handle(new Packet.NotifyLobbyRoomRenamed(buffer));
                break;
            case PacketType.NOTIFY_LOBBY_ROOM_REMOVED:
                handler.handle(new Packet.NotifyLobbyRoomRemoved(buffer));
                break;
            case PacketType.NOTIFY_ROOM_ENTER_ROOM:
                handler.handle(new Packet.NotifyRoomEnterRoom(buffer));
                break;
            case PacketType.NOTIFY_ROOM_LEAVE_ROOM:
                handler.handle(new Packet.NotifyRoomLeaveRoom(buffer));
                break;
            case PacketType.NOTIFY_ROOM_ROOM_RENAMED:
                handler.handle(new Packet.NotifyRoomRoomRenamed(buffer));
                break;
            case PacketType.NOTIFY_LOBBY:
                handler.handle(new Packet.NotifyLobby(buffer));
                break;
            case PacketType.NOTIFY_ROOM:
                handler.handle(new Packet.NotifyRoom(buffer));
                break;
            case PacketType.NOTIFY_GAME_INIT:
                handler.handle(new Packet.NotifyGameInit(buffer));
                break;
            case PacketType.NOTIFY_GAME_BEGIN:
                handler.handle(new Packet.NotifyGameBegin());
                break;
            case PacketType.NOTIFY_GAME_TURN:
                handler.handle(new Packet.NotifyGameTurn(buffer));
                break;
            case PacketType.NOTIFY_GAME_FIRE_FRIENDLY:
                handler.handle(new Packet.NotifyGameFireFriendly(buffer));
                break;
            case PacketType.NOTIFY_GAME_FIRE_ENEMY:
                handler.handle(new Packet.NotifyGameFireEnemy(buffer));
                break;
            case PacketType.NOTIFY_GAME_FIRE_REJECTED:
                handler.handle(new Packet.NotifyGameFireRejected());
                break;
            case PacketType.NOTIFY_GAME_SET:
                handler.handle(new Packet.NotifyGameSet(buffer));
                break;
            case PacketType.BROADCAST_CHAT_NORMAL:
                handler.handle(new Packet.BroadcastChatNormal(buffer));
                break;
            case PacketType.BROADCAST_CHAT_WHISPER:
                handler.handle(new Packet.BroadcastChatWhisper(buffer));
                break;
            case PacketType.REJECT_ENTER_ROOM_NOTFOUND:
                handler.handle(new Packet.RejectEnterRoomNotFound());
                break;
            case PacketType.REJECT_ENTER_ROOM_NOTINROOM:
                handler.handle(new Packet.RejectEnterRoomNotInRoom());
                break;
            default:
                break;
        }
    }

    public void handle(Packet.ServerHello packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyLobbyEnterLobby packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyLobbyLeaveLobby packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyLobbyRoomCreated packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyLobbyRoomRenamed packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyLobbyRoomRemoved packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyRoomEnterRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyRoomLeaveRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyRoomRoomRenamed packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyLobby packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyRoom packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameInit packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameBegin packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameTurn packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameFireFriendly packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameFireEnemy packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameFireRejected packet) {
        // Do nothing.
    }

    public void handle(Packet.NotifyGameSet packet) {
        // Do nothing.
    }

    public void handle(Packet.BroadcastChatNormal packet) {
        // Do nothing.
    }

    public void handle(Packet.BroadcastChatWhisper packet) {
        // Do nothing.
    }

    public void handle(Packet.RejectEnterRoomNotFound packet) {
        // Do nothing.
    }

    public void handle(Packet.RejectEnterRoomNotInRoom packet) {
        // Do nothing.
    }
}
