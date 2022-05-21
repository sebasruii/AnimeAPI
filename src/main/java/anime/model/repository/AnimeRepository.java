package anime.model.repository;

import java.util.Collection;

import anime.model.Anime;
import anime.model.Review;

public interface AnimeRepository {
	
	//Review
	public void addReview(Review r);
	public Review getReview(String reviewId);
	public Collection<Review> getReviewsUser(String user);  //OJO
	public void updateReview(Review review);
	public void deleteReview(Review review);//Ojo el reviewId
	public Collection<Review> getAllReview(Integer animeId);
	
	
	//Anime
	
//	public Collection<Anime> getAllAnime();
//	public Anime getAnime(String animeId);
	
	

}
