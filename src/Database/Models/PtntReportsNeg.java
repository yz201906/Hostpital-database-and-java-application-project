package Database.Models;

import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import Database.DBConnection;
import Database.QueryResult;

public class PtntReportsNeg {
	static Connection conn = DBConnection.getConnection();

	private int PatientId;
	private Timestamp TreatmentStartTime;
	private int NegativeExperienceCode;
	private String Description;

	public PtntReportsNeg(int id, Timestamp treatmentStartTime, int negativeExperienceCode, String description) {
		this.PatientId = id;
		this.TreatmentStartTime = treatmentStartTime;
		this.NegativeExperienceCode = negativeExperienceCode;
		this.Description = description;
	}

	public int getPatientId() {
		return PatientId;
	}

	public Timestamp getTreatmentStartTime() {
		return TreatmentStartTime;
	}

	public int getNegativeExperienceCode() {
		return NegativeExperienceCode;
	}

	public String getDescription() {
		return Description;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement(
					"INSERT INTO Ptnt_Reports_Neg (PatientId, TreatmentStartTime, NegativeExperienceCode, Description) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, PatientId);
			stmt.setTimestamp(2, TreatmentStartTime);
			stmt.setInt(3, NegativeExperienceCode);
			stmt.setString(4, Description);

			stmt.execute();
			result = new QueryResult(true, "Ptnt_Reports_Neg added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding Ptnt_Reports_Neg to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

}
