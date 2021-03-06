package anime.api.resources;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import anime.api.resources.comparators.ComparatorRatingReview;
import anime.api.resources.comparators.ComparatorRatingReviewReversed;
import anime.model.Review;
import anime.model.User;
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
	public Collection<Review> getAll(@QueryParam("user") String user,@QueryParam("year") Integer year,
			@QueryParam("idAnime") Integer idAnime,@QueryParam("order") String order){
		if(idAnime==null) {
			throw new BadRequestException("The idAnime parameter must not be null.");
		}
		
		List<Review> reviews= (List<Review>) repository.getAllReview(idAnime);
		
		if(reviews==null) {
			throw new NotFoundException("The reviews from the anime with idAnime="+ idAnime+" were not found");
		}
		if(year!=null) {
			reviews=reviews.stream().filter(r->r.getDate().getYear()==year).collect(Collectors.toList());
		}
		if(user!=null) {
			reviews=reviews.stream().filter(r->r.getUserName().equals(user)).collect(Collectors.toList());
		}
		
		if(order != null) {
			if(order.equals("positivos")) {
				Collections.sort(reviews, new ComparatorRatingReviewReversed());
			}else if(order.equals("negativos")) {
				Collections.sort(reviews, new ComparatorRatingReview());
			}else {
				throw new BadRequestException("The order parameter must be 'positivo' or 'negativo'.");
			}
		}
		
		return reviews;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addReview(@Context UriInfo uriInfo, @HeaderParam("token") String token, Review review) { //A traves de un token obtendremos el nombre de usuario
		
		User user = repository.getUserByToken(token);
		
		if (user == null)
			throw new BadRequestException("Incorrect token");
		
		if (review.getIdAnime()==null)
			throw new BadRequestException("The animeid must not be null.");
		
		try {
//			Comprueba que existe un anime con esa Id
			AnimeResource.getInstance().get(review.getIdAnime());
		} catch (NotFoundException e) {
			throw e;
		}
		
		
		if (review.getRating()==null)
			throw new BadRequestException("The rating must not be null.");
		
		if (review.getRating() < 1 || review.getRating() > 5)
			throw new BadRequestException("The rating must be an integer between 1 and 5 (inclusive)");

		review.setUserName(user.getUserName());
		repository.addReview(review);

		
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(review.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(review);			
		return resp.build();
	}
	

	@PUT
	@Path("/{reviewId}")
	@Consumes("application/json")
	public Response updateReview(@PathParam("reviewId") String reviewId, @HeaderParam("token") String token, Review review) { //necesario el token de usuario
		
		User user = repository.getUserByToken(token);
		
		if (user == null)
			throw new BadRequestException("Incorrect token");
		
		Review oldReview= repository.getReview(reviewId);
		
		if(oldReview==null) {
			throw new NotFoundException("The review with Id="+ reviewId+" was not found.");
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
	
	
	@DELETE
	@Path("/{reviewId}")
	public Response removeReview(@PathParam("reviewId") String reviewId, @HeaderParam("token") String token) { //necesario el token de usuario
		User user = repository.getUserByToken(token);
		
		if (user == null)
			throw new BadRequestException("Incorrect token");
		
		Review toberemoved= repository.getReview(reviewId);
		if (toberemoved == null)
			throw new NotFoundException("The review with id="+ reviewId +" was not found.");
		else
			repository.deleteReview(toberemoved);
		
		return Response.noContent().build();
	}
	
	@GET
	@Path("/{reviewId}")
	@Produces("application/json")
	public Review get(@PathParam("reviewId") String reviewId)
	{
		Review review = repository.getReview(reviewId);
		if(review==null)
			throw new NotFoundException("The review with id="+ reviewId +" was not found");
		return review;
	}
	
}
