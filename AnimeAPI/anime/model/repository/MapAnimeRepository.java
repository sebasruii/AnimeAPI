package anime.model.repository;

import java.util.Collection;
import java.util.Map;

import anime.model.Anime;
import anime.model.Review;

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
	
	public void init() {
		
	}

	public void addReview(String animeId, Review r) {
		Anime anime = getAnime(animeId);
		anime.addReview(r);	
	}

	@Override
	public Review getReviewsUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateReview(Review r) {
		Review review = r;
		review.setComment(r.getComment());
		review.setRating(r.getRating());
		review.setDate(r.getDate());
	}

	public void deleteReview(String animeId, Review reviewId) {
		
	}

	@Override
	public void addAnime(Anime a) {
		String id = "a" + index++;
		a.setId(id);
		animeMap.put(id, a);		
	}

	public Collection<Anime> getAllAnime() {
		return animeMap.values();
	}

	public Anime getAnime(String animeId) {
		return animeMap.get(animeId);
	}

	public void updateAnime(Anime a) {
		animeMap.put(a.getId(), a);
	}

	public void deleteAnime(String animeId) {
		animeMap.remove(animeId);
	}

	public Collection<Review> getAllReview(String animeId) {
		return getAnime(animeId).getReviews();
	}
	
	

}
