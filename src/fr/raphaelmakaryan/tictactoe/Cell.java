package fr.raphaelmakaryan.tictactoe;

public class Cell {
    private String representation = "   ";

    public String getRepresentation() {
        return representation;
    }

    public void setRepresentation(String value) {
        this.representation = value;
    }

    public boolean isEmpty() {
        return "   ".equals(this.representation);
    }
}