package fr.raphaelmakaryan.bibliotheque;

public class Tools {
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
}
