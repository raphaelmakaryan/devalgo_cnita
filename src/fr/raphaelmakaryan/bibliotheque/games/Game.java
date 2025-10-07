package fr.raphaelmakaryan.bibliotheque.games;

import fr.raphaelmakaryan.bibliotheque.configurations.*;
import fr.raphaelmakaryan.bibliotheque.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public int size;
    public Cell[][] board;
    public boolean started;
    public String whoPlayNow;
    public List<String> players = new ArrayList<>();
    public String[] listRepresentation;
    public Tools tools = new Tools();
    public View view = new View();
    public String mode;
    public int victoryValue;
    public InteractionUtilisateur interactionUtilisateur = new InteractionUtilisateur();

    public Game(int size, int victoryValue) {
        this.size = size;
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
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
