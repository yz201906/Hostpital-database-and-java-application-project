package Database.Models;

public class Service {

	private String ServiceCode;
	private String ServiceName;

	public Service(String code, String name) {
		this.ServiceCode = code;
		this.ServiceName = name;
	}

	public String getServiceCode() {
		return ServiceCode;
	}

	public String getServiceName() {
		return ServiceName;
	}

}
