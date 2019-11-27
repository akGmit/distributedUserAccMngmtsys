package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class UserApiConfig extends Configuration{
	@NotNull
	private String passServiceHost = "127.0.0.1";
	
	@NotNull
	private int passServicePort = 33333;

	@JsonProperty
	public String getPassServiceHost() {
		return passServiceHost;
	}
	
	@JsonProperty
	public int getPassServicePort() {
		return passServicePort;
	}
	
	//For testing purposes
	@JsonProperty
	public void setPassServiceHost(String passServiceHost) {
		this.passServiceHost = passServiceHost;
	}
	
	@JsonProperty
	public void setPassServicePort(int passServicePort) {
		this.passServicePort = passServicePort;
	}
	
	
}
