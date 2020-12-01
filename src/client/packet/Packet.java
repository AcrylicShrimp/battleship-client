package client.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Packet {
    public static class ServerHello {
        public final int clientId;
        public final String clientName;

        public ServerHello(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.clientName = PacketReader.string(buffer);
        }
    }

    public static class NotifyLobbyEnterLobby {
        public final int clientId;
        public final String clientName;

        public NotifyLobbyEnterLobby(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.clientName = PacketReader.string(buffer);
        }
    }

    public static class NotifyLobbyLeaveLobby {
        public final int clientId;

        public NotifyLobbyLeaveLobby(ByteBuffer buffer) {
            this.clientId = PacketReader.integer(buffer);
        }
    }

    public static class NotifyLobbyRoomCreated {
        public final int roomId;
        public final String roomName;

        public NotifyLobbyRoomCreated(ByteBuffer buffer) throws Exception {
            this.roomId = PacketReader.integer(buffer);
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class NotifyLobbyRoomRenamed {
        public final int roomId;
        public final String roomName;

        public NotifyLobbyRoomRenamed(ByteBuffer buffer) throws Exception {
            this.roomId = PacketReader.integer(buffer);
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class NotifyLobbyRoomRemoved {
        public final int roomId;

        public NotifyLobbyRoomRemoved(ByteBuffer buffer) {
            this.roomId = PacketReader.integer(buffer);
        }
    }

    public static class NotifyRoomEnterRoom {
        public final int clientId;
        public final String clientName;

        public NotifyRoomEnterRoom(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.clientName = PacketReader.string(buffer);
        }
    }

    public static class NotifyRoomLeaveRoom {
        public final int clientId;

        public NotifyRoomLeaveRoom(ByteBuffer buffer) {
            this.clientId = PacketReader.integer(buffer);
        }
    }

    public static class NotifyRoomRoomRenamed {
        public final String roomName;

        public NotifyRoomRoomRenamed(ByteBuffer buffer) throws Exception {
            this.roomName = PacketReader.string(buffer);
        }
    }

    public static class NotifyLobby {
        public final List<Client> clients;
        public final List<Room> rooms;

        public NotifyLobby(ByteBuffer buffer) throws Exception {
            this.clients = new ArrayList<>();
            int clientLength = PacketReader.integer(buffer);
            for (int index = 0; index < clientLength; ++index)
                this.clients.add(new Client(PacketReader.integer(buffer), PacketReader.string(buffer)));

            this.rooms = new ArrayList<>();
            int roomLength = PacketReader.integer(buffer);
            for (int index = 0; index < roomLength; ++index)
                this.rooms.add(new Room(PacketReader.integer(buffer), PacketReader.string(buffer)));
        }

        public static class Client {
            public final int clientId;
            public final String clientName;

            public Client(int clientId, String clientName) {
                this.clientId = clientId;
                this.clientName = clientName;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;
                Client client = (Client) o;
                return clientId == client.clientId;
            }

            @Override
            public int hashCode() {
                return this.clientId;
            }

            @Override
            public String toString() {
                return this.clientName;
            }
        }

        public static class Room {
            public final int roomId;
            public final String roomName;

            public Room(int roomId, String roomName) {
                this.roomId = roomId;
                this.roomName = roomName;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;
                Room room = (Room) o;
                return roomId == room.roomId;
            }

            @Override
            public int hashCode() {
                return this.roomId;
            }

            @Override
            public String toString() {
                return this.roomName;
            }
        }
    }

    public static class NotifyRoom {
        public final int roomId;
        public final String roomString;
        public final List<Client> clients;

        public NotifyRoom(ByteBuffer buffer) throws Exception {
            this.roomId = PacketReader.integer(buffer);
            this.roomString = PacketReader.string(buffer);
            this.clients = new ArrayList<>();
            int clientLength = PacketReader.integer(buffer);
            for (int index = 0; index < clientLength; ++index)
                this.clients.add(new NotifyRoom.Client(PacketReader.integer(buffer), PacketReader.string(buffer)));
        }

        public static class Client {
            public final int clientId;
            public final String clientName;

            public Client(int clientId, String clientName) {
                this.clientId = clientId;
                this.clientName = clientName;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (o == null || getClass() != o.getClass())
                    return false;
                Client client = (Client) o;
                return clientId == client.clientId;
            }

            @Override
            public int hashCode() {
                return this.clientId;
            }

            @Override
            public String toString() {
                return this.clientName;
            }
        }
    }

    public static class NotifyGameInit {
        public final boolean isBlue;

        public NotifyGameInit(ByteBuffer buffer) {
            this.isBlue = PacketReader.integer(buffer) != 0;
        }
    }

    public static class NotifyGameBegin {
    }

    public static class NotifyGameTurn {
        public final boolean isMyTurn;

        public NotifyGameTurn(ByteBuffer buffer) {
            this.isMyTurn = PacketReader.integer(buffer) != 0;
        }
    }

    public static class NotifyGameFireFriendly {
        public final int x;
        public final int y;
        public final boolean hit;

        public NotifyGameFireFriendly(ByteBuffer buffer) {
            this.x = PacketReader.integer(buffer);
            this.y = PacketReader.integer(buffer);
            this.hit = PacketReader.integer(buffer) != 0;
        }
    }

    public static class NotifyGameFireEnemy {
        public final int x;
        public final int y;
        public final boolean hit;

        public NotifyGameFireEnemy(ByteBuffer buffer) {
            this.x = PacketReader.integer(buffer);
            this.y = PacketReader.integer(buffer);
            this.hit = PacketReader.integer(buffer) != 0;
        }
    }

    public static class NotifyGameFireRejected {
    }

    public static class NotifyGameSet {
        public final boolean won;

        public NotifyGameSet(ByteBuffer buffer) {
            this.won = PacketReader.integer(buffer) != 0;
        }
    }

    public static class BroadcastChatNormal {
        public final int clientId;
        public final String clientName;
        public final String message;

        public BroadcastChatNormal(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.clientName = PacketReader.string(buffer);
            this.message = PacketReader.string(buffer);
        }
    }

    public static class BroadcastChatWhisper {
        public final int clientId;
        public final String clientName;
        public final String message;

        public BroadcastChatWhisper(ByteBuffer buffer) throws Exception {
            this.clientId = PacketReader.integer(buffer);
            this.clientName = PacketReader.string(buffer);
            this.message = PacketReader.string(buffer);
        }
    }

    public static class RejectEnterRoomNotFound {
        public RejectEnterRoomNotFound() {
        }
    }

    public static class RejectEnterRoomNotInRoom {
        public RejectEnterRoomNotInRoom() {
        }
    }
}
