package Menus.Checkout;

import java.util.Scanner;

import Database.QueryResult;
import Database.Models.PtntReportsNeg;
import Menus.Main;

public class StaffPatientReport {

	public static String Description;
	private static String Treatment;
	private static String dischargeStatus;

	public static void main(String[] args) {
		DisplayOptions();
	}

	private static void DisplayOptions() {
		if (Main.SelectedPatientId > -1) {
			Output();

			int choice = getInput();
			Choose(choice);
		} else {
			System.out.println("No patient selected.");
			Menus.Staff.TreatedPatientList.main(null);
		}
	}

	private static void Output() {
		System.out.println("Enter a choice.");
		System.out.println("1. Discharge Status");
		System.out.println("2. Referral Status");
		System.out.println("3. Treatment");
		System.out.println("4. Negative Experience");
		System.out.println("5. Go Back");
		System.out.println("6. Submit");
	}

	private static int getInput() {
		Scanner s = new Scanner(System.in);
		while (s.hasNextInt()) {
			int choice = s.nextInt();
			if (choice > 6 || choice < 1) {
				Output();
				continue;
			}

			return choice;
		}

		return -1;
	}

	private static void Choose(int choice) {
		switch (choice) {
		case 1: {
			dischargeStatus = Menus.Checkout.DischargeStatus.main(null); // Call up the DischargeStatus menu and modify
																			// global variable accordingly

			DisplayOptions();
			break;
		}
		case 2: {
			if (dischargeStatus == null || dischargeStatus.equals("Referred")) {
				Menus.Checkout.ReferralStatus.main(null); // Call up the referrakStatus menu and modify global variable
			} else {
				System.out.println("Discharge status has to be referred");
				DisplayOptions();
			}
			break;
		}
		case 3: {
			System.out.println("Enter a treatment description");
			Scanner s = new Scanner(System.in);

			if (s.hasNextLine()) {
				Treatment = s.nextLine();
				System.out.println("Treatment description saved.");
			} else {
				System.out.println("Invalid treatment description");
			}

			DisplayOptions();
			break;
		}
		case 4: {
			NegativeExperience.GetPtntReportsRpt();
			break;
		}
		case 5: {
			Menus.Staff.TreatedPatientList.main(null);
			break;
		}
		case 6: {
			boolean confirmed = Confirmation.Confirm(Description, Treatment, dischargeStatus);
			if (confirmed) {
				if (NegativeExperience.Reports != null && NegativeExperience.Reports.size() > 0) {
					for (PtntReportsNeg report : NegativeExperience.Reports) {
						QueryResult qr = report.add();
						if (!qr.IsSuccessful) {
							System.out.println(qr.Message);
						}
					}
					NegativeExperience.Reports.clear();
				}
				Menus.Staff.Menu.main(null);
			} else {
				DisplayOptions();
			}
			break;
		}
		default: {
			DisplayOptions();
		}
		}
	}
}
