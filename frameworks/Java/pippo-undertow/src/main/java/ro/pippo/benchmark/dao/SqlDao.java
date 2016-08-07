package ro.pippo.benchmark.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.dbcp2.BasicDataSource;
import ro.pippo.benchmark.model.World;
import ro.pippo.benchmark.Utils;
import ro.pippo.benchmark.model.Fortune;
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

  @Override public World getRandomWorld() throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(READ_RANDOM_WORLD);
      statement.setInt(1, Utils.random());
      ResultSet resultSet = statement.executeQuery();
      resultSet.next();
      int id = resultSet.getInt(1);
      int randomNumber = resultSet.getInt(2);
      return new World(id, randomNumber);
    } finally {
      if (statement != null) {
        statement.close();
      }
      if (connection != null) {
        connection.close();
      }
    }
  }

  @Override public void updateRandomWorlds(List<World> models) throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      connection.setAutoCommit(false);
      statement = connection.prepareStatement(UPDATE_RANDOM_WORLD);

      for (World model : models) {
        statement.setInt(1, model.randomNumber);
        statement.setInt(2, model.id);
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

  @Override public List<Fortune> getFortunes() throws SQLException {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(READ_FORTUNES);
      ResultSet resultSet = statement.executeQuery();
      List<Fortune> models = new LinkedList<>();
      while (resultSet.next()) {
        int id = resultSet.getInt(1);
        String message = resultSet.getString(2);
        models.add(new Fortune(id, message));
      }
      return models;
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
