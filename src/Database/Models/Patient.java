package Database.Models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Database.DBConnection;
import Database.QueryResult;

public class Patient {
	static Connection conn = DBConnection.getConnection();
	private int patientId;
	private String lastName;
	private String firstName;
	private String phoneNumber;
	private Date dateOfBirth;
	private int addressId;

	public Patient(int id, String lName, String fName, String phone, Date dOB, int addressId) {
		this.patientId = id;
		this.lastName = lName;
		this.firstName = fName;
		this.phoneNumber = phone;
		this.dateOfBirth = dOB;
		this.addressId = addressId;
	}

	public int getPatientId() {
		return patientId;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public int getAddressId() {
		return addressId;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO PATIENT VALUES(?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, patientId);
			stmt.setString(2, lastName);
			stmt.setString(3, firstName);
			stmt.setString(4, phoneNumber);
			stmt.setDate(5, dateOfBirth);
			stmt.setInt(6, addressId);
			stmt.execute();
			result = new QueryResult(true, "Patient was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding patient to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static Patient findPatientByCredentials(String lastName, Date dob, String city) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Patient patient = null;
		try {
			stmt = conn.prepareStatement(
					"SELECT * FROM PATIENT P, ADDRESS A WHERE P.ADDRESSID = A.ADDRESSID AND P.LASTNAME=? AND P.DOB=? AND A.CITY=?");
			stmt.setString(1, lastName);
			stmt.setDate(2, dob);
			stmt.setString(3, city);
			rs = stmt.executeQuery();
			while (rs.next()) {
				patient = new Patient(rs.getInt("PID"), rs.getString("LASTNAME"), rs.getString("FIRSTNAME"),
						rs.getString("PHONENO"), rs.getDate("DOB"), rs.getInt("ADDRESSID"));
			}
		} catch (Exception ex) {
			System.out.println("Error in finding patient");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return patient;
	}

	public static ArrayList<Patient> getListOfTreatedPatients(int facilityId) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Patient> patients = new ArrayList<Patient>();
		try {
			stmt = conn.prepareStatement(
					"SELECT P.PID, P.LASTNAME, P.FIRSTNAME, P.PHONENO, P.DOB, P.ADDRESSID FROM PATIENT P, CHECKIN C WHERE C.TreatmentStartTime IS NOT NULL AND C.FACILITYID=? AND C.PID=P.PID AND NOT EXISTS (SELECT * FROM PATIENTREPORTS PR WHERE PR.TreatmentStartTime = C.TreatmentStartTime AND PR.PID = C.PID)");
			stmt.setInt(1, facilityId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				patients.add(new Patient(rs.getInt("PID"), rs.getString("LASTNAME"), rs.getString("FIRSTNAME"),
						rs.getString("PHONENO"), rs.getDate("DOB"), rs.getInt("ADDRESSID")));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting treated patients");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return patients;
	}

	public static ArrayList<Patient> getListOfCheckedInPatients(int facilityId) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Patient> patients = new ArrayList<Patient>();
		try {
			stmt = conn.prepareStatement(
					"SELECT P.PID, P.LASTNAME, P.FIRSTNAME, P.PHONENO, P.DOB, P.ADDRESSID FROM PATIENT P, CHECKIN C WHERE C.TreatmentStartTime IS NULL AND C.FACILITYID=? AND C.PID=P.PID");
			stmt.setInt(1, facilityId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				patients.add(new Patient(rs.getInt("PID"), rs.getString("LASTNAME"), rs.getString("FIRSTNAME"),
						rs.getString("PHONENO"), rs.getDate("DOB"), rs.getInt("ADDRESSID")));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting checked in patients");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return patients;
	}
}
