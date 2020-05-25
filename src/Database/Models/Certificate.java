package Database.Models;

import java.util.Date;

import Database.DBConnection;

import java.sql.*;

public class Certificate {
	static Connection conn = DBConnection.getConnection();

	private String CCode;
	private String CName;
	private Date CDate;
	private Date EDate;
	private int FId;

	public Certificate(String ccode, String cname, Date cdate, Date edate, int fid) {
		this.CCode = ccode;
		this.CName = cname;
		this.CDate = cdate;
		this.EDate = edate;
		this.FId = fid;
	}

	public String getCCode() {
		return this.CCode;
	}

	public String getCName() {
		return this.CName;
	}

	public Date getCDate() {
		return this.CDate;
	}

	public Date getEDate() {
		return this.EDate;
	}

	public int getFId() {
		return this.FId;
	}

}
