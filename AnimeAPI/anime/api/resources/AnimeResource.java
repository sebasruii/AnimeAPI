package anime.api.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import anime.api.resources.comparators.ComparatorRating;
import anime.api.resources.comparators.ComparatorRatingReversed;
import anime.model.Anime;
import anime.model.TipoFormato;
import anime.model.repository.AnimeRepository;
import anime.model.repository.MapAnimeRepository;



@Path("/animes")
public class AnimeResource {
	/*Singleton*/
	
	private static AnimeResource _instance;
	AnimeRepository repository;
	
	private AnimeResource() {
		repository = MapAnimeRepository.getInstance();
		
	}
	
	public static AnimeResource getInstance() {
		if(_instance == null) {
			_instance= new AnimeResource();
		}
		return _instance;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Anime> getAll(@QueryParam("year") String year, @QueryParam("format") String format,
			@QueryParam("title") String title, @QueryParam("order") String order,
			@QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset){
		List<Anime> result = new ArrayList<Anime>();
		
			
		for(Anime a: repository.getAllAnime()) {
			Boolean cFecha = year == null || a.getYear().equals(year);
			Boolean cFormato = format == null || a.getFormat().equals(TipoFormato.valueOf(format));
			Boolean cTitulo = title == null || a.getTitle().contains(title);
			
			if(cFecha && cFormato && cTitulo) {
				result.add(a);
			}
		}
				
		if(order !=null) {
			if(order.equals("rating")) {
				Collections.sort(result, new ComparatorRatingReversed());
			} else if(order.equals("rating-")) {
				Collections.sort(result, new ComparatorRating());
			} else {
				throw new BadRequestException("the order must be 'rating' or 'rating-'");
			}
		}
		
		if(offset != null)
			result = result.subList(offset + 1, result.size());
			
		if(limit != null)
			result = result.stream().limit(limit).collect(Collectors.toList());
		
		
		return result;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addAnime(@Context UriInfo uriInfo, Anime anime) {
		
		if(anime.getTitle() == null || "".equals(anime.getTitle())) {
			throw new BadRequestException("The title of the anime cannot be null");
		}
		
		if(anime.reviews() != null) {
			throw new BadRequestException("The reviews property of the anime is not editable.");
		}
		repository.addAnime(anime);
		
		
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(anime.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(anime);
		return resp.build();
		
		
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Anime get(@PathParam("id") String id) {
		Anime anime = repository.getAnime(id);
		
		if(anime==null) {
			throw new NotFoundException("The anime with id "+id+" was not found.");
		}
		
		return anime;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	public Response updateAnime(@PathParam("id") String id, Anime anime) {
		Anime oldAnime = repository.getAnime(id);
		if(oldAnime == null) {
			throw new NotFoundException("The anime with id=" + id + " was not found.");
		}
		
		if(oldAnime.reviews()!=null) {
			throw new BadRequestException("The reviews property of the anime is not editable.");
		}
		
		if(anime.getTitle()!=null) 
			oldAnime.setTitle(anime.getTitle());
		if(anime.getYear()!=null) 
			oldAnime.setYear(anime.getYear());
		if(anime.getNumberOfEpisodes()!=null) 
			oldAnime.setNumberOfEpisodes(anime.getNumberOfEpisodes());
		if(anime.getSeasons()!=null) 
			oldAnime.setSeasons(anime.getSeasons());
		if(anime.getFormat()!=null) 
			oldAnime.setFormat(anime.getFormat());
		if(anime.getSeasons()!=null) 
			oldAnime.setSeasons(anime.getSeasons());
		
		return Response.noContent().build();

	}
	
	@DELETE
	@Path("/{id}")
	public Response removeAnime(@PathParam("id") String id) {
		Anime toBeRemoved = repository.getAnime(id);
		if(toBeRemoved == null)
			throw new NotFoundException("The anime with id=" + id + " was not found.");
		else
			repository.deleteAnime(id);
		
		return Response.noContent().build();
	}
}
