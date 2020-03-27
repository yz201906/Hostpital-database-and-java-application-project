package Database.Models;

import java.sql.*;
import Database.DBConnection;
import Database.QueryResult;

public class Address {
	static Connection conn = DBConnection.getConnection();
	private int AddressID;
	private String Street;
	private int Number;
	private String City;
	private String State;
	private String Country;
	private String ZipCode;

	public Address(int id, String street, int number, String city, String state, String country, String zipCode) {
		this.AddressID = id;
		this.Street = street;
		this.Number = number;
		this.City = city;
		this.State = state;
		this.Country = country;
		this.ZipCode = zipCode;
	}

	public int getAddressID() {
		return this.AddressID;
	}

	public String getStreet() {
		return this.Street;
	}

	public int getNumber() {
		return this.Number;
	}

	public String getCity() {
		return this.City;
	}

	public String getState() {
		return this.State;
	}

	public String getCountry() {
		return this.Country;
	}

	public String getZipCode() {
		return this.ZipCode;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO ADDRESS VALUES(?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, AddressID);
			stmt.setString(2, Street);
			stmt.setInt(3, Number);
			stmt.setString(4, City);
			stmt.setString(5, State);
			stmt.setString(6, Country);
			stmt.setString(7, ZipCode);
			stmt.execute();
			result = new QueryResult(true, "Address was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding address to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

}
