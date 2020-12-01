package client.screen;

import javax.swing.*;
import java.awt.*;

public class Connect
        implements HasContent, Lockable {
    private JPanel panel;
    private JTextField hostTextField;
    private JTextField portTextField;
    private JTextField nameTextField;
    private JButton connectButton;

    @Override
    public Container getContentPane() {
        return this.panel;
    }

    @Override
    public int preferredWidth() {
        return 400;
    }

    @Override
    public int preferredHeight() {
        return 200;
    }

    @Override
    public void lock() {
        this.hostTextField.setEnabled(false);
        this.portTextField.setEnabled(false);
        this.nameTextField.setEnabled(false);
        this.connectButton.setEnabled(false);
    }

    @Override
    public void unlock() {
        this.hostTextField.setEnabled(true);
        this.portTextField.setEnabled(true);
        this.nameTextField.setEnabled(true);
        this.connectButton.setEnabled(true);
    }

    public void onClickConnect(ClickEventHandler handler) {
        this.connectButton.addActionListener(event -> {
            handler.handle(this.hostTextField.getText(), Integer.parseInt(this.portTextField.getText()), this.nameTextField.getText());
        });
    }

    public interface ClickEventHandler {
        void handle(String host, int port, String name);
    }
}
