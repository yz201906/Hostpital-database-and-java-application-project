package DemoQueriesObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import Database.DBConnection;

public class CheckInPhase {
	private static Connection conn = DBConnection.getConnection();

	private String LastName;
	private String FirstName;
	private Timestamp StartTime;
	private int Duration;
	private String FacilityName;
	private String Symptom;

	public CheckInPhase(String lastName, String firstName, Timestamp startTime, int duration, String facilityName,
			String symptom) {
		this.LastName = lastName;
		this.FirstName = firstName;
		this.StartTime = startTime;
		this.Duration = duration;
		this.FacilityName = facilityName;
		this.Symptom = symptom;
	}

	public String getSymptom() {
		return Symptom;
	}

	public int getDuration() {
		return Duration;
	}

	public Timestamp getStartTime() {
		return StartTime;
	}

	public String getFacilityName() {
		return FacilityName;
	}

	public String getFirstName() {
		return FirstName;
	}

	public String getLastName() {
		return LastName;
	}

	public static ArrayList<CheckInPhase> getLongestCheckInPhases() {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		ArrayList<CheckInPhase> phases = new ArrayList<CheckInPhase>();
		try {
			stmt = conn.prepareStatement("SELECT LastName, FirstName, StartTime, DurInMin, FName, SName"
					+ " FROM (SELECT * FROM (SELECT P.PId, P.LastName, P.FirstName, C.StartTime,"
					+ " (C.TreatmentStartTime - C.StartTime) * 24 * 60 AS DurInMin, F.FName"
					+ " FROM Patient P, CheckIn C, Facility F"
					+ " WHERE C.TreatmentStartTime IS NOT NULL AND P.PId = C.PId AND F.FId = C.FacilityId"
					+ " ORDER BY DurInMin DESC) WHERE ROWNUM <= 5) LEFT JOIN"
					+ " (SELECT PatientId, CheckInStartTime, SName FROM Ptnt_Describes_Symp, Symptoms WHERE SymptomCode = SCode)"
					+ " ON PId = PatientId AND StartTime = CheckInStartTime ORDER BY DurInMin DESC");

			rs = stmt.executeQuery();
			while (rs.next()) {
				String symptom = rs.getString("SName");
				if (rs.wasNull()) {
					symptom = "None";
				}
				phases.add(new CheckInPhase(rs.getString("LastName"), rs.getString("FirstName"),
						rs.getTimestamp("StartTime"), rs.getInt("DurInMin"), rs.getString("FName"), symptom));
			}
		} catch (Exception e) {
			System.out.println("Error in check in phases");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return phases;
	}

}
