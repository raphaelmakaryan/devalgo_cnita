package fr.raphaelmakaryan.bibliotheque.pertinent;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.BsonObjectId;
import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.ServerApi;
import com.mongodb.client.*;
import com.mongodb.ServerApiVersion;
import org.bson.types.ObjectId;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;


public class GameSerialization implements Persistence {

    public MongoDatabase dbConnection() {
        ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(connectionString)).serverApi(serverApi).build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("devalgo");
    }

    public String dbCreateUser(MongoDatabase database, String type, String place, String symbole) {
        MongoCollection<Document> collection = database.getCollection("users");
        InsertOneResult result = collection.insertOne(new Document("dateCreation", LocalDateTime.now()).append("type", type).append("place", place).append("representation", symbole).append("score", 0));
        return result.getInsertedId().asObjectId().getValue().toString();
    }

    public String dbCreateGame(MongoDatabase database, String mode, String game, int taille, int jetonVictoire, String player1, String player2) {
        MongoCollection<Document> collection = database.getCollection("games");
        Map<String, Map<String, String>> board = new HashMap<>();
        for (int i = 0; i < taille; i++) {
            Map<String, String> row = new HashMap<>();
            for (int j = 0; j < taille; j++) {
                row.put(String.valueOf(j), "");
            }
            board.put(String.valueOf(i), row);
        }
        InsertOneResult result = collection.insertOne(new Document("dateCreation", LocalDateTime.now()).append("gameChoose", game).append("mod", mode).append("size", taille).append("victoryValue", jetonVictoire).append("turn", "UNDEFINED").append("player1", new ObjectId(player1)).append("player2", new ObjectId(player2)).append("board", board));
        return result.getInsertedId().asObjectId().getValue().toString();
    }

    public void dbUpdateUser() {

    }

    public void dbUpdateGame(MongoDatabase database, String playerId, String gameId, String symbole, int colonne, int row) {
        MongoCollection<Document> collection = database.getCollection("games");
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        if (game != null) {
            Document board = (Document) game.get("board");
            Document rowDoc = (Document) board.get(String.valueOf(row));
            if (rowDoc == null) {
                rowDoc = new Document();
                board.put(String.valueOf(row), rowDoc);
            }
            rowDoc.put(String.valueOf(colonne), symbole);
            collection.replaceOne(filter, game);
        } else {
            System.out.println("Aucun document trouvé avec l'ID " + gameId);
        }
    }

    public void dbUpdateGameTurnPlayer(MongoDatabase database, String gameId, String playerId) {
        MongoCollection<Document> collection = database.getCollection("games");
        Document filter = new Document("_id", new ObjectId(gameId));
        Document game = collection.find(filter).first();
        if (game != null) {
            game.put("turn", new ObjectId(playerId));
            collection.replaceOne(filter, game);
        } else {
            System.out.println("Aucun document trouvé avec l'ID " + gameId);
        }
    }

    static Logger root = (Logger) LoggerFactory
            .getLogger(Logger.ROOT_LOGGER_NAME);

    static {
        root.setLevel(Level.INFO);
    }
}
