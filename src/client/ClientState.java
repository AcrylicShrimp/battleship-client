package client;

public abstract class ClientState {
    public final int id;
    public final String name;

    public ClientState(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
