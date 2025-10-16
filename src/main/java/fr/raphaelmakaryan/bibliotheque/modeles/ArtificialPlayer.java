package fr.raphaelmakaryan.bibliotheque.modeles;

public class ArtificialPlayer {
    public String representation;
    public String idDatabase;

    /**
     * Création d'un joueur "bot"
     *
     * @param gameModele Le jeu
     * @param value      Valeur de la place
     */
    public ArtificialPlayer(GameModele gameModele, int value, String idDatabase) {
        if (gameModele.mode.equals("JvB")) {
            this.representation = gameModele.listRepresentation[1];
        } else {
            if (value == 1) {
                this.representation = gameModele.listRepresentation[0];
            } else {
                this.representation = gameModele.listRepresentation[1];
            }
        }
        this.idDatabase = idDatabase;
    }

    /**
     * Récupère le signe
     *
     * @return retourne le signe
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * Récupérer l'id du bot dans la base de donnée
     * @return L'id
     */
    public String getIdDatabase() {
        return idDatabase;
    }

    /**
     * Insert l'id du bot de la base de donnée
     * @param idDatabase Id
     */
    public void setIdDatabase(String idDatabase) {
        this.idDatabase = idDatabase;
    }

    /**
     * Ajoute la representation du bot
     * @param representation Symbole
     */
    public void setRepresentation(String representation) {
        this.representation = representation;
    }

    @Override
    public String toString() {
        return "ArtificialPlayer{" +
                "representation='" + representation + '\'' +
                '}';
    }
}
