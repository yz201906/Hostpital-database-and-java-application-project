package Database.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.DBConnection;
import Database.QueryResult;

public class PriorityRule {
	static Connection conn = DBConnection.getConnection();

	private int RuleId;
	private String SCode;
	private Integer SeverityId;
	private String BPCode;
	private Integer Operator;
	private int RuleSetId;

	public PriorityRule(Integer id, String sCode, Integer severityId, String bPCode, Integer operator, int ruleSetId) {
		this.RuleId = (id == null) ? getHighestRuleId() + 1 : id;
		this.SCode = sCode;
		this.SeverityId = severityId;
		this.BPCode = bPCode;
		this.Operator = operator;
		this.RuleSetId = ruleSetId;
	}

	public void setRuleSetId(int RuleSetId) {
		this.RuleSetId = RuleSetId;
	}

	public int getRuleId() {
		return RuleId;
	}

	public String getSCode() {
		return SCode;
	}

	public Integer getSeverityId() {
		return SeverityId;
	}

	public String getBPCode() {
		return BPCode;
	}

	public Integer getOperator() {
		return Operator;
	}

	public int getRuleSetId() {
		return RuleSetId;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement(
					"INSERT INTO PriorityRules (RuleId, SCode, SeverityId, BPCode, Operator, RuleSetId) VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, RuleId);
			stmt.setString(2, SCode);
			if (SeverityId == null)
				stmt.setNull(3, Types.INTEGER);
			else
				stmt.setInt(3, SeverityId);
			if (BPCode == null) {
				stmt.setNull(4, Types.CHAR);
			} else {
				stmt.setString(4, BPCode);
			}

			if (Operator == null)
				stmt.setNull(5, Types.INTEGER);
			else
				stmt.setInt(5, Operator);

			stmt.setInt(6, RuleSetId);

			stmt.execute();
			result = new QueryResult(true, "Priority rule was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding priority rule to data base");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static int getHighestRuleId() {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT MAX(RuleId) AS m FROM PriorityRules");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("m");
			}
		} catch (SQLException e) {
			System.out.println("Error in getting rule ID");
		} finally {
			DBConnection.close(stmt);
		}

		return -1;
	}

	public static List<PriorityRule> listAllPriorityRules() {
		List<PriorityRule> list = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM PRIORITYRULES");
			rs = ps.executeQuery();
			while (rs.next()) {
				PriorityRule priorityRule = new PriorityRule(rs.getInt("RULEID"), rs.getString("SCODE"),
						rs.getInt("SEVERITYID"), rs.getString("BPCODE"), rs.getInt("OPERATOR"), rs.getInt("RULESETID"));
				list.add(priorityRule);
			}
		} catch (SQLException e) {
			System.out.println("Error in getting priority rules");
		} finally {
			DBConnection.close(ps);
			DBConnection.close(rs);
		}

		return list;
	}
}
