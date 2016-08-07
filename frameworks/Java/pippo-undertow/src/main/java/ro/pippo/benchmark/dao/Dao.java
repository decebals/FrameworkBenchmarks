package ro.pippo.benchmark.dao;

import java.sql.SQLException;
import java.util.List;
import ro.pippo.benchmark.model.Fortune;
import ro.pippo.benchmark.model.World;

public interface Dao {

  String READ_RANDOM_WORLD = "SELECT id, randomnumber FROM hello_world.World WHERE id = ?";
  String UPDATE_RANDOM_WORLD = "UPDATE hello_world.World SET randomnumber = ? WHERE id = ?";
  String READ_FORTUNES = "SELECT id, message FROM hello_world.Fortune";

  World getRandomWorld() throws SQLException;

  void updateRandomWorlds(List<World> model) throws SQLException;

  List<Fortune> getFortunes() throws SQLException;
}
