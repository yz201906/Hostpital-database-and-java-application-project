package Database.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.DBConnection;

public class Priority {
	static Connection conn = DBConnection.getConnection();

	private int PriorityID;
	private String Type;

	Priority(int PriorityID, String Type) {
		this.PriorityID = PriorityID;
		this.Type = Type;
	}

	public int getPriorityID() {
		return PriorityID;
	}

	public String getType() {
		return Type;
	}

	public static List<Priority> getAll() {
		List<Priority> list = new ArrayList<Priority>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM Priority");
			rs = ps.executeQuery();
			while (rs.next()) {
				Priority priorityRule = new Priority(rs.getInt("PriorityId"), rs.getString("TYPE"));
				list.add(priorityRule);
			}
		} catch (SQLException e) {
			System.out.println("Error in getting priorities");
		} finally {
			DBConnection.close(ps);
			DBConnection.close(rs);
		}

		return list;
	}

	public static Priority getById(int pid) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Priority priority = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM PRIORITY WHERE PRIORITYID=?");
			stmt.setInt(1, pid);
			rs = stmt.executeQuery();
			while (rs.next()) {
				priority = new Priority(pid, rs.getString("TYPE"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting priority");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return priority;
	}

	@Override
	public String toString() {
		return getType();
	}
}
