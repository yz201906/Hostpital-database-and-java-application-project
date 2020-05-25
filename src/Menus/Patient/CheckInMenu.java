package Menus.Patient;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Database.Models.Symptom;
import Menus.Home.homeMenu;

public class CheckInMenu {
	public static void main(String[] args) throws SQLException, ParseException {

		Scanner scan = new Scanner(System.in);

		List<String> sympCode = new ArrayList<String>();
		List<Symptom> symptoms = Symptom.getAll();
		for (Symptom symptom : symptoms) {
			if (!symptom.isAddedByPatient()) {
				sympCode.add(symptom.getSymptomCode());
				System.out.println(symptom.getSymptomCode() + " (" + symptom.getSymptomName() + ")");
			}
		}
		Symptom choice = null;
		String strChoice = "";
		System.out.println("N-1. Other");
		System.out.println("N. Done");

		while (choice == null) {
			System.out.println("Please select a symptom:");
			if (scan.hasNextLine()) {
				strChoice = scan.nextLine();
				if (strChoice.equalsIgnoreCase("n-1") || strChoice.equalsIgnoreCase("n")) {
					break;
				}
				for (Symptom symptom : symptoms) {
					if (symptom.getSymptomCode().equalsIgnoreCase(strChoice)) {
						choice = symptom;
						break;
					}
				}
			} else {
				System.out.println("An error occurred reading your input.");
				CheckInMenu.main(null);
				return;
			}
		}

		String newSymp; // for when 'other' is selected
		if (strChoice.equalsIgnoreCase("n-1")) {
			System.out.println("Please describe symptom:");
			if (scan.hasNextLine()) {
				newSymp = scan.nextLine();

				Symptom symp = new Symptom(Symptom.generateCode(), newSymp, false, true, null, null);
				symp.add();

				SymptomMeta.main(symp.getSymptomCode());
			} else {
				System.out.println("An error occurred reading your input.");
				CheckInMenu.main(null);
				return;
			}

		} else if (strChoice.equalsIgnoreCase("n")) {
			homeMenu.main(null);
		} else {
			SymptomMeta.main(choice.getSymptomCode());
		}

	}
}
