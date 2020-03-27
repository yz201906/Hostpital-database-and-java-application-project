package Database.Models;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import Database.DBConnection;

public class Staff {
	static Connection conn = DBConnection.getConnection();
	private int staffId;
	private int addressId;
	private boolean designation;
	private String firstName;
	private String lastName;
	private Date dateOfBirth;

	public Staff(int id, int addressId, boolean designation, String fName, String lName, Date dOB) {
		this.staffId = id;
		this.addressId = addressId;
		this.designation = designation;
		this.firstName = fName;
		this.lastName = lName;
		this.dateOfBirth = dOB;
	}

	public int getStaffId() {
		return staffId;
	}

	public int getAddressId() {
		return addressId;
	}

	public boolean isDesignation() {
		return designation;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void add() {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO STAFF VALUES(?, ?, ?, ?, ?, ?)");
			stmt.setInt(1, staffId);
			stmt.setInt(2, addressId);
			stmt.setBoolean(3, designation);
			stmt.setString(4, firstName);
			stmt.setString(5, lastName);
			stmt.setDate(6, dateOfBirth);
			stmt.execute();
		} catch (Exception ex) {
			System.out.println("Error in adding staff");
		} finally {
			DBConnection.close(stmt);
		}
	}

	public static Staff getById(int id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Staff staff = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM STAFF WHERE SID=" + id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				staff = new Staff(id, rs.getInt("AID"), rs.getBoolean("ISMEDICAL"), rs.getString("FIRSTNAME"),
						rs.getString("LASTNAME"), rs.getDate("DOB"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting staff");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return staff;
	}

	public static Staff findStaffByCredentials(String lastName, Date dob, String city) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Staff staff = null;
		try {
			stmt = conn.prepareStatement(
					"SELECT * FROM STAFF S, ADDRESS A WHERE S.AID = A.ADDRESSID AND S.LASTNAME=? AND S.DOB=? AND A.CITY=?");
			stmt.setString(1, lastName);
			stmt.setDate(2, dob);
			stmt.setString(3, city);
			rs = stmt.executeQuery();
			while (rs.next()) {
				staff = new Staff(rs.getInt("SID"), rs.getInt("AID"), rs.getBoolean("ISMEDICAL"),
						rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getDate("DOB"));
			}
		} catch (Exception ex) {
			System.out.println("Error in finding staff");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return staff;
	}

	public static Boolean hasAccessToPatientCheckin(int staffId, int patientId, Date checkinStartTime) {
		CallableStatement stmt = null;
		ResultSet rs = null;
		int hasAccess = 0;
		try {
			stmt = conn.prepareCall("BEGIN CheckStaffAccess(?,?,?,?); END;");
			stmt.setInt(1, patientId);
			stmt.setDate(2, checkinStartTime);
			stmt.setInt(3, staffId);
			stmt.registerOutParameter(4, Types.NUMERIC);
			stmt.execute();
			hasAccess = stmt.getInt(4);
		} catch (Exception ex) {
			System.out.println("Error in checking access to patient");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return hasAccess > 0;
	}

	public static ArrayList<Staff> listAllMedicalStaff() {
		ResultSet rs = null;
		ArrayList<Staff> medicalStaff = new ArrayList<>();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM STAFF WHERE ISMEDICAL=?");
			stmt.setInt(1, 1);
			rs = stmt.executeQuery();
			while (rs.next()) {
				medicalStaff.add(new Staff(rs.getInt("SID"), rs.getInt("AID"), rs.getBoolean("ISMEDICAL"),
						rs.getString("FIRSTNAME"), rs.getString("LASTNAME"), rs.getDate("DOB")));
			}
		} catch (Exception ex) {
			System.out.println("Error in listing staff");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return medicalStaff;
	}

	public static Boolean hasAccessToPatientCheckin(int staffId, int patientId, Timestamp checkinStartTime) {
		CallableStatement stmt = null;
		ResultSet rs = null;
		int hasAccess = 0;
		try {
			stmt = conn.prepareCall("BEGIN CheckStaffAccess(?,?,?,?); END;");
			stmt.setInt(1, patientId);
			stmt.setTimestamp(2, checkinStartTime);
			stmt.setInt(3, staffId);
			stmt.registerOutParameter(4, Types.NUMERIC);
			stmt.execute();
			hasAccess = stmt.getInt(4);
		} catch (Exception ex) {
			System.out.println("Error in checking staff access");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return hasAccess > 0;
	}

}
