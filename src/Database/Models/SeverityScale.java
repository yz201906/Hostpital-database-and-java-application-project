package Database.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Database.DBConnection;
import Database.QueryResult;

public class SeverityScale {
	static Connection conn = DBConnection.getConnection();

	private String Name;

	public SeverityScale(String name) {
		this.Name = name;
	}

	public String getName() {
		return Name;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO SEVERITYSCALE VALUES(?)");
			stmt.setString(1, Name);
			stmt.execute();
			result = new QueryResult(true, "Severity scale was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding severity scale to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static ArrayList<SeverityScale> listAll() {
		ResultSet rs = null;
		ArrayList<SeverityScale> scales = new ArrayList<SeverityScale>();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM SEVERITYSCALE");
			rs = stmt.executeQuery();
			while (rs.next()) {
				scales.add(new SeverityScale(rs.getString("NAME")));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting severity scales");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}

		return scales;
	}

}
