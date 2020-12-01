package client.logic;

import client.Client;
import client.packet.PacketHandler;

public abstract class Logic extends PacketHandler {
    protected final Client client;

    public Logic(Client client) {
        this.client = client;
    }

    public abstract void handleEvent(String type);
}
