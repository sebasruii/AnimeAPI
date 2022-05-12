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
		
		animeMap = new HashMap<String, Anime>();
		reviewMap = new HashMap<String, Review>();
		
		List<Anime> animes = leerAnimes("CSVOFICIAL.csv", true);   //Reemplazar ruta
		List<Review> reviews = leerReview("ReviewsOficial.csv", true);
		
		for(Anime a : animes) {
			addAnime(a);
		}
		
		for(Review r: reviews) {
			addReview(r);
		}
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
	
	public List<Anime> leerAnimes(String ruta, Boolean cabecera){
		List<Anime> coleccionAnimes = new ArrayList<Anime>();
		List<String> lineas = leeLineas(ruta);
		
		if(cabecera) {
			lineas.remove(0);
		}
		
		for(String linea: lineas) {
			Anime nuevoAnime = parsearAnime(linea);
			coleccionAnimes.add(nuevoAnime);
		}
		
		return coleccionAnimes;		
	}

	private Anime parsearAnime(String linea) {
		String[] splits =  linea.split(",");		
		String title = splits[0].trim();
		String anyo = splits[1].trim();
		Integer temporadas = Integer.valueOf(splits[2].trim());
		Integer n_capitulos = Integer.valueOf(splits[3].trim());
		
		return new Anime(title, anyo, temporadas, n_capitulos);
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
		
		String idAnime = splits[0].trim();
		String user = splits[1].trim();
		String comment = splits[3].trim();
		Integer rating = Integer.valueOf(splits[2].trim());
		LocalDate date = LocalDate.now();
		
		return new Review(idAnime, user, comment, rating, date);
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
		return getAnime(animeId).reviews();
	}

	@Override
	public void addReview(Review r) {
		String id = "r" + index++;
		r.setId(id);
		Anime a = animeMap.get(r.getIdAnime());
		a.addReview(r);
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
