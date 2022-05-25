package anime.api.resources;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import anime.api.utils.PasswordUtils;
import anime.model.User;
import anime.model.repository.AnimeRepository;
import anime.model.repository.MapAnimeRepository;

@Path("/user")
public class UserResource {
	
	public static UserResource _instance=null;
	AnimeRepository repository;
	
	private UserResource() {
		repository=MapAnimeRepository.getInstance();
	}
	
	public static UserResource getInstance() {
		if(_instance==null) _instance= new UserResource();
			return _instance;
	}
	
	@GET
	@Produces("application/json")
	public String getToken(@QueryParam("userName") String userName, @QueryParam("password") String password) {
		
		User user = repository.getUser(userName);
		if(user==null || !PasswordUtils.verifyUserPassword(password, user.getPassword()))
			throw new NotFoundException("The user with the name="+ userName +" was not found or the password is incorrect.");
		return user.getToken();
		
	}
	
	@Path("/{token}")
	@GET
	@Produces("application/json")
	public String get(@PathParam("token") String token) {
		
		User user = repository.getUserByToken(token);
		if(user==null)
			throw new NotFoundException("Incorrect token");
		return user.getToken();
		
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addUser(@Context UriInfo uriInfo, User user) {
		
		if (user.getUserName() == null || "".equals(user.getUserName()))
			throw new BadRequestException("The user name must not be null");
		
		if (user.getPassword()==null)
			throw new BadRequestException("The password must not be null.");
		//la clase PasswordUtils sirve para encriptar la contraseña
		user.setPassword(PasswordUtils.generateSecurePassword(user.getPassword()));
		repository.addUser(user);

		
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(user.getToken());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(user.getToken());			
		return resp.build();
	}
	
	@PUT
	@Path("/{token}")
	@Consumes("application/json")
	public Response updateUser(@PathParam("token") String token, User user) {
		
		User oldUser= repository.getUserByToken(token);
		
		if(oldUser==null) {
			throw new NotFoundException("The user from "+ token+" were not found");
		}
		
		// Update password
		if(user.getPassword()!=null)
			oldUser.setPassword(PasswordUtils.generateSecurePassword(user.getPassword()));
		
		return Response.noContent().build();
	}
	

}
