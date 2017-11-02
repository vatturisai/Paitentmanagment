package com2002.models;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com2002.utils.Database;

// remind John to remove password from class diag


public abstract class Staff {
	
	private String firstName;
	private String lastName;
	private String username;
	private String role;
	//private String password;
	
	public Staff(String userN, String pass) {
		try {
			Connection conn = Database.getConnection();
			ResultSet rs = DBQueries.execQuery("SELECT * FROM Employees WHERE username = '" 
					+ userN + "' AND password = '" + pass + "'", conn);
			if(rs.next()) {
				firstName = rs.getString("FirstName");
				lastName = rs.getString("LastName");
				username = userN;
				role = rs.getString("Role");
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Staff(String fName, String lName, String userN, String pass, Role r) {
		if(!dbHasUsername(userN)) {
			try {
				DBQueries.execUpdate("INSERT INTO Employees VALUES ('" + fName + "', '" + lName + "', '" 
						+ userN + "', '" + pass + "', '" + getRoleString(r) + "')");
				
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("An error has occurred. A staff member with username " + userN + " might already exist.");
				return;
			}
			firstName = fName;
			lastName = lName;
			username = userN;
			role = getRoleString(r);
		}
		
	}
	
	private boolean dbHasUsername(String userN) {
		String found = DBQueries.getData("Username", "Employees", "Username", userN);
		return userN == found;
		
	}
	
	protected String getFirstName() {
		return firstName;
	}
	
	protected void setFirstName(String firstN) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET FirstName = '" + firstN 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("first name");
			return;
		}
		firstName = firstN;
	}
	
	protected String getLastName() {
		return lastName;
	}
	
	protected void setLastName(String lastN) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET LastName = '" + lastN 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("last name");
			return;
		}
		lastName = lastN;
	}
	
	protected String getUsername() {
		return username;
	}
	
	protected void setUsername(String userN) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET Username = '" + userN 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("username");
			return;
		}
		username = userN;
	}
	
	protected String getRole() {
		return role;
	}
	
	protected void setRole(Role r) {
		try {
			DBQueries.execUpdate("UPDATE Employees SET Role = '" + getRoleString(r) 
					+ "' WHERE Username = '" + username + "'");
		} catch (SQLException e) {
			e.printStackTrace();
			printError("role");
			return;
		}
		role = getRoleString(r);
	}
	
	protected String getRoleString(Role r) {
		if(r == Role.HYGIENIST) {
			return "Hygienist";
		} else if(r == Role.SECRETARY) {
			return "Secretary";
		}
		return "Dentist";
	}
	
	private void printError(String method) {
		System.out.println("Something went wrong with updating the " + method + ". "
				+ "The staff member may have not been initialised properly "
				+ "(some instance variables might be null).");
	}
	
	public static void main(String[] args) {
		Staff arthur = new Secretary("Arthur", "Granacher", "ayjee", "password");
		System.out.println(arthur.getFirstName() + " " + arthur.getLastName() + " with username " 
		+ arthur.getUsername() + " is a " + arthur.getRole());
	}
	
}
