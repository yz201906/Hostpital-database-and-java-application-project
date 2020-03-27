package Menus.Staff;

import java.util.*;

import Database.QueryResult;
import Database.Models.*;

public class AddSymptoms {
	public static void main(String[] args) {
		try {
			System.out.println("Please enter Symptom name");

			Scanner s = new Scanner(System.in);

			String symptomName = "";
			while (s.hasNextLine()) {
				symptomName = s.nextLine();
				if (symptomName.isEmpty())
					continue;
				break;
			}

			System.out.println("Select body part");

			System.out.println("Enter 'no' if no body part is associated");
			ArrayList<BodyPart> bodyParts = BodyPart.listAll();
			ArrayList<String> bpCodes = new ArrayList<String>();
			for (BodyPart bodyPart : bodyParts) {
				System.out.println(bodyPart.getBPCode() + " (" + bodyPart.getBPName() + ")");
				bpCodes.add(bodyPart.getBPCode());
			}

			String bpCode = "";
			boolean hasBodyPart = true;
			while (s.hasNextLine()) {
				bpCode = s.nextLine();
				if (bpCode.equals("no")) {
					bpCode = null;
					hasBodyPart = false;
					break;
				}
				if (!bpCodes.contains(bpCode)) {
					System.out.println("Please select a valid code");
					continue;
				}
				break;
			}

			System.out.println("Select a severity scale");
			System.out.println("Enter 'no' if no severity scale is applicable");

			ArrayList<SeverityScale> scales = SeverityScale.listAll();
			ArrayList<String> scaleNames = new ArrayList<String>();
			for (SeverityScale severityScale : scales) {
				String scaleName = severityScale.getName();
				System.out.println(scaleName);
				scaleNames.add(scaleName);
			}

			String scale = "";
			while (s.hasNextLine()) {
				scale = s.nextLine();
				if (scale.equals("no")) {
					scale = null;
					break;
				}
				if (!scaleNames.contains(scale)) {
					System.out.println("Please select a valid scale name");
					continue;
				}
				break;
			}

			System.out.println("1. Record");
			System.out.println("2. Go Back");

			int choice = 0;
			while (s.hasNextInt()) {
				choice = s.nextInt();
				if (choice < 1 || choice > 2) {
					System.out.println("Please select 1 or 2");
					continue;
				}
				break;
			}

			if (choice == 1) {
				String code = Symptom.generateCode();
				Symptom symptom = new Symptom(code, symptomName, hasBodyPart, false, bpCode, scale);
				QueryResult result = symptom.add();
				if (result.IsSuccessful) {
					Menus.Staff.Menu.main(null);
				} else {
					System.err.println(result.Message);
					AddSymptoms.main(null);
				}
			}
			else {
				Menu.main(null);
			}
		} catch (Exception ex) {
			AddSymptoms.main(null);
		}
	}
}
