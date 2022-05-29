package anime.api.resources;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.InternalServerErrorException;
import org.jboss.resteasy.spi.NotFoundException;
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
	
	public EventResource() {
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
		
		event.setDescription("New episode of: "+anime.getTitle());
		LocalDate date = calcNext(anime.getBroadcast().getDayOfTheWeek());
		
		event.setDate(date.format(DateTimeFormatter.ISO_DATE));
		event.setTime(anime.getBroadcast().getStartTime());
		
		
		
		try {
			cr.setEntityBuffering(true);
			EventoResp response = cr.post(event, EventoResp.class);
			return response.getEvent();
		}catch (ResourceException re) {
			throw new InternalServerErrorException("Error when creating the Event: " + cr.getResponse().getStatus());
		}			
	}
	
	private LocalDate calcNext(String dayOfWeek) {
		DayOfWeek day = DayOfWeek.valueOf(dayOfWeek.toUpperCase());
		return LocalDate.now().with(TemporalAdjusters.next(day));
	}
	
	@GET
    @Produces("application/json")
    public Collection<Event> getEvents(@HeaderParam("token") String token){

        User user = repository.getUserByToken(token);

        if (user == null)
            throw new BadRequestException("Incorrect token");

        Event[] events = null;
        List<Event> eventsUser = new ArrayList<Event>();
        String nameUser = user.getUserName();

        try {
            ClientResource cr = new ClientResource(BASE_URI + "?eventName="+nameUser);

            Token t = repository.getToken();

            ChallengeResponse authHeader = new ChallengeResponse(new ChallengeScheme("JWT", "JWT"));
            authHeader.setRawValue(t.getAccess());
            cr.setChallengeResponse(authHeader);
            
            try {
            	cr.setEntityBuffering(true);
            	events = cr.get(Event[].class);
			} catch (Exception e) {
				events = null;
			}
            
            
            if(events == null) {
                throw new NotFoundException("No events found for the user: " + nameUser);
            }
            for(Event e : events) {
                eventsUser.add(e);
            }

        } catch (ResourceException re) {
            throw new InternalServerErrorException("There was and error retrieving the events from EventsAPI " + re.getResponse());
        }

        

        return eventsUser;

    }

	@DELETE
	@Produces("application/json")
	@Path("/{eventId}")
    public Response removeEvent(@HeaderParam("token") String token,@PathParam("eventId") String eventId ){

        User user = repository.getUserByToken(token);
        
        if (user == null)
            throw new BadRequestException("Incorrect token");

        ClientResource cr = new ClientResource(BASE_URI+eventId+"/");
        
        Token t = repository.getToken();
        
    	ChallengeResponse authHeader = new ChallengeResponse(new ChallengeScheme("JWT", "JWT"));
		authHeader.setRawValue(t.getAccess());

		cr.setChallengeResponse(authHeader);
		
		
        try {
        	cr.setEntityBuffering(true);
        	cr.setRetryAttempts(0);
            cr.delete();
            
            return Response.noContent().build();

        } catch (Exception e) {
            throw new NotFoundException("The event with eventId=" + eventId + " was not found.");
        }

        

    }
}
