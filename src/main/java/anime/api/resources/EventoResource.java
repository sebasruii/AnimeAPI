package anime.api.resources;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import anime.model.Event;
import anime.model.Token;
import anime.model.User;
import anime.model.repository.AnimeRepository;

public class EventoResource {
	private static final String BASE_URI = "https://eventsapi-v1.herokuapp.com/api/events/";
	public static ReviewResource _instance=null;
	AnimeRepository repository;
	
	@POST

	@Consumes("application/json")
	@Produces("application/json")
	public Event addEvent(@HeaderParam("tokenCal") String tokenCal,@QueryParam("animeId") Integer animeId, Event evento) { //A través de un token obtendremos el nombre de usuario
		
		User user = repository.getUserByTokenCal(tokenCal);
		ClientResource cr=null;
		Event newEvent=null;
		
		if (user == null)
			throw new BadRequestException("Incorrect token");
		
		if (evento.getEventName()==null)
			throw new BadRequestException("The eventname must not be null.");
		
		if (evento.getDate()==null)
			throw new BadRequestException("The date must not be null.");
		
		//review.setUserName(user.getUserName());
		
		evento.
		
		try {
			cr=new ClientResource(BASE_URI);
			cr.setEntityBuffering(true);
			newEvent= cr.post(evento,Event.class);
		}catch (ResourceException re) {
			System.err.println("Error when creating the Calendar: " + cr.getResponse().getStatus());
		}			
		return newEvent;
	}
}
