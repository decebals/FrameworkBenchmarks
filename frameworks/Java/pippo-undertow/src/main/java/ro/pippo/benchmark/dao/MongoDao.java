package ro.pippo.benchmark.dao;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.LinkedList;
import java.util.List;
import org.bson.Document;
import ro.pippo.benchmark.Utils;
import ro.pippo.benchmark.model.Fortune;
import ro.pippo.benchmark.model.World;
import ro.pippo.core.PippoSettings;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class MongoDao implements Dao {

  private MongoCollection<Document> worldsCollection;
  private MongoCollection<Document> fortunesCollection;

  public MongoDao(PippoSettings settings) {
    MongoClientURI url = new MongoClientURI(settings.getString("db.mongo.url", null));
    MongoClient mongoClient = new MongoClient(url);
    MongoDatabase database = mongoClient.getDatabase("hello_world");

    worldsCollection = database.getCollection("World");
    fortunesCollection = database.getCollection("Fortune");
  }

  @Override public World getRandomWorld() {
    Document document = worldsCollection.find(eq("_id", Utils.random())).first();
    int id = document.getInteger("_id");
    int random = document.getInteger("randomNumber");
    return new World(id, random);
  }

  @Override public void updateRandomWorlds(List<World> worlds) {
    for (World world : worlds) {
      worldsCollection.updateOne(
          eq("_id", world.id),
          set("randomNumber", world.randomNumber));
    }
  }

  @Override public List<Fortune> getFortunes() {
    List<Fortune> fortunes = new LinkedList<>();
    MongoCursor<Document> cursor = fortunesCollection.find().iterator();
    try {
      while (cursor.hasNext()) {
        Document document = cursor.next();
        int id = document.getInteger("_id");
        String message = document.getString("message");
        fortunes.add(new Fortune(id, message));
      }
      return fortunes;
    } finally {
      cursor.close();
    }
  }
}
