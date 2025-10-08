package fr.raphaelmakaryan.bibliotheque.tools;

public class Tools {

    /**
     * Fonction de temps pour permettre un affichage fluide
     * @param timeout Temps en seconde qu'on souhaite faire attendre
     */
    public void setTimeout(int timeout) {
        try {
            Thread.sleep(timeout * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Fonction de séparation pour l'affichage dans la console
     */
    public void clearLine() {
        System.out.println(Colors.SEPARATION_BLACK + "\n" + "-".repeat(40) + "\n" + Colors.RESET);
    }
}
