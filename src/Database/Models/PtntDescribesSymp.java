package Database.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Database.DBConnection;
import Database.QueryResult;

public class PtntDescribesSymp {
	static Connection conn = DBConnection.getConnection();

	private int PatientId;
	private Timestamp CheckinStartTime;
	private Integer SeverityId;
	private String SymptomCode;
	private String BPCode;
	private int Duration;
	private int DurationType;
	private Boolean Occurance;
	private String Cause;

	public PtntDescribesSymp(int patientId, Timestamp checkinStartTime, Integer severityId, String symptomCode,
			String bpCode, int duration, int durationType, Boolean occurance, String cause) {
		this.PatientId = patientId;
		this.CheckinStartTime = checkinStartTime;
		this.SeverityId = severityId;
		this.SymptomCode = symptomCode;
		this.BPCode = bpCode;
		this.Duration = duration;
		this.DurationType = durationType;
		this.Occurance = occurance;
		this.Cause = cause;
	}

	public int getPatientId() {
		return PatientId;
	}

	public String getCause() {
		return Cause;
	}

	public Boolean getOccurance() {
		return Occurance;
	}

	public int getDurationType() {
		return DurationType;
	}

	public int getDuration() {
		return Duration;
	}

	public String getBPCode() {
		return BPCode;
	}

	public String getSymptomCode() {
		return SymptomCode;
	}

	public Integer getSeverityId() {
		return SeverityId;
	}

	public Timestamp getCheckinStartTime() {
		return CheckinStartTime;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO PTNT_DESCRIBES_SYMP VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, PatientId);
			stmt.setTimestamp(2, CheckinStartTime);
			if (SeverityId == null)
				stmt.setNull(3, Types.INTEGER);
			else
				stmt.setInt(3, SeverityId);
			stmt.setString(4, SymptomCode);
			stmt.setString(5, BPCode);
			stmt.setInt(6, Duration);
			stmt.setInt(7, DurationType);
			stmt.setBoolean(8, Occurance);
			stmt.setString(9, Cause);
			stmt.execute();
			result = new QueryResult(true, "PTNT_DESCRIBES_SYMP was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding PTNT_DESCRIBES_SYMP to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static List<PtntDescribesSymp> getRelationFromPatientInfo(int patientId, Timestamp checkinStartTime) {
		List<PtntDescribesSymp> list = new ArrayList<>();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PtntDescribesSymp sympRelations = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM PTNT_DESCRIBES_SYMP WHERE PATIENTID=? AND CHECKINSTARTTIME=?");
			stmt.setInt(1, patientId);
			stmt.setTimestamp(2, checkinStartTime);
			rs = stmt.executeQuery();
			while (rs.next()) {
				sympRelations = new PtntDescribesSymp(patientId, checkinStartTime, rs.getInt("SEVERITYID"),
						rs.getString("SYMPTOMCODE"), rs.getString("BPCODE"), rs.getInt("DURATION"),
						rs.getInt("DURATIONTYPE"), rs.getBoolean("OCCURRENCE"), rs.getString("CAUSE"));
				list.add(sympRelations);
			}
		} catch (Exception ex) {
			System.out.println("Error in getting patient symptom");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return list;
	}

	public static void delete(int patientId, Timestamp checkinStartTime) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM PTNT_DESCRIBES_SYMP WHERE PATIENTID=? AND CHECKINSTARTTIME=?");
			stmt.setInt(1, patientId);
			stmt.setTimestamp(2, checkinStartTime);
			stmt.execute();
		} catch (Exception ex) {
			System.out.println("Error in deleting patient symptoms");
		} finally {
			DBConnection.close(stmt);
		}
	}
}
