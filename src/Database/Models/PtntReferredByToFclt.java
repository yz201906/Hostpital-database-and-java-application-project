package Database.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;

import Database.DBConnection;
import Database.QueryResult;

public class PtntReferredByToFclt {
	static Connection conn = DBConnection.getConnection();

	private int PId;
	private Timestamp TreatmentStartTime;
	private int Sid;
	private Integer FId;

	public PtntReferredByToFclt(int pId, Timestamp treatmentStartTime, int sId, Integer fId) {
		this.PId = pId;
		this.TreatmentStartTime = treatmentStartTime;
		this.Sid = sId;
		this.FId = fId;
	}

	public int getPId() {
		return PId;
	}

	public Timestamp getTreatmentStartTime() {
		return TreatmentStartTime;
	}

	public int getSid() {
		return Sid;
	}

	public Integer getFId() {
		return FId;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement(
					"INSERT INTO Ptnt_ReferredByTo_Fclt (PId, TreatmentStartTime, Sid, FId) VALUES (?, ?, ?, ?)");
			stmt.setInt(1, PId);
			stmt.setTimestamp(2, TreatmentStartTime);
			stmt.setInt(3, Sid);
			if (FId == null)
				stmt.setNull(4, Types.INTEGER);
			else
				stmt.setInt(4, FId);

			stmt.execute();
			result = new QueryResult(true, "Ptnt_ReferredByTo_Fclt was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding Ptnt_ReferredByTo_Fclt to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

}
