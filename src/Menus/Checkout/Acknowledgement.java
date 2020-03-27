package Menus.Checkout;

import java.sql.Timestamp;
import java.util.Scanner;

import Database.Models.Checkin;
import Database.Models.PatientReport;
import Menus.Main;

public class Acknowledgement {
	public static void main(String[] args) {
		Checkin lastCheckin = Checkin.getLastCheckinByPatientFacilityId(Main.PatientId, Main.FacilityId);
		Timestamp treatmentStartTime = lastCheckin.getTreatmentStartTime();

		PatientReport report = PatientReport.getByPatientIdStartTime(Main.PatientId, treatmentStartTime);
		System.out.println("Acknowledge Checkout?");
		System.out.println("Patient ID: " + Main.PatientId);
		System.out.println("Treatment Start Time: " + treatmentStartTime);
		System.out.println("Treatment: " + report.getTreatment());
		System.out.println("Discharge Status: " + report.getDischargeStatus());
		System.out.println("Report Submit Time: " + report.getReportSubmitTime());

		int choice = 0;
		while ((choice != 1) && (choice != 2) && (choice != 3)) {
			System.out.println("1. Yes");
			System.out.println("2. No");
			System.out.println("3. Go back");
			Scanner pickANumber = new Scanner(System.in);
			if (pickANumber.hasNextInt()) {
				choice = pickANumber.nextInt();
			}
		}

		switch (choice) {
		case 3:
			Menus.Patient.Routing.main(null);
		case 2:
			Main.CheckinStartTime = null;
			System.out.println("Enter reason:");
			Scanner enterReason = new Scanner(System.in);

			if (enterReason.hasNextLine()) {
				String reason = enterReason.next();
				PatientReport.setToHasAcknowledged(Main.PatientId, treatmentStartTime);
				PatientReport.updateDeclineDescription(Main.PatientId, treatmentStartTime, reason);
			} else {
				System.out.println("There was an error reading your input.");
			}

			Menus.Patient.Routing.main(null);
		case 1:
			PatientReport.setToHasAcknowledged(Main.PatientId, treatmentStartTime);
			Main.CheckinStartTime = null;
			Menus.Patient.Routing.main(null);
		}
	}

}
