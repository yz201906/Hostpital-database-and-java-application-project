package Menus.Staff;

import java.util.ArrayList;
import java.util.Scanner;

import Database.QueryResult;
import Database.Models.Severity;
import Database.Models.SeverityScale;

public class AddSeverityScale {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		System.out.println("Please enter a name for this severity scale:");
		SeverityScale scale = new SeverityScale(scan.nextLine());
		QueryResult result = scale.add();

		if (!result.IsSuccessful) {
			System.err.println(result.Message);
			new AddSeverityScale();
		}

		ArrayList<Severity> severities = new ArrayList<Severity>();

		int choice = 0;
		int severityInt = 0;
		String severityStr = null;
		while (choice <= 0 || choice > 2) {
			System.out.println("Please select one of the following:");
			System.out.println("1. There's another level for this scale");
			System.out.println("2. There are no more levels, go back");

			if (scan.hasNextInt()) {
				choice = scan.nextInt();
				if (choice == 1) {
					System.out.println("Please enter new severity level:");
					if (scan.hasNextInt()) {
						severityInt = scan.nextInt();
					} else {
						severityStr = scan.nextLine();
					}
					Severity severity = new Severity(0, severityStr, severityInt, scale.getName());
					severities.add(severity);

					choice = 0;
				}
			} else if (scan.hasNextLine()) {
				scan.nextLine();
			}
		}

		if (!severities.isEmpty()) {
			for (Severity s : severities) {
				s.setSeverityId(Severity.generateSeverityId());
				QueryResult result2 = s.add();

				if (!result2.IsSuccessful) {
					System.err.println(result2.Message);
					new AddSeverityScale();
				}
			}
		}

		// User chose option 2 -> to back to Staff Main Menu
		Menus.Staff.Menu.main(null);
	}
}
