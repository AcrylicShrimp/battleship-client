package client.screen;

import javax.swing.*;

public class ScreenManager {
    private JFrame frame;

    public ScreenManager() {
        this.frame = new JFrame();
        this.frame.setTitle("battleship-client");
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public void setContentPane(HasContent content) {
        this.frame.setContentPane(content.getContentPane());
        this.frame.setSize(content.preferredWidth(), content.preferredHeight());
    }
}
