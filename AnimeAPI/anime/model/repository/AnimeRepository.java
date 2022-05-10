package anime.model.repository;

import java.util.Collection;

import anime.model.Anime;
import anime.model.Review;

public interface AnimeRepository {
	
	//Review
	public void addReview(String animeId, Review r);
	public Review getReviewsUser();  //OJO
	public void updateReview(Review r);
	public void deleteReview(String animeId, Review reviewId);
	
	
	//Anime
	public void addAnime(Anime a);
	public Collection<Anime> getAllAnime();
	public Anime getAnime(String animeId);
	public void updateAnime(Anime a);
	public void deleteAnime(String animeId);
	public Collection<Review> getAllReview(String animeId);
	

}
