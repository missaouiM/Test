package com.filrouge.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAOUtilitaire {
	public static void fermetureSilencieuse(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();

			} catch (SQLException e) {
				System.out.println("Echec de fermeture du ResultSet " + e.getMessage());
			}
		}

	}

	public static void fermetureSilencieuse(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("Echec de fermeture du statement " + e.getMessage());
			}
		}

	}

	public static void fermetureSilencieuse(Connection connexion) {
		if (connexion != null) {
			try {
				connexion.close();
			} catch (SQLException e) {
				System.out.println("Echec de fermeture du connection " + e.getMessage());
			}
		}

	}

	public static void fermeturesSilencieuses(Statement statement, Connection connexion) {
		fermetureSilencieuse(statement);
		fermetureSilencieuse(connexion);

	}

	public static void fermeturesSilencieuses(ResultSet resultSet, Statement statement, Connection connexion) {
		fermetureSilencieuse(resultSet);
		fermetureSilencieuse(statement);
		fermetureSilencieuse(connexion);

	}

	public static PreparedStatement initialisationRequetePreparee(Connection connexion, String sql,
			boolean returnGeneratedKeys, Object... objets) throws SQLException {
		PreparedStatement preparedStatement = connexion.prepareStatement(sql,
				returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
		if (objets != null) {
			for (int i = 0; i < objets.length; i++) {
				preparedStatement.setObject(i + 1, objets[i]);

			}
		}

		return preparedStatement;

	}

}
