package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.games.TicTacToe;

public class Player {
    public String representation;

    public Player(TicTacToe ticTacToe, int value) {
        if (ticTacToe.mode.equals("JvB")) {
            this.representation = ticTacToe.listRepresentation[0];
        } else {
            if (value == 1) {
                this.representation = ticTacToe.listRepresentation[0];
            } else {
                this.representation = ticTacToe.listRepresentation[1];
            }
        }
    }

    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return "Player{" + "representation='" + representation + '\'' + '}';
    }


}
