package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.modeles.Cell;

import javax.swing.*;
import java.awt.*;

public class DisplayBoard extends JFrame {

    /**
     * Affichage du jeu a l'écran
     *
     * @param strings Toutes les infos récupérer du jeu
     * @param cells   Tableau du jeu récupérer
     */
    public DisplayBoard(String[] strings, Cell[][] cells) {
        super("Plateau de jeu");
        if (strings.length != 0) {
            int size = Integer.parseInt(strings[0]);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            DrawPane drawPane = new DrawPane(size, cells);
            setContentPane(drawPane);
            int displaySize = 500 * size / 2;
            setSize(displaySize, displaySize);
            setVisible(true);
        }
    }

    static class DrawPane extends JPanel {
        private final int size;
        private final Cell[][] board;

        /**
         * Initiation de DrawPane pour la creation visuel
         *
         * @param size  Taille du tableau
         * @param board Tableau
         */
        public DrawPane(int size, Cell[][] board) {
            this.size = size;
            this.board = board;
        }

        /**
         * Création visuelle du tableau
         *
         * @param g the <code>Graphics</code> object to protect
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrid(g);
            drawPieces(g);
        }

        /**
         * Création du tableau visuel
         *
         * @param g Graphics
         */
        private void drawGrid(Graphics g) {
            int width = getWidth();
            int height = getHeight();
            int cellSize = Math.min(width, height) / size;

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int x = i * cellSize;
                    int y = j * cellSize;
                    g.drawRect(x, y, cellSize, cellSize);
                }
            }
        }

        /**
         * Création des pieces dans le tableau
         *
         * @param g Graphics
         */
        private void drawPieces(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            int cellSize = Math.min(width, height) / size;
            int fontSize = (int) (cellSize * 0.6);
            Font f = new Font("Comic Sans MS", Font.BOLD, fontSize);
            g2.setFont(f);
            FontMetrics fm = g2.getFontMetrics();
            for (int col = 0; col < size; col++) {
                for (int row = size - 1; row >= 0; row--) {
                    Cell c = board[row][col];
                    String text = c.getRepresentation();
                    if (text != null && !text.isEmpty() && !c.isEmpty()) {
                        int x = col * cellSize + (cellSize - fm.stringWidth(text)) / 2;
                        int y = row * cellSize + ((cellSize - fm.getHeight()) / 2) + fm.getAscent();
                        g2.drawString(text, x, y);
                    }
                }
            }
        }
    }

    public static void main(String[] args, Cell[][] board) {
        new DisplayBoard(args, board);
    }
}