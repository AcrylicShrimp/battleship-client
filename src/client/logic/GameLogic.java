package client.logic;

import client.Client;
import client.packet.Packet;
import client.packet.PacketBuilder;
import client.screen.Game;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

class Helper {
    public static void removeShip(int[][] board, int size) {
        for (int x = 0; x < 10; ++x)
            for (int y = 0; y < 10; ++y)
                if (board[x][y] == size)
                    board[x][y] = 0;
    }

    public static boolean putShip(int[][] board, PacketBuilder.Ship ship, int size) {
        try {
            int x = ship.x;
            int y = ship.y;

            for (int index = 0; index < size; ++index) {
                if (board[x][y] != 0)
                    return false;

                board[x][y] = size;

                if (ship.rotated)
                    ++y;
                else
                    ++x;
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }
}

public class GameLogic extends Logic implements Game.ClickGridEventHandler {
    private final Game game;
    private final Vector<String> chats;
    private final int[][] board;
    private final PacketBuilder.Ship[] ships;
    private boolean isReady;
    private boolean isMyTurn;

    public GameLogic(Client client, Packet.NotifyGameInit packet) {
        super(client);
        this.game = new Game();
        this.chats = new Vector<>();

        this.client.screenManager.setContentPane(this.game);
        this.chats.add(String.format("[SYSTEM] Your name is '%s'.", this.client.clientName()));
        this.chats.add("[SYSTEM] You are now in the game!");
        this.game.chatList().setListData(this.chats);
        this.game.doneButton().setEnabled(false);
        this.game.placeVerticallyCheckBox().setEnabled(true);
        this.board = new int[10][10];
        this.ships = new PacketBuilder.Ship[4];
        this.ships[0] = null;
        this.ships[1] = null;
        this.ships[2] = null;
        this.ships[3] = null;
        this.isReady = false;
        this.isMyTurn = false;

        for (int[] row : this.board)
            Arrays.fill(row, 0);

        this.game.onClickDone(() -> {
            this.game.doneButton().setEnabled(false);
            this.game.placeVerticallyCheckBox().setEnabled(false);
            this.client.send(PacketBuilder.buildGameInit(this.ships));
        });
        this.game.onClickGrid(this);
        this.game.onChat(msg -> {
            msg = msg.trim();

            if (msg.isEmpty())
                return;

            if (msg.matches("^\\/w [+-]?\\d+ [\\s\\S]+$")) {
                String[] split = msg.split(" ", 3);

                if (split.length != 3) {
                    this.client.send(PacketBuilder.buildChatNormal(msg));
                    return;
                }

                int to = Integer.parseInt(split[1]);
                String splittedMsg = split[2].trim();

                if (splittedMsg.isEmpty())
                    this.client.send(PacketBuilder.buildChatNormal(msg));
                else
                    this.client.send(PacketBuilder.buildChatWhisper(to, splittedMsg));
            } else
                this.client.send(PacketBuilder.buildChatNormal(msg));
        });
    }

    @Override
    public void handleEvent(String type) {
        // Do nothing.
    }

    @Override
    public void handle(Packet.NotifyLobby packet) {
        this.client.setLogic(new LobbyLogic(this.client, packet));
    }

    @Override
    public void handle(Packet.NotifyGameBegin packet) {
        this.setShipColor(this.ships[0], 3, Color.DARK_GRAY);
        this.setShipColor(this.ships[1], 4, Color.DARK_GRAY);
        this.setShipColor(this.ships[2], 5, Color.DARK_GRAY);
        this.setShipColor(this.ships[3], 7, Color.DARK_GRAY);

        this.chats.addElement("[SYSTEM] Game started!");
        this.game.chatList().setListData(this.chats);
        this.game.chatList().ensureIndexIsVisible(this.chats.size() - 1);
        this.isReady = true;
    }

    @Override
    public void handle(Packet.NotifyGameTurn packet) {
        if (this.isMyTurn = packet.isMyTurn)
            this.chats.addElement("[SYSTEM] Get ready, it's your turn!");
        else
            this.chats.addElement("[SYSTEM] It's enemy's turn.");

        this.game.chatList().setListData(this.chats);
        this.game.chatList().ensureIndexIsVisible(this.chats.size() - 1);
        this.isReady = true;
    }

    @Override
    public void handle(Packet.NotifyGameFireFriendly packet) {
        this.game.setEnemyButtonColor(packet.x, packet.y, packet.hit ? Color.RED : Color.GRAY);
    }

    @Override
    public void handle(Packet.NotifyGameFireEnemy packet) {
        this.game.setFriendlyButtonColor(packet.x, packet.y, packet.hit ? Color.RED : Color.GRAY);
    }

    @Override
    public void handle(Packet.NotifyGameFireRejected packet) {
        JOptionPane.showMessageDialog(null, "Invalid position!");
    }

    @Override
    public void handle(Packet.NotifyGameSet packet) {
        JOptionPane.showMessageDialog(null, packet.won ? "Congratulations! You win!" : "It was close! You lose!");
    }

    @Override
    public void handle(Packet.BroadcastChatNormal packet) {
        this.chats.addElement(String.format("[%s] %s", packet.clientName, packet.message));
        this.game.chatList().setListData(this.chats);
        this.game.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }

    @Override
    public void handle(Packet.BroadcastChatWhisper packet) {
        this.chats.addElement(String.format("(from %s) %s", packet.clientName, packet.message));
        this.game.chatList().setListData(this.chats);
        this.game.chatList().ensureIndexIsVisible(this.chats.size() - 1);
    }

    @Override
    public void handleFriendly(int x, int y) {
        if (this.isReady)
            return;

        if (this.board[x][y] == 0) {
            if (this.ships[0] == null) {
                PacketBuilder.Ship ship = new PacketBuilder.Ship(x, y, this.game.placeVerticallyCheckBox().isSelected());

                if (Helper.putShip(this.board, ship, 3)) {
                    this.ships[0] = ship;
                    this.setShipColor(ship, 3, Color.BLUE);
                } else {
                    Helper.removeShip(board, 3);
                    JOptionPane.showMessageDialog(null, "Invalid position!");
                }
            } else if (this.ships[1] == null) {
                PacketBuilder.Ship ship = new PacketBuilder.Ship(x, y, this.game.placeVerticallyCheckBox().isSelected());

                if (Helper.putShip(this.board, ship, 4)) {
                    this.ships[1] = ship;
                    this.setShipColor(ship, 4, Color.ORANGE);
                } else {
                    Helper.removeShip(board, 4);
                    JOptionPane.showMessageDialog(null, "Invalid position!");
                }
            } else if (this.ships[2] == null) {
                PacketBuilder.Ship ship = new PacketBuilder.Ship(x, y, this.game.placeVerticallyCheckBox().isSelected());

                if (Helper.putShip(this.board, ship, 5)) {
                    this.ships[2] = ship;
                    this.setShipColor(ship, 5, Color.MAGENTA);
                } else {
                    Helper.removeShip(board, 5);
                    JOptionPane.showMessageDialog(null, "Invalid position!");
                }
            } else if (this.ships[3] == null) {
                PacketBuilder.Ship ship = new PacketBuilder.Ship(x, y, this.game.placeVerticallyCheckBox().isSelected());

                if (Helper.putShip(this.board, ship, 7)) {
                    this.ships[3] = ship;
                    this.setShipColor(ship, 7, Color.GRAY);
                } else {
                    Helper.removeShip(board, 7);
                    JOptionPane.showMessageDialog(null, "Invalid position!");
                }
            }

            this.game.doneButton().setEnabled(this.ships[0] != null && this.ships[1] != null && this.ships[2] != null && this.ships[3] != null);
        } else {
            switch (this.board[x][y]) {
                case 3 -> {
                    Helper.removeShip(board, 3);
                    this.setShipColor(this.ships[0], 3, Color.WHITE);
                    this.ships[0] = null;
                    this.game.doneButton().setEnabled(false);
                }
                case 4 -> {
                    Helper.removeShip(board, 4);
                    this.setShipColor(this.ships[1], 4, Color.WHITE);
                    this.ships[1] = null;
                    this.game.doneButton().setEnabled(false);
                }
                case 5 -> {
                    Helper.removeShip(board, 5);
                    this.setShipColor(this.ships[2], 5, Color.WHITE);
                    this.ships[2] = null;
                    this.game.doneButton().setEnabled(false);
                }
                case 7 -> {
                    Helper.removeShip(board, 7);
                    this.setShipColor(this.ships[3], 7, Color.WHITE);
                    this.ships[3] = null;
                    this.game.doneButton().setEnabled(false);
                }
            }
        }
    }

    @Override
    public void handleEnemy(int x, int y) {
        if (this.isReady && this.isMyTurn)
            this.client.send(PacketBuilder.buildGameFire(x, y));
    }

    private void setShipColor(PacketBuilder.Ship ship, int size, Color color) {
        int x = ship.x;
        int y = ship.y;

        for (int index = 0; index < size; ++index) {
            this.game.setFriendlyButtonColor(x, y, color);

            if (ship.rotated)
                ++y;
            else
                ++x;
        }
    }
}
