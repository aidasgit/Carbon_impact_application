package model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
//This  was taken from your sample provided
//There was a bug for query without return - fixed
//and some modifications made to suit my program

public class DatabaseConnection {
	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/";

	Connection conn;
	Statement stmt;
	ResultSet rs;

	public boolean makeConnection(String name, String pwd) {

		try {
			// STEP 2: Register JDBC driver
			//Class.forName(JDBC_DRIVER);

			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, name, pwd);
			return true;
		} catch (SQLException se) {
			// Handle errors for JDBC
			//se.printStackTrace();
			return false;
		} catch (Exception e) {
			// Handle errors for Class.forName
			//e.printStackTrace();
			return false;
		}
	}// end make connection

	private void getResults(String query) throws SQLException {
		// Statements allow to issue SQL queries to the database
		stmt = conn.createStatement();
		// Result set get the result of the SQL query
		rs = stmt.executeQuery(query);
	}

	//Executes query and return list of list of strings
	public List<List<String>> executeQueryForResults(String query) throws SQLException {
		//executes query
		getResults(query);
		//stores results from instance variable rs to local variable
		ResultSetMetaData meta = rs.getMetaData();
		//stores amount of columns
		final int columnCount = meta.getColumnCount();
		//local variable for return value
		List<List<String>> rowList = new ArrayList<List<String>>();
		//cycles through returned object by the query
		while (rs.next()) {
			List<String> columnList = new ArrayList<String>();
			//ads string list to result list of list of strings
			rowList.add(columnList);
			//cycles trough all the columns returned
			for (int column = 1; column <= columnCount; column++) {
				//gets value as object from rs object
				Object value = rs.getObject(column);
				//ads that value as a string to list of strings
				columnList.add(String.valueOf(value));
			}
		}
		return rowList;
	}

	//execute a query where we will not get results from the database but just update it
	public void executeUpdate(String query) throws SQLException {
		// Statements allow to issue SQL queries to the database
		stmt = conn.createStatement();
		// Result set get the result of the SQL query
		stmt.executeUpdate(query);
	}

	// start close connections
	public void closeConnection() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
		} // nothing we can do
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}finally{

			System.out.println("disConnected from database!");
			// end finally try
		}
	}
}