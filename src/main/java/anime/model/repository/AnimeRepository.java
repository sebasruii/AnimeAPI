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
	
	
	
	//Anime
	public void addAnime(Anime a);
	public Collection<Anime> getAllAnime();
	public Anime getAnime(String animeId);
	public void updateAnime(Anime a);
	public void deleteAnime(String animeId);
	public Collection<Review> getAllReview(String animeId);
	

}
