package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.games.Game;

public class ArtificialPlayer {
    public String representation;

    /**
     * Création d'un joueur "bot"
     *
     * @param game  Le jeu
     * @param value Valeur de la place
     */
    public ArtificialPlayer(Game game, int value) {
        if (game.mode.equals("JvB")) {
            this.representation = game.listRepresentation[1];
        } else {
            if (value == 1) {
                this.representation = game.listRepresentation[0];
            } else {
                this.representation = game.listRepresentation[1];
            }
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
        return "ArtificialPlayer{" +
                "representation='" + representation + '\'' +
                '}';
    }
}
