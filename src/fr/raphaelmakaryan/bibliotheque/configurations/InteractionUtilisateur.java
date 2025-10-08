package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.controllers.GomokuController;
import fr.raphaelmakaryan.bibliotheque.controllers.Puissance4Controller;
import fr.raphaelmakaryan.bibliotheque.controllers.TicTacToeController;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;
import fr.raphaelmakaryan.bibliotheque.modeles.Gomoku;
import fr.raphaelmakaryan.bibliotheque.modeles.Puissance4;
import fr.raphaelmakaryan.bibliotheque.modeles.TicTacToe;
import fr.raphaelmakaryan.bibliotheque.view.GameView;

import javax.swing.*;

public class InteractionUtilisateur {
    GameView gameView = new GameView();

    /**
     * Affiche la boite de dialogue
     *
     * @param message Message a afficher
     * @return Retourne la valeur récuperer
     */
    public String userInterfaceMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }


    /**
     * Boite d'affichage pour le choix du mode dans TicTacToe
     *
     * @param gameController Controller du tictactoe
     */
    public void chooseGameTicTacToe(TicTacToeController gameController) {
        GameModele modele = gameController.getGame();
        modele.setGameSelected("tictactoe");
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
                gameView.println("Mode 1v1 Humain sélectionné");
                modele.setMode("JvJ");
                gameController.initializePlayers(new int[]{10, 11});
                break;
            case 1:
                // Mode 1v1 Contre Bot donc 10 et 20 (B1)
                gameView.println("Mode 1v1 Contre Bot sélectionné");
                modele.setMode("JvB");
                gameController.initializePlayers(new int[]{10, 20});
                break;
            case 2:
                // Mode 1v1 Bots 20 et 21 (B2)
                gameView.println("Mode 1v1 Bots sélectionné");
                modele.setMode("BvB");
                gameController.initializePlayers(new int[]{20, 21});
                break;
            default:
                gameView.println("Aucun mode sélectionné. Fin du jeu.");
                break;
        }
    }

    /**
     * Affichage pour le choix du mode dans Puissance 4
     *
     * @param gameController Controller du puissance 4
     */
    public void chooseGameP4(Puissance4Controller gameController) {
        GameModele modele = gameController.getGame();
        modele.setGameSelected("p4");
        gameView.println("Mode 1v1 Humain");

    }

    /**
     * Affichage pour le choix du mode dans Gomoku
     *
     * @param gameController Controller du go
     */
    public void chooseGameGomoku(GomokuController gameController) {
        GameModele modele = gameController.getGame();
        modele.setGameSelected("gomoku");
        gameView.println("Mode 1v1 Humain");
        modele.setMode("JvJ");
        gameController.initializePlayers(new int[]{10, 11});
    }

    /**
     * Boite d'affichage pour le choix du mode de jeu
     *
     * @return renvoie le nom du jeu choisi
     */
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
                gameView.println("TicTacToe sélectionné");
                return "tictactoe";
            case 1:
                gameView.println("Puissance 4 sélectionné");
                return "p4";
            case 2:
                gameView.println("Gomoku sélectionné");
                return "gomoku";
            default:
                gameView.println("Aucun mode sélectionné. Fin du jeu.");
                return "null";
        }
    }
}