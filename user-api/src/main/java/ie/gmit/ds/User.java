package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ie.gmit.ds.validations.Login;
import ie.gmit.ds.validations.Modify;
import ie.gmit.ds.validations.Read;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement
public class User {

	@NotNull(message = "ID is required", groups = { Modify.class, Login.class })
	private Integer userID;

	@NotBlank(message = "User name is required", groups = { Modify.class })
	@NotNull(message = "User name is required", groups = { Modify.class })
	@Length(min = 2, max = 255)
	private String userName;

	@NotBlank(message = "Email is required", groups = { Modify.class })
	@NotNull(message = "Email is required", groups = { Modify.class })
	@Pattern(regexp = ".+@.+\\.[a-z]+")
	private String email;

	@NotNull(message = "Password hash must be not null", groups = { Read.class })
	private byte[] hashedPassword;

	@NotNull(message = "Salt must be not null", groups = { Read.class })
	private byte[] salt;

	public User() {
	}

	public User(int userID, String userName, String email) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.email = email;
	}

	@JsonProperty
	public Integer getUserID() {
		return userID;
	}

	@JsonProperty
	public String getUserName() {
		return userName;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public byte[] getHashedPassword() {
		return hashedPassword;
	}

	@JsonProperty(access = Access.READ_ONLY)
	public byte[] getSalt() {
		return salt;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
}