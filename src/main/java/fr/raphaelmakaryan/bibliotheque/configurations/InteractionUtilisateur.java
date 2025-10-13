package fr.raphaelmakaryan.bibliotheque.configurations;

import fr.raphaelmakaryan.bibliotheque.controllers.CustomGameController;
import fr.raphaelmakaryan.bibliotheque.controllers.GomokuController;
import fr.raphaelmakaryan.bibliotheque.controllers.Puissance4Controller;
import fr.raphaelmakaryan.bibliotheque.controllers.TicTacToeController;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;
import fr.raphaelmakaryan.bibliotheque.pertinent.GameSerialization;
import fr.raphaelmakaryan.bibliotheque.pertinent.Persistence;
import fr.raphaelmakaryan.bibliotheque.view.GameView;

import javax.swing.*;

public class InteractionUtilisateur {
    GameView gameView = new GameView();

    /**
     * Affiche la boite de dialogue
     *
     * @param message Message à afficher
     * @return Retourne la valeur récupérée
     */
    public String userInterfaceMessage(String message) {
        return JOptionPane.showInputDialog(message);
    }

    /**
     * Boite d'affichage pour le choix du mode de jeu
     *
     * @return renvoie le nom du jeu choisi
     */
    public String chooseGame() {
        String[] options = {"TicTacToe", "Puissance 4", "Gomoku", "Jeu personnalisé", "Charger une partie"};
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

        return switch (choix) {
            case 0 -> {
                gameView.println("TicTacToe sélectionné");
                yield "tictactoe";
            }
            case 1 -> {
                gameView.println("Puissance 4 sélectionné");
                yield "p4";
            }
            case 2 -> {
                gameView.println("Gomoku sélectionné");
                yield "gomoku";
            }
            case 3 -> {
                gameView.println("Jeu personnalisé sélectionné");
                yield "perso";
            }
            case 4 -> {
                gameView.println("Charger une partie sélectionné");
                yield "loadGame";
            }
            default -> {
                gameView.println("Aucun mode sélectionné. Fin du jeu.");
                yield "null";
            }
        };
    }

    /**
     * Boite d'affichage pour le choix du mode dans TicTacToe
     *
     * @param gameController Controller du tictactoe
     */
    public void chooseGameTicTacToe(TicTacToeController gameController, String[][] usersDatabase, String mode) {
        GameModele modele = gameController.getGame();
        if (usersDatabase.length == 0 && mode.equals("")) {
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
                    creationElementImportant(modele, "tictactoe", "JvJ");
                    gameController.initializePlayers(new int[]{10, 11}, new String[][]{});
                    break;
                case 1:
                    // Mode 1v1 Contre Bot donc 10 et 20 (B1)
                    gameView.println("Mode 1v1 Contre Bot sélectionné");
                    creationElementImportant(modele, "tictactoe", "JvB");
                    gameController.initializePlayers(new int[]{10, 20}, new String[][]{});
                    break;
                case 2:
                    // Mode 1v1 Bots 20 et 21 (B2)
                    gameView.println("Mode 1v1 Bots sélectionné");
                    creationElementImportant(modele, "tictactoe", "BvB");
                    gameController.initializePlayers(new int[]{20, 21}, new String[][]{});
                    break;
                default:
                    gameView.println("Aucun mode sélectionné. Fin du jeu.");
                    break;
            }
        } else {
            switch (mode) {
                case "JvJ":
                    gameView.println("Mode 1v1 Humain sélectionné");
                    creationElementImportant(modele, "tictactoe", "JvJ");
                    gameController.initializePlayers(new int[]{10, 20}, usersDatabase);
                    break;

                case "JvB":
                    gameView.println("Mode 1v1 Contre Bot sélectionné");
                    creationElementImportant(modele, "tictactoe", "JvB");
                    gameController.initializePlayers(new int[]{10, 20}, usersDatabase);
                    break;

                case "BvB":
                    gameView.println("Mode 1v1 Bots sélectionné");
                    creationElementImportant(modele, "tictactoe", "BvB");
                    gameController.initializePlayers(new int[]{20, 21}, usersDatabase);
                    break;
            }
        }
    }

    /**
     * Affichage pour le choix du mode dans Puissance 4
     *
     * @param gameController Controller de puissance 4
     */
    public void chooseGameP4(Puissance4Controller gameController, String[][] usersDatabase) {
        GameModele modele = gameController.getGame();
        creationElementImportant(modele, "p4", "JvJ");
        gameView.println("Mode 1v1 Humain");
        gameController.initializePlayers(new int[]{10, 11}, usersDatabase);
    }

    /**
     * Affichage pour le choix du mode dans Gomoku
     *
     * @param gameController Controller du go
     */
    public void chooseGameGomoku(GomokuController gameController, String[][] usersDatabase) {
        GameModele modele = gameController.getGame();
        creationElementImportant(modele, "gomoku", "JvJ");
        gameView.println("Mode 1v1 Humain");
        gameController.initializePlayers(new int[]{10, 11}, new String[][]{});
    }

    /**
     * Affichage pour le choix du mode dans Gomoku
     *
     * @param gameController Controller du go
     */
    public void chooseGameCustomGame(CustomGameController gameController, String[][] usersDatabase) {
        GameModele modele = gameController.getGame();
        gameView.println("Mode 1v1 Humain");
        creationElementImportant(modele, "perso", "JvJ");
        gameController.initializePlayers(new int[]{10, 11}, usersDatabase);
    }

    /**
     *
     * @param modele
     * @param type
     * @param mode
     */
    public void creationElementImportant(GameModele modele, String type, String mode) {
        modele.setGameSelected(type);
        modele.setGameSerialization(new GameSerialization());
        modele.setDatabase(modele.getGameSerialization().dbConnection());
        modele.setMode(mode);
    }
}