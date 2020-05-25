package Database.Models;

import java.sql.*;

import Database.DBConnection;
import Database.QueryResult;

public class Checkin {
	static Connection conn = DBConnection.getConnection();

	private int PId;
	private Timestamp StartTime;
	private Timestamp EndTime;
	private String BloodPressure;
	private double BodyTemperature;
	private Integer StaffId;
	private int FacilityId;
	private Integer PriorityId;
	private Timestamp TreatmentStartTime;

	public Checkin(int pid, Timestamp startTime, Timestamp endTime, String bloodPreasure, double bodyTemperature,
			Integer staffId, int facilityId, Integer priorityId, Timestamp treatmentStartTime) {
		this.PId = pid;
		this.StartTime = startTime;
		this.EndTime = endTime;
		this.BloodPressure = bloodPreasure;
		this.BodyTemperature = bodyTemperature;
		this.StaffId = staffId;
		this.FacilityId = facilityId;
		this.PriorityId = priorityId;
		this.TreatmentStartTime = treatmentStartTime;
	}

	public Timestamp getTreatmentStartTime() {
		return TreatmentStartTime;
	}

	public int getPId() {
		return PId;
	}

	public Integer getPriorityId() {
		return PriorityId;
	}

	public int getFacilityId() {
		return FacilityId;
	}

	public Integer getStaffId() {
		return StaffId;
	}

	public String getBloodPressure() {
		return BloodPressure;
	}

	public double getBodyTemperature() {
		return BodyTemperature;
	}

	public Timestamp getEndTime() {
		return EndTime;
	}

	public Timestamp getStartTime() {
		return StartTime;
	}

	public void setEndTime(Timestamp endTime) {
		EndTime = endTime;
	}

	public void setBloodPressure(String bloodPressure) {
		BloodPressure = bloodPressure;
	}

	public void setBodyTemperature(double bodyTemperature) {
		BodyTemperature = bodyTemperature;
	}

	public void setStaffId(Integer staffId) {
		StaffId = staffId;
	}

	public void setPriorityId(Integer priorityId) {
		PriorityId = priorityId;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO CHECKIN VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, PId);
			stmt.setTimestamp(2, StartTime);
			stmt.setTimestamp(3, EndTime);
			stmt.setString(4, BloodPressure);
			stmt.setDouble(5, BodyTemperature);
			if (StaffId == null)
				stmt.setNull(6, Types.INTEGER);
			else
				stmt.setInt(6, StaffId);
			stmt.setInt(7, FacilityId);

			if (PriorityId == null)
				stmt.setNull(8, Types.INTEGER);
			else
				stmt.setInt(8, PriorityId);

			stmt.setTimestamp(9, TreatmentStartTime);
			stmt.execute();
			
			result = new QueryResult(true, "Checkin was added");
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 20001) {
				result = new QueryResult(false, "Patient can have only one checkin in a facility at a time.", true);
			} else {
				result = new QueryResult(false, "Error in adding checkin to database");
			}
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding checkin to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static Checkin getById(int pid, Timestamp startTime) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Checkin checkin = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM CHECKIN WHERE PID=? AND STARTTIME=?");
			stmt.setInt(1, pid);
			stmt.setTimestamp(2, startTime);
			rs = stmt.executeQuery();
			while (rs.next()) {
				Integer priorityid = rs.getInt("PRIORITYID");
				if (rs.wasNull()) {
					priorityid = null;
				}

				Integer sid = rs.getInt("StaffID");
				if (rs.wasNull()) {
					sid = null;
				}

				Integer fid = rs.getInt("FacilityId");
				if (rs.wasNull()) {
					fid = null;
				}

				checkin = new Checkin(rs.getInt("PId"), rs.getTimestamp("STARTTIME"), rs.getTimestamp("ENDTIME"),
						rs.getString("BLOODPRESSURE"), rs.getDouble("BODYTEMPERATURE"), sid, fid, priorityid,
						rs.getTimestamp("TreatmentStartTime"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting checkin");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return checkin;
	}

	public static Checkin getLastCheckinByPatientFacilityId(int pid, int fid) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Checkin checkin = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM CHECKIN WHERE PID=? AND FACILITYID=? ORDER BY StartTime DESC");
			stmt.setInt(1, pid);
			stmt.setInt(2, fid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				Integer priorityid = rs.getInt("PRIORITYID");
				if (rs.wasNull()) {
					priorityid = null;
				}

				Integer sid = rs.getInt("StaffID");
				if (rs.wasNull()) {
					sid = null;
				}

				checkin = new Checkin(pid, rs.getTimestamp("STARTTIME"), rs.getTimestamp("ENDTIME"),
						rs.getString("BLOODPRESSURE"), rs.getDouble("BODYTEMPERATURE"), sid, fid, priorityid,
						rs.getTimestamp("TreatmentStartTime"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting checkin");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return checkin;
	}

	public static QueryResult checkPatientIn(int pid, Timestamp startTime, Timestamp treatmentStartTime) {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("UPDATE CHECKIN SET TreatmentStartTime=? WHERE PID=? AND STARTTIME=?");
			stmt.setTimestamp(1, treatmentStartTime);
			stmt.setInt(2, pid);
			stmt.setTimestamp(3, startTime);
			stmt.execute();
			result = new QueryResult(true, "Checkin was updated");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in updating checkin");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static QueryResult update(Checkin checkin) {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement(
					"UPDATE CHECKIN SET ENDTIME=?, BLOODPRESSURE=?, BODYTEMPERATURE=?, STAFFID=?, FACILITYID=?, PRIORITYID=?, TreatmentStartTime=? WHERE PID=? AND STARTTIME=?");
			stmt.setTimestamp(1, checkin.getEndTime());
			stmt.setString(2, checkin.getBloodPressure());
			stmt.setDouble(3, checkin.getBodyTemperature());
			Integer sid = checkin.getStaffId();
			if (sid == null) {
				stmt.setNull(4, Types.INTEGER);
			} else {
				stmt.setInt(4, checkin.getStaffId());
			}

			stmt.setInt(5, checkin.getFacilityId());

			Integer priorityId = checkin.getPriorityId();
			if (priorityId == null)
				stmt.setNull(6, Types.INTEGER);
			else
				stmt.setInt(6, priorityId);

			stmt.setTimestamp(7, checkin.getTreatmentStartTime());

			stmt.setInt(8, checkin.getPId());
			stmt.setTimestamp(9, checkin.getStartTime());
			stmt.execute();
			result = new QueryResult(true, "Checkin was updated");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in updating checkin");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static void delete(int pid, Timestamp startTime) {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("DELETE FROM CHECKIN WHERE PID=? AND STARTTIME=?");
			stmt.setInt(1, pid);
			stmt.setTimestamp(2, startTime);
			stmt.execute();
		} catch (Exception ex) {
			System.out.println("Error in deleting checkin");
		} finally {
			DBConnection.close(stmt);
		}
	}

}
