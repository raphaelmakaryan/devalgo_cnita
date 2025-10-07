package fr.raphaelmakaryan.tictactoe.configurations;

import fr.raphaelmakaryan.tictactoe.games.TicTacToe;

import javax.swing.*;

public class InteractionUtilisateur {
    View view = new View();

    public String userInterfaceMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }

    public void chooseGame(TicTacToe game) {
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

}