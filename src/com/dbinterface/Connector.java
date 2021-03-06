package com.dbinterface;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is responsible for connecting to a remote
 * mysql database using information given in the the 
 * MyDBInfo file. It is mainly responsible for creating 
 * the connection; closing the connection; and returning
 * a statement object that a client can use to make queries.
 * @author eliezer
 *
 */
public class Connector {
	/*
	 * Connection object responsible for mysql connection.
	 */
	private Connection con;
	
	/*
	 * Statement object responsible for sql queries.
	 */
	private Statement stmt;
	
	/**
	 * Name of remote mysql account.
	 */
	static String account = MyDBInfo.MYSQL_USERNAME;
	
	/**
	 * Password for account.
	 */
	static String password = MyDBInfo.MYSQL_PASSWORD;
	
	/**
	 * Name of server.
	 */
	static String server = MyDBInfo.MYSQL_DATABASE_SERVER;
	
	/**
	 * Name of database.
	 */
	static String database = MyDBInfo.MYSQL_DATABASE_NAME;
	
	/**
	 * Establishes a connection with a mysql repository
	 * located on the Stanford myth machines.
	 */
	public Connector() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection
				( "jdbc:mysql://" + server, account ,password);
			stmt = con.createStatement();
			stmt.executeQuery("USE " + database);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to setup Database connection.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to setup Database connection.");
		}
	}
	
	/**
	 * Allows the client to receive statement
	 * @return Statement object.
	 */
	public Statement getStatement() {
		return this.stmt;
	}
	
	/**
	 * Close the database connection.
	 */
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to close Database connection.");
		}
	}

	public Connection getConnection() {
		return con;
	}
	
	
}
