package Database.Models;

public class SrvUsesEqp {

	private String EName;
	private String SCode;

	public SrvUsesEqp(String eName, String sCode) {
		this.EName = eName;
		this.SCode = sCode;
	}

	public String getEquipmentName() {
		return EName;
	}

	public String getServiceCode() {
		return SCode;
	}

}
