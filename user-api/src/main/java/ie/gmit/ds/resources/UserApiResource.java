package ie.gmit.ds.resources;

import java.util.ArrayList;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import ie.gmit.ds.User;
import ie.gmit.ds.UserApiConfig;
import ie.gmit.ds.client.PwdHashClient;
import ie.gmit.ds.dao.UserDB;
import ie.gmit.ds.validations.Modify;
import io.dropwizard.validation.Validated;

@Path("/users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class UserApiResource {

	public final Validator validator;
	private PwdHashClient pwdHashClient;

	public UserApiResource(Validator validator, UserApiConfig c) {
		String host = c.getPassServiceHost();
		int port = c.getPassServicePort();

		this.validator = validator;
		this.pwdHashClient = new PwdHashClient(host, port);
	}

	@GET
	public Response getUsers() {
		return Response.ok(UserDB.getUsers()).build();
	}

	@GET
	@Path("/{userID}")
	public Response getUserByID(@PathParam("userID") Integer id) {

		User user = UserDB.getUser(id);

		if (user != null) {
			return Response.ok(user).build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}

	@POST
	public Response addNewUser(@Valid @Validated(Modify.class) User u) {

		Set<ConstraintViolation<User>> violations = validator.validate(u);

		if (violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<String>();
			for (ConstraintViolation<User> violation : violations) {
				validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
			}
			return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
		}

		if (UserDB.userExists(u.getUserID())) {
			return Response.status(Status.CONFLICT).entity("User with this ID already exists").build();
		} else {
			u = pwdHashClient.hash(u.getUserID(), u.getPassword(), u);
			UserDB.addUser(u);
		}
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("/{userID}")
	public Response deleteUser(@PathParam("userID") Integer id) {
		if (UserDB.userExists(id)) {
			UserDB.removeUser(id);
			return Response.ok().build();
		} else {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
