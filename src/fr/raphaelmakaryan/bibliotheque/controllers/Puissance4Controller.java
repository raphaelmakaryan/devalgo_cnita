package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Puissance4Controller extends GameController {
    public Puissance4Controller(GameModele game) {
        super(game);
    }

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice Choix du joueur
     */
    @Override
    public void getMoveFromPlayer(String choice) {
        if (game.verificationChoiceUser(choice)) {
            int[] valueUser = tokenDescent(game.returnValueUser(choice));
            Cell[][] board = game.getBoard();
            if (verificationOutside(valueUser)) {
                game.gameView.println("Vous êtes sorti du tableau !");
                display();
            } else if (game.verificationHavePlayer(board, valueUser)) {
                game.gameView.println("Vous avez choisi une case deja prise !");
                display();
            } else {
                handlePlayerMove(valueUser[0], valueUser[1], "player");
            }
        } else {
            display();
        }
    }
}