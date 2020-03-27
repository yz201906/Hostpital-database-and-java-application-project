package DemoQueriesObjects;
import Database.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class query1 {
    private static Connection conn = DBConnection.getConnection();

    private String FirstName;
    private String LastName;
    private String FacilityName;
    private Timestamp StartTime;
    private Timestamp DischargeDate;
    private String NegativeExperience;

    public query1(String firstName, String lastName, String facilityName, Timestamp startTime, Timestamp dischargeDate, String negativeExperience) {
        this.FirstName = firstName;
        this.LastName = lastName;
        this.StartTime = startTime;
        this.FacilityName = facilityName;
        this.DischargeDate = dischargeDate;
        this.NegativeExperience = negativeExperience;
    }

    public String getNegativeExperience() {
        return NegativeExperience;
    }

    public Timestamp getDischargeDate() {
        return DischargeDate;
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

    public static ArrayList<query1> listDischargedPatientsWithNe() {
        ResultSet rs=null;
        PreparedStatement stmt=null;
        ArrayList<query1> patientInfo = new ArrayList<>();
        try {
            stmt = conn.prepareStatement("SELECT DISTINCT PT.FIRSTNAME, PT.LASTNAME, F.FNAME, CK.STARTTIME, PR.REPORTSUBMITTIME, NE.NAME "
            + "FROM PATIENT PT, CHECKIN CK, NEGATIVEEXPERIENCES NE, FACILITY F, PATIENTREPORTS PR, PTNT_REPORTS_NEG PRN "
            + "WHERE PT.PID=PRN.PATIENTID AND CK.PID=PT.PID AND F.FID=CK.FACILITYID AND PR.PID=PT.PID AND NE.NECODE = PRN.NEGATIVEEXPERIENCECODE "
            + "AND PR.TREATMENTSTARTTIME = CK.TREATMENTSTARTTIME AND PRN.TREATMENTSTARTTIME = CK.TREATMENTSTARTTIME");
            rs = stmt.executeQuery();
            while (rs.next()) {
                patientInfo.add(new query1(rs.getString("FIRSTNAME"),rs.getString("LASTNAME"),rs.getString("FNAME"), rs.getTimestamp("STARTTIME"), rs.getTimestamp("REPORTSUBMITTIME"), rs.getString("NAME")));
            }
        } catch (SQLException e) {
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return patientInfo;
    }
}
