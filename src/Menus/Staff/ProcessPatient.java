package Menus.Staff;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import Database.QueryResult;
import Database.Models.Checkin;
import Database.Models.Patient;
import Database.Models.Staff;
import Menus.Main;

public class ProcessPatient {

	public static void main(String[] args) throws SQLException, ParseException {
		Main.SelectedPatientId = -1;
		Main.SelectedCheckinStartTime = new Timestamp(0);

		Staff staff = Staff.getById(Menus.Main.StaffId);
		if (staff == null || !staff.isDesignation()) {
			System.out.println("ERROR - not medical staff");
			Menus.Staff.Menu.main(null);
		}

		Scanner scan = new Scanner(System.in);

		ArrayList<Patient> patients = Patient.getListOfCheckedInPatients(Main.FacilityId);

		if (patients.size() == 0) {
			System.out.println("This list is empty. Going back to main menu...");
			Menus.Staff.Menu.main(null);
		}

		int patientChoice = 0;
		while (patientChoice <= 0 || patientChoice > patients.size()) {
			System.out.println("Please select a patient:");
			for (int i = 0; i < patients.size(); i++) {
				Patient p = patients.get(i);
				System.out.println((i + 1) + ". " + p.getFirstName() + " " + p.getLastName());
			}
			while (!scan.hasNextInt()) {
				System.out.println("Please choose a valid patient number.");
				scan.next();
			}
			patientChoice = scan.nextInt();
		}

		Main.SelectedPatientId = patients.get(patientChoice - 1).getPatientId();
		int processChoice = 0;
		while (processChoice <= 0 || processChoice > 3) {
			System.out.println("Please select an option:");
			System.out.println("1. Enter vitals");
			System.out.println("2. Treat Patient");
			System.out.println("3. Go back");
			if (scan.hasNextInt()) {
				processChoice = scan.nextInt();
			} else if (scan.hasNextLine()) {
				scan.nextLine();
			}
		}

		Checkin lastCheckin = Checkin.getLastCheckinByPatientFacilityId(Main.SelectedPatientId, Main.FacilityId);
		Main.SelectedCheckinStartTime = lastCheckin.getStartTime();

		if (processChoice == 1) {
			Menus.Staff.Vitals.main(null);
		} else if (processChoice == 2) {
			boolean relevant = Staff.hasAccessToPatientCheckin(Main.StaffId, Main.SelectedPatientId,
					Main.SelectedCheckinStartTime);

			if (relevant) {
				QueryResult result = Checkin.checkPatientIn(Main.SelectedPatientId, Main.SelectedCheckinStartTime,
						new Timestamp(System.currentTimeMillis()));
				if (result.IsSuccessful) {
					System.out.println("Patient added to treated patient list.");
					Menus.Staff.Menu.main(null);
				} else {
					System.err.println(result.Message);
					ProcessPatient.main(null);
				}
			} else {
				System.out.println("ERROR - inadequate privilege");
				Menus.Staff.Menu.main(null);
			}
		} else {
			Menus.Staff.Menu.main(null);
		}

	}

}
