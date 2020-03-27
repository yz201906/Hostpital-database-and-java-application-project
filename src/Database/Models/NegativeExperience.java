package Database.Models;

import java.sql.*;
import java.util.ArrayList;

import Database.DBConnection;

public class NegativeExperience {

	static Connection conn = DBConnection.getConnection();

	private int NECode;
	private String Name;

	public NegativeExperience(int NECode, String Name) {
		this.NECode = NECode;
		this.Name = Name;
	}

	public int getNECode() {
		return NECode;
	}

	public String getName() {
		return Name;
	}

	public static ArrayList<NegativeExperience> listAll() {
		ResultSet rs = null;
		ArrayList<NegativeExperience> experiences = new ArrayList<NegativeExperience>();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM NegativeExperiences");
			rs = stmt.executeQuery();
			while (rs.next()) {
				experiences.add(new NegativeExperience(rs.getInt("NECode"), rs.getString("Name")));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting negative experiences");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return experiences;
	}
}
