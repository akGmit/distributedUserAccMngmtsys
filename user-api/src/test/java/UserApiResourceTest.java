import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.gmit.ds.RequestUser;
import ie.gmit.ds.User;
import ie.gmit.ds.UserApiApplication;
import ie.gmit.ds.UserApiConfig;
import ie.gmit.ds.dao.UserDB;
import ie.gmit.ds.resources.UserApiResource;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Environment;

class UserApiResourceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserApiResourceTest.class);
	private static final UserApiConfig config = new UserApiConfig();
	private static UserApiResource res = new UserApiResource(Validators.newValidator(), config);

	@AfterClass
	public static void printDB() {
		LOGGER.debug(UserDB.getUsers().toString());
	}

	@Test
	void testGetUsers() throws Exception, Exception {
		Response r = res.getUsers();
		assertEquals(r.getStatus(), 200);
	}

	@Test
	void testGetUserByID() {
		Response r = res.getUserByID(1);
		assertEquals(200, r.getStatus());

		r = res.getUserByID(9999);
		assertEquals(404, r.getStatus());
	}

	@Test
	// Test fails with violations in addNewUser() method body, comment it out before
	// testing
	void testAddNewUser() {
		// Valid user
		RequestUser u = new RequestUser(64896, "affdsf", "a@a.com", "000");
		Response r = res.addNewUser(u);
		assertEquals(201, r.getStatus());

		// Duplicate ID
		u = new RequestUser(1, "xxva", "a@a.com", "000");
		r = res.addNewUser(u);
		assertEquals(409, r.getStatus());
	}

	@Test
	void testDeleteUser() {
		RequestUser u = new RequestUser(333, "axvxv", "a@a.com", "000");
		res.addNewUser(u);

		// Valid user id
		Response r = res.deleteUser(u.getUserID());
		assertEquals(200, r.getStatus());

		// Invalid user id
		r = res.deleteUser(456456);
		assertEquals(404, r.getStatus());
	}

	@Test
	// Test fails with violations in updateUser() method body, comment it out before
	// testing
	void testUpdateUser() {
		RequestUser u = (RequestUser) UserDB.getUser(1);

		u.setPassword("654321");

		// Valid update
		Response r = res.updateUser(u);
		assertEquals(200, r.getStatus());

		// Invalid update

		u.setUserID(3215);
		r = res.updateUser(u);
		assertEquals(404, r.getStatus());
	}
}
