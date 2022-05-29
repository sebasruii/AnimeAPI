package anime.model.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jboss.resteasy.spi.InternalServerErrorException;
import org.restlet.resource.ClientResource;

import anime.model.Review;
import anime.model.User;
import anime.model.events.Token;
import anime.model.events.UserCalendar;


public class MapAnimeRepository implements AnimeRepository{
	

	Map<String, Review> reviewMap;
	Map<String, User> userMapToken;
	Map<String, User> userMapUsername;
	UserCalendar userCal;
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
		

		reviewMap = new HashMap<String, Review>();
		userMapToken = new HashMap<String, User>();
		userMapUsername = new HashMap<String, User>();
		
		User u = new User();
		u.setUserName("pruebaJUnit");
		u.setPassword("1234");
		u.setToken("1234");
		userMapToken.put(u.getToken(), u);
		userMapUsername.put(u.getUserName(), u);
		
		userCal = new UserCalendar();
		userCal.setUsername("AnimeAPI");
		userCal.setPassword("AnimeAPI123");
		
		List<Review> reviews = null;
		try {
			reviews = leerReview("ReviewsOficial.csv", true);
		} catch (Exception e) {
			reviews = new ArrayList<Review>();
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
	
	public UserCalendar getUserCalendar() {
		return userCal;
	}
	
	public Token getToken() {
//		crear la uri
		String uri = "https://eventsapi-v1.herokuapp.com/api/auth/token/";
//		crear la llamada para acceder al servicio
		try {
			ClientResource cr = new ClientResource(uri);
			cr.setEntityBuffering(true);
			Token token = cr.post(userCal,Token.class);

		    return token;
		} catch (Exception e) {
			throw new InternalServerErrorException("An error occurred when trying to use EventsAPI");
		}
		
	}

	
	public List<Review> leerReview(String ruta, Boolean cabecera){
		List<Review> coleccionReviews = new ArrayList<Review>();
		List<String> lineas = leeLineas(ruta);
		
		if(cabecera) {
			lineas.remove(0);
		}
		
		for(String linea: lineas) {
			Review nuevaReview = parsearReview(linea);
			coleccionReviews.add(nuevaReview);
		}
		
		return coleccionReviews;		
	}
	
	private Review parsearReview(String linea) {
		String[] splits =  linea.split(";");		
		
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
				.filter(r -> r.getUserName().equals(user))
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

	@Override
	public void addUser(User u) {
		//UUID es un identificador unico para producir el token del usuario
		u.setToken(UUID.randomUUID().toString().replaceAll(" ",""));
		userMapToken.put(u.getToken(), u);
		userMapUsername.put(u.getUserName(), u);
	}
	
	public void deleteUser(User u) {
		userMapUsername.remove(u.getUserName());
		userMapToken.remove(u.getToken());
	}
	
	public Boolean userExist(User u) {
		return userMapUsername.containsKey(u.getUserName());
	}


	@Override
	public void updateUser(User user) {
		userMapToken.put(user.getToken(), user);
		
	}

	@Override
	public User getUser(String userName) {
		return userMapUsername.get(userName);
	}

	@Override
	public User getUserByToken(String token) {
		return userMapToken.get(token);
	}
	
	public Double getAverageRating(Integer animeId) {
		Double averageRating = reviewMap.values().stream()
				.filter(r -> r.getIdAnime().equals(animeId))
				.mapToInt(x ->x.getRating())
				.average().orElse(0);
		
		return averageRating;
	}
}
