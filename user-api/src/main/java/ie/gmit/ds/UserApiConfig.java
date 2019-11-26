package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class UserApiConfig extends Configuration{
	@NotNull
	private String passServiceHost;
	
	@NotNull
	private int passServicePort;

	@JsonProperty
	public String getPassServiceHost() {
		return passServiceHost;
	}
	
	@JsonProperty
	public int getPassServicePort() {
		return passServicePort;
	}
	
	
}
