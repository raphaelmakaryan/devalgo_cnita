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

        // Evalue le score
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
            int best = Integer.MIN_VALUE;
            for (Cell[] cells : board) {
                for (int j = 0; j < board[0].length; j++) {
                    if (cells[j].isEmpty()) {
                        // simule le coup du bot
                        cells[j].setRepresentation(botSymbol);

                        // Vérifie
                        int val = minimax(board, depth + 1, false, botSymbol, playerSymbol);

                        // annule le coup
                        cells[j].setRepresentation("   ");

                        // Ressors lequel est le mieux
                        best = Math.max(best, val);
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (Cell[] cells : board) {
                for (int j = 0; j < board[0].length; j++) {
                    if (cells[j].isEmpty()) {
                        // simule le coup du joueur
                        cells[j].setRepresentation(playerSymbol);

                        // Vérifie
                        int val = minimax(board, depth + 1, true, botSymbol, playerSymbol);

                        // annule le coup
                        cells[j].setRepresentation("   ");

                        // Ressors lequel est le pire
                        best = Math.min(best, val);
                    }
                }
            }
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
     * Évalue le plateau : renvoie +10 si le bot gagne, -10 si le joueur gagne, 0 sinon.
     * Ici, on ne vérifie que les lignes (horizontal).
     */
    private int evaluate(Cell[][] board, String botSymbol, String playerSymbol) {
        if (checkVertHori(board, botSymbol)) return +10;
        if (checkVertHori(board, playerSymbol)) return -10;

        if (checkDiagonal(board, botSymbol)) return +10;
        if (checkDiagonal(board, playerSymbol)) return -10;

        return 0;
    }

    /**
     * Ressors si y'a encore des coups possible
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
     * @param board Tableau
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
     * @param board Tableau
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
