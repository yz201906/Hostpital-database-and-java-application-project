package Menus.Staff;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import Database.Models.*;
import Database.QueryResult;
import Menus.Main;

import java.sql.Timestamp;

public class Vitals {
	public static void main(String[] args) throws ParseException, SQLException {
		Double temperature = null;
		Double systolicBloodP = null;
		Double diastolicBloodP = null;

		while (temperature == null) {
			System.out.println("Enter patient temperature:");
			Scanner scanner = new Scanner(System.in);
			if (scanner.hasNextDouble()) {
				temperature = scanner.nextDouble();
			}
		}

		while (systolicBloodP == null) {
			System.out.println("Enter patient Systolic blood pressure:");
			Scanner scanner = new Scanner(System.in);
			if (scanner.hasNextDouble()) {
				systolicBloodP = scanner.nextDouble();
			}
		}

		while (diastolicBloodP == null) {
			System.out.println("Enter patient Diastolic blood pressure:");
			Scanner scanner = new Scanner(System.in);
			if (scanner.hasNextDouble()) {
				diastolicBloodP = scanner.nextDouble();
			}
		}

		String bloodPressure = systolicBloodP + "/" + diastolicBloodP;

		int choice = 0;
		while ((choice != 1) && (choice != 2)) { // Cycle the menu until 1 or 2 is selected
			System.out.println("1.Record");
			System.out.println("2.Go back");
			Scanner scanner = new Scanner(System.in);
			if (scanner.hasNextInt()) {
				choice = scanner.nextInt();
			}
		}
		switch (choice) {
		case 1:
			List<PtntDescribesSymp> pStmptoms = PtntDescribesSymp.getRelationFromPatientInfo(Main.SelectedPatientId,
					Main.SelectedCheckinStartTime);
			HashMap<List<String>, Integer> pSympDict = new HashMap<>();
			for (PtntDescribesSymp pDescribesSymp : pStmptoms) { // Get symptomCodes, severityIds
				List<String> pSCodeBPCode = new ArrayList<>(); // and BPCodes
				pSCodeBPCode.add(pDescribesSymp.getSymptomCode()); // from this patient
				pSCodeBPCode.add(pDescribesSymp.getBPCode()); // and store
				pSympDict.put(pSCodeBPCode, pDescribesSymp.getSeverityId()); // in a hashmap {[sCode,
																				// bpCode]:severityId} as key, and the
																				// rest in a array as values
			}

			List<PriorityRule> pRules = PriorityRule.listAllPriorityRules(); // get all info from priority rules table
			HashMap<List<Integer>, List<String>> rulesDict = new HashMap<>(); // initiate a hashmap to store
																				// priorityrule elements {[RuleId,
																				// severityId,operator,
																				// ruleSetId]:[sCode, bpCode]}
			HashMap<Integer, List<Integer>> satisfyRules = new HashMap<>(); // initiate a hashmap to store ruleSetId and
																			// whether each ruleSet condition is
																			// fulfilled {ruleSetId: [patient symptom
																			// match(int), number of rules for
																			// theset(int)]}
			for (PriorityRule priorityRule : pRules) {
				List<Integer> rIdSvrIdORSId = new ArrayList<>();
				rIdSvrIdORSId.add(priorityRule.getRuleId());
				rIdSvrIdORSId.add(priorityRule.getSeverityId());
				rIdSvrIdORSId.add(priorityRule.getOperator());
				rIdSvrIdORSId.add(priorityRule.getRuleSetId());
				rulesDict.put(rIdSvrIdORSId, new ArrayList<>());
				rulesDict.get(rIdSvrIdORSId).add(priorityRule.getSCode());
				rulesDict.get(rIdSvrIdORSId).add(priorityRule.getBPCode());
				if (satisfyRules.containsKey(priorityRule.getRuleSetId())) {
					int newValue = satisfyRules.get(priorityRule.getRuleSetId()).get(1) + 1;
					satisfyRules.get(priorityRule.getRuleSetId()).set(1, newValue);
				} else {
					satisfyRules.put(priorityRule.getRuleSetId(), new ArrayList<>());
					satisfyRules.get(priorityRule.getRuleSetId()).add(0);
					satisfyRules.get(priorityRule.getRuleSetId()).add(1);
				}
			}
			for (Entry<List<Integer>, List<String>> entry : rulesDict.entrySet()) { // Iterate through the priority
																					// rules hashmap
				List<Integer> key = entry.getKey(); // For each combination of sCode and bpCode
				List<String> value = entry.getValue(); // if severity from patient matches that of severity rule
				if (pSympDict.containsKey(value)) { // increment counter of associated ruleset
					Severity severityFromRules = Severity.getById(key.get(1));
					String sName = severityFromRules.getName();
					int operator = key.get(2);
					int sNumber = severityFromRules.getNumber();

					int pSeverityId = pSympDict.get(value);
					Severity severityFromPatient = Severity.getById(pSeverityId);
					String pSName = severityFromPatient.getName();
					int pSNumber = severityFromPatient.getNumber();

					if (sName == null) {
						if ((operator == 1 || operator == 13) && pSNumber <= sNumber) {
							int oldValue = satisfyRules.get(key.get(3)).get(0) + 1;
							satisfyRules.get(key.get(3)).set(0, oldValue);
						} else if ((operator == 2 || operator == 23) && pSNumber >= sNumber) {
							int oldValue = satisfyRules.get(key.get(3)).get(0) + 1;
							satisfyRules.get(key.get(3)).set(0, oldValue);
						}
					} else if (sNumber == 0) {
						if (sName.equals(pSName)) {
							int oldValue = satisfyRules.get(key.get(3)).get(0) + 1;
							satisfyRules.get(key.get(3)).set(0, oldValue);
						}
					}
				}
			}
			int finalPriorityId = 0;
			for (Entry<Integer, List<Integer>> entry : satisfyRules.entrySet()) { // Finally, iterate through the
																					// satifyRules hashmap
				int rSetId = entry.getKey(); // if value 1 equals value 2 then the ruleset is fulfilled
				List<Integer> conditions = entry.getValue();
				if (conditions.get(0).equals(conditions.get(1))) {
					PriorityRuleSet getPriorityId = PriorityRuleSet.getById(rSetId);
					int priorityId = getPriorityId.getPriorityId(); // Get priorityId for the satisfied ruleset
					Priority getPriorityName = Priority.getById(priorityId);
					String matchingPriorityName = getPriorityName.getType(); // Get type for the satisfied priority
					if (finalPriorityId == 0) {
						finalPriorityId = priorityId;
					}
					Priority getPriorityName1 = Priority.getById(finalPriorityId);
					String recordedPriorityName = getPriorityName1.getType(); // Get type for priority already recorded
																				// in variable
					if (recordedPriorityName.equals("Quarantine")) {
						break;
					} else if (matchingPriorityName.equals("Quarantine")) { // Compare which priority is more severe
						finalPriorityId = priorityId;
						break;
					} else if (matchingPriorityName.equals("High") && recordedPriorityName.equals("Normal")) {
						finalPriorityId = priorityId;
					}
				}
			}

			if (finalPriorityId == 0)
				finalPriorityId = 3;

			Date currentTime = new Date();
			Timestamp endTime = new Timestamp(currentTime.getTime());
			Checkin checkin = Checkin.getById(Main.SelectedPatientId, Main.SelectedCheckinStartTime);
			checkin.setPriorityId(finalPriorityId); // set the determined priorityId
			checkin.setBodyTemperature(temperature); // update from temperature variable
			checkin.setBloodPressure(bloodPressure); // update from blood pressure variable
			checkin.setEndTime(endTime); // update from endTime variable
			checkin.setStaffId(Main.StaffId);
			QueryResult result = Checkin.update(checkin);
			if (!result.IsSuccessful) {
				System.err.println(result.Message);
				Vitals.main(null);
			}

			ProcessPatient.main(null);
		case 2:
			ProcessPatient.main(null);
		}

	}
}
