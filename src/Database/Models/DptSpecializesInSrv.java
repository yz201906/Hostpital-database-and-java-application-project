package Database.Models;

public class DptSpecializesInSrv {

	private int FacilityId;
	private String ServiceDepartmentCode;
	private String BodyPartCode;

	public DptSpecializesInSrv(int facilityId, String serviceDepartmentCode, String bodyPartCode) {
		this.FacilityId = facilityId;
		this.ServiceDepartmentCode = serviceDepartmentCode;
		this.BodyPartCode = bodyPartCode;
	}

	public int getFacilityId() {
		return FacilityId;
	}

	public String getServiceDepartmentCode() {
		return ServiceDepartmentCode;
	}

	public String getBodyPartCode() {
		return BodyPartCode;
	}

}
