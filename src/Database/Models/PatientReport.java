package Database.Models;

import java.sql.*;
import Database.DBConnection;
import Database.QueryResult;

public class PatientReport {
	static Connection conn = DBConnection.getConnection();

	private int PId;
	private Timestamp TreatmentStartTime;
	private String DeclineDescription;
	private String Treatment;
	private String DischargeStatus;
	private Timestamp ReportSubmitTime;
	private boolean PatientHasAcknowledged;

	public PatientReport(int pid, Timestamp startTime, String declineDescription, String treatment,
			String dischargeStatus, Timestamp reportSubmitTime, boolean patientHasAcknowledged) {
		this.PId = pid;
		this.TreatmentStartTime = startTime;
		this.DeclineDescription = declineDescription;
		this.Treatment = treatment;
		this.DischargeStatus = dischargeStatus;
		this.ReportSubmitTime = reportSubmitTime;
		this.PatientHasAcknowledged = patientHasAcknowledged;
	}

	public boolean hasPatientAcknowledged() {
		return PatientHasAcknowledged;
	}

	public int getPid() {
		return PId;
	}

	public Timestamp getTreatmentStartTime() {
		return TreatmentStartTime;
	}

	public String getDeclineDescription() {
		return DeclineDescription;
	}

	public String getTreatment() {
		return Treatment;
	}

	public String getDischargeStatus() {
		return DischargeStatus;
	}

	public Timestamp getReportSubmitTime() {
		return ReportSubmitTime;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO PatientReports VALUES (?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, PId);
			stmt.setTimestamp(2, TreatmentStartTime);
			stmt.setString(3, DeclineDescription);
			stmt.setString(4, Treatment);
			stmt.setString(5, DischargeStatus);
			stmt.setTimestamp(6, ReportSubmitTime);
			stmt.setBoolean(7, PatientHasAcknowledged);
			stmt.execute();
			result = new QueryResult(true, "Patient Report was added to database");
		} catch (SQLException ex) {

			if (ex.getErrorCode() == 1) // unique constraint
			{
				result = new QueryResult(false, "Patient report already exists in DB.", true);
			} else {
				result = new QueryResult(false, "Error in adding patient report to database");
			}

		} catch (Exception e) {
			result = new QueryResult(false, "Error in adding patient report to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static PatientReport getByPatientIdStartTime(int pid, Timestamp startTime) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PatientReport report = null;
		try {
			Connection conn = DBConnection.getConnection();
			stmt = conn.prepareStatement("SELECT * FROM PatientReports WHERE TreatmentStartTime=? AND PId=" + pid);
			stmt.setTimestamp(1, startTime);
			rs = stmt.executeQuery();
			if (rs.next()) {
				report = new PatientReport(pid, startTime, rs.getString("DeclineDescription"),
						rs.getString("Treatment"), rs.getString("DischargeStatus"), rs.getTimestamp("ReportSubmitTime"),
						rs.getBoolean("PATIENTHASACKNOWLEDGED"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting patient report");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return report;
	}

	public static PatientReport getLastPatientReportByPatientId(int pid) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PatientReport report = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM PatientReports WHERE PID=? ORDER BY TreatmentStartTime DESC");
			stmt.setInt(1, pid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				report = new PatientReport(pid, rs.getTimestamp("TreatmentStartTime"),
						rs.getString("DeclineDescription"), rs.getString("Treatment"), rs.getString("DischargeStatus"),
						rs.getTimestamp("ReportSubmitTime"), rs.getBoolean("PATIENTHASACKNOWLEDGED"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting patient report");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return report;
	}

	public static QueryResult updateDeclineDescription(int pid, Timestamp startTime, String reason) {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement(
					"UPDATE PatientReports SET DeclineDescription =? WHERE TreatmentStartTime=? AND PId=" + pid);
			stmt.setString(1, reason);
			stmt.setTimestamp(2, startTime);
			stmt.execute();
			result = new QueryResult(true, "Patient report was updated");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in updating patient report");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static QueryResult setToHasAcknowledged(int pid, Timestamp startTime) {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement(
					"UPDATE PatientReports SET PATIENTHASACKNOWLEDGED =? WHERE TreatmentStartTime=? AND PId=" + pid);
			stmt.setBoolean(1, true);
			stmt.setTimestamp(2, startTime);
			stmt.execute();
			result = new QueryResult(true, "Patient report was updated");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in updating patient report");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static PatientReport getByPatientIdTreatmentStartTime(int id, Timestamp time) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PatientReport report = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM PATIENTREPORTS WHERE PID=? AND TreatmentStartTime=?");
			stmt.setInt(1, id);
			stmt.setTimestamp(2, time);
			rs = stmt.executeQuery();
			while (rs.next()) {
				report = new PatientReport(id, time, rs.getString("DeclineDescription"), rs.getString("Treatment"),
						rs.getString("DischargeStatus"), rs.getTimestamp("ReportSubmitTime"),
						rs.getBoolean("PATIENTHASACKNOWLEDGED"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting patient report");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return report;
	}
}
