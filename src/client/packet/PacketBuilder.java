package client.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface PacketBuilder {
    private static IntegerPacketBuilder pack(int value) {
        return new IntegerPacketBuilder(value);
    }

    private static StringPacketBuilder pack(String value) {
        return new StringPacketBuilder(value);
    }

    private static <T extends PacketBuilder> ListPacketBuilder<T> packBuilders(T[] values) {
        return new ListPacketBuilder<T>(values);
    }

    private static <T extends PacketBuilder> ListPacketBuilder<T> packBuilders(List<T> values) {
        return new ListPacketBuilder<T>(values);
    }

    private static ListPacketBuilder<IntegerPacketBuilder> packIntegers(Collection<Integer> values) {
        return packBuilders(values.stream().map(PacketBuilder::pack).toArray(IntegerPacketBuilder[]::new));
    }

    private static ListPacketBuilder<StringPacketBuilder> packStrings(Collection<String> values) {
        return packBuilders(values.stream().map(PacketBuilder::pack).toArray(StringPacketBuilder[]::new));
    }

    private static ByteBuffer build(int code) {
        return ByteBuffer.allocateDirect(Integer.BYTES * 2).putInt(code).putInt(0).flip();
    }

    private static ByteBuffer build(int code, PacketBuilder builder) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.BYTES * 2 + builder.size()).putInt(code).putInt(builder.size());
        builder.put(buffer);
        return buffer.flip();
    }

    private static ByteBuffer build(int code, PacketBuilder... builders) {
        int size = Arrays.stream(builders).map(builder -> builder.size()).reduce(0, Integer::sum);
        ByteBuffer buffer = ByteBuffer.allocateDirect(Integer.BYTES * 2 + size).putInt(code).putInt(size);

        for (PacketBuilder builder : builders)
            builder.put(buffer);

        return buffer.flip();
    }

    static ByteBuffer buildClientHello(String name) {
        return build(20001, pack(name));
    }

    static ByteBuffer buildRequestCreateRoom(String roomName) {
        return build(20101, pack(roomName));
    }

    static ByteBuffer buildRequestEnterRoom(int roomId) {
        return build(20102, pack(roomId));
    }

    static ByteBuffer buildRequestLeaveRoom() {
        return build(20103);
    }

    static ByteBuffer buildRequestRenameRoom(String roomName) {
        return build(20104, pack(roomName));
    }

    static ByteBuffer buildChatNormal(String message) {
        return build(40001, pack(message));
    }

    static ByteBuffer buildChatWhisper(int clientId, String message) {
        return build(40002, pack(clientId), pack(message));
    }

    int size();

    void put(ByteBuffer buffer);
}
