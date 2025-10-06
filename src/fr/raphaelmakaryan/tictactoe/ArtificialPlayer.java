package fr.raphaelmakaryan.tictactoe;

public class ArtificialPlayer {
    public String representation;

    public ArtificialPlayer(TicTacToe ticTacToe, int value) {

        if (ticTacToe.mode.equals("JvB")) {
            this.representation = ticTacToe.listRepresentation[1];
        } else {
            if (value == 1) {
                this.representation = ticTacToe.listRepresentation[0];
            } else {
                this.representation = ticTacToe.listRepresentation[1];
            }
        }
    }

    public String getRepresentation() {
        return representation;
    }

    @Override
    public String toString() {
        return "ArtificialPlayer{" +
                "representation='" + representation + '\'' +
                '}';
    }
}
