package api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import api.domain.User;


@Path("/users")
@Consumes(value= MediaType.APPLICATION_JSON)
@Produces(value = MediaType.APPLICATION_JSON)
public class UserService {
	
	//User database pre-initialization
	private static final List<User> users = new ArrayList<>();
	
	static {
		users.add(new User(1L, "oscar", "1234"));
		users.add(new User(2L, "juan", "1234"));
		users.add(new User(3L, "maria", "1234"));
	}
	
	
	@GET
	public Response findAllUsers() {
		return Response.ok(this.users).build();
	}
	
	@POST
	public Response createUser(User userRequest) {
		userRequest.setId(users.size()+1l);
		this.users.add(userRequest);
		return Response.ok(userRequest).build();
	}
	
	@PUT
	public Response updateUser(User userRequest) {
		List<User> found = this.users.stream().filter(x -> userRequest.getId() == x.getId()).collect(Collectors.toList());
		
		//Throws error in case of the user not found
		if(found.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("User not found").build();
		
		User updateUser = found.get(0);
		updateUser.setPassword(userRequest.getPassword());
		updateUser.setUsername(userRequest.getUsername());
		return Response.ok(updateUser).build();
	}
	
	@DELETE
	@Path("{userId}")
	public Response deleteUser( @PathParam("userId") long userId) {
		System.out.println("userId ==> " + userId);
		List<User> found = this.users.stream().filter(x -> userId == x.getId().longValue()).collect(Collectors.toList());
		
		//Throws error in case of the user not found
		if(found.isEmpty()) return Response.status(Status.BAD_REQUEST).entity("User not found").build();
		
		User updateUser = found.get(0);
		this.users.remove(updateUser);
		return Response.noContent().build();
	}
	
	
	@HEAD
	public Response pingUsersService() {
		return Response.noContent().header("running", true).build();
	}
	
}

