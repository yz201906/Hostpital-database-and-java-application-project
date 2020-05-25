package Menus.Checkout;

import java.util.Scanner;

import Menus.Main;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class ReferralReason {

	public static List<Database.Models.ReferralReason> refReasons = new ArrayList<Database.Models.ReferralReason>();

	public static void main(String[] args) {
		DisplayMenu();
	}

	public static void DisplayMenu() {
		Output();

		int choice = getInput();

		Choose(choice);
	}

	private static void Output() {
		System.out.println("1. Reason");
		System.out.println("2. Go Back");
	}

	private static int getInput() {
		Scanner s = new Scanner(System.in);
		while (s.hasNextInt()) {
			int choice = s.nextInt();
			if (choice > 2 || choice < 1) {
				Output();
				continue;
			}
			return choice;
		}
		return -1;
	}

	private static int getHighestID() {
		int HighestID = Database.Models.ReferralReason.getHighestID();
		for (Database.Models.ReferralReason reason : refReasons) {
			int reason_id = reason.getReasonId();
			if (reason_id > HighestID) {
				HighestID = reason_id;
			}
		}

		return HighestID;
	}

	private static void Choose(int choice) {
		switch (choice) {
		case 1: {
			Map<Integer, String> Reasons = new HashMap<Integer, String>();

			Reasons.put(1, "Service unavailable at time of visit");
			Reasons.put(2, "Service not present at facility");
			Reasons.put(3, "Non payment");

			for (Map.Entry<Integer, String> entry : Reasons.entrySet()) {
				System.out.println(entry.getKey() + ". " + entry.getValue());
			}

			System.out.println("Enter a reason code");
			Scanner s = new Scanner(System.in);

			if (s.hasNextInt()) {
				int code = s.nextInt();
				s.nextLine();

				if (code < 4) {
					System.out.println("Enter the service to refer this patient to");
					if (s.hasNextLine()) {
						String dept = s.nextLine();

						System.out.println("Enter an additional description");

						if (s.hasNextLine()) {
							String description = s.nextLine();
							int highestID = getHighestID();
							Database.Models.ReferralReason reason = new Database.Models.ReferralReason(highestID + 1,
									code, description, dept, Main.SelectedTreatmentStartTime, Main.SelectedPatientId);

							refReasons.add(reason);
						} else {
							System.out.println("Error: Could not read input. Going back to the top of this menu.");
							DisplayMenu();

							return;
						}
					} else {
						System.out.println("Error: Could not read input. Going back to the top of this menu.");
						DisplayMenu();

						return;
					}
				} else {
					System.out.println("Invalid reason specified. Please select a reason.");
				}
				DisplayMenu();
			}
		}
		case 2: {
			ReferralStatus.main(null);
		}
		}
	}

}
