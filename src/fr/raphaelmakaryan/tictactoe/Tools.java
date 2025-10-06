package fr.raphaelmakaryan.tictactoe;

public class Tools {

    public void setTimeout(int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clearLine() {
        System.out.println(Colors.SEPARATION_BLACK + "\n" + "-".repeat(40) + "\n" + Colors.RESET);
    }


}
