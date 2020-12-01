package client.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements HasContent, Lockable {
    private final JButton[][] friendlyButtons;
    private final JButton[][] enemyButtons;
    private JPanel panel;
    private JList chatList;
    private JTextField msgField;
    private JPanel friendlyBoard;
    private JPanel enemyBoard;
    private JButton doneButton;
    private JCheckBox placeVerticallyCheckBox;

    public Game() {
        this.chatList.setSelectionModel(new NoSelectionModel());
        this.friendlyBoard.setLayout(new GridLayout(10, 10));
        this.enemyBoard.setLayout(new GridLayout(10, 10));

        this.friendlyButtons = new JButton[10][10];
        this.enemyButtons = new JButton[10][10];

        for (int y = 0; y < 10; ++y)
            for (int x = 0; x < 10; ++x) {
                JButton friendlyButton = this.friendlyButtons[x][y] = new JButton();
                JButton enemyButton = this.enemyButtons[x][y] = new JButton();
                friendlyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
                enemyButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));
                friendlyButton.setBackground(Color.WHITE);
                enemyButton.setBackground(Color.WHITE);
                this.friendlyBoard.add(friendlyButton);
                this.enemyBoard.add(enemyButton);
            }
    }

    public JList chatList() {
        return this.chatList;
    }

    public JButton doneButton() {
        return this.doneButton;
    }

    public JCheckBox placeVerticallyCheckBox() {
        return this.placeVerticallyCheckBox;
    }

    @Override
    public Container getContentPane() {
        return this.panel;
    }

    @Override
    public int preferredWidth() {
        return 1000;
    }

    @Override
    public int preferredHeight() {
        return 600;
    }

    @Override
    public void lock() {
        this.chatList.setEnabled(false);
        this.msgField.setEnabled(false);
        for (int x = 0; x < 10; ++x)
            for (int y = 0; y < 10; ++y) {
                this.friendlyButtons[x][y].setEnabled(false);
                this.enemyButtons[x][y].setEnabled(false);
            }
    }

    @Override
    public void unlock() {
        this.chatList.setEnabled(true);
        this.msgField.setEnabled(true);
        for (int x = 0; x < 10; ++x)
            for (int y = 0; y < 10; ++y) {
                this.friendlyButtons[x][y].setEnabled(true);
                this.enemyButtons[x][y].setEnabled(true);
            }
    }

    public void setFriendlyButtonColor(int x, int y, Color color) {
        this.friendlyButtons[x][y].setBackground(color);
    }

    public void setEnemyButtonColor(int x, int y, Color color) {
        this.enemyButtons[x][y].setBackground(color);
    }

    public void onClickDone(ClickEventHandler handler) {
        this.doneButton.addActionListener(event -> handler.handle());
    }

    public void onClickGrid(ClickGridEventHandler handler) {
        for (int x = 0; x < 10; ++x)
            for (int y = 0; y < 10; ++y) {
                int finalX = x;
                int finalY = y;
                this.friendlyButtons[x][y].addActionListener(event -> handler.handleFriendly(finalX, finalY));
                this.enemyButtons[x][y].addActionListener(event -> handler.handleEnemy(finalX, finalY));
            }
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

    public interface ClickGridEventHandler {
        void handleFriendly(int x, int y);

        void handleEnemy(int x, int y);
    }

    public interface ChatEventHandler {
        void handle(String msg);
    }
}
