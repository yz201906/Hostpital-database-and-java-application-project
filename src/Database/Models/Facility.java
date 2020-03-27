package Database.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import Database.DBConnection;

public class Facility {
    private static Connection conn = DBConnection.getConnection();

    private int               facilityId;
    private String            facilityName;
    private String            classification;
    private int               availableBeds;
    private int               addressId;

    public Facility ( int fId, String fName, String fClass, int beds, int aId ) {
        this.facilityId = fId;
        this.facilityName = fName;
        this.classification = fClass;
        this.availableBeds = beds;
        this.addressId = aId;
    }

    public int getFacilityId () {
        return facilityId;
    }

    public String getFacilityName () {
        return facilityName;
    }

    public String getClassification () {
        return classification;
    }

    public int getAvailableBeds () {
        return availableBeds;
    }

    public int getAddressId () {
        return addressId;
    }

    public static Facility getById ( int id ) {
        ResultSet rs = null;
        Facility facility = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement( "SELECT * FROM Facility WHERE FId = ?" );
            stmt.setInt( 1, id );
            rs = stmt.executeQuery();
            while ( rs.next() ) {
                facility = new Facility( id, rs.getString( "FNAME" ), rs.getString( "CLASSIFICATION" ),
                        rs.getInt( "BEDS" ), rs.getInt( "ADDRESSID" ) );
            }
        }
        catch ( Exception e ) {
            System.out.println( "Error in getting facility" );
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return facility;
    }

    public static ArrayList<Facility> listAll () {
        ResultSet rs = null;
        ArrayList<Facility> facilities = new ArrayList<Facility>();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement( "SELECT * FROM FACILITY" );
            rs = stmt.executeQuery();
            while ( rs.next() ) {
                facilities.add( new Facility( rs.getInt( "FID" ), rs.getString( "FNAME" ),
                        rs.getString( "CLASSIFICATION" ), rs.getInt( "BEDS" ), rs.getInt( "ADDRESSID" ) ) );
            }
        }
        catch ( Exception ex ) {
            System.out.println( "Error in getting facilities" );
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return facilities;
    }

    public static ArrayList<String> listMostReferredFacilitiesByFacility () {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ArrayList<String> facilityRefer = new ArrayList<String>();
        try {
            stmt = conn.prepareStatement("SELECT FID, RFID "
            + "FROM (SELECT F.FID, PRF.FID AS RFID, COUNT(*) AS RCOUNT "
            + "FROM Ptnt_ReferredByTo_Fclt PRF, CHECKIN C, FACILITY F "
            + "WHERE PRF.PID = C.PID AND PRF.TREATMENTSTARTTIME = C.TREATMENTSTARTTIME AND F.FID = C.FACILITYID "
            + "GROUP BY F.FID, PRF.FID) G1 "
            + "WHERE RCOUNT = (SELECT MAX(RCOUNT) FROM "
            + "(SELECT F.FID, PRF.FID AS RFID, COUNT(*) AS RCOUNT "
            + "FROM Ptnt_ReferredByTo_Fclt PRF, CHECKIN C, FACILITY F "
            + "WHERE PRF.PID = C.PID AND PRF.TREATMENTSTARTTIME = C.TREATMENTSTARTTIME AND F.FID = C.FACILITYID "
            + "GROUP BY F.FID, PRF.FID) G2 "
            + "WHERE G1.FID = G2.FID)");
            rs = stmt.executeQuery();
            while ( rs.next() ) {
                String fromId = Integer.toString( rs.getInt( "FID" ) );
                String toId = Integer.toString( rs.getInt( "RFID" ) );
                facilityRefer.add( fromId + " referred to facility " + toId + " the most." );
            }
        }
        catch ( Exception e ) {
            System.out.println( "Error in getting faciities" );
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return facilityRefer;
    }

    public static int mostNegativeCount = 0;

    public static Facility getMostNegativeFacility () {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                    "SELECT * FROM (SELECT c.FacilityId AS fid, COUNT(*) as theCount FROM Ptnt_Reports_Neg prr, CheckIn c WHERE prr.PatientId = c.PId AND c.TreatmentStartTime = prr.TreatmentStartTime AND (prr.NegativeExperienceCode = 1 OR prr.NegativeExperienceCode = 2) GROUP BY c.FacilityId ORDER BY theCount DESC) WHERE ROWNUM=1" );

            rs = stmt.executeQuery();
            if ( rs.next() ) {
                // count = rs.getInt("theCount");
                mostNegativeCount = rs.getInt( "theCount" );
                return Facility.getById( rs.getInt( "fid" ) );
            }

        }
        catch ( Exception e ) {
            System.out.println( "Error in getting facility" );
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return null;
    }

    public static List<Facility> listFacilitiesWithNoNegExpWithHeart () {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List<Facility> facilites = new ArrayList<Facility>();
        try {
            stmt = conn.prepareStatement( "SELECT FID FROM FACILITY WHERE FID NOT IN ( " + "SELECT F.FID "
                    + "FROM FACILITY F, CHECKIN C, SYMPTOMS S, Ptnt_Describes_Symp D, Ptnt_Reports_Neg R "
                    + "WHERE C.FACILITYID = F.FID AND D.PATIENTID = C.PID AND D.CHECKINSTARTTIME = C.STARTTIME "
                    + "AND D.BPCODE = 'HRT000' "
                    + "AND R.PATIENTID = C.PID AND R.TREATMENTSTARTTIME = C.TREATMENTSTARTTIME)" );

            rs = stmt.executeQuery();
            while ( rs.next() ) {
                facilites.add( getById( rs.getInt( "fid" ) ) );
            }

        }
        catch ( Exception e ) {
            System.out.println( "Error in getting facilities" );
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return facilites;
    }

    public static ArrayList<Facility> listFacilitiesWithNoNeFT ( Timestamp startTime, Timestamp endTime ) {
        ResultSet rs = null;
        PreparedStatement stmt = null;
        ArrayList<Facility> listFacilities = new ArrayList<Facility>();
        try {
            stmt = conn.prepareStatement("SELECT DISTINCT F.FID, F.FNAME, F.CLASSIFICATION, F.BEDS, F.ADDRESSID "
            + "FROM FACILITY F, PATIENTREPORTS PR "
            + "WHERE F.FID NOT IN "
            + "(SELECT CK.FACILITYID "
            + "FROM PTNT_REPORTS_NEG PRN, PATIENTREPORTS PR, CHECKIN CK "
            + "WHERE CK.PID=PR.PID AND PR.PID=PRN.PATIENTID AND CK.TreatmentStartTime=PR.TreatmentStartTime AND PRN.TreatmentStartTime = CK.TreatmentStartTime) "
            + "AND PR.REPORTSUBMITTIME >= ? AND PR.REPORTSUBMITTIME <= ?");
            stmt.setTimestamp( 1, startTime );
            stmt.setTimestamp( 2, endTime );
            rs = stmt.executeQuery();
            while ( rs.next() ) {
                listFacilities.add( new Facility( rs.getInt( "FID" ), rs.getString( "FNAME" ),
                        rs.getString( "CLASSIFICATION" ), rs.getInt( "BEDS" ), rs.getInt( "ADDRESSID" ) ) );
            }
        }
        catch ( Exception e ) {
            System.out.println( "Error in getting facilities" );
        }
        finally {
            DBConnection.close( stmt );
            DBConnection.close( rs );
        }
        return listFacilities;
    }
}
