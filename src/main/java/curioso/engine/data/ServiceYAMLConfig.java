package curioso.engine.data;

import java.io.Serializable;
import java.util.List;

public class ServiceYAMLConfig implements Serializable {

	private static final long serialVersionUID = -3722103416907432082L;

	/**
	 * The list of services.
	 */
	private List<ServiceConfig> services;

	/**
	 * The service name.
	 */
	private String name;

	public List<ServiceConfig> getServices() {
		return services;
	}

	public void setServices(List<ServiceConfig> services) {
		this.services = services;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}