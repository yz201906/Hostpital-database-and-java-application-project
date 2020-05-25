package Menus.Home;

import Database.DBConnection;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import static Database.DBConnection.close;
import Database.Models.Address;
import Database.Models.Patient;
import Database.QueryResult;

public class SignUp {

	private static Date getValidDOB() {
		try {
			Scanner s = new Scanner(System.in);
			Date entered = Date.valueOf(s.nextLine());
			long millis = System.currentTimeMillis();
			java.sql.Date currDate = new java.sql.Date(millis);
			if (entered.after(currDate)) {
				System.out.println("Please enter a date in the past.");
				return null;
			}
			return entered;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static void main(String[] args) throws SQLException, ParseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<String> flName;
		Scanner pName = new Scanner(System.in);
		String namePattern = ".*[\\s].*";

		String input = null;
		while (input == null) {
			System.out.println("Please enter your first and last name (separated by space):");
			String nextLine = pName.nextLine();
			if (nextLine.matches(namePattern)) {
				input = nextLine;
			} else
				System.out.println("You entered an INVALID name format.");
		}
		flName = Arrays.asList(input.split("\\s"));

		Date dateOfBirth = null;
		while (dateOfBirth == null) {
			System.out.println("Please enter your date of birth in yyyy-[m]m-[d]d format:");
			dateOfBirth = getValidDOB();
		}

		List<String> pAddress = null;
		Scanner pAddr = new Scanner(System.in);
		String addressPattern = "[^\\n^,]*[,][\\s]*[\\d]*[,][\\s]*[^\\n^,]*[,][\\s]*[^\\n^,]*[,][\\s]*[^\\n^,]*[,][\\s]*[\\d]*";

		input = null;
		while (input == null) {
			System.out.println("Please enter your address (streetname, streetnumber, city, state, country, zipcode):");
			String nextLine = pAddr.nextLine();
			if (nextLine.matches(addressPattern)) {
				input = nextLine;
				pAddress = Arrays.asList(input.split(","));
				try {
					int number = Integer.parseInt(pAddress.get(1).trim());
				} catch (NumberFormatException e) {
					System.out.println("You entered an INVALID streetnumber.");
					input = null;
				}
			} else
				System.out.println("You entered an INVALID Home Address format.");
		}

		System.out.println("Please enter your phone number:");
		Scanner pno = new Scanner(System.in);
		String phoneNumber = null;
		if (pno.hasNext()) {
			phoneNumber = pno.next();
		}
		int aId = 0;
		int pId = 0;
		try {
			Connection conn = DBConnection.getConnection();
			stmt = conn.prepareStatement("SELECT MAX(AddressID) AS lastAid FROM Address");
			rs = stmt.executeQuery(); // Get largest address id value
			while (rs.next())
				aId = rs.getInt("lastAid") + 1; // store the id to assign
		} catch (Exception ex) {
			System.out.println("Error in getting next address ID");
		} finally {
			close(stmt);
			close(rs);
		}
		try {
			Connection conn = DBConnection.getConnection();
			stmt = conn.prepareStatement("SELECT MAX(PId) AS lastPid FROM Patient");
			rs = stmt.executeQuery(); // Get largest patient id value
			while (rs.next())
				pId = rs.getInt("lastPid") + 1; // store the id to assign
		} catch (Exception ex) {
			System.out.println("Error in getting next patient ID");
		} finally {
			close(stmt);
			close(rs);
		}

		int choice = 0;
		while ((choice != 1) && (choice != 2)) { // Cycle the menu until 1 or 2 is selected
			System.out.println("1.Sign up");
			System.out.println("2.Go back");
			Scanner pickANumber = new Scanner(System.in);
			if (pickANumber.hasNextInt()) {
				choice = pickANumber.nextInt();
			}
		}

		switch (choice) {
		case 2:
			homeMenu.main(null);
		case 1: // Create new Address and Patient objects and write to tables
			Address createAddress = new Address(aId, pAddress.get(0).trim(), Integer.parseInt(pAddress.get(1).trim()),
					pAddress.get(2).trim(), pAddress.get(3).trim(), pAddress.get(4).trim(), pAddress.get(5));
			QueryResult result = createAddress.add();
			if (!result.IsSuccessful) {
				System.err.println(result.Message);
				SignUp.main(null);
			}
			Patient createPatient = new Patient(pId, flName.get(1), flName.get(0), phoneNumber, dateOfBirth, aId);
			result = createPatient.add();
			if (!result.IsSuccessful) {
				System.err.println(result.Message);
				SignUp.main(null);
			}
			SignIn.main(null);
		}
	}
}
