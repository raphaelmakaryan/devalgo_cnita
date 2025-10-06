package fr.raphaelmakaryan.tictactoe;

import fr.raphaelmakaryan.tictactoe.Cell;

public class TicTacToe {
    public int size = 9;
    public int[] chiffres = {2, 5, 8};
    public Cell[] board;

    public void display() {
        Cell cell = new Cell();
        System.out.println("-------------");
        for (int i = 0; i < size; i++) {
            System.out.print("|");
            System.out.print(cell.getRepresentation());
            for (int j = 0; j < chiffres.length; j++) {
                if (i == chiffres[j]) {
                    System.out.print("|");
                    System.out.print("\n");
                    System.out.println("-------------");
                }
            }
        }
    }
}