package fr.raphaelmakaryan.bibliotheque.controllers;

import fr.raphaelmakaryan.bibliotheque.configurations.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class DisplayBoard extends JFrame {

    public DisplayBoard(String[] strings, Cell[][] cells) {
        super("Plateau de jeu");
        if (strings.length != 0) {
            int size = Integer.parseInt(strings[0]);
            Cell[][] board = cells;
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            DrawPane drawPane = new DrawPane(size, board);
            setContentPane(drawPane);
            int displaySize = 500 * size / 2;
            setSize(displaySize, displaySize);
            setVisible(true);
        }
    }

    class DrawPane extends JPanel {
        private int size;
        private Cell[][] board;

        public DrawPane(int size, Cell[][] board) {
            this.size = size;
            this.board = board;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrid(g);
            drawPieces(g);
        }

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

        private void drawPieces(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();
            int cellSize = Math.min(width, height) / size;

            // taille du texte proportionnelle à la taille de la case
            int fontSize = (int) (cellSize * 0.6);
            Font f = new Font("Comic Sans MS", Font.BOLD, fontSize);
            g2.setFont(f);
            FontMetrics fm = g2.getFontMetrics();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    Cell c = board[i][j];
                    String text = c.getRepresentation();
                    if (text != null && !text.isEmpty()) {
                        int x = i * cellSize + (cellSize - fm.stringWidth(text)) / 2;
                        int y = j * cellSize + ((cellSize - fm.getHeight()) / 2) + fm.getAscent();
                        g2.drawString(text, x, y);
                    }
                }
            }
        }
    }

    public static void main(String[] args, Cell[][] board) {
        JFrame frame = new DisplayBoard(args, board);
    }
}