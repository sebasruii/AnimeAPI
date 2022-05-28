package anime.api.resources;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import anime.model.Anime;
import anime.model.User;
import anime.model.events.Event;
import anime.model.events.EventoResp;
import anime.model.events.Token;
import anime.model.repository.AnimeRepository;
import anime.model.repository.MapAnimeRepository;


@Path("/user/events")
public class EventResource {
	private static final String BASE_URI = "https://eventsapi-v1.herokuapp.com/api/events/";
	public static EventResource _instance=null;
	AnimeRepository repository;
	
	private EventResource() {
		repository = MapAnimeRepository.getInstance();
		
	}
	
	public static EventResource getInstance() {
		if(_instance == null) {
			_instance= new EventResource();
		}
		return _instance;
	}
	
	@POST
	@Produces("application/json")
	public Event addEvent(@HeaderParam("token") String token,@QueryParam("animeId") Integer animeId) { //A traves de un token obtendremos el nombre de usuario
		
		User user = repository.getUserByToken(token);
		
		if (user == null)
			throw new BadRequestException("Incorrect token");
		
		Anime anime = AnimeResource.getInstance().get(animeId);
		
		if(anime.getBroadcast()==null)
			throw new BadRequestException("The anime "+ anime.getTitle() +" is currently not in broadcast.");
		
		ClientResource cr = new ClientResource(BASE_URI);
		Token t = repository.getToken();
		
		
		
		
		ChallengeResponse authHeader = new ChallengeResponse(new ChallengeScheme("JWT", "JWT"));
		authHeader.setRawValue(t.getAccess());
		
		
		cr.setChallengeResponse(authHeader);
		
		
		Event event = new Event();
		event.setEventName(user.getUserName());
		
		LocalDate date = calcNext(anime.getBroadcast().getDayOfTheWeek());
		
		event.setDate(date.format(DateTimeFormatter.ISO_DATE));
		event.setTime(anime.getBroadcast().getStartTime());
		
		
		
		try {
			EventoResp response = cr.post(event, EventoResp.class);
			return response.getEvent();
		}catch (ResourceException re) {
			throw new InternalServerErrorException("Error when creating the Event: " + cr.getResponse().getStatus() + cr.getAttribute("Authorization"));
		}			
	}
	
	private LocalDate calcNext(String dayOfWeek) {
		DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
		return LocalDate.now().with(TemporalAdjusters.next(day));
	}
}
