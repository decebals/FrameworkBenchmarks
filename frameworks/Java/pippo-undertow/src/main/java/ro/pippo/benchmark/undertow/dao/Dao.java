package ro.pippo.benchmark.undertow.dao;

import java.sql.SQLException;
import java.util.List;
import ro.pippo.benchmark.undertow.dto.FortuneDto;
import ro.pippo.benchmark.undertow.dto.WorldDto;

public interface Dao {

  String READ_RANDOM_WORLD = "SELECT id, randomnumber FROM hello_world.World WHERE id = ?";
  String UPDATE_RANDOM_WORLD = "UPDATE hello_world.World SET randomnumber = ? WHERE id = ?";
  String READ_FORTUNES = "SELECT id, message FROM hello_world.Fortune";

  WorldDto getRandomWorld() throws SQLException;

  void updateRandomWorlds(List<WorldDto> dto) throws SQLException;

  List<FortuneDto> getFortunes() throws SQLException;
}
