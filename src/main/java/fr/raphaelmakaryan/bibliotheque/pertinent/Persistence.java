package fr.raphaelmakaryan.bibliotheque.pertinent;

import com.mongodb.client.MongoDatabase;
import org.bson.BsonObjectId;

public interface Persistence {
    String connectionString = "mongodb+srv://raph:ZtpI0vAe2RoDg3AQ@campusnumerique.8msqkrp.mongodb.net/?retryWrites=true&w=majority&appName=campusnumerique";

    MongoDatabase dbConnection();

    String dbCreateUser(MongoDatabase database, String type, String place, String symbole);

    String dbCreateGame(MongoDatabase database, String mode, String game, int taille, int jetonVictoire, String player1, String player2);

    void dbUpdateUser();

    void dbUpdateGame(MongoDatabase database, String playerId, String gameId, String symbole, int colonne, int row);

    void dbUpdateGameTurnPlayer(MongoDatabase database, String gameId, String playerId);
}
