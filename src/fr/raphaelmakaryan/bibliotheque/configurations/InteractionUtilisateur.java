package fr.raphaelmakaryan.bibliotheque.configurations;

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
     * @param gameTTT l'instance du jeu tictactoe
     */
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
                gameView.println("Mode 1v1 Humain sélectionné");
                gameTTT.setMode("JvJ");
                gameTTT.createPlayer(new int[]{10, 11});
                break;
            case 1:
                // Mode 1v1 Contre Bot donc 10 et 20 (B1)
                gameView.println("Mode 1v1 Contre Bot sélectionné");
                gameTTT.setMode("JvB");
                gameTTT.createPlayer(new int[]{10, 20});
                break;
            case 2:
                // Mode 1v1 Bots 20 et 21 (B2)
                gameView.println("Mode 1v1 Bots sélectionné");
                gameTTT.setMode("BvB");
                gameTTT.createPlayer(new int[]{20, 21});
                break;
            default:
                gameView.println("Aucun mode sélectionné. Fin du jeu.");
                break;
        }
    }

    /**
     * Affichage pour le choix du mode dans Puissance 4
     *
     * @param gameP4 l'instance du jeu puissance 4
     */
    public void chooseGameP4(Puissance4 gameP4) {
        gameP4.setGameSelected("p4");
        gameView.println("Mode 1v1 Humain");
        gameP4.setMode("JvJ");
        gameP4.createPlayer(new int[]{10, 11});
    }

    /**
     * Affichage pour le choix du mode dans Gomoku
     *
     * @param gameGo l'instance du jeu gomoku
     */
    public void chooseGameGomoku(Gomoku gameGo) {
        gameGo.setGameSelected("gomoku");
        gameView.println("Mode 1v1 Humain");
        gameGo.setMode("JvJ");
        gameGo.createPlayer(new int[]{10, 11});
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