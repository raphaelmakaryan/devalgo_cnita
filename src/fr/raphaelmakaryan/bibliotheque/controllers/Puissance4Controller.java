package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Puissance4Controller extends GameController {
    public Puissance4Controller(GameModele game) {
        super(game);
    }

    /**
     * Permet de vérifier si il y a deja un jeton tout en bas
     *
     * @param value Valeur écrit de base par le joueur
     * @return Retourne les nouvel valeur
     */
    public int[] tokenDescent(int[] value) {
        int valeurColonne = value[0];
        int[] newValue = new int[2];
        int valueDecrease = game.size - 1;
        boolean valueFind = false;
        for (int i = 0; i < game.board.length; i++) {
            Cell c = game.board[valueDecrease][valeurColonne];
            if (game.whoPlayNow.contains("J") && c.getRepresentation().equals("   ") && !valueFind) {
                newValue[0] = valueDecrease;
                newValue[1] = valeurColonne;
                valueFind = true;
            } else {
                valueDecrease = valueDecrease - 1;
            }
        }
        return newValue;
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
            if (valueUser[0] > game.size || valueUser[1] > game.size || valueUser[0] < -1 || valueUser[1] < -1) {
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
