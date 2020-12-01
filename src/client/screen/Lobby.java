package client.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Lobby implements HasContent, Lockable {
    private JPanel panel;
    private JList roomList;
    private JList playerList;
    private JList chatList;
    private JTextField msgField;
    private JButton enterButton;
    private JButton createButton;

    public Lobby() {
        this.roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.chatList.setSelectionModel(new NoSelectionModel());
    }

    public JList roomList() {
        return this.roomList;
    }

    public JList playerList() {
        return this.playerList;
    }

    public JList chatList() {
        return this.chatList;
    }

    public JTextField msgField() {
        return this.msgField;
    }

    @Override
    public Container getContentPane() {
        return this.panel;
    }

    @Override
    public int preferredWidth() {
        return 800;
    }

    @Override
    public int preferredHeight() {
        return 600;
    }

    @Override
    public void lock() {
        this.roomList.setEnabled(false);
        this.playerList.setEnabled(false);
        this.chatList.setEnabled(false);
        this.msgField.setEnabled(false);
        this.enterButton.setEnabled(false);
        this.createButton.setEnabled(false);
    }

    @Override
    public void unlock() {
        this.roomList.setEnabled(true);
        this.playerList.setEnabled(true);
        this.chatList.setEnabled(true);
        this.msgField.setEnabled(true);
        this.enterButton.setEnabled(true);
        this.createButton.setEnabled(true);
    }

    public void onClickEnter(ClickEnterEventHandler handler) {
        this.enterButton.addActionListener(event -> {
            if (this.roomList.isSelectionEmpty())
                return;

            handler.handle(this.roomList.getSelectedIndex());
        });
    }

    public void onClickCreate(ClickEventHandler handler) {
        this.createButton.addActionListener(event -> handler.handle());
    }

    public void onClickPlayer(ClickPlayerEventHandler handler) {
        this.playerList.addListSelectionListener(event -> {
            handler.handle(event.getFirstIndex());
        });
    }

    public void onChat(ChatEventHandler handler) {
        this.msgField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Do nothing.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ENTER)
                    return;

                handler.handle(msgField.getText());
                msgField.setText("");
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Do nothing.
            }
        });
    }

    public interface ClickEventHandler {
        void handle();
    }

    public interface ClickEnterEventHandler {
        void handle(int index);
    }

    public interface ClickPlayerEventHandler {
        void handle(int index);
    }

    public interface ChatEventHandler {
        void handle(String msg);
    }
}
