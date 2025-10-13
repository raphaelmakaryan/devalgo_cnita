package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModeleInterface;

import java.util.Objects;
import java.util.Random;

public abstract class GameController {
    protected GameModele game;
    private GameState state;

    public GameController(GameModele game) {
        this.game = game;
        this.state = GameState.INITIAL;
    }

    /**
     * Logique de jeu
     */
    public void play() {
        isOver();
        if (!game.started) {
            randomPlayer();
            game.gameSerialization.dbUpdateGameTurnPlayer(game.database, game.getIdGameDatabase(), game.whoPlayNow);
            game.started = true;
        } else {
            handleEvent("PartieContinue");
            handleEvent("TourSuivant");
            nextPlayer();
            game.gameSerialization.dbUpdateGameTurnPlayer(game.database, game.getIdGameDatabase(), game.whoPlayNow);
        }
        GameModeleInterface.tools.setTimeout(1);
        display();
    }

    /**
     * Affichage du tableau
     */
    public void display() {
        GameModeleInterface.gameView.println("Au tour de " + game.whoPlayNow + " (" + game.getCurrentPlayerRepresentation() + ")");
        GameModeleInterface.gameView.println(game.separationBoardGame());
        for (int i = 0; i < game.size; i++) {
            GameModeleInterface.gameView.print("|");
            for (int j = 0; j < game.size; j++) {
                Cell c = game.board[i][j];
                GameModeleInterface.gameView.print(c.getRepresentation());
                GameModeleInterface.gameView.print("|");
            }
            System.out.print("\n");
            GameModeleInterface.gameView.println(game.separationBoardGame());
        }
        GameModeleInterface.tools.clearLine();
        if (game.whoPlayNow.contains("J")) {
            getMoveFromPlayer(GameModeleInterface.interactionUtilisateur.userInterfaceMessage("Quelle case souhaiteriez-vous capturer ? (exemple : '1 1')"));
        } else {
            getMoveFromPlayer("bot");
        }
    }

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice Choix du joueur
     */
    public void getMoveFromPlayer(String choice) {
        if (choice == null) {
            GameModeleInterface.gameView.println("Vous avez décidé de fermer la page, fermeture du jeu.");
            System.exit(0);
        }
        Cell[][] board = game.getBoard();
        if (!Objects.equals(choice, "bot")) {
            if (game.verificationChoiceUser(choice)) {
                int[] valueUser = game.returnValueUser(choice);
                if (verificationOutside(valueUser)) {
                    GameModeleInterface.gameView.println("Vous êtes sorti du tableau !");
                    display();
                } else if (game.verificationHavePlayer(board, valueUser)) {
                    GameModeleInterface.gameView.println("Vous avez choisi une case deja prise !");
                    display();
                } else {
                    handlePlayerMove(valueUser[0], valueUser[1], "player");
                }
            } else {
                display();
            }
        } else {
            Minimax minimax = new Minimax(game);
            int[] best = minimax.findBestMove();
            if (game.verificationHavePlayer(board, best)) {
                GameModeleInterface.gameView.println("Vous avez choisi une case deja prise !");
                display();
            } else if (best[0] != -1) {
                handlePlayerMove(best[0], best[1], "bot");
            }
        }
    }

    /**
     * Condition de vérification si le jouer sort du tableau
     *
     * @param valueUser Valeurs de l'user
     * @return Vrai faux
     */
    public boolean verificationOutside(int[] valueUser) {
        return valueUser[0] > game.size || valueUser[1] > game.size || valueUser[0] < -1 || valueUser[1] < -1;
    }

    /**
     * Choix du joueur random au debut du jeu
     */
    public void randomPlayer() {
        int value = new Random().nextInt(2);
        if (game.players.get(0).contains("J") && game.players.get(1).contains("J")) {
            if (value == 0) {
                game.whoPlayNow = "J1";
            } else {
                game.whoPlayNow = "J2";
            }
        } else if (game.players.get(0).contains("J") && game.players.get(1).contains("B")) {
            if (value == 0) {
                game.whoPlayNow = "J1";
            } else {
                game.whoPlayNow = "B2";
            }
        } else if (game.players.get(0).contains("B") && game.players.get(1).contains("B")) {
            if (value == 0) {
                game.whoPlayNow = "B1";
            } else {
                game.whoPlayNow = "B2";
            }
        }
    }

    /**
     * Changement de joueur
     */
    public void nextPlayer() {
        if (game.players.get(0).contains("J") && game.players.get(1).contains("J")) {
            if (Objects.equals(game.whoPlayNow, "J1")) {
                game.whoPlayNow = "J2";
            } else if (Objects.equals(game.whoPlayNow, "J2")) {
                game.whoPlayNow = "J1";
            }
        }
        if (game.players.get(0).contains("J") && game.players.get(1).contains("B")) {
            if (Objects.equals(game.whoPlayNow, "J1")) {
                game.whoPlayNow = "B1";
            } else if (Objects.equals(game.whoPlayNow, "B1")) {
                game.whoPlayNow = "J1";
            }
        }
        if (game.players.get(0).contains("B") && game.players.get(1).contains("B")) {
            if (game.whoPlayNow.equals("B1")) {
                game.whoPlayNow = "B2";
            } else if (Objects.equals(game.whoPlayNow, "B2")) {
                game.whoPlayNow = "B1";

            }
        }
    }

    /**
     * Vérification de fin de jeu
     */
    public void isOver() {
        if (!Objects.equals(game.whoPlayNow, "null")) {
            if (game.checkWin()) {
                handleEvent("PartieTerminée");
                game.gameSerialization.dbUpdateGameState(game.database, game.idGameDatabase);
                game.gameSerialization.dbUpdateUser(game.database, game.getPlayerPlayNow().getIdDatabase());
                System.out.println("GG " + game.whoPlayNow);
                System.exit(0);
            }
            if (game.checkCellFilled() == (game.size * game.size)) {
                handleEvent("PartieTerminée");
                game.gameSerialization.dbUpdateGameState(game.database, game.idGameDatabase);
                game.gameSerialization.dbUpdateUser(game.database, game.getPlayerPlayNow().getIdDatabase());
                System.out.println("Match nul !");
                System.exit(0);
            }
        }
    }

    /**
     * Modification du tableau
     *
     * @param ligne   Ligne
     * @param colonne Colonne
     * @param type    Type de joueur
     */
    public void handlePlayerMove(int ligne, int colonne, String type) {
        handleEvent("ActionJoueur");
        game.setOwner(ligne, colonne, type);
        play();
    }

    /**
     * Création des joueurs
     *
     * @param value         Valeur récuperer si c'est une nouvelle partie
     * @param usersDatabase Valeur récuperer si c'est d'une partie qui existe deja
     */
    public void initializePlayers(int[] value, String[][] usersDatabase) {
        handleEvent("JeuInitialisé");
        game.createPlayer(value, usersDatabase);
        handleEvent("DébutDePartie");
        play();
    }

    /**
     * Evenements
     *
     * @param event Evenement précis
     */
    public void handleEvent(String event) {
        switch (state) {
            case INITIAL:
                if ("JeuInitialisé".equals(event)) {
                    setState(GameState.ATTENTE_DEBUT);
                }
                break;
            case ATTENTE_DEBUT:
                if ("DébutDePartie".equals(event)) {
                    setState(GameState.TOUR_JOUEUR);
                }
                break;
            case TOUR_JOUEUR:
                if ("TourTerminé".equals(event)) {
                    setState(GameState.ATTENTE_TOUR_SUIVANT);
                } else if ("ActionJoueur".equals(event)) {
                    setState(GameState.VERIFICATION_FIN_PARTIE);
                }
                break;
            case ATTENTE_TOUR_SUIVANT:
                if ("TourSuivant".equals(event)) {
                    setState(GameState.TOUR_JOUEUR);
                } else if ("ActionJoueur".equals(event)) {
                    setState(GameState.VERIFICATION_FIN_PARTIE);
                }
                break;
            case VERIFICATION_FIN_PARTIE:
                if ("PartieTerminée".equals(event)) {
                    setState(GameState.FIN_PARTIE);
                } else if ("PartieContinue".equals(event)) {
                    setState(GameState.TOUR_JOUEUR);
                }
                break;
        }
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
     * Retourne quel jeu est lancé
     *
     * @return Retourne les informations du jeu
     */
    public GameModele getGame() {
        return game;
    }

    /**
     * Met à jour l'état du jeu
     *
     * @param state Etat
     */
    public void setState(GameState state) {
        this.state = state;
    }
}