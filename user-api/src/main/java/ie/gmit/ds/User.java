package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import ie.gmit.ds.validations.Modify;
import ie.gmit.ds.validations.Read;

@XmlRootElement
public class User {
	
	@NotNull
	private int userID;
	
	@NotBlank
	@Length(min=2, max=255)
	private String userName;
	
	@NotBlank
	@Pattern(regexp=".+@.+\\.[a-z]+")
	private String email;
	
	@NotBlank(message = "Password is required", groups = {Modify.class})
	private String password;
	
	@NotNull(message = "Password hash must be not null", groups = {Read.class})
	private byte[] hashedPassword;
	
	@NotNull(message = "Salt must be not null", groups = {Read.class})
	private byte[] salt;
	
	public User () {
	}

	public User(int userID, String userName, String email, String password, byte[] hashedPassword, byte[] salt) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.hashedPassword = hashedPassword;
		this.salt = salt;
	}
	
	@JsonProperty
	@XmlElement
	public int getUserID() {
		return userID;
	}
	
	@JsonProperty
	@XmlElement
	public String getUserName() {
		return userName;
	}
	
	@JsonProperty
	@XmlElement
	public String getEmail() {
		return email;
	}
	
	@JsonProperty
	@XmlElement
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	@XmlElement
	public byte[] getHashedPassword() {
		return hashedPassword;
	}
	
	@JsonProperty
	@XmlElement
	public byte[] getSalt() {
		return salt;
	}

	public void setUserID(int userID) {
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
