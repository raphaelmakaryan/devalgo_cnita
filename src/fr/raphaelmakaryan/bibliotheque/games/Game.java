package fr.raphaelmakaryan.bibliotheque.games;

import fr.raphaelmakaryan.bibliotheque.configurations.*;
import fr.raphaelmakaryan.bibliotheque.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Game {
    protected int size;
    protected int victoryValue;
    protected Cell[][] board;
    protected boolean started;
    protected String whoPlayNow;
    public String[] listRepresentation;
    public String mode;

    protected List<String> players = new ArrayList<>();
    protected Player player1;
    protected Player player2;
    protected ArtificialPlayer bot1;
    protected ArtificialPlayer bot2;

    protected Tools tools = new Tools();
    protected View view = new View();
    protected InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();

    public Game(int size, int victoryValue) {
        this.size = size;
        this.victoryValue = victoryValue;
        this.board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = new Cell();
            }
        }
        this.started = false;
        this.whoPlayNow = "null";
        this.listRepresentation = new String[]{" O ", " X "};
    }

    public void chooseGame() {
        String value = interactionUtilisateur.chooseGame();
        switch (value) {
            case "tictactoe":
                TicTacToe ticTacToe = new TicTacToe(3, 3);
                ticTacToe.setGameAll(this);
                ticTacToe.setGameTTT(ticTacToe);
                interactionUtilisateur.chooseGameTicTacToe(ticTacToe);
                break;

            case "p4":
                Puissance4 puissance4 = new Puissance4(7, 4);
                puissance4.setGameAll(this);
                puissance4.setGameP4(puissance4);
                interactionUtilisateur.chooseGameP4(puissance4);
                break;

            case "gomaku":
                Gomoku gomoku = new Gomoku(15, 5);
                gomoku.setGameAll(this);
                gomoku.setGameGomoku(gomoku);
                interactionUtilisateur.chooseGameGomoku(gomoku);
                break;
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Logique de jeu
     */
    public void play() {
        isOver();
        if (!started) {
            randomPlayer();
            started = true;
        } else {
            //nextPlayer();
        }
        display();
    }

    public void isOver() {
        if (!Objects.equals(whoPlayNow, "null")) {
            /*
            if (checkWin()) {
                System.out.println("GG " + whoPlayNow);
                System.exit(0);
            }
            if (checkCellFilled() == (size * size)) {
                System.out.println("Match nul !");
                System.exit(0);
            }
             */
        }
    }

    /**
     * Affichage du tableau
     */
    public void display() {
        //view.println("Au tour de " + whoPlayNow + " (" + getCurrentPlayerRepresentation() + ")");
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
        if (whoPlayNow.contains("J")) {
            getMoveFromPlayer(interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
        } else {
            getMoveFromPlayer("bot");
        }
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

    public void getMoveFromPlayer(String choice) {
        if (!Objects.equals(choice, "bot")) {
            if (verificationChoiceUser(choice)) {
                int[] valueUser = returnValueUser(choice);
                Cell[][] board = this.getBoard();
                if (valueUser[0] > size || valueUser[1] > size || valueUser[0] < -1 || valueUser[1] < -1) {
                    view.println("Vous êtes sorti du tableau !");
                    display();
                } else if (verificationHavePlayer(board, valueUser)) {
                    view.println("Vous avez choisi une case deja prise !");
                    display();
                } else {
                    setOwner(valueUser[0], valueUser[1], "player");
                }
            } else {
                display();
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


    public boolean verificationChoiceUser(String choice) {
        String[] splitValeur = choice.split(" ");
        int[] valueUser = new int[2];
        for (int i = 0; i < splitValeur.length; i++) {
            try {
                valueUser[i] = Integer.parseInt(splitValeur[i]);
            } catch (Exception e) {
                view.println("Veuillez récrire des chiffres avec un espace !");
                return false;
            }
        }
        if (choice.length() != 3) {
            view.println("Veuillez récrire !");
            return false;
        }
        if (valueUser[0] < 0 || valueUser[0] > 2 || valueUser[1] < 0 || valueUser[1] > 2) {
            view.println("Une des valeur des cases définis et sois inférieur a 0 ou supérieur a 2 !");
            return false;
        }
        return true;
    }

    public int[] returnValueUser(String choice) {
        String[] splitValeur = choice.split(" ");
        int[] valueUser = new int[2];
        for (int i = 0; i < splitValeur.length; i++) {
            valueUser[i] = Integer.parseInt(splitValeur[i]);
        }
        return valueUser;
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

    /**
     * Retourne quel user est actuellement en train de jouer
     *
     * @return Retourne le joueur actuel
     */
    public Player getPlayerPlayNow() {
        if (whoPlayNow.equals("J1")) {
            return player1;
        } else {
            return player2;
        }
    }

    /**
     * Retourne quel bot est actuellement en train de jouer
     *
     * @return Retourne le bot actuel
     */
    public ArtificialPlayer getBotPlayNow() {
        if (whoPlayNow.equals("B1")) {
            return bot1;
        } else {
            return bot2;
        }
    }

    public boolean verificationHavePlayer(Cell[][] board, int[] valueUser) {
        String[] listPlayers = this.listRepresentation;
        for (String listPlayer : listPlayers) {
            Cell c = board[valueUser[0]][valueUser[1]];
            if (c.getRepresentation().equals(listPlayer)) {
                return true;
            }
        }
        return false;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public String getMode() {
        return mode;
    }
}
