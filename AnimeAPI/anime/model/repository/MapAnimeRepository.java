package anime.model.repository;

import java.util.Collection;
import java.util.Map;

import anime.model.Anime;
import anime.model.Review;
import src.main.java.api.model.repository.MapAnimeRepository;

public class MapAnimeRepository implements AnimeRepository{
	
	Map<String, Anime> animeMap;
	Map<String, Review> reviewMap;
	private static MapAnimeRepository instance= null;
	private Integer index = 0;
	
	public static MapAnimeRepository getInstance() {
		if (instance==null) {
			instance = new MapAnimeRepository();
			instance.init();
		}
		
		return instance;
	}

	@Override
	public void addReview(String animeId, Review r) {
		Anime anime = getAnime(animeId);
		anime.addReview(r);	
	}

	@Override
	public Review getReviewsUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateReview(Review r) {
		Review review = r;
		review.setComment(r.getComment());
		review.setRating(r.getRating());
		review.setDate(r.getDate());
	}

	@Override
	public void deleteReview(String animeId, Review reviewId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAnime(Anime a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Anime> getAllAnime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Anime getAnime(String animeId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAnime(Anime a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAnime(String animeId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<Review> getAllReview(String animeId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
