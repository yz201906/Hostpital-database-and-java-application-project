package Menus.Checkout;

import java.util.Scanner;

public class DischargeStatus {
	public static String main(String[] args) {
		int choice = 0;
		while ((choice != 1) && (choice != 2) && (choice != 3) && (choice != 4)) { // Cycle menu choices
			System.out.println("1.Successful treatment");
			System.out.println("2.Deceased");
			System.out.println("3.Referred");
			System.out.println("4.Go back");
			Scanner pickANumber = new Scanner(System.in);
			if (pickANumber.hasNextInt()) {
				choice = pickANumber.nextInt(); // Store choice
			} else {
				System.out.println("Invalid selection");
			}
		}
		String dischargeStatus = "";
		switch (choice) { // Update dischargeStatus variable depending on the choice
		case 1:
			dischargeStatus = "Successful treatment";
			break;
		case 2:
			dischargeStatus = "Deceased";
			break;
		case 3:
			dischargeStatus = "Referred";
			break;
		case 4:
			break;
		default:
			System.out.println("Invalid selection.");
			break;
		}
		return dischargeStatus;
	}
}
