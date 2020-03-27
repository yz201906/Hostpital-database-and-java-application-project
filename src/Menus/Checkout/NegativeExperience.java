package Menus.Checkout;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Database.Models.PtntReportsNeg;
import Menus.Main;

public class NegativeExperience {
	public static List<PtntReportsNeg> Reports = new ArrayList<PtntReportsNeg>();

	private static int DisplayMenu() {
		int choice = 0;

		while ((choice != 1) && (choice != 2)) {
			System.out.println("1. Negative Experience Code");
			System.out.println("2. Go back");
			Scanner pickANumber = new Scanner(System.in);
			if (pickANumber.hasNextInt()) {
				choice = pickANumber.nextInt();
			}
		}

		return choice;
	}

	public static void GetPtntReportsRpt() {
		int choice = DisplayMenu();

		if (choice == 1) {
			ArrayList<Database.Models.NegativeExperience> experiences = Database.Models.NegativeExperience.listAll();
			if (experiences.isEmpty()) {
				System.out.println("No codes available.");
				Menus.Checkout.StaffPatientReport.main(null);
				return;
			}

			System.out.println("Choose code:");
			ArrayList<Integer> codes = new ArrayList<Integer>();
			for (Database.Models.NegativeExperience experience : experiences) {
				int code = experience.getNECode();
				System.out.println(code + ". " + experience.getName());
				codes.add(code);
			}

			Scanner pickACode = new Scanner(System.in);
			Integer code = null;
			while (!codes.contains(code)) {
				while (!pickACode.hasNextInt()) {
					System.out.println("Please choose a valid code.");
					pickACode.next();
				}
				code = pickACode.nextInt();
				if (!codes.contains(code)) {
					System.out.println("Please choose a valid code.");
				}
			}

			System.out.println("Enter description:");
			Scanner enterDescription = new Scanner(System.in);
			String description = enterDescription.next();

			Reports.add(new PtntReportsNeg(Main.SelectedPatientId, Main.SelectedTreatmentStartTime, code, description));
			GetPtntReportsRpt();
		} else {
			Menus.Checkout.StaffPatientReport.main(null);
		}
	}

}
