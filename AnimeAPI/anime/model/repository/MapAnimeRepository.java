package anime.model.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
		
		animeMap = new HashMap<String, Anime>();
		reviewMap = new HashMap<String, Review>();
		
		List<Anime> animes = leerAnimes("ruta", true);   //Reemplazar ruta
		
		for(Anime a : animes) {
			addAnime(a);
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

	public void addReview(String animeId, Review r) {
		Anime anime = getAnime(animeId);
		anime.addReview(r);	
	}

	@Override
	public Review getReviewsUser(String user,Integer year) {
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
