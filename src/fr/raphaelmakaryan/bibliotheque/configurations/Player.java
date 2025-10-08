package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.games.Game;
import fr.raphaelmakaryan.bibliotheque.games.TicTacToe;

public class Player {
    public String representation;

    public Player(Game game, int value) {
        if (game.getMode().equals("JvB")) {
            this.representation = game.listRepresentation[0];
        } else {
            this.representation = (value == 1)
                    ? game.listRepresentation[0]
                    : game.listRepresentation[1];
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
