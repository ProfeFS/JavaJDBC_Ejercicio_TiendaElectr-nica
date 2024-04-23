package edu.cesurformacion.programacion.ejercicios.jdbc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	private static Connection instance;

	private DatabaseConnection() {
	}

	public static Connection getConnection() {
		if (instance == null) {
			try {
				String url = "jdbc:postgresql://localhost:5432/db_tienda";
				String user = "postgres";
				String password = "1234";
				instance = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				throw new RuntimeException("Error connecting to the database", e);
			}
		}
		return instance;
	}

	public static void closeConnection() {
		if (instance != null) {
			try {
				instance.close();
				instance = null;
			} catch (SQLException e) {
				throw new RuntimeException("Error closing the database connection", e);
			}
		}
	}

}
