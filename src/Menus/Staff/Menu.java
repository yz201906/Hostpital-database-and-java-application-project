package Menus.Staff;

import java.util.Scanner;

import Menus.Main;
import Menus.Home.SignIn;
import Menus.Home.homeMenu;

public class Menu {

	public static void main(String[] args) {
		new Menu();
	}

	public Menu() {
		if (Main.StaffId == -1) {
			System.out.println("Please sign in as a staff member to open the staff menu.");
			SignIn.main(null);

			return;
		}
		DisplayOptions();
	}

	public void DisplayOptions() {
		Output();
		int input = getInput();

		Choose(input);
	}

	public void Output() {
		System.out.println("1. Get Checked-in patient list");
		System.out.println("2. Treated patient list");
		System.out.println("3. Add symptoms");
		System.out.println("4. Add severity scale");
		System.out.println("5. Add assessment rule");
		System.out.println("6. Go back");
	}

	public int getInput() {
		int choice = 0;
		while (choice > 6 || choice < 1) {
			System.out.println("Select an option:");
			Scanner pickANumber = new Scanner(System.in);
			if (pickANumber.hasNextInt()) {
				choice = pickANumber.nextInt();
			} else if (pickANumber.hasNextLine()) {
				pickANumber.nextLine();
			}
		}
		return choice;
	}

	public void Choose(int choice) {
		try {
			switch (choice) {
				case 1: {
					ProcessPatient.main(null);
				}
				case 2: {
					TreatedPatientList.main(null);
				}
				case 3: {
					AddSymptoms.main(null);
				}
				case 4: {
					AddSeverityScale.main(null);
				}
				case 5: {
					AddAssessmentRule.main(null);
				}
				case 6: {
					homeMenu.main(null);
				}
			}
		} catch (Exception ex) {
		}
	}
}
