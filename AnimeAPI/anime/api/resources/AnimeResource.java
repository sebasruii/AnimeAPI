package anime.api.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
	public Collection<Anime> getAll(@QueryParam("anyo") String anyo, @QueryParam("formato") String formato,
			@QueryParam("titulo") String titulo, @QueryParam("order") String order){
		List<Anime> result = new ArrayList<Anime>();
		
			
		for(Anime a: repository.getAllAnime()) {
			Boolean cFecha = anyo == null || a.getAnyo().equals(anyo);
			Boolean cFormato = formato == null || a.getTipoFormato().equals(TipoFormato.valueOf(formato));
			Boolean cTitulo = titulo == null || a.getTitulo().contains(titulo);
			
			if(cFecha && cFormato && cTitulo) {
				result.add(a);
			}
		}
				
		if(order !=null) {
			if(order.equals("rating")) {
				Collections.sort(result, new ComparatorRating());
			} else if(order.equals("rating-")) {
				Collections.sort(result, new ComparatorRatingReversed());
			} else {
				throw new BadRequestException("the order must be 'rating' or 'rating-'");
			}
		}
		return result;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addAnime(@Context UriInfo uriInfo, Anime anime) {
		
		if(anime.getTitulo() == null || "".equals(anime.getTitulo())) {
			throw new BadRequestException("The title of the anime cannot be null");
		}
		
		if(anime.getReviews() != null) {
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
		
		if(oldAnime.getReviews()!=null) {
			throw new BadRequestException("The reviews property of the anime is not editable.");
		}
		
		if(anime.getTitulo()!=null) 
			oldAnime.setTitulo(anime.getTitulo());
		if(anime.getAnyo()!=null) 
			oldAnime.setAnyo(anime.getAnyo());
		if(anime.getN_capitulos()!=null) 
			oldAnime.setN_capitulos(anime.getN_capitulos());
		if(anime.getTemporada()!=null) 
			oldAnime.setTemporada(anime.getTemporada());
		if(anime.getTipoFormato()!=null) 
			oldAnime.setTipoFormato(anime.getTipoFormato());
		if(anime.getTemporada()!=null) 
			oldAnime.setTemporada(anime.getTemporada());
		
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
