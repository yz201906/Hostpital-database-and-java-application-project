package Menus.Checkout;

import java.sql.Timestamp;
import java.util.Scanner;

import Database.QueryResult;
import Database.Models.PatientReport;
import Menus.Checkout.ReferralReason;
import Menus.Main;

public class Confirmation {
	public static boolean Confirm(String description, String treatment, String dischargeStatus) {
		System.out.println("Confirm?");
		System.out.println("Patient ID: " + Main.SelectedPatientId);
		System.out.println("Treatment Start Time: " + Main.SelectedTreatmentStartTime);
		System.out.println("Treatment: " + treatment);
		System.out.println("Discharge Status: " + dischargeStatus);

		if (dischargeStatus == null) {
			System.out.println("Warning: Discharge Status cannot be empty.");
		}

		int choice = 0;
		while ((choice != 1) && (choice != 2)) {
			System.out.println("1. Confirm");
			System.out.println("2. Go back");
			Scanner pickANumber = new Scanner(System.in);
			if (pickANumber.hasNextInt()) {
				choice = pickANumber.nextInt();
			}
		}

		if (choice == 1) {
			Timestamp submitTime = new Timestamp(System.currentTimeMillis());
			PatientReport patientReport = new PatientReport(Main.SelectedPatientId, Main.SelectedTreatmentStartTime,
					description, treatment, dischargeStatus, submitTime, false);
			QueryResult result = patientReport.add();
			if (result.IsSuccessful) {
				System.out.println(result.Message);
			} else if (result.isSpecificException) {
				System.err.println(result.Message);
			} else {
				System.err.println(result.Message);

				return false;
			}

			if(tryInsertReferralReasons())
            {
	            if (ReferralStatus.ReferralRelation != null && patientReport.getDischargeStatus().equalsIgnoreCase("Referred")) 
	            {
	                QueryResult result2 = ReferralStatus.ReferralRelation.add();
	                if (result2.IsSuccessful) {
	                    System.out.println(result2.Message);
	                } else {
	                    System.err.println(result2.Message);
	                    ReferralStatus.main(null);
	                }
	            }
            }

            ReferralStatus.ReferralRelation = null;
			Main.SelectedPatientId = -1;
			Main.SelectedTreatmentStartTime = new Timestamp(0);

			return true;
		}

		return false;
	}

	private static boolean tryInsertReferralReasons() {
		if (ReferralReason.refReasons.size() > 0) {
			for (Database.Models.ReferralReason reason : ReferralReason.refReasons) {
				QueryResult qr = reason.add();
				if (!qr.IsSuccessful && qr.isSpecificException) {
					System.out.println(qr.Message);
					System.out.println("The first 4 reasons were added to database.");
				} else if (!qr.IsSuccessful) {
					System.out.println(qr.Message);
					System.out.println("Please enter referral reasons again.");
					ReferralReason.refReasons.clear();

					ReferralReason.main(null);

					return false;
				}
			}
			ReferralReason.refReasons.clear();
		}

		return true;
	}
}
