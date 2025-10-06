package fr.raphaelmakaryan.tictactoe;

import java.util.Random;

public class Player {
    public String representation;
    public String[] listRepresentation = {" O ", " X "};

    public Player(int value) {
        if (value == 1) {
            this.representation = listRepresentation[0];
        } else {
            this.representation = listRepresentation[1];
        }
    }

    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return "Player{" + "representation='" + representation + '\'' + '}';
    }

    public String[] getListRepresentation() {
        return listRepresentation;
    }
}
