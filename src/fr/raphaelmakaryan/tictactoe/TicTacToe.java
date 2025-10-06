package fr.raphaelmakaryan.tictactoe;

import java.util.Random;

public class TicTacToe extends Admin {
    private int size = 3;
    private Cell[][] board;
    public boolean started = false;
    public int whoPlayNow;

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
        view.println("Au tour du joueur " + whoPlayNow);
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
        getMoveFromPlayer(interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
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
            view.println("Vous etes sorti du tableau !");
            display();
        } else if (verificationHavePlayer(board, valueUser, player)) {
            view.println("Vous avez choisi une case deja prise !");
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

    public void isOver() {
        if (checkCellFilled() == 9) {
            view.println("Vous avez vous rempli !");
            System.exit(0);
        }

        if (checkWin()) {
            view.println("GG joueur" + whoPlayNow);
            System.exit(0);
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
                if (c.getRepresentation().equals(getPlayerPlayNow().representation)) {
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
                player1 = new Player(1);
            } else if (value[i] == 11) {
                player2 = new Player(2);
            } else if (value[i] == 20) {
                bot1 = new ArtificialPlayer();
            } else if (value[i] == 21) {
                bot2 = new ArtificialPlayer();
            }
        }
        play();
    }
}