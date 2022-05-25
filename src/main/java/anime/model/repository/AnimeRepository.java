package anime.model.repository;

import java.util.Collection;

import anime.model.Anime;
import anime.model.Review;
import anime.model.User;

public interface AnimeRepository {
	
	//Review
	public void addReview(Review r);
	public Review getReview(String reviewId);
	public Collection<Review> getReviewsUser(String user);  //OJO
	public void updateReview(Review review);
	public void deleteReview(Review review);//Ojo el reviewId
	public Collection<Review> getAllReview(Integer animeId);
	
	//User
	public void addUser(User u);
	public void updateUser(User user);
	public User getUser(String userName);
	public User getUserByToken(String token);
	
	
	//Anime
	
//	public Collection<Anime> getAllAnime();
//	public Anime getAnime(String animeId);
	
	

}
