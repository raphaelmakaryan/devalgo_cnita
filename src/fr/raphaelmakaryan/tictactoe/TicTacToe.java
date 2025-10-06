package fr.raphaelmakaryan.tictactoe;

import java.util.*;

public class TicTacToe extends Admin {
    private int size = 3;
    private Cell[][] board;
    public boolean started = false;
    public List<String> players = new ArrayList<String>();
    public String whoPlayNow = "null";
    public String mode;
    public String[] listRepresentation = {" O ", " X "};

    public Player player1;
    public Player player2;
    public ArtificialPlayer bot1;
    public ArtificialPlayer bot2;

    public InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();
    public Tools tools = new Tools();
    public View view = new View();

    public TicTacToe() {
        this.board = new Cell[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    public void display() {
        view.println("Au tour de " + whoPlayNow);
        if (debugDisplayBoard) {
            view.println("-------------");
            for (int i = 0; i < this.size; i++) {
                view.print("|");
                for (int j = 0; j < this.size; j++) {
                    Cell c = this.board[i][j];
                    view.print(c.getRepresentation());
                    view.print("|");
                }
                System.out.print("\n");
                view.println("-------------");
            }
            tools.clearLine();
        }
        if (whoPlayNow.contains("J")) {
            getMoveFromPlayer(interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
        } else {
            getMoveFromPlayer("bot");
        }
    }

    public int[] returnValueUser(String choice) {
        String[] splitValeur = choice.split(" ");
        int[] valueUser = new int[2];
        for (int i = 0; i < splitValeur.length; i++) {
            valueUser[i] = Integer.parseInt(splitValeur[i]);
        }
        return valueUser;
    }

    public void getMoveFromPlayer(String choice) {
        if (!Objects.equals(choice, "bot")) {
            int[] valueUser = returnValueUser(choice);
            Cell[][] board = getBoard();
            Player player = getPlayerPlayNow();
            if (valueUser[0] > size || valueUser[1] > size || valueUser[0] < -1 || valueUser[1] < -1) {
                view.println("Vous etes sorti du tableau !");
                display();
            } else if (verificationHavePlayer(board, valueUser)) {
                view.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                setOwner(valueUser[0], valueUser[1], "player");
            }
        } else {
            int lineRandomBot = new Random().nextInt(0, 3);
            int columnRandomBot = new Random().nextInt(0, 3);
            int[] valueBot = returnValueUser(lineRandomBot + " " + columnRandomBot);
            if (verificationHavePlayer(board, valueBot)) {
                view.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                setOwner(valueBot[0], valueBot[1], "bot");
            }
        }
    }

    public void setOwner(int ligne, int colonne, String type) {
        Cell[][] board = getBoard();
        if (type.equals("player")) {
            Player player = getPlayerPlayNow();
            board[ligne][colonne].setRepresentation(player.getRepresentation());
        } else if (type.equals("bot")) {
            ArtificialPlayer bot = getBotPlayNow();
            board[ligne][colonne].setRepresentation(bot.getRepresentation());
        }
        play();
    }

    public boolean verificationHavePlayer(Cell[][] board, int[] valueUser) {
        String[] listPlayers = this.listRepresentation;
        for (int i = 0; i < listPlayers.length; i++) {
            Cell c = board[valueUser[0]][valueUser[1]];
            if (c.getRepresentation().equals(listPlayers[i])) {
                return true;
            }
        }
        return false;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void play() {
        isOver();
        if (!started) {
            randomPlayer();
            started = true;
        } else {
            nextPlayer();
        }
        display();
    }

    public void randomPlayer() {
        int value = new Random().nextInt(0, 1);
        if (players.get(0).contains("J") && players.get(1).contains("J")) {
            if (value == 0) {
                whoPlayNow = "J1";
            } else {
                whoPlayNow = "J2";
            }
        } else if (players.get(0).contains("J") && players.get(1).contains("B")) {
            if (value == 0) {
                whoPlayNow = "J1";
            } else {
                whoPlayNow = "B2";
            }
        } else if (players.get(0).contains("B") && players.get(1).contains("B")) {
            if (value == 0) {
                whoPlayNow = "B1";
            } else {
                whoPlayNow = "B2";
            }
        }
    }

    public void nextPlayer() {
        if (players.get(0).contains("J") && players.get(1).contains("J")) {
            if (whoPlayNow == "J1") {
                whoPlayNow = "J2";
            } else {
                whoPlayNow = "J2";
            }
        }
        if (players.get(0).contains("J") && players.get(1).contains("B")) {
            if (whoPlayNow == "J1") {
                whoPlayNow = "B1";
            } else {
                whoPlayNow = "J1";
            }
        }
        if (players.get(0).contains("B") && players.get(1).contains("B")) {
            if (whoPlayNow.equals("B1")) {
                whoPlayNow = "B2";
            } else {
                whoPlayNow = "B1";
            }
        }
    }

    public Player getPlayerPlayNow() {
        if (whoPlayNow.equals("J1")) {
            return player1;
        } else {
            return player2;
        }
    }

    public ArtificialPlayer getBotPlayNow() {
        if (whoPlayNow.equals("B1")) {
            return bot1;
        } else {
            return bot2;
        }
    }

    public void isOver() {
        if (!Objects.equals(whoPlayNow, "null")) {
            if (checkCellFilled() == 9) {
                view.println("Vous avez vous rempli !");
                System.exit(0);
            }

            if (checkWin()) {
                view.println("GG " + whoPlayNow);
                System.exit(0);
            }
        }
    }

    public int checkCellFilled() {
        int valueRempli = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[i][j].isEmpty()) {
                    valueRempli = valueRempli + 1;
                }
            }
        }
        return valueRempli;
    }

    public boolean checkWin() {
        int valueWin = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                Cell c = board[i][j];
                if (whoPlayNow.contains("J") && c.getRepresentation().equals(getPlayerPlayNow().representation)) {
                    valueWin = valueWin + 1;
                } else if (whoPlayNow.contains("B") && c.getRepresentation().equals(getBotPlayNow().representation)) {
                    valueWin = valueWin + 1;
                }
            }
        }
        if (valueWin == 3) {
            return true;
        } else {
            return false;
        }
    }

    public void chooseGame() {
        interactionUtilisateur.chooseGame(this);
    }

    public void createPlayer(int[] value) {
        for (int i = 0; i < value.length; i++) {
            if (value[i] == 10) {
                player1 = new Player(this, 1);
                players.add("J1");
            }
            if (value[i] == 11) {
                player2 = new Player(this, 2);
                players.add("J2");
            }
            if (value[i] == 20) {
                bot1 = new ArtificialPlayer(this, 1);
                players.add("B1");
            }
            if (value[i] == 21) {
                bot2 = new ArtificialPlayer(this, 2);
                players.add("B1");
            }
        }
        play();
    }
}