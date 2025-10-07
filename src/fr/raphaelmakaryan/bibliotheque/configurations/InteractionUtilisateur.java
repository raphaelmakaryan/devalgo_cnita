package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.games.Game;
import fr.raphaelmakaryan.bibliotheque.games.TicTacToe;

import javax.swing.*;

public class InteractionUtilisateur {
    View view = new View();

    public String userInterfaceMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }

    /*
    public void chooseGameTicTacToe(TicTacToe gameTTT, Game gameALl) {
        String[] options = {"1v1 Humain", "1v1 Contre Bot", "1v1 Bots"};
        int choix = JOptionPane.showOptionDialog(
                null,
                "Choisissez le mode de jeu :",
                "Mode de jeu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choix) {
            case 0:
                // Mode 1v1 Humain donc 10 (J1) et 11 (J2)
                view.println("Mode 1v1 Humain sélectionné");
                gameTTT.mode = "JvJ";
                gameALl.createPlayer(new int[]{10, 11}, gameTTT);
                break;
            case 1:
                // Mode 1v1 Contre Bot donc 10 et 20 (B1)
                view.println("Mode 1v1 Contre Bot sélectionné");
                gameTTT.mode = "JvB";
                gameALl.createPlayer(new int[]{10, 20}, gameTTT);
                break;
            case 2:
                // Mode 1v1 Bots 20 et 21 (B2)
                view.println("Mode 1v1 Bots sélectionné");
                gameTTT.mode = "BvB";
                gameALl.createPlayer(new int[]{20, 21}, gameTTT);
                break;
            default:
                view.println("Aucun mode sélectionné. Fin du jeu.");
                break;
        }
    }

     */

    public void chooseGameTicTacToe(TicTacToe game) {
        String[] options = {"1v1 Humain", "1v1 Contre Bot", "1v1 Bots"};
        int choix = JOptionPane.showOptionDialog(
                null,
                "Choisissez le mode de jeu :",
                "Mode de jeu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choix) {
            case 0:
                // Mode 1v1 Humain donc 10 (J1) et 11 (J2)
                view.println("Mode 1v1 Humain sélectionné");
                game.mode = "JvJ";
                game.createPlayer(new int[]{10, 11});
                break;
            case 1:
                // Mode 1v1 Contre Bot donc 10 et 20 (B1)
                view.println("Mode 1v1 Contre Bot sélectionné");
                game.mode = "JvB";
                game.createPlayer(new int[]{10, 20});
                break;
            case 2:
                // Mode 1v1 Bots 20 et 21 (B2)
                view.println("Mode 1v1 Bots sélectionné");
                game.mode = "BvB";
                game.createPlayer(new int[]{20, 21});
                break;
            default:
                view.println("Aucun mode sélectionné. Fin du jeu.");
                break;
        }
    }

    public String chooseGame() {
        String[] options = {"TicTacToe", "Puissance 4", "Gomuku"};
        int choix = JOptionPane.showOptionDialog(
                null,
                "Choisissez le mode de jeu :",
                "Mode de jeu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (choix) {
            case 0:
                view.println("TicTacToe sélectionné");
                return "tictactoe";
            case 1:
                view.println("Puissance 4 sélectionné");
                return "p4";
            case 2:
                view.println("Gomaku sélectionné");
                return "gomaku";
            default:
                view.println("Aucun mode sélectionné. Fin du jeu.");
                return "null";
        }
    }

}