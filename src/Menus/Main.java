package Menus;

import Database.DBConnection;
import Menus.Home.homeMenu;

import java.sql.Connection;
import java.sql.Timestamp;

public class Main {
	public static int PatientId = -1;
	public static int StaffId = -1;
	public static int FacilityId = -1;
	public static Timestamp CheckinStartTime = new Timestamp(0);
	public static Timestamp SelectedTreatmentStartTime = new Timestamp(0);
	public static Timestamp SelectedCheckinStartTime = new Timestamp(0);
	public static int SelectedPatientId = -1;

	public static void main(String[] args) throws Exception {
		Connection conn = DBConnection.getConnection();
		if (conn != null) {
			System.out.println("Database Connected!!");
			try {
				homeMenu.main(null); // Start the top level menu
			} finally {
				DBConnection.close(conn);
			}
		} else {
			System.out.println("Database not connected. try again.");
		}

	}
}
