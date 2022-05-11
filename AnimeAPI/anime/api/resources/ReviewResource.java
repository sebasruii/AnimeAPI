package anime.api.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;

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
	
	@GET
	@Produces("application/json")
	public Collection<Review> get(@QueryParam("user") String user,@QueryParam("year") Integer year){
		Review review= repository.getReviewsUser(user, year);
		if(review==null) {
			throw new NotFoundException("The reviews from "+ user+" were not found");
		}
		return null;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addReview(@Context UriInfo uriInfo, Review review) {
		if (review.getId() == null || "".equals(review.getId()))
			throw new BadRequestException("The id of the user must not be null");
		
		if (review.getIdAnime()!=null|| "".equals(review.getIdAnime()))
			throw new BadRequestException("The animeid property is not editable.");
		
		if (review.getRating()!=null)
			throw new BadRequestException("The rating must not be null.");

		repository.addReview(review.getIdAnime(), review);

		
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(review.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(review);			
		return resp.build();
	}
	
	
	//El put hay que mirarlo
	@PUT
	@Path("/{reviewId}")
	@Consumes("application/json")
	public Response updateReview(@PathParam("reviewId") String reviewId,Review review) {
		
		Review oldReview= repository.getReviewsUser(reviewId, null);
		
		if(oldReview==null) {
			throw new NotFoundException("The reviews from "+ reviewId+" were not found");
		}
		//Update Rating
		if(review.getRating()!=null)
			oldReview.setRating(review.getRating());
		
		// Update comment
		if(review.getComment()!=null)
			oldReview.setComment(review.getComment());
		
		oldReview.setDate(LocalDate.now());
		
		return Response.noContent().build();
	}
	
	
	///OJO
	@DELETE
	@Path("/{reviewId}")
	public Response removeReview(@PathParam("reviewId") String reviewId) {
		Review toberemoved= repository.getReviewsUser(reviewId, null);
		if (toberemoved == null)
			throw new NotFoundException("The review with id="+ reviewId +" was not found");
		else
			repository.deleteReview(null, toberemoved);
		
		return Response.noContent().build();
	}
	
	@GET
	@Path("/{animeId}")
	@Produces("application/json")
	public Collection<Review> getAll(@PathParam("animeId") String animeId)
	{
		return repository.getAllReview(animeId);
		
		
	}
	
}
