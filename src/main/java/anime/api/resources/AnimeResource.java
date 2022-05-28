package anime.api.resources;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;
import org.restlet.resource.ClientResource;

import anime.api.resources.comparators.ComparatorRating;
import anime.api.resources.comparators.ComparatorRatingReversed;
import anime.model.Anime;
import anime.model.QueryAnimes;
import anime.model.repository.AnimeRepository;
import anime.model.repository.MapAnimeRepository;



@Path("/animes")
public class AnimeResource {
	/*Singleton*/
	
	private static AnimeResource _instance;
	private static final String MYANIMELIST_API_KEY = "30638e47e89d8af41096f3305da839c0";
	private static final String BASE_URI = "https://api.myanimelist.net/v2/anime";
	private static final String ANIME_FIELDS = "fields=id,title,start_date,end_date,media_type,status,num_episodes,start_season,broadcast";

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
	public Collection<Anime> getAll(@QueryParam("year") Integer year, @QueryParam("mediaType") String mediaType,
			@QueryParam("title") String title, @QueryParam("order") String order,
			@QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset) {
		
		
		if(title==null || "".equals(title))
			throw new BadRequestException("The title parameter must not be null.");
		if(title.length()<3)
			throw new BadRequestException("The title parameter must contain three or more characters.");
		
		List<Anime> result = new ArrayList<Anime>();
		
//		codificar la cadena
		try {
			QueryAnimes animeSearch;
			
			if(title==null || "".equals(title)) {
				ClientResource cr = new ClientResource(BASE_URI);
				cr.getRequest().getHeaders().add("X-MAL-CLIENT-ID", MYANIMELIST_API_KEY);
				
				animeSearch = cr.get(QueryAnimes.class);
			} else {
				String encodedQuery = URLEncoder.encode(title, "UTF-8");
//				crear la uri
				String uri = BASE_URI + "?q="+ 
								encodedQuery + "&"+
								ANIME_FIELDS;
//				crear la llamada para acceder al servicio
				
				ClientResource cr = new ClientResource(uri);
				cr.getRequest().getHeaders().add("X-MAL-CLIENT-ID", MYANIMELIST_API_KEY);
				
				animeSearch = cr.get(QueryAnimes.class);
				
			}
			
			List<Anime> animes = animeSearch.getData().stream()
					.map(d -> d.getNode())
					.collect(Collectors.toList());
			
			
			for(Anime a: animes) {
				Boolean cFecha = year == null || a.getStartSeason().getYear().equals(year);
				Boolean cFormato = mediaType == null || a.getMediaType().equals(mediaType);
				Boolean cTitulo = title == null || a.getTitle().contains(title);
				
				if(cFecha && cFormato && cTitulo) {
					result.add(a);
				}
			}
			
		} catch (Exception e) {
			throw new BadRequestException("Error on query");
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
		
		if(result.isEmpty())
			throw new NotFoundException("There were no results matching the criteria.");
		
		return result;
	}
	
	@GET
	@Path("/{animeId}")
	@Produces("application/json")
	public Anime get(@PathParam("animeId") Integer animeId) {
//		crear la uri
		String uri = BASE_URI + "/" +
					animeId + "?"+
					ANIME_FIELDS;

//		crear la llamada para acceder al servicio
		ClientResource cr = new ClientResource(uri);
		cr.getRequest().getHeaders().add("X-MAL-CLIENT-ID", "30638e47e89d8af41096f3305da839c0");
		Anime anime;
		
		try {
			anime = cr.get(Anime.class);
		} catch (Exception e) {
			throw new NotFoundException("The anime with id=" + animeId + "was not found." );
		}
		
		return anime;
	}
	
	
}
