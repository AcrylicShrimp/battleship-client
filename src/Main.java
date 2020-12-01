import client.Client;
import client.LoopManager;
import client.screen.Connect;
import client.screen.ScreenManager;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        LoopManager loopManager = new LoopManager();
        ScreenManager screenManager = new ScreenManager();
        Client client = new Client(screenManager);

        Connect connectScreen = new Connect();
        connectScreen.onClickConnect((host, port, name) -> {
            connectScreen.lock();
            loopManager.execute(() -> {
                client.run(host, port, name);
                screenManager.setContentPane(connectScreen);
                connectScreen.unlock();
            });
        });
        screenManager.setContentPane(connectScreen);
        screenManager.show();

        for (; ; ) {
            loopManager.loop();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }
    }
}
