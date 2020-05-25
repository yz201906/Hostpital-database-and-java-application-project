package Database.Models;

import java.sql.*;
import Database.DBConnection;
import Database.QueryResult;

public class ReferralReason {
	static Connection conn = DBConnection.getConnection();

	private int RCode;
	private String description;
	private String ServiceName;
	private Timestamp TreatmentStartTime;
	private int PatientID;
	private int ReasonId;

	public ReferralReason(int ReasonId, int rcode, String desc, String svcName, Timestamp treatmentTime, int pid) {
		this.ReasonId = ReasonId;
		RCode = rcode;
		description = desc;
		ServiceName = svcName;
		TreatmentStartTime = treatmentTime;
		PatientID = pid;
	}

	public int getReasonId() {
		return ReasonId;
	}

	public int getRCode() {
		return RCode;
	}

	public String getDescription() {
		return description;
	}

	public String getServiceName() {
		return ServiceName;
	}

	public Timestamp getTreatmentStartTime() {
		return TreatmentStartTime;
	}

	public int getPatientID() {
		return PatientID;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO ReferralReasons VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, ReasonId);
			stmt.setInt(2, RCode);
			stmt.setString(3, description);
			stmt.setString(4, ServiceName);
			stmt.setTimestamp(5, TreatmentStartTime);
			stmt.setInt(6, PatientID);

			stmt.execute();
			result = new QueryResult(true, "Referral reason was added to database");
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 20000)
				result = new QueryResult(false, "You can't add more than 4 reasons", true);
			else {
				result = new QueryResult(false, "Error in adding referral reason to database");
			}
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding referral reason to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static int getHighestID() {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT MAX(ReasonId) AS m FROM ReferralReasons");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("m");
			}
		} catch (SQLException e) {
			System.out.println("Error in getting reason ID");
		} finally {
			DBConnection.close(stmt);
		}

		return -1;
	}
}
