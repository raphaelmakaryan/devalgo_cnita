package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.models.Cell;
import fr.raphaelmakaryan.bibliotheque.models.GameModele;
import fr.raphaelmakaryan.bibliotheque.models.GameModeleInterface;

import java.util.Objects;
import java.util.Random;

public abstract class GameController extends DisplayBoard {
    protected GameModele game;
    private GameState state;

    public GameController(GameModele game) {
        super(new String[]{}, new Cell[][]{});
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
            game.gameSerialization.dbUpdateGameTurnPlayer(game.database, game.getIdGameDatabase(), game.whoPlayNow, game.getMode());
            game.started = true;
        } else {
            handleEvent("PartieContinue");
            handleEvent("TourSuivant");
            nextPlayer();
            game.gameSerialization.dbUpdateGameTurnPlayer(game.database, game.getIdGameDatabase(), game.whoPlayNow, game.getMode());
        }
        GameModeleInterface.tools.setTimeout(1);
        display();
    }

    /**
     * Affichage du tableau
     */
    public void display() {
        DisplayBoard displayBoard = new DisplayBoard(
                new String[]{String.valueOf(game.size)},
                game.board
        );
        if (game.whoPlayNow.contains("J")) {
            getMoveFromPlayer(GameModeleInterface.interactionUtilisateur.inputInterface("Quelle case souhaiteriez-vous capturer " + game.whoPlayNow + " ? (exemple : '1 1')"), displayBoard);
        } else {
            getMoveFromPlayer("bot", displayBoard);
        }
    }

    /**
     * Deuxieme fonction de vérification avant modification du plateau
     *
     * @param choice       Choix du joueur
     * @param displayBoard Plateau d'affichage
     */
    public void getMoveFromPlayer(String choice, DisplayBoard displayBoard) {
        // Ferme la page du jeu
        if (game.getMode().equals("BvB") || game.getMode().equals("JvB")) {
            GameModeleInterface.tools.setTimeout(1);
            displayBoard.dispose();
        } else {
            displayBoard.dispose();
        }
        //Si y'a rien fermeture du jeu
        if (choice == null) {
            GameModeleInterface.gameView.onLeaveGame(GameModeleInterface.interactionUtilisateur, "Vous avez décidé de fermer la page, fermeture du jeu.");
            System.exit(0);
        }
        Cell[][] board = game.getBoard();
        int[] valueUser;
        if (!Objects.equals(choice, "bot")) {
            if (game.verificationChoiceUser(choice)) {
                if (game.getGameSelected().equals("p4")) {
                    valueUser = tokenDescent(game.returnValueUser(choice));
                } else {
                    valueUser = game.returnValueUser(choice);
                }
                if (verificationOutside(valueUser)) {
                    GameModeleInterface.interactionUtilisateur.inputMessage("Vous êtes sorti du tableau !");
                    display();
                } else if (game.verificationHavePlayer(board, valueUser)) {
                    GameModeleInterface.interactionUtilisateur.inputMessage("Vous avez choisi une case deja prise !");
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
                GameModeleInterface.interactionUtilisateur.inputMessage("Vous avez choisi une case deja prise !");
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
                game.whoPlayNow = "Joueur 1";
            } else {
                game.whoPlayNow = "Joueur 2";
            }
        } else if (game.players.get(0).contains("J") && game.players.get(1).contains("B")) {
            if (value == 0) {
                game.whoPlayNow = "Joueur 1";
            } else {
                game.whoPlayNow = "BOT 2";
            }
        } else if (game.players.get(0).contains("B") && game.players.get(1).contains("B")) {
            if (value == 0) {
                game.whoPlayNow = "BOT 1";
            } else {
                game.whoPlayNow = "BOT 2";
            }
        }
    }

    /**
     * Changement de joueur
     */
    public void nextPlayer() {
        if (game.players.get(0).contains("J") && game.players.get(1).contains("J")) {
            if (Objects.equals(game.whoPlayNow, "Joueur 1")) {
                game.whoPlayNow = "Joueur 2";
            } else if (Objects.equals(game.whoPlayNow, "Joueur 2")) {
                game.whoPlayNow = "Joueur 1";
            }
        }
        if (game.players.get(0).contains("J") && game.players.get(1).contains("B")) {
            if (Objects.equals(game.whoPlayNow, "Joueur 1")) {
                game.whoPlayNow = "BOT 1";
            } else if (Objects.equals(game.whoPlayNow, "BOT 1")) {
                game.whoPlayNow = "Joueur 1";
            }
        }
        if (game.players.get(0).contains("B") && game.players.get(1).contains("B")) {
            if (game.whoPlayNow.equals("BOT 1")) {
                game.whoPlayNow = "BOT 2";
            } else if (Objects.equals(game.whoPlayNow, "BOT 2")) {
                game.whoPlayNow = "BOT 1";

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
                game.gameSerialization.dbUpdateGameState(game.database, game.idGameDatabase, game.getMode());
                game.gameSerialization.dbUpdateUser(game.database, game, game.getMode());
                GameModeleInterface.interactionUtilisateur.inputMessage("GG " + game.whoPlayNow);
                System.exit(0);
            }
            if (game.checkCellFilled() == (game.size * game.size)) {
                handleEvent("PartieTerminée");
                game.gameSerialization.dbUpdateGameState(game.database, game.idGameDatabase, game.getMode());
                game.gameSerialization.dbUpdateUser(game.database, game, game.getMode());
                GameModeleInterface.interactionUtilisateur.inputMessage("Match nul !");
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
     * @param value         Valeur récupérer si c'est une nouvelle partie
     * @param usersDatabase Valeur récupérer si c'est d'une partie qui existe deja
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