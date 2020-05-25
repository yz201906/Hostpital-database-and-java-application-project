package Menus.Patient;

import java.util.*;

import Database.QueryResult;
import Database.Models.*;
import Enums.SymptomDurationType;
import Menus.*;

public class SymptomMeta {
	public static void main(String symptomCode) {
		try {
			Symptom symptom = Symptom.getById(symptomCode);

			Scanner s = new Scanner(System.in);
			System.out.println("Please specify the following information:");

			System.out.println("1. Body Part");
			String bpCode = "";
			if (symptom.hasBodyPart()) {
				bpCode = symptom.getBpCode();
				System.out.println("Body Part is preselected. BPCode=" + bpCode);
			} else {
				System.out.println("Select body part:");

				ArrayList<BodyPart> bodyParts = BodyPart.listAll();
				ArrayList<String> bpCodes = new ArrayList<>();
				for (BodyPart bodyPart : bodyParts) {
					System.out.println(bodyPart.getBPCode() + " (" + bodyPart.getBPName() + ")");
					bpCodes.add(bodyPart.getBPCode());
				}

				while (s.hasNextLine()) {
					bpCode = s.nextLine();
					if (!bpCodes.contains(bpCode)) {
						System.out.println("Please select a valid code");
						continue;
					}
					break;
				}
			}

			System.out.println("2. Duration");

			System.out.println("Select duration type:");
			System.out.println(SymptomDurationType.MINUTES.getNumVal() + ". Minutes");
			System.out.println(SymptomDurationType.HOURS.getNumVal() + ". Hours");
			System.out.println(SymptomDurationType.DAYS.getNumVal() + ". Days");
			System.out.println(SymptomDurationType.WEEKS.getNumVal() + ". Weeks");
			System.out.println(SymptomDurationType.MONTHS.getNumVal() + ". Months");
			System.out.println(SymptomDurationType.YEARS.getNumVal() + ". Years");

			int durationType = 0;
			while (s.hasNextInt()) {
				durationType = s.nextInt();
				if (durationType < 1 || durationType > 6) {
					System.out.println("Please select a number between 1 and 6");
					continue;
				}
				break;
			}

			Integer duration = null;
			while (duration == null) {
				System.out.println("Enter duration amount (numeric)");
				Scanner scan = new Scanner(System.in);
				if (scan.hasNextInt())
					duration = scan.nextInt();
			}

			System.out.println("3. Reoccuring");
			System.out.println("Is this symptom reoccuring? Type yes or no");
			String yesNo = "";
			while (s.hasNextLine()) {
				yesNo = s.nextLine();
				if (yesNo.isEmpty())
					continue;
				if (yesNo.equals("yes") || yesNo.equals("no"))
					break;
				else {
					System.out.println("Invalid input. Please type yes or no");
				}
			}
			boolean isReoccuring = false;
			if (yesNo.equals("yes"))
				isReoccuring = true;

			System.out.println("4. Severity");

			ArrayList<Severity> serverities = Severity.getSymptomSeverities(symptomCode);

			int severityId = 0;
			int severityIndex = 1;
			if (serverities.size() == 0) {
				System.out.println("No severity scale assigned to this symptom.");
			} else {
				System.out.println("Select the severity of your symptom:");
				HashMap<Integer, Integer> sidToIndex = new HashMap<>();
				for (Severity severity : serverities) {
					int sid = severity.getSeverityId();
					sidToIndex.put(severityIndex, sid);
					if (severity.getName() == null || severity.getName().isEmpty()) {
						System.out.println(severityIndex + ". " + severity.getNumber());
					} else {
						System.out.println(severityIndex + ". " + severity.getName());
					}
					severityIndex++;
				}

				while (s.hasNextInt()) {
					int severitySelected = s.nextInt();
					if (!sidToIndex.containsKey(severitySelected)) {
						System.out.println("Please select a valid number");
						continue;
					}
					severityId = sidToIndex.get(severitySelected);
					break;
				}
			}

			System.out.println("5. Cause");
			System.out.println("Please enter the cause of your symptom (incident)");
			String cause = "";
			while (s.hasNextLine()) {
				cause = s.nextLine();
				if (cause.isEmpty())
					continue;
				break;
			}

			PtntDescribesSymp ptntDescribesSymp = new PtntDescribesSymp(Main.PatientId, Main.CheckinStartTime,
					(severityId == 0 ? null : severityId), symptomCode, bpCode, duration, durationType, isReoccuring,
					cause);
			QueryResult result = ptntDescribesSymp.add();
			if (result.IsSuccessful) {
				CheckInMenu.main(null);
			} else {
				System.err.println(result.Message);
				CheckInMenu.main(null);
			}

		} catch (Exception ex) {
		}
	}

}
