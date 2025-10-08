package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.games.Game;

public class Player {
    public String representation;

    /**
     * Création d'un joueur "joueur"
     *
     * @param game  Le jeu
     * @param value Valeur de la place
     */
    public Player(Game game, int value) {
        if (game.getMode().equals("JvB")) {
            this.representation = game.listRepresentation[0];
        } else {
            this.representation = (value == 1)
                    ? game.listRepresentation[0]
                    : game.listRepresentation[1];
        }
    }

    /**
     * Récupere le signe
     *
     * @return retourne le signe
     */
    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return "Player{" + "representation='" + representation + '\'' + '}';
    }


}
