
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class TestConfig extends Configuration{
	private String passServiceHost = "127.0.0.1";
	
	private int passServicePort = 33333;

	@JsonProperty
	public String getPassServiceHost() {
		return passServiceHost;
	}
	
	@JsonProperty
	public int getPassServicePort() {
		return passServicePort;
	}
	
	
}