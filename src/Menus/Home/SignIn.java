package Menus.Home;

import java.sql.Date;
import java.util.*;

import Database.Models.Facility;
import Database.Models.Patient;
import Database.Models.Staff;
import Database.Models.StfWorksAtDpt;
import Menus.Main;
import Menus.Patient.Routing;

public class SignIn {

	private static Date getValidDOB() {
		try {
			Scanner s = new Scanner(System.in);
			if (s.hasNextLine()) {
				return Date.valueOf(s.nextLine());
			} else {
				return null;
			}
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static void main(String[] args) {

		try {
			ArrayList<Facility> facilities = Facility.listAll();
			ArrayList<Integer> fids = new ArrayList<>();
			for (Facility facility : facilities) {
				int fid = facility.getFacilityId();
				System.out.println(fid + ". " + facility.getFacilityName());
				fids.add(fid);
			}

			Main.FacilityId = 0;
			while (!fids.contains(Main.FacilityId)) {
				System.out.println("Please choose a facility:");
				Scanner scan = new Scanner(System.in);
				if (scan.hasNextInt())
					Main.FacilityId = scan.nextInt();
				else if (scan.hasNextLine())
					scan.nextLine();
			}

			Scanner s = new Scanner(System.in);
			System.out.println("Enter your last name");
			String lastName = null;
			if (s.hasNextLine()) {
				lastName = s.nextLine();
			}

			Date dob = null;
			while (dob == null) {
				System.out.println("Enter your date of birth in yyyy-[m]m-[d]d format:");
				dob = getValidDOB();
			}

			System.out.println("Enter your city of address");

			String city = null;
			while (s.hasNextLine()) {
				city = s.nextLine();
				if (city != null) {
					break;
				} else {
					System.out.println("City cannot be empty, please enter a city");
				}
			}
			System.out.println("Are you a patient? Type yes or no");
			String yesNo = "";
			while (s.hasNextLine()) {
				yesNo = s.nextLine();
				if (yesNo.equals("yes") || yesNo.equals("no"))
					break;
				else {
					System.out.println("Invalid input. Please type yes or no");
				}
			}

			int choice = 0;
			while ((choice != 1) && (choice != 2)) {
				System.out.print("Please select an option:\n1. Sign-In\n2. Go Back\n");
				Scanner scan = new Scanner(System.in);
				if (scan.hasNextInt())
					choice = scan.nextInt();
				else if (scan.hasNextLine())
					scan.nextLine();
			}

			if (choice == 1) {
				if (yesNo.equals("yes")) {
					Patient patient = Patient.findPatientByCredentials(lastName, dob, city);
					if (patient == null) {
						System.out.println("ERROR - Sign-in Incorrect.");
						SignIn.main(null);
					} else {
						Main.PatientId = patient.getPatientId();
						Routing.main(null);
					}

				} else if (yesNo.equals("no")) {
					Staff staff = Staff.findStaffByCredentials(lastName, dob, city);
					if (staff == null) {
						System.out.println("ERROR - Sign-in Incorrect.");
						SignIn.main(null);
					} else {
						Main.StaffId = staff.getStaffId();
						if (!StfWorksAtDpt.doesStaffWorkAtFacility(Main.FacilityId, Main.StaffId)) {
							System.out.println("ERROR - You do not work for this facility.");
							SignIn.main(null);
							Main.StaffId = 0;
						} else {
							Menus.Staff.Menu.main(null);
						}
					}
				}
			} else {
				homeMenu.main(null);
			}
		} catch (Exception ex) {
			System.out.println("Error in signing in");
		}

	}

}
