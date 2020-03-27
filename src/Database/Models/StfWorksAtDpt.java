package Database.Models;

import java.sql.*;
import java.util.ArrayList;

import Database.DBConnection;

public class StfWorksAtDpt {

	static Connection conn = DBConnection.getConnection();

	private int Fid;
	private String SDCode;
	private Timestamp HireDate;
	private Boolean IsPrimary;
	private int Sid;

	StfWorksAtDpt(int Fid, String SDCode, Timestamp HireDate, Boolean IsPrimary, int Sid) {
		this.Fid = Fid;
		this.SDCode = SDCode;
		this.HireDate = HireDate;
		this.IsPrimary = IsPrimary;
		this.Sid = Sid;
	}

	public int getFid() {
		return Fid;
	}

	public String getSDCode() {
		return SDCode;
	}

	public Timestamp getHireDate() {
		return HireDate;
	}

	public Boolean getPrimary() {
		return IsPrimary;
	}

	public int getSid() {
		return Sid;
	}

	public static boolean doesStaffWorkAtFacility(int Fid, int Sid) {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.prepareStatement("SELECT * FROM Stf_WorksAt_Dpt WHERE Fid = ? AND Sid = ?");
			stmt.setInt(1, Fid);
			stmt.setInt(2, Sid);

			rs = stmt.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (Exception ex) {
			System.out.println("Error in checking staff employment");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return false;
	}

	public static ArrayList<Integer> getStaffIDByFacilityId(int fId) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<Integer> staffId = new ArrayList<>();
		try {
			stmt = conn.prepareStatement("SELECT DISTINCT SID FROM Stf_WorksAt_Dpt WHERE FID=" + fId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				staffId.add(rs.getInt("SID"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting staff ID");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return staffId;
	}
}
