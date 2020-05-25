package Menus.Staff;

import java.sql.Timestamp;
import java.util.*;

import Database.Models.Checkin;
import Database.Models.Patient;
import Menus.Main;

public class TreatedPatientList {

	public static void main(String[] args) {
		try {
			Main.SelectedPatientId = -1;
			Main.SelectedTreatmentStartTime = new Timestamp(0);
			System.out.println("Please choose a patient from this list:");
			ArrayList<Patient> patients = Patient.getListOfTreatedPatients(Main.FacilityId);

			if (patients.size() == 0) {
				System.out.println("This list is empty. Going back to main menu...");
				Menus.Staff.Menu.main(null);
			} else {
				ArrayList<Integer> patientIds = new ArrayList<Integer>();
				for (Patient patient : patients) {
					int pid = patient.getPatientId();
					System.out.println(pid + ". " + patient.getFirstName() + " " + patient.getLastName());
					patientIds.add(pid);
				}

				Scanner s = new Scanner(System.in);

				int patientId = 0;
				while (s.hasNextInt()) {
					patientId = s.nextInt();
					if (!patientIds.contains(patientId)) {
						System.out.println("Please select a valid patient id.");
						continue;
					}
					break;
				}

				System.out.println("1. Check Out");
				System.out.println("2. Go Back");

				int choice = 0;
				while (s.hasNextInt()) {
					choice = s.nextInt();
					if (choice < 1 || choice > 2) {
						System.out.println("Please enter 1 or 2.");
						continue;
					}
					break;
				}

				if (choice == 1) {
					Main.SelectedPatientId = patientId;
					Checkin lastCheckin = Checkin.getLastCheckinByPatientFacilityId(patientId, Main.FacilityId);
					Main.SelectedTreatmentStartTime = lastCheckin.getTreatmentStartTime();

					Menus.Checkout.StaffPatientReport.main(null);
				} else {
					Menus.Staff.Menu.main(null);
				}
			}
		} catch (Exception ex) {
		}
	}

}
