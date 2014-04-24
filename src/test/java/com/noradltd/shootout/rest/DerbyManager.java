package com.noradltd.shootout.rest;

import static com.noradltd.shootout.rest.RowMatcher.contains;
import static com.noradltd.shootout.rest.IsResultSetEmpty.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.sql.Connection;
import java.sql.DriverManager;
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

	@Test
	public void startStopTest() throws SQLException {
		final String firstName = "Rich";
		final String lastName = "Dammkoehler";
		startDerby();
		Connection connection = getConnection("derbyDB");
		Statement statement = connection.createStatement();
		try {
			statement.execute("create table contact(first_name varchar(40), last_name varchar(40))");
			statement.execute("INSERT INTO contact (first_name, last_name) VALUES ('" + firstName + "', '" + lastName
					+ "')");
			ResultSet resultSet = statement.executeQuery("select * from contact");
			assertThat(resultSet, contains(firstName, lastName));
			assertThat(resultSet, is(empty()));
		} finally {
			statement.execute("drop table contact");
			stopDerby();
		}
	}

}
