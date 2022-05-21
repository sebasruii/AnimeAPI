package anime.api.resources;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.restlet.resource.ClientResource;

import anime.model.Anime;
import anime.model.QueryAnimes;
import anime.model.repository.AnimeRepository;
import anime.model.repository.MapAnimeRepository;



@Path("/animes")
public class AnimeResource {
	/*Singleton*/
	
	private static AnimeResource _instance;
	private static final String MYANIMELIST_API_KEY = "30638e47e89d8af41096f3305da839c0";

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
			@QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset) throws UnsupportedEncodingException{
		List<Anime> result = new ArrayList<Anime>();
		
//		codificar la cadena
		String encodedQuery = URLEncoder.encode(title, "UTF-8");
//		crear la uri
		String uri = "https://api.myanimelist.net/v2/anime?q="+ 
						encodedQuery +
						"&fields=id,title,start_date,end_date,media_type,status,num_episodes,start_season,broadcast";
//		crear la llamada para acceder al servicio
		
		ClientResource cr = new ClientResource(uri);
		cr.getRequest().getHeaders().add("X-MAL-CLIENT-ID", MYANIMELIST_API_KEY);
		
		QueryAnimes animeSearch = cr.get(QueryAnimes.class);
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
		
		
		//TODO order aun sin implementar (por ahora)
//		if(order !=null) {
//			if(order.equals("rating")) {
//				Collections.sort(result, new ComparatorRatingReversed());
//			} else if(order.equals("rating-")) {
//				Collections.sort(result, new ComparatorRating());
//			} else {
//				throw new BadRequestException("the order must be 'rating' or 'rating-'");
//			}
//		}
		
		if(offset != null)
			result = result.subList(offset + 1, result.size());
			
		if(limit != null)
			result = result.stream().limit(limit).collect(Collectors.toList());
		
		
		return result;
	}
	
	@GET
	@Path("/{animeId}")
	@Produces("application/json")
	public Anime get(@PathParam("animeId") Integer animeId) {
//		crear la uri
		String uri = "https://api.myanimelist.net/v2/anime/" + 
					animeId + 
					"?fields=id,title,start_date,end_date,media_type,status,num_episodes,start_season,broadcast";
;
//		crear la llamada para acceder al servicio
		ClientResource cr = new ClientResource(uri);
		cr.getRequest().getHeaders().add("X-MAL-CLIENT-ID", "30638e47e89d8af41096f3305da839c0");
		Anime anime = cr.get(Anime.class);
		
		return anime;
	}
	
	
}
