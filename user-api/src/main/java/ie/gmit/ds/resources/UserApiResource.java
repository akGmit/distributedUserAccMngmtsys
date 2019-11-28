package ie.gmit.ds.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.gmit.ds.RequestUser;
import ie.gmit.ds.User;
import ie.gmit.ds.UserApiConfig;
import ie.gmit.ds.client.PwdHashClient;
import ie.gmit.ds.dao.UserDB;
import ie.gmit.ds.validations.Login;
import ie.gmit.ds.validations.Modify;
import io.dropwizard.validation.Validated;

@Path("/users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class UserApiResource {
	
	private final Validator validator;
	private PwdHashClient pwdHashClient;
	private final String USR_NOT_FOUND = "User NOT found.";

	public UserApiResource(Validator validator, UserApiConfig c) {
		String host = c.getPassServiceHost();
		int port = c.getPassServicePort();

		this.validator = validator;
		this.pwdHashClient = new PwdHashClient(host, port);
	}

	@GET
	public Response getUsers() {
		GenericEntity<Collection<User>> userList = new GenericEntity<Collection<User>>(UserDB.getUsers()) {};
		return Response.ok(userList).build();
	}

	@GET
	@Path("/{userID}")
	public Response getUserByID(@PathParam("userID") Integer id) {

		User user = UserDB.getUser(id);

		if (user != null) {
			return Response.ok(user).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity(USR_NOT_FOUND).build();
		}
	}

	@POST
	public Response addNewUser(@Valid @Validated(Modify.class) RequestUser reqUsr) {
		List<String> v = checkForViolations(reqUsr);
		if (v != null)
			return Response.status(Status.BAD_REQUEST).entity("Violations: {}" + v).build();

		User usr = (User) reqUsr;

		if (UserDB.userExists(reqUsr.getUserID())) {
			return Response.status(Status.CONFLICT).entity("User with this ID already exists").build();
		} else {
			usr = pwdHashClient.hash(reqUsr.getUserID(), reqUsr.getPassword(), usr);
			UserDB.addUser(usr);
		}
		return Response.status(Status.CREATED).entity("User with " + reqUsr.getUserID() + "id CREATED.").build();
	}

	@POST
	@Path("/login")
	public Response login(@Valid @Validated(Login.class) RequestUser reqUsr) {
		
		if (!UserDB.userExists(reqUsr.getUserID()))
			return Response.status(Status.NOT_FOUND).entity(USR_NOT_FOUND).build();
		
		User usrDB = UserDB.getUser(reqUsr.getUserID());
		
		boolean valid = pwdHashClient.validate(reqUsr.getPassword(), usrDB.getHashedPassword(), usrDB.getSalt());
		
		if (valid)
			return Response.status(Status.ACCEPTED).entity("User logged in.").build();
		
		return Response.status(Status.FORBIDDEN).entity("Password incorrect.").build();
	}

	@PUT
	public Response updateUser(@Valid @Validated(Modify.class) RequestUser reqUsr) {
		User usr = (RequestUser) reqUsr;
		
		if (UserDB.userExists(reqUsr.getUserID())) {
			usr = pwdHashClient.hash(reqUsr.getUserID(), reqUsr.getPassword(), usr);
			UserDB.updateUser(reqUsr.getUserID(), usr);
			return Response.status(Status.OK).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity(USR_NOT_FOUND).build();
		}
	}
	
	@DELETE
	@Path("/{userID}")
	public Response deleteUser(@PathParam("userID") Integer id) {
		if (UserDB.userExists(id)) {
			UserDB.removeUser(id);
			return Response.ok().build();
		} else {
			return Response.status(Status.NOT_FOUND).entity(USR_NOT_FOUND).build();
		}
	}

	private <E> List<String> checkForViolations(@NotNull E v) {
		Set<ConstraintViolation<E>> violations = validator.validate(v);

		if (violations.size() > 0) {
			ArrayList<String> validationMessages = new ArrayList<String>();
			for (ConstraintViolation<E> violation : violations) {
				validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
			}
			return validationMessages;
		}
		return null;
	}

}
