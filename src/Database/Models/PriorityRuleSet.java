package Database.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DBConnection;
import Database.QueryResult;

public class PriorityRuleSet {
	static Connection conn = DBConnection.getConnection();

	private int RuleSetId;
	private int PriorityId;

	public PriorityRuleSet(int id, int priorityId) {
		this.RuleSetId = id;
		this.PriorityId = priorityId;
	}

	public int getRuleSetId() {
		return RuleSetId;
	}

	public int getPriorityId() {
		return PriorityId;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO PriorityRuleSet (RuleSetId, PriorityId) VALUES (?, ?)");
			stmt.setInt(1, RuleSetId);
			stmt.setInt(2, PriorityId);

			stmt.execute();
			result = new QueryResult(true, "Priority rule set was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding priority rule set to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static PriorityRuleSet getById(int id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PriorityRuleSet priorityRuleSet = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM PriorityRuleSet WHERE RuleSetId = ?");
			stmt.setInt(1, id);

			rs = stmt.executeQuery();
			while (rs.next()) {
				priorityRuleSet = new PriorityRuleSet(id, rs.getInt("PriorityId"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting priority rule set");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return priorityRuleSet;
	}

	public static int getHighestRuleSetId() {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT MAX(RuleSetId) AS m FROM PriorityRuleSet");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("m");
			}
		} catch (SQLException e) {
			System.out.println("Error in getting priority rule set ID");
		} finally {
			DBConnection.close(stmt);
		}

		return -1;
	}
}
