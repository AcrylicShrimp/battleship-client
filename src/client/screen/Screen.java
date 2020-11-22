package client.screen;

import client.ClientState;

public abstract class Screen<T extends ClientState> {
    public abstract void onInit(T state);
    public abstract void onFin(T state);
}
