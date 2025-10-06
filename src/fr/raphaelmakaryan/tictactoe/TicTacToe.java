package fr.raphaelmakaryan.tictactoe;

public class TicTacToe {
    private int size = 3;
    private Cell[][] board; // tableau 2D de Cell
    Player player = new Player();
    Ui ui = new Ui();
    Tools tools = new Tools();

    public TicTacToe() {
        this.board = new Cell[this.size][this.size];
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                this.board[i][j] = new Cell();
            }
        }
    }

    public void display() {
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
        tools.clearLine();
        getMoveFromPlayer(ui.userInterfaceMessage("Quel case souhaiterez vous capturez ? (exemple : '1 1')"));
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

        if (valueUser[0] > size || valueUser[1] > size || valueUser[0] < -1 || valueUser[1] < -1) {
            System.out.println("Vous etes sorti du tableau !");
            display();
        } else if (verificationHavePlayer(board, valueUser)) {
            System.out.println("Vous avez choisi une case deja prise !");
            display();
        } else {
            setOwner(valueUser[0], valueUser[1], player);
        }
    }

    public void setOwner(int ligne, int colonne, Player player) {
        Cell[][] board = getBoard();
        board[ligne][colonne].setRepresentation(player.getRepresentation());
        display();
    }

    public boolean verificationHavePlayer(Cell[][] board, int[] valueUser) {
        String[] listPlayers = this.player.getListRepresentation();
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
}