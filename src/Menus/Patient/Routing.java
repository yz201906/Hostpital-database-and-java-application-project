package Menus.Patient;

import java.sql.Timestamp;
import java.util.Scanner;

import Database.Models.*;
import Database.QueryResult;
import Menus.Main;

public class Routing {
	public static final int CheckIn = 1;
	public static final int CheckOutAcknowledgement = 2;
	public static final int GoBack = 3;

	public static void main(String[] args) {
		try {
			int choice = 0;
			while ((choice != CheckIn) && (choice != CheckOutAcknowledgement) && (choice != GoBack)) {
				System.out.println("1. Check in");
				System.out.println("2. Check out acknowledgement");
				System.out.println("3. Go back");
				Scanner pickANumber = new Scanner(System.in);
				if (pickANumber.hasNextInt()) {
					choice = pickANumber.nextInt();
				}
			}

			if (choice == GoBack) {
				Menus.Home.SignIn.main(null);
			} else { // chose CheckIn or CheckOutAcknowledgement
				// Find the last time the patient has checked into this facility
				Checkin lastCheckin = Checkin.getLastCheckinByPatientFacilityId(Main.PatientId, Main.FacilityId);

				Timestamp currentStartTime = null;
				Timestamp treatmentStartTime = null;
				boolean hasCheckedout = true;
				PatientReport report = null;
				if (lastCheckin != null) {
					// Patient has checked into this facility before, so check to see if there is a
					// report already generated for this patient and start time
					report = PatientReport.getByPatientIdTreatmentStartTime(Main.PatientId,
							lastCheckin.getTreatmentStartTime());
					if (report == null || !report.hasPatientAcknowledged()) {
						// No report so patient is already checked in and waiting to be seen
						hasCheckedout = false;
					}
					treatmentStartTime = lastCheckin.getTreatmentStartTime();
					currentStartTime = lastCheckin.getStartTime();
				}

				if (choice == CheckOutAcknowledgement) {
					if (treatmentStartTime == null || hasCheckedout) {
						System.out.println("ERROR - not checked in");
						main(null);
					} else { // patient is checked in, so see if he/she has already been seen
						PatientReport lastReport = PatientReport.getLastPatientReportByPatientId(Main.PatientId);
						if ((lastReport == null) || !lastReport.getTreatmentStartTime().equals(treatmentStartTime)) {
							System.out.println("ERROR - report not yet available");
							main(null);
						} else {
							Menus.Checkout.Acknowledgement.main(null);
						}
					}
				} else { // choice == CheckIn
					Timestamp checkinStartTime = new Timestamp(System.currentTimeMillis());
					Checkin checkIn = new Checkin(Menus.Main.PatientId, checkinStartTime, null, null, 0, null,
							Menus.Main.FacilityId, null, null);
					QueryResult result = checkIn.add();

					if (!result.IsSuccessful && result.isSpecificException) {
						// Patient can have only one checkin in a facility at a time.
						System.out.println("ERROR - " + result.Message);

						if (treatmentStartTime == null) {
							// if treatment hasn't started yet patient can change checkin info
							System.out.println("Do you want to re-enter check in info (yes/no)?");
							Scanner s2 = new Scanner(System.in);
							if (s2.hasNextLine()) {
								String question = s2.nextLine().replaceAll("\\P{Print}", "");

								if (question.equals("yes")) {
									PtntDescribesSymp.delete(Main.PatientId, currentStartTime);
									Checkin.delete(Main.PatientId, currentStartTime);
									Routing.main(null);
								} else {
									Routing.main(null);
								}
							}
						} else {
							Routing.main(null);
						}

					} else if (!result.IsSuccessful) {
						System.err.println(result.Message);
						Routing.main(null);
					} else {
						Menus.Main.CheckinStartTime = checkinStartTime;
						Menus.Patient.CheckInMenu.main(null);
					}
				}
			}
		} catch (Exception ex) {
		}
	}

}
