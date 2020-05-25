package Database.Models;

import java.sql.*;
import java.util.*;

import Database.DBConnection;

public class BodyPart {
	static Connection conn = DBConnection.getConnection();

	private String BPCode;
	private String BPName;

	public BodyPart(String bpcode, String bpname) {
		this.BPCode = bpcode;
		this.BPName = bpname;
	}

	public String getBPCode() {
		return this.BPCode;
	}

	public String getBPName() {
		return this.BPName;
	}

	public static BodyPart getById(String code) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		BodyPart bodyPart = null;
		try {
			stmt = conn.prepareStatement(String.format("SELECT * FROM BODYPARTS WHERE BPCODE='%s'", code));
			rs = stmt.executeQuery();
			while (rs.next()) {
				bodyPart = new BodyPart(code, rs.getString("BPNAME"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting body part");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return bodyPart;
	}

	public static ArrayList<BodyPart> listAll() {
		ResultSet rs = null;
		ArrayList<BodyPart> bodyParts = new ArrayList<BodyPart>();
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM BODYPARTS");
			rs = stmt.executeQuery();
			while (rs.next()) {
				bodyParts.add(new BodyPart(rs.getString("BPCODE"), rs.getString("BPNAME")));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting body parts");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}

		return bodyParts;
	}

	@Override
	public String toString() {
		return getBPCode() + ": " + getBPName();
	}

}
