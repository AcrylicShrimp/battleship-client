package client.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Room implements HasContent, Lockable {
    private JPanel panel;
    private JList playerList;
    private JList chatList;
    private JTextField msgField;
    private JButton leaveButton;
    private JButton renameButton;
    private JButton startButton;

    public Room() {
        this.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.chatList.setSelectionModel(new NoSelectionModel());
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
        return 640;
    }

    @Override
    public int preferredHeight() {
        return 480;
    }

    @Override
    public void lock() {
        this.playerList.setEnabled(false);
        this.chatList.setEnabled(false);
        this.msgField.setEnabled(false);
        this.leaveButton.setEnabled(false);
        this.renameButton.setEnabled(false);
        this.startButton.setEnabled(false);
    }

    @Override
    public void unlock() {
        this.playerList.setEnabled(true);
        this.chatList.setEnabled(true);
        this.msgField.setEnabled(true);
        this.leaveButton.setEnabled(true);
        this.renameButton.setEnabled(true);
        this.startButton.setEnabled(true);
    }

    public void onClickLeave(Lobby.ClickEventHandler handler) {
        this.leaveButton.addActionListener(event -> handler.handle());
    }

    public void onClickRename(Lobby.ClickEventHandler handler) {
        this.renameButton.addActionListener(event -> handler.handle());
    }

    public void onClickStart(Lobby.ClickEventHandler handler) {
        this.startButton.addActionListener(event -> handler.handle());
    }

    public void onClickPlayer(Lobby.ClickPlayerEventHandler handler) {
        this.playerList.addListSelectionListener(event -> {
            handler.handle(event.getFirstIndex());
        });
    }

    public void onChat(Lobby.ChatEventHandler handler) {
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

    public interface ClickPlayerEventHandler {
        void handle(int index);
    }

    public interface ChatEventHandler {
        void handle(String msg);
    }
}
