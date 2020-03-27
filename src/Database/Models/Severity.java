package Database.Models;

import static Database.DBConnection.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Database.DBConnection;
import Database.QueryResult;

public class Severity {
	static Connection conn = DBConnection.getConnection();

	private int SeverityId;
	private String Name;
	private int Number;
	private String ScaleName;

	public Severity(int id, String name, int number, String scale) {
		this.SeverityId = id;
		this.Name = name;
		this.Number = number;
		this.ScaleName = scale;
	}

	public int getSeverityId() {
		return SeverityId;
	}

	public void setSeverityId(int severityId) {
		SeverityId = severityId;
	}

	public String getName() {
		return Name;
	}

	public int getNumber() {
		return Number;
	}

	public String getScaleName() {
		return ScaleName;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO SEVERITY VALUES(?, ?, ?, ?)");
			stmt.setInt(1, SeverityId);
			stmt.setString(2, Name);
			stmt.setInt(3, Number);
			stmt.setString(4, ScaleName);
			stmt.execute();
			result = new QueryResult(true, "Severity was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding Severity to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static Severity getById(int id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Severity severity = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM SEVERITY WHERE SEVERITYID=" + id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				severity = new Severity(id, rs.getString("NAME"), rs.getInt("NUMBER"), rs.getString("SCALENAME"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting severity");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return severity;
	}

	public static ArrayList<Severity> getSymptomSeverities(String symptomCode) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Severity> severities = new ArrayList<Severity>();
		try {
			stmt = conn.prepareStatement(
					"SELECT SV.SEVERITYID, SV.\"NAME\", SV.\"NUMBER\", SV.SCALENAME FROM SYMPTOMS SY, SEVERITY SV WHERE SY.SCODE=? AND SV.SCALENAME=SY.SCALENAME");
			stmt.setString(1, symptomCode);
			rs = stmt.executeQuery();
			while (rs.next()) {
				severities.add(new Severity(rs.getInt("SEVERITYID"), rs.getString("NAME"), rs.getInt("NUMBER"),
						rs.getString("SCALENAME")));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting severities");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return severities;
	}

	public static int generateSeverityId() {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int severityId = 0;
		try {
			stmt = conn.prepareStatement("SELECT MAX(SeverityID) AS lastSid FROM Severity");
			rs = stmt.executeQuery(); // Get largest severity id value
			while (rs.next()) {
				// store the id to assign
				severityId = rs.getInt("lastSid") + 1;
			}
		} catch (SQLException e) {
			System.out.println("Error in getting severity ID");
		} finally {
			close(stmt);
			close(rs);
		}
		return severityId;
	}

}
