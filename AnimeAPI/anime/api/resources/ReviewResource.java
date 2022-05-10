package anime.api.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import anime.model.Review;
import anime.model.repository.AnimeRepository;
import anime.model.repository.MapAnimeRepository;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/animes/reviews")
public class ReviewResource {
	public static ReviewResource _instance=null;
	AnimeRepository repository;
	
	private ReviewResource() {
		repository=MapAnimeRepository.getInstance();
	}
	
	public static ReviewResource getInstance() {
		if(_instance==null)
			_instance=new ReviewResource();
		return _instance;
	}
}
