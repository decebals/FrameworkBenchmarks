package ro.pippo.benchmark.undertow.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.dbcp2.BasicDataSource;
import ro.pippo.benchmark.undertow.Utils;
import ro.pippo.benchmark.undertow.dto.FortuneDto;
import ro.pippo.benchmark.undertow.dto.WorldDto;
import ro.pippo.core.PippoSettings;

public class SqlDao implements Dao {

  private final BasicDataSource dataSource;

  public SqlDao(PippoSettings settings, String keyPreffix) {
    dataSource = new BasicDataSource();
    dataSource.setDriverClassName(settings.getString(keyPreffix + ".driver", null));
    dataSource.setUrl(settings.getString(keyPreffix + ".url", null));
    dataSource.setUsername(settings.getString(keyPreffix + ".username", null));
    dataSource.setPassword(settings.getString(keyPreffix + ".password", null));

    // TODO verify optimal parameters for the pool
    dataSource.setMinIdle(settings.getInteger(keyPreffix + ".connection.min", 0));
    dataSource.setMaxIdle(settings.getInteger(keyPreffix + ".connection.max", 0));
  }

  @Override public WorldDto getRandomWorld() throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(Dao.READ_RANDOM_WORLD);
      statement.setInt(1, Utils.random());
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      int id = resultSet.getInt(1);
      int randomNumber = resultSet.getInt(2);
      return new WorldDto(id, randomNumber);
    } finally {
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  @Override public void updateRandomWorlds(List<WorldDto> dtos) throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      statement = connection.prepareStatement(Dao.UPDATE_RANDOM_WORLD);

      for (WorldDto dto : dtos) {
        statement.setInt(1, dto.randomNumber);
        statement.setInt(2, dto.id);
        statement.addBatch();
      }
      statement.executeBatch();
      connection.commit();
    } finally {
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  @Override public List<FortuneDto> getFortunes() throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(Dao.READ_FORTUNES);
      ResultSet resultSet = statement.executeQuery();
      List<FortuneDto> dtos = new LinkedList<>();
      while (resultSet.next()) {
        int id = resultSet.getInt(1);
        String message = resultSet.getString(2);
        dtos.add(new FortuneDto(id, message));
      }
      return dtos;
    } finally {
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }
}
