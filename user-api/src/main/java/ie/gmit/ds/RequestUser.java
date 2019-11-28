package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ie.gmit.ds.validations.Login;
import ie.gmit.ds.validations.Modify;

@XmlAccessorType(value = XmlAccessType.PROPERTY)
@XmlRootElement(name = "requestUser")
public class RequestUser extends User {

	@NotBlank(groups = { Modify.class, Login.class })
	@NotNull(message = "Password is required", groups = { Modify.class, Login.class })
	private String password;

	public RequestUser() {
	}

	public RequestUser(int userID, String userName, String email, String password) {
		super(userID, userName, email);
		this.password = password;
	}

	@JsonProperty(access = Access.WRITE_ONLY)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
