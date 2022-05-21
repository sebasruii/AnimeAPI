package anime.model.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import anime.model.Anime;
import anime.model.Review;

public class MapAnimeRepository implements AnimeRepository{
	
	Map<Integer, Anime> animeMap;
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
		
		animeMap = new HashMap<Integer, Anime>();
		reviewMap = new HashMap<String, Review>();
		
//		List<Review> reviews = leerReview("ReviewsOficial.csv", true);
//		
//		
//		
//		for(Review r: reviews) {
//			addReview(r);
//		}
	}
	
	public static List<String> leeLineas(String ruta){
		List<String> result = new ArrayList<>();
		
		try {
			result = Files.readAllLines(Paths.get(ruta));
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return result;
	}

	
	public List<Review> leerReview(String ruta, Boolean cabecera){
		List<Review> coleccionReviews = new ArrayList<Review>();
		List<String> lineas = leeLineas(ruta);
		
		if(cabecera) {
			lineas.remove(0);
		}
		
		for(String linea: lineas) {
			Review nuevoAnime = parsearReview(linea);
			coleccionReviews.add(nuevoAnime);
		}
		
		return coleccionReviews;		
	}
	
	private Review parsearReview(String linea) {
		String[] splits =  linea.split(",");		
		
		Integer idAnime = Integer.valueOf(splits[0].trim());
		String user = splits[1].trim();
		String comment = splits[3].trim();
		Integer rating = Integer.valueOf(splits[2].trim());
		LocalDate date = LocalDate.now();
		
		return new Review(idAnime, user, comment, rating, date);
	}

	
	public Collection<Review> getAllReview(Integer animeId) {
		return reviewMap.values().stream().filter(r -> r.getIdAnime().equals(animeId))
				.collect(Collectors.toList());
	}

	@Override
	public void addReview(Review r) {
		String id = "r" + index++;
		r.setId(id);
		r.setDate(LocalDate.now());
		
		reviewMap.put(id, r);
		
	}

	@Override
	public Collection<Review> getReviewsUser(String user) {
		
		return reviewMap.values().stream()
				.filter(r -> r.getUser().equals(user))
				.collect(Collectors.toList());
	}

	@Override
	public void updateReview(Review review) {
		reviewMap.put(review.getId(), review);
		
	}

	@Override
	public void deleteReview(Review review) {
		
		reviewMap.remove(review.getId());
	}

	@Override
	public Review getReview(String reviewId) {
		return reviewMap.get(reviewId);
	}
}
