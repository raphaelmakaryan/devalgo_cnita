package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.games.Game;
import fr.raphaelmakaryan.bibliotheque.games.Gomoku;
import fr.raphaelmakaryan.bibliotheque.games.Puissance4;
import fr.raphaelmakaryan.bibliotheque.games.TicTacToe;

import javax.swing.*;

public class InteractionUtilisateur {
    View view = new View();

    public String userInterfaceMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }

    public void chooseGameTicTacToe(TicTacToe gameTTT) {
        String[] options = {"1v1 Humain", "1v1 Contre Bot", "1v1 Bots"};
        gameTTT.setGameSelected("tictactoe");
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
                gameTTT.setMode("JvJ");
                gameTTT.createPlayer(new int[]{10, 11});
                break;
            case 1:
                // Mode 1v1 Contre Bot donc 10 et 20 (B1)
                view.println("Mode 1v1 Contre Bot sélectionné");
                gameTTT.setMode("JvB");
                gameTTT.createPlayer(new int[]{10, 20});
                break;
            case 2:
                // Mode 1v1 Bots 20 et 21 (B2)
                view.println("Mode 1v1 Bots sélectionné");
                gameTTT.setMode("BvB");
                gameTTT.createPlayer(new int[]{20, 21});
                break;
            default:
                view.println("Aucun mode sélectionné. Fin du jeu.");
                break;
        }
    }

    public void chooseGameP4(Puissance4 gameP4) {
        gameP4.setGameSelected("p4");
        view.println("Mode 1v1 Humain");
        gameP4.setMode("JvJ");
        gameP4.createPlayer(new int[]{10, 11});
    }

    public void chooseGameGomoku(Gomoku gameGo) {
        gameGo.setGameSelected("gomoku");
        view.println("Mode 1v1 Humain");
        gameGo.setMode("JvJ");
        gameGo.createPlayer(new int[]{10, 11});
    }

    public String chooseGame() {
        String[] options = {"TicTacToe", "Puissance 4", "Gomoku"};
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
                view.println("Gomoku sélectionné");
                return "gomoku";
            default:
                view.println("Aucun mode sélectionné. Fin du jeu.");
                return "null";
        }
    }

}