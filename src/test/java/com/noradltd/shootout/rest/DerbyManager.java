package com.noradltd.shootout.rest;

import static com.noradltd.shootout.rest.RowMatcher.contains;
import static com.noradltd.shootout.rest.IsResultSetEmpty.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class DerbyManager {

	public void startDerby() {
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";
		try {
			Class.forName(driver).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Connection getConnection(String dbName) throws SQLException {
		Properties props = new Properties();
		return DriverManager.getConnection("jdbc:derby:" + dbName + ";create=true", props);
	}

	public void stopDerby() {
		try {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			if (!isDerbyShutdownException(e)) {
				throw new RuntimeException(e);
			}
		}
	}

	private boolean isDerbyShutdownException(SQLException e) {
		return (e.getErrorCode() == 50000) && ("XJ015".equals(e.getSQLState()));
	}

	private static final String TABLE_NAME = "contact";
	private static final String CREATE_TABLE__SQL = "create table " + TABLE_NAME
			+ "(first_name varchar(40), last_name varchar(40))";
	private static final String INSERT_INTO_CONTACT_FIRST_NAME_LAST_NAME_VALUES_SQL = "INSERT INTO " + TABLE_NAME
			+ " (first_name, last_name) VALUES (?, ?)";
	private static final String SELECT_FROM_CONTACT_SQL = "select * from " + TABLE_NAME;
	private static final String DROP_TABLE_CONTACT_SQL = "drop table " + TABLE_NAME;

	@Test
	public void startStopTest() throws SQLException {
		final String firstName = "Rich";
		final String lastName = "Dammkoehler";
		startDerby();
		Connection connection = getConnection("derbyDB");
		Statement statement = connection.createStatement();
		try {
			createTable(statement);
			insertTestData(firstName, lastName, connection);
			ResultSet resultSet = getData(statement);
			assertThat(resultSet, contains(firstName, lastName));
			assertThat(resultSet, is(empty()));
		} finally {
			dropTable(statement);
			statement.close();
			connection.close();
			stopDerby();
		}
	}

	private void dropTable(Statement statement) throws SQLException {
		statement.execute(DROP_TABLE_CONTACT_SQL);
	}

	private ResultSet getData(Statement statement) throws SQLException {
		return statement.executeQuery(SELECT_FROM_CONTACT_SQL);
	}

	private void insertTestData(final String firstName, final String lastName, Connection connection)
			throws SQLException {
		PreparedStatement pStatement = connection.prepareStatement(INSERT_INTO_CONTACT_FIRST_NAME_LAST_NAME_VALUES_SQL);
		pStatement.setString(1, firstName);
		pStatement.setString(2, lastName);
		pStatement.execute();
	}

	private void createTable(Statement statement) throws SQLException {
		statement.execute(CREATE_TABLE__SQL);
	}

}
