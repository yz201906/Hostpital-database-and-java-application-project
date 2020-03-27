package Menus.Staff;

import java.util.List;
import java.util.ArrayList;
import Database.Models.Symptom;
import Database.QueryResult;
import Database.Models.*;
import Menus.Staff.Menu;

import java.util.Scanner;

public class AddAssessmentRule {

	List<Symptom> SymptomList = new ArrayList<Symptom>();

	// The priority rules to associate with a priority rule set
	List<PriorityRule> priority_rules = new ArrayList<PriorityRule>();

	public static void main(String[] args) {
		new AddAssessmentRule();
	}

	AddAssessmentRule() {
		DisplayMenu();
	}

	private void DisplayMenu() {
		Output();

		int choice = getInput();
		Choose(choice);
	}

	private void Output() {
		System.out.println("Select a Symptom");

		SymptomList = Symptom.getAllByStaff();
		int i = 0;

		if (SymptomList.size() == 0) {
			System.out.println("No symptoms found. Returning to previous menu");
			Menu.main(null);
		}

		for (; i < SymptomList.size(); ++i) {
			Symptom sym = SymptomList.get(i);
			System.out.println((i + 1) + ". " + sym.getSymptomName());
		}

		i++;

		System.out.println(i + ". Select priority");
	}

	private int getInput() {
		Scanner s = new Scanner(System.in);
		if (s.hasNextInt()) {
			int choice = s.nextInt();
			if (choice > SymptomList.size() + 1 || choice < 1) {
				Output();

				return getInput();
			}
			return choice;
		} else if (s.hasNextLine()) {
			s.nextLine();
			Output();
			return getInput();
		}
		return -1;

	}

	private BodyPart SelectAdditionalBodyPart() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Select additional body part(s) to associate with this rule.");
		System.out.println("Possible Body Parts: ");
		ArrayList<BodyPart> allBp = BodyPart.listAll();
		int size = allBp.size();
		for (int i = 1; i < size + 1; ++i) {
			BodyPart bp = allBp.get(i - 1);
			System.out.println(i + ". " + bp.toString());
		}

		if (scanner.hasNextInt()) {
			int option = scanner.nextInt();

			if (option < size) {
				return allBp.get(option);
			} else {
				System.out.println("Invalid body part.");
				return SelectAdditionalBodyPart();
			}
		} else if (scanner.hasNextLine()) {
			scanner.nextLine();
		}
		return null;
	}

	private Severity getSeverity(Symptom s) {
		Scanner scanner = new Scanner(System.in);

		ArrayList<Severity> severity_list = Severity.getSymptomSeverities(s.getSymptomCode());

		System.out.println("Select a severity");

		for (int i = 1; i < severity_list.size() + 1; ++i) {
			Severity sev = severity_list.get(i - 1);

			if (sev.getName() == null)
				System.out.println(i + ". " + sev.getNumber());
			else
				System.out.println(i + ". " + sev.getName());
		}

		if (scanner.hasNextInt()) {
			int selection = scanner.nextInt();

			if (selection < severity_list.size() + 1) {
				return severity_list.get(selection - 1);
			} else {
				System.out.println("Invalid severity");
				return getSeverity(s);
			}
		} else if (scanner.hasNextLine()) {
			scanner.nextLine();

			System.out.println("Invalid severity");
			return getSeverity(s);
		} else {
			System.out.println("Input could not be read, restarting menu.");
			DisplayMenu();
			return null;
		}
	}

	private Integer selectOperator() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Select operator (> / = / <)");
		Integer i = null;
		if (scanner.hasNext()) {
			String select = scanner.next();

			switch (select.charAt(0)) {
			case '<': {
				i = new Integer(1);
				break;
			}
			case '=': {
				i = new Integer(2);
				break;
			}
			case '>': {
				i = new Integer(3);
				break;
			}
			default: {
				System.out.println("Invalid operator.");
				return selectOperator();
			}
			}
		} else {
			System.out.println("Input could not be read. Starting menu over.");
			DisplayMenu();

			return null;
		}

		return i;
	}

	private Priority getPriority() {
		List<Priority> priorities = Priority.getAll();
		for (int i = 1; i < priorities.size() + 1; ++i) {
			System.out.println(i + ". " + priorities.get(i - 1).toString());
		}
		System.out.println("Enter a Priority");

		Scanner scanner = new Scanner(System.in);

		if (scanner.hasNextInt()) {
			int selection = scanner.nextInt();
			if (selection < priorities.size() + 1) {
				return priorities.get(selection - 1);
			} else {
				return getPriority();
			}
		} else if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		return null;
	}

	private int getHighestID() {
		int highestID = 0;
		for (PriorityRule rule : priority_rules) {
			int ruleid = rule.getRuleId();
			if (highestID <= ruleid) {
				highestID = ruleid;
			}
		}

		int dbHighestID = PriorityRule.getHighestRuleId();

		if (highestID <= dbHighestID) {
			highestID = dbHighestID;
		}

		return highestID;
	}

	private void Choose(int choice) {
		if (choice < SymptomList.size() + 1) {
			Symptom s = SymptomList.get(choice - 1);

			BodyPart bp = null;

			if (s.hasBodyPart()) {
				String bpCode = s.getBpCode();

				bp = BodyPart.getById(bpCode);

				System.out.println("Body part associated with " + s.getSymptomName());
				System.out.println(bp.toString());
			} else {
				bp = SelectAdditionalBodyPart();
			}

			Severity severity = getSeverity(s);

			Integer op = selectOperator();

			int highestID = getHighestID();

			PriorityRule pr = new PriorityRule(highestID + 1, s.getSymptomCode(), severity.getSeverityId(),
					(bp == null) ? null : bp.getBPCode(), op, -1);

			priority_rules.add(pr);

			System.out.println(
					"Symptom added to the priority rule. Select another symptom or set the priority for this rule.");

			DisplayMenu();

		} else if (choice == SymptomList.size() + 1) {
			if (priority_rules.size() != 0) {
				Priority pr;
				while ((pr = getPriority()) == null) {
					System.out.println("Invalid priority");
				}

				System.out.println("Saving priority rule...");

				PriorityRuleSet prs = new PriorityRuleSet(PriorityRuleSet.getHighestRuleSetId() + 1,
						pr.getPriorityID());
				QueryResult qr = prs.add();
				if (!qr.IsSuccessful) {
					System.out.println("Error adding the priority rule set. Going back to previous menu");
					priority_rules.clear();

					Menu.main(null);
				}

				for (PriorityRule rule : priority_rules) {
					rule.setRuleSetId(prs.getRuleSetId());
					qr = rule.add();
					if (!qr.IsSuccessful) {
						System.out.println("Error adding one of the priority rules.");
					}
				}

				priority_rules.clear();

				Menu.main(null);
			} else {
				System.out.println(
						"Error: No symptoms have been selected for this Priority Rule Set. Please select at least one symptom before a priority.");
				DisplayMenu();
			}
		}
	}
}
