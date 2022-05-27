package anime.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import anime.model.Token;
import anime.model.User;

public class TokenCalResource {
	private static final String BASE_URI = "https://eventsapi-v1.herokuapp.com/api/auth/";
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public static Token addTokenCal(User user) {
		// TODO
		ClientResource cr=null;
		Token newToken=null;
		try {
			cr=new ClientResource(BASE_URI+"token/");
			cr.setEntityBuffering(true);
			newToken= cr.post(user,Token.class);
		}catch (ResourceException re) {
			System.err.println("Error when creating the Calendar: " + cr.getResponse().getStatus());
		}
		user.setTokenCal(newToken.getAccess());
		user.setTokenCalRefresh(newToken.getRefresh());
		return newToken;

	}
	
	
	public static Token addRefreshtokenCal(User user) {
		// TODO
		ClientResource cr=null;
		Token newRToken=null;
		try {
			cr=new ClientResource(BASE_URI+"refreshToken/");
			cr.setEntityBuffering(true);
			newRToken= cr.post(user.getTokenCalRefresh(),Token.class);
		}catch (ResourceException re) {
			System.err.println("Error when creating the Clalendar: " + cr.getResponse().getStatus());
		}
		user.setTokenCal(newRToken.getAccess());
		user.setTokenCalRefresh(newRToken.getRefresh());
		
		return newRToken;

	}
	
}
