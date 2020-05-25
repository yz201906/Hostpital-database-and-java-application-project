package Menus.Home;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Database.Models.Facility;
import DemoQueriesObjects.CheckInPhase;
import DemoQueriesObjects.query1;

public class DemoQueries {
	private static Date getValidDate() {
		try {
			Scanner s = new Scanner(System.in);
			Date entered = Date.valueOf(s.nextLine());
			//removed this because if you enter current date in sql does not return data from current date.
			// long millis = System.currentTimeMillis();
			// java.sql.Date currDate = new java.sql.Date(millis);
			// if (entered.after(currDate)) {
			// 	System.out.println("Please enter a date in the past.");
			// 	return null;
			// }
			return entered;
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static void main(String args[]) {

		int choice = 0;

		while (choice <= 0 || choice > 7) {
			Scanner s = new Scanner(System.in);
			System.out.println("Please select a query:");
			System.out.println(
					"1. Find all patients that were discharged but had negative experiences at any facility, list their names, facility, check-in date, discharge date and negative experiences");
			System.out.println(
					"2. Find facilities that did not have a negative experience for a specific period (to be given).");
			System.out.println("3. For each facility, find the facility that is sends the most referrals to.");
			System.out.println("4. Find facilities that had no negative experience for patients with cardiac symptoms");
			System.out.println(
					"5. Find the facility with the most number of negative experiences (overall i.e. of either kind)");
			System.out.println(
					"6. Find each facility, list the patient encounters with the top five longest check-in phases (i.e. time from begin check-in to when treatment phase begins (list the name of patient, date, facility, duration and list of symptoms).");
			System.out.println("7. Go Back");
			if (s.hasNextInt()) {
				choice = s.nextInt();
			} else if (s.hasNextLine()) {
				s.nextLine();
			}
		}

		if (choice == 1) {
			ArrayList<query1> query = query1.listDischargedPatientsWithNe();
			if (query.size() != 0) {
				for (query1 patientInfo : query) {
					System.out.println("Name: "+patientInfo.getFirstName() + " " + patientInfo.getLastName() + ", Facility: "
							+ patientInfo.getFacilityName() + ", Checkin Start Time: " + patientInfo.getStartTime() + ", Discharge Date: "
							+ patientInfo.getDischargeDate() + ", Negative Experience: " + patientInfo.getNegativeExperience());
				}
			} else {
				System.out.println("No such patients are found");
			}
			DemoQueries.main(null);

		} else if (choice == 2) {
			Date startDate = null;
			while (startDate == null) {
				System.out.println("Please enter startDate in yyyy-[m]m-[d]d format:");
				startDate = getValidDate();
			}

			Date endDate = null;
			while (endDate == null) {
				System.out.println("Please enter endDate in yyyy-[m]m-[d]d format:");
				endDate = getValidDate();
			}

			if (startDate.after(endDate)) {
				System.out.println("Error - entered startDate is after endDate");
				DemoQueries.main(null);
			}

			ArrayList<Facility> facilities = Facility.listFacilitiesWithNoNeFT(new Timestamp(startDate.getTime()),
					new Timestamp(endDate.getTime()));
			for (Facility facility : facilities) {
				System.out.println("FID: " + facility.getFacilityId() + ", FName: " + facility.getFacilityName());
			}
			DemoQueries.main(null);
		} else if (choice == 3) {
			try {
				ArrayList<String> facilityResults = Facility.listMostReferredFacilitiesByFacility();

				for (String result : facilityResults) {
					System.out.println(result);
				}
			} catch (Exception e) {
				System.out.println("Error in finding facilities.");
			}
			DemoQueries.main(null);
		} else if (choice == 4) {
			List<Facility> query4result = Facility.listFacilitiesWithNoNegExpWithHeart();
			if (query4result.size() == 0)
				System.out.println("No facilities were returned for this query.");
			else {
				for (Facility facility : query4result) {
					System.out.println("FID: " + facility.getFacilityId() + ", FName: " + facility.getFacilityName());
				}
			}
			DemoQueries.main(null);
		} else if (choice == 5) {
			Facility mostNegative = Facility.getMostNegativeFacility();

			if (mostNegative != null) {
				System.out.println("The facility with the most negative experiences was ");
				System.out.println(mostNegative.getFacilityId() + " " + mostNegative.getFacilityName() + " with "
						+ Facility.mostNegativeCount + " negative experiences");
			} else {
				System.out.println("No facility has a negative experience.");
			}
			DemoQueries.main(null);

		} else if (choice == 6) {
			ArrayList<CheckInPhase> phases = CheckInPhase.getLongestCheckInPhases();
			Timestamp currResultStartTime = null;
			for (CheckInPhase phase : phases) {
				if (currResultStartTime == null || !currResultStartTime.equals(phase.getStartTime())) {
					currResultStartTime = phase.getStartTime();
					System.out.println("Name: " + phase.getFirstName() + " " + phase.getLastName());
					System.out.println("StartTime: " + phase.getStartTime());
					System.out.println("Duration: " + phase.getDuration() + " minutes");
					System.out.println("Facility: " + phase.getFacilityName());
					System.out.println("Symptom: " + phase.getSymptom());
				} else {
					System.out.println("Symptom: " + phase.getSymptom());
				}
			}
			DemoQueries.main(null);
		} else {
			try {
				homeMenu.main(null);
			} catch (Exception e) {
			}
		}
	}

}
