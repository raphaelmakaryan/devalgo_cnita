package fr.raphaelmakaryan.tictactoe;

import java.util.Random;

public class Player {
    public String representation;
    public String[] listRepresentation = {" O ", " X "};

    public Player() {
        this.representation = listRepresentation[new Random().nextInt(1, listRepresentation.length)];
    }

    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return "Player{" +
                "representation='" + representation + '\'' +
                '}';
    }

    public String[] getListRepresentation() {
        return listRepresentation;
    }
}
