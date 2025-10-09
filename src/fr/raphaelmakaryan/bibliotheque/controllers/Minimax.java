package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;
import fr.raphaelmakaryan.bibliotheque.modeles.GameModele;

public class Minimax {
    protected GameModele game;

    public Minimax(GameModele game) {
        this.game = game;
    }

    /**
     * Fonction minimax, simule tous les coups possibles
     *
     * @param board        le plateau sur lequel on simule
     * @param depth        profondeur actuelle (0 au début)
     * @param isBot        true si c’est au tour du bot (on veut maximiser le score)
     * @param botSymbol    symbole du bot
     * @param playerSymbol symbole du joueur
     * @return un score estimé pour cette position
     */
    private int minimax(Cell[][] board, int depth, boolean isBot, String botSymbol, String playerSymbol) {
        int best;
        // Évalué le score
        int score = evaluate(board, botSymbol, playerSymbol);
        if (score == 10) {
            return score - depth;  // victoire bot
        }
        if (score == -10) {
            return score + depth;  // victoire joueur
        }
        if (!isMovesLeft(board)) {
            return 0;  // match nul
        }

        // Si c'est le bot qui joue
        if (isBot) {
            best = simulationMinimax(Integer.MIN_VALUE, board, isBot, depth, botSymbol, playerSymbol);
            return best;
        } else {
            best = simulationMinimax(Integer.MAX_VALUE, board, isBot, depth, botSymbol, playerSymbol);
            return best;
        }
    }

    /**
     * Trouve le meilleur coup pour le bot en utilisant minimax
     */
    public int[] findBestMove() {
        String botSymbol = game.getCurrentPlayerRepresentation();
        String playerSymbol = botSymbol.equals(" O ") ? " X " : " O ";

        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < game.size; i++) {
            for (int j = 0; j < game.size; j++) {
                if (game.board[i][j].isEmpty()) {
                    // simule le coup du bot
                    game.board[i][j].setRepresentation(botSymbol);

                    // Vérifie
                    int moveVal = minimax(game.board, 0, false, botSymbol, playerSymbol);

                    // annule le coup
                    game.board[i][j].setRepresentation("   ");

                    // Ressors les meilleures coordonnées
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * Crée la simulation pour chercher le meilleur coup
     *
     * @param bestOld      Valeur max/min selon le joueur
     * @param board        Tableau
     * @param isBot        Si c'est le bot qui joue
     * @param depth        Profondeur
     * @param botSymbol    Symbole du bot
     * @param playerSymbol Symbole du joueur
     * @return Retourne la valeur
     */
    private int simulationMinimax(int bestOld, Cell[][] board, boolean isBot, int depth, String botSymbol, String playerSymbol) {
        int val;
        for (Cell[] cells : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (cells[j].isEmpty()) {
                    // simule le coup du bot
                    if (isBot) {
                        cells[j].setRepresentation(botSymbol);
                    } else {
                        cells[j].setRepresentation(playerSymbol);
                    }

                    // Vérifie
                    if (isBot) {
                        val = minimax(board, depth + 1, false, botSymbol, playerSymbol);
                    } else {
                        val = minimax(board, depth + 1, true, botSymbol, playerSymbol);
                    }

                    // annule le coup
                    cells[j].setRepresentation("   ");

                    // Ressors lequel est le mieux selon si c'est le bot ou non
                    if (isBot) {
                        bestOld = Math.max(bestOld, val);
                    } else {
                        bestOld = Math.min(bestOld, val);
                    }
                }
            }
        }
        return bestOld;
    }

    /**
     * Évalue le plateau : renvoie +10 si le bot gagne, -10 si le joueur gagne, ZÉRO sinon.
     */
    private int evaluate(Cell[][] board, String botSymbol, String playerSymbol) {
        if (checkVertHori(board, botSymbol)) return +10;
        if (checkVertHori(board, playerSymbol)) return -10;

        if (checkDiagonal(board, botSymbol)) return +10;
        if (checkDiagonal(board, playerSymbol)) return -10;

        return 0;
    }

    /**
     * Ressors si il y a encore des coups possibles
     *
     * @param board Le tableau
     * @return Vrai ou faux
     */
    private boolean isMovesLeft(Cell[][] board) {
        for (int i = 0; i < game.size; i++) {
            for (int j = 0; j < game.size; j++) {
                if (board[i][j].isEmpty()) return true;
            }
        }
        return false;
    }

    /**
     * Check a l'horizontale et vertical
     *
     * @param board  Tableau
     * @param symbol Le symbole du joueur
     * @return Vrai ou faux
     */
    private boolean checkVertHori(Cell[][] board, String symbol) {
        for (int j = 0; j < game.size; j++) {
            int consecutive = 0;
            for (int i = 0; i < game.size; i++) {
                if (board[i][j].getRepresentation().equals(symbol)) {
                    consecutive++;
                    if (consecutive == game.victoryValue) return true;
                } else {
                    consecutive = 0;
                }
            }
        }
        return false;
    }

    /**
     * Check a la diagonal
     *
     * @param board  Tableau
     * @param symbol Symbole
     * @return Vrai Faux
     */
    private boolean checkDiagonal(Cell[][] board, String symbol) {
        // diagonale principale
        int consecutive = 0;
        for (int i = 0; i < game.size; i++) {
            if (board[i][i].getRepresentation().equals(symbol)) {
                consecutive++;
                if (consecutive == game.victoryValue) return true;
            } else {
                consecutive = 0;
            }
        }
        // diagonale secondaire
        consecutive = 0;
        for (int i = 0; i < game.size; i++) {
            if (board[i][game.size - 1 - i].getRepresentation().equals(symbol)) {
                consecutive++;
                if (consecutive == game.victoryValue) return true;
            } else {
                consecutive = 0;
            }
        }
        return false;
    }
}
