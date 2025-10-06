package fr.raphaelmakaryan.tictactoe;

import java.util.Random;

public class TicTacToe {
    public boolean started = false;
    public int whoPlayNow;
    private int size = 3;
    private Cell[][] board; // tableau 2D de Cell
    public Player player1 = new Player(1);
    public Player player2 = new Player(2);
    public Ui ui = new Ui();
    public Tools tools = new Tools();

    public TicTacToe() {
        this.board = new Cell[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    public void display() {
        System.out.println("Au tour du joueur " + whoPlayNow);
        System.out.println("-------------");
        for (int i = 0; i < this.size; i++) {
            System.out.print("|");
            for (int j = 0; j < this.size; j++) {
                Cell c = this.board[i][j];
                System.out.print(c.getRepresentation());
                System.out.print("|");
            }
            System.out.print("\n");
            System.out.println("-------------");
        }
        verificationGame();
        tools.clearLine();
        getMoveFromPlayer(ui.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
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
        int[] valueUser = returnValueUser(choice);
        Cell[][] board = getBoard();
        Player player = getPlayerPlayNow();
        if (valueUser[0] > size || valueUser[1] > size || valueUser[0] < -1 || valueUser[1] < -1) {
            System.out.println("Vous etes sorti du tableau !");
            display();
        } else if (verificationHavePlayer(board, valueUser, player)) {
            System.out.println("Vous avez choisi une case deja prise !");
            display();
        } else {
            setOwner(valueUser[0], valueUser[1], player);
        }
    }

    public void setOwner(int ligne, int colonne, Player player) {
        Cell[][] board = getBoard();
        board[ligne][colonne].setRepresentation(player.getRepresentation());
        play();
    }

    public boolean verificationHavePlayer(Cell[][] board, int[] valueUser, Player player) {
        String[] listPlayers = player.getListRepresentation();
        for (int i = 0; i < listPlayers.length; i++) {
            Cell c = this.board[valueUser[0]][valueUser[1]];
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
        if (value == 0) {
            whoPlayNow = 1;
        } else {
            whoPlayNow = 2;
        }
    }

    public void nextPlayer() {
        if (whoPlayNow == 1) {
            whoPlayNow = 2;
        } else {
            whoPlayNow = 1;
        }
    }


    public Player getPlayerPlayNow() {
        if (whoPlayNow == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    public void verificationGame() {
        int valueRempli = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (!board[i][j].isEmpty()) {
                    valueRempli = valueRempli + 1;
                }
            }
        }
        if (valueRempli == 9) {
            System.out.println("GG tout est rempli");
            System.exit(0);
        }
    }
}