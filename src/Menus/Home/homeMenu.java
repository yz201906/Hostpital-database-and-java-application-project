package Menus.Home;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class homeMenu {
	public static void main(String[] args) throws SQLException, ParseException {
		int choice = 0;
		while (choice <= 0 || choice > 4) {
			System.out.println("Please select an item:");
			System.out.println("1. Sign In");
			System.out.println("2. Patient Sign Up");
			System.out.println("3. Demo Queries");
			System.out.println("4. Exit");
			Scanner scan = new Scanner(System.in);
			if (scan.hasNextInt())
				choice = scan.nextInt();
			else if (scan.hasNextLine())
				scan.nextLine();
		}

		if (choice == 1) {
			SignIn.main(null);
		} else if (choice == 2) {
			SignUp.main(null);
		} else if (choice == 3) {
			DemoQueries.main(null);
		}
	}
}
