package fr.raphaelmakaryan.bibliotheque.tools;

public class Tools {
    public static final String RESET = "\u001B[0m";
    public static final String SEPARATION_BLACK = "\u001B[30m";

    /**
     * Fonction de temps pour permettre un affichage fluide
     *
     * @param timeout Temps en seconde qu'on souhaite faire attendre
     */
    public void setTimeout(int timeout) {
        try {
            Thread.sleep(timeout * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Fonction de séparation pour l'affichage dans la console
     */
    public void clearLine() {
        System.out.println(SEPARATION_BLACK + "\n" + "-".repeat(40) + "\n" + RESET);
    }
}
