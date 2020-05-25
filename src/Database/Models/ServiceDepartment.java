package Database.Models;

import java.sql.Connection;
import Database.DBConnection;

public class ServiceDepartment {
	static Connection conn = DBConnection.getConnection();

	private int SdCode;
	private int FacilityId;
	private String DeptName;
	private boolean Type;
	private int StaffId;

	public ServiceDepartment(int code, int id, String name, boolean type, int directorId) {
		this.SdCode = code;
		this.FacilityId = id;
		this.DeptName = name;
		this.Type = type;
		this.StaffId = directorId;
	}

	public int getSdCode() {
		return SdCode;
	}

	public int getFacilityId() {
		return FacilityId;
	}

	public String getDeptName() {
		return DeptName;
	}

	public boolean getType() {
		return Type;
	}

	public int getStaffId() {
		return StaffId;
	}

}
