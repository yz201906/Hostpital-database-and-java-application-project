package Menus.Checkout;

import Database.Models.PtntReferredByToFclt;
import Database.Models.Staff;
import Database.QueryResult;
import Database.Models.Facility;
import Database.Models.StfWorksAtDpt;
import Menus.Main;

import java.util.ArrayList;
import java.util.Scanner;

public class ReferralStatus {
	
	public static Integer FacilityId = null;
	private static Integer ReferrerID = null;
	public static PtntReferredByToFclt ReferralRelation =null;
	
    public static void main(String[] args) 
    {
    	//FacilityId = null;
    	//ReferrerID = null;
    	DisplayOptions();        
    }
    
    private static void DisplayOptions()
    {
    	Output();
		
		int choice = getInput();
		Choose(choice);
    }
    
    private static void Output()
    {
    	System.out.println("1. Facility id");
        System.out.println("2. Referrer id");
        System.out.println("3. Add reason");
        System.out.println("4. Go back");
    }
    
    private static int getInput()
    {
    	Scanner s = new Scanner(System.in);
		while(s.hasNextInt())
		{
			int choice = s.nextInt();
			if(choice > 4 || choice < 1) 
			{
				Output();
				continue;
			}
			
			return choice;
		}

		System.out.println("Invalid input");
		return getInput();
    }
    
    private static void Choose(int choice)
    {
    	Scanner scanner = new Scanner(System.in);
    	
    	switch (choice) 
    	{
	        case 1:
	        {
	        	System.out.println("Enter the facility ID.");
				ArrayList<Integer> idList = new ArrayList<>();
				try {
					ArrayList<Facility> facilities = Facility.listAll();
					for (Facility facility : facilities) {
						int fid = facility.getFacilityId();
						idList.add(fid);
						System.out.println(fid + ". " + facility.getFacilityName());
					}
				} catch (Exception e) {
					System.out.println("Connection failed.");
				}

				while(scanner.hasNextInt())
	        	{
	        		ReferralStatus.FacilityId = scanner.nextInt();                        //modifies global variable for facility Id
	        		if(!idList.contains(ReferralStatus.FacilityId))
	        		{
	        			System.out.println("Invalid facility selected.");
	        			ReferralStatus.FacilityId = null;
	        		}
	        		else
	        		{
	        			break;
	        		}
	        	}
	        	
	        	DisplayOptions();
	            break;
	        }
	        case 2:
	        {	        	
	        	System.out.println("Enter the Referrer ID.");

				try {
					ArrayList<Integer> staffInThisFacility= StfWorksAtDpt.getStaffIDByFacilityId(Main.FacilityId);
					ArrayList<Staff> medicalStaff = Staff.listAllMedicalStaff();
					for (Staff staff : medicalStaff) {
						int sid = staff.getStaffId();
						String fname=staff.getFirstName();
						String lname=staff.getLastName();
						if (staffInThisFacility.contains(sid)) {
							System.out.println(sid + ": " + fname + " " + lname);
						}
					}
				} catch (Exception e) {
					System.out.println("Connection failed.");
				}

	        	if(scanner.hasNextInt()) 
	        	{
	        		int referrerId = scanner.nextInt();
	        		
	        		if(!StfWorksAtDpt.doesStaffWorkAtFacility(Main.FacilityId, referrerId))
	        		{
	        			System.out.println("Invalid staff ID");
	        		}
					else{
						ReferralStatus.ReferrerID=referrerId;
					}
	        	}

	        	DisplayOptions();
	            break;
	        }
	        case 3:
	        {
	            ReferralReason.main(null);                              //go to referralReasons menu
	            break;
	        }
	        case 4:
	        {
	        	// This insert has to be done after the patient report is inserted. 
	        	// This is already done in the Confirmation menu.
	        	// This gets an integrity constraint violation.
	        	
	        	if(ReferralStatus.FacilityId != null && ReferralStatus.ReferrerID != null)
				{
					ReferralRelation = new PtntReferredByToFclt(Main.SelectedPatientId, Main.SelectedTreatmentStartTime, ReferralStatus.ReferrerID, ReferralStatus.FacilityId);
					/*QueryResult result = ReferralRelation.add();
					if (result.IsSuccessful) {
						System.out.println(result.Message);
					}
					else{
						System.err.println(result.Message);
						ReferralStatus.main(null);
					}*/
				}
	            StaffPatientReport.main(null);
	            break;
	        }
	        default:
	        {
	        	DisplayOptions();
	        	break;
	        }
    	}
    }
}
