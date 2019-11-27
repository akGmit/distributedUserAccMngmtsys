package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import ie.gmit.ds.validations.Modify;
import ie.gmit.ds.validations.Read;

@XmlRootElement(name="user")
public class User {
	
	@NotNull
	private Integer userID;
	
	@NotNull
	@NotBlank
	@Length(min=2, max=255)
	private String userName;
	
	@NotNull
	@NotBlank
	@Pattern(regexp=".+@.+\\.[a-z]+")
	private String email;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@NotBlank
	@NotNull(message = "Password is required", groups = {Modify.class})
	private String password;
	
	@JsonProperty(access = Access.READ_ONLY)
	@NotNull(message = "Password hash must be not null", groups = {Read.class})
	private byte[] hashedPassword;
	
	@JsonProperty(access = Access.READ_ONLY)
	@NotNull(message = "Salt must be not null", groups = {Read.class})
	private byte[] salt;
	
	public User () {
	}
	
	public User (int userID, String userName, String email, String password) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	@XmlElement
	@JsonProperty
	public Integer getUserID() {
		return userID;
	}
	@XmlElement
	@JsonProperty
	public String getUserName() {
		return userName;
	}
	@XmlElement
	@JsonProperty
	public String getEmail() {
		return email;
	}
	@XmlElement
	@JsonProperty
	public String getPassword() {
		return password;
	}
	
	public byte[] getHashedPassword() {
		return hashedPassword;
	}
	
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

	public void setPassword(String password) {
		this.password = password;
	}

	public void setHashedPassword(byte[] hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
}
