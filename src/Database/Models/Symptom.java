package Database.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;

import Database.DBConnection;
import Database.QueryResult;

public class Symptom {
	static Connection conn = DBConnection.getConnection();

	private String SymptomCode;
	private String SymptomName;
	private boolean AddedByPatient;
	private boolean HasBodyPart;
	private String BpCode;
	private String ScaleName;

	public Symptom(String code, String name, boolean hasBodyPart, boolean userAdd, String bpCode, String scaleName) {
		this.SymptomCode = code;
		this.SymptomName = name;
		this.AddedByPatient = userAdd;
		this.HasBodyPart = hasBodyPart;
		this.BpCode = bpCode;
		this.ScaleName = scaleName;
	}

	public String getScaleName() {
		return ScaleName;
	}

	public String getBpCode() {
		return BpCode;
	}

	public String getSymptomCode() {
		return SymptomCode;
	}

	public String getSymptomName() {
		return SymptomName;
	}

	public boolean isAddedByPatient() {
		return AddedByPatient;
	}

	public boolean hasBodyPart() {
		return HasBodyPart;
	}

	public QueryResult add() {
		PreparedStatement stmt = null;
		QueryResult result = null;
		try {
			stmt = conn.prepareStatement("INSERT INTO SYMPTOMS VALUES(?, ?, ?, ?, ?, ?)");
			stmt.setString(1, SymptomCode);
			stmt.setString(2, SymptomName);
			stmt.setBoolean(3, HasBodyPart);
			stmt.setBoolean(4, AddedByPatient);
			stmt.setString(5, BpCode);
			stmt.setString(6, ScaleName);
			stmt.execute();
			result = new QueryResult(true, "Symptom was added to database");
		} catch (Exception ex) {
			result = new QueryResult(false, "Error in adding Symptom to database");
		} finally {
			DBConnection.close(stmt);
		}
		return result;
	}

	public static Symptom getById(String id) {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Symptom symptom = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM SYMPTOMS WHERE SCODE=?");
			stmt.setString(1, id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				symptom = new Symptom(id, rs.getString("SNAME"), rs.getBoolean("HASBODYPART"),
						rs.getBoolean("ADDEDBYPATIENT"), rs.getString("BPCODE"), rs.getString("SCALENAME"));
			}
		} catch (Exception ex) {
			System.out.println("Error in getting symptom");
		} finally {
			DBConnection.close(stmt);
			DBConnection.close(rs);
		}
		return symptom;
	}

	public static List<Symptom> getAll() {
		List<Symptom> list = new ArrayList<Symptom>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM Symptoms");

			rs = ps.executeQuery();
			while (rs.next()) {
				Symptom c = new Symptom(rs.getString("SCode"), rs.getString("SName"), rs.getBoolean("HasBodyPart"),
						rs.getBoolean("AddedByPatient"), rs.getString("BPCode"), rs.getString("SCALENAME"));

				list.add(c);
			}
		} catch (SQLException e) {
			System.out.println("Error in getting symptoms");
		} finally {
			DBConnection.close(ps);
			DBConnection.close(rs);
		}

		return list;
	}

	public static List<Symptom> getAllByStaff() {
		List<Symptom> list = new ArrayList<Symptom>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * FROM Symptoms WHERE ADDEDBYPATIENT=0");

			rs = ps.executeQuery();
			while (rs.next()) {
				Symptom c = new Symptom(rs.getString("SCode"), rs.getString("SName"), rs.getBoolean("HasBodyPart"),
						rs.getBoolean("AddedByPatient"), rs.getString("BPCode"), rs.getString("SCALENAME"));

				list.add(c);
			}
		} catch (SQLException e) {
			System.out.println("Error in getting symptoms");
		} finally {
			DBConnection.close(ps);
			DBConnection.close(rs);
		}

		return list;
	}

	public static String generateCode() {
		String code = "SYM";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT SCODE FROM Symptoms WHERE ROWNUM <= 1 ORDER BY SCODE DESC");
			rs = ps.executeQuery();
			String lastCode = "";
			while (rs.next()) {
				lastCode = rs.getString("SCODE");
			}
			if (lastCode.isEmpty())
				code += "000";
			else {
				lastCode = lastCode.substring(3);
				int lastNumber = Integer.parseInt(lastCode);
				code += String.format("%03d", (lastNumber + 1));
			}
		} catch (SQLException e) {
			System.out.println("Error in getting symptom code");
		} finally {
			DBConnection.close(ps);
			DBConnection.close(rs);
		}
		return code;
	}
}
