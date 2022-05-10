package anime.api.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.spi.NotFoundException;

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
	public Collection<Anime> getAll(@QueryParam("anyo") String anyo, @QueryParam("formato") String formato, @QueryParam("titulo") String titulo){
		List<Anime> result = new ArrayList<Anime>();
		
			
		for(Anime a: repository.getAllAnime()) {
			Boolean cFecha = anyo == null || a.getAnyo().equals(anyo);
			Boolean cFormato = formato == null || a.getTipoFormato().equals(TipoFormato.valueOf(formato));
			Boolean cTitulo = titulo == null || a.getTitulo().contains(titulo);
			
			if(cFecha && cFormato && cTitulo) {
				result.add(a);
			}
		}
		
		//TODO: ordenacion a partir de valoraciones por implementar
		return result;
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
}
