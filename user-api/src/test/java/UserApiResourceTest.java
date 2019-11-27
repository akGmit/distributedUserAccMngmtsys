import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import ie.gmit.ds.User;
import ie.gmit.ds.UserApiApplication;
import ie.gmit.ds.UserApiConfig;
import ie.gmit.ds.resources.UserApiResource;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;

class UserApiResourceTest {
	private static final Environment environment = mock(Environment.class);
	private final JerseyEnvironment jersey = mock(JerseyEnvironment.class);
	private final UserApiApplication application = new UserApiApplication();
	private static final UserApiConfig config = new UserApiConfig();
	private static UserApiResource res  = new UserApiResource(environment.getValidator(), config);
	
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
	//Test fails with violations in addNewUser method body, comment it out before testing
	void testAddNewUser() {
		// Valid user
		User u = new User(44, "a", "a@a.com", "123456");
		Response r = res.addNewUser(u);
		assertEquals(201, r.getStatus());

		// Duplicate ID
		u = new User(1, "a", "a@a.com", "123456");
		r = res.addNewUser(u);
		assertEquals(409, r.getStatus());
	}

	@Test
	void testDeleteUser() {
		User u = new User(333, "a", "a@a.com", "123456");
		res.addNewUser(u);
		
		//Valid user id
		Response r = res.deleteUser(u.getUserID());
		assertEquals(200, r.getStatus());
		
		//Invalid user id
		r = res.deleteUser(456456);
		assertEquals(404, r.getStatus());
	}

}
