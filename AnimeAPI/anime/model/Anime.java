package anime.model;


import java.util.ArrayList;
import java.util.List;


public class Anime {
	
	private String id;
	private String title;
	private String year;
	private Integer seasons;
	private Integer numberOfEpisodes;
	private TipoFormato format;
	private List<Review> reviews;
	
	public Anime() {
		this.reviews = new ArrayList<Review>();
	}
	
	public Anime(String titulo) {
		this();
		this.title = titulo;
	}
	
	public Anime(String titulo, String anyo, Integer temporada, Integer n_capitulos) {
		this();
		this.title = titulo;
		this.year = anyo;
		this.seasons = temporada;
		this.numberOfEpisodes = n_capitulos;
		if(n_capitulos.equals(1)) {
			this.format = TipoFormato.FILM;
		} else {
			this.format = TipoFormato.SERIES;
		}
	}

	protected void setReviews(List<Review> r) {
		reviews = r;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String titulo) {
		this.title = titulo;
	}
	
	public String getYear() {
		return year;
	}
	
	public void setYear(String anyo) {
		this.year = anyo;
	}
	
	public Integer getSeasons() {
		return seasons;
	}
	
	public void setSeasons(Integer temporada) {
		this.seasons = temporada;
	}
	
	public Integer getNumberOfEpisodes() {
		return numberOfEpisodes;
	}
	
	public void setNumberOfEpisodes(Integer n_capitulos) {
		this.numberOfEpisodes = n_capitulos;
	}
	
	public TipoFormato getFormat() {
		return format;
	}
	
	public void setFormat(TipoFormato formato) {
		this.format = formato;
	}
	
	public List<Review> reviews() {
		return reviews;
	}
	
	public Review getReview(String id) {
		if (reviews==null || reviews.isEmpty())
			return null;
		
		Review review =null;
		for(Review r: reviews)
			if (r.getId().equals(id))
			{
				review=r;
				break;
			}
		
		return review;
	}
	
	public void addReview(Review r) {
		if (reviews==null)
			reviews = new ArrayList<Review>();
		reviews.add(r);
	}
	
	public void deleteReview(Review r) {
		reviews.remove(r);
	}
	
	public void deleteReview(String id) {
		Review r = getReview(id);
		if (r!=null)
			reviews.remove(r);
	}
	
	public Double getAverageRating() {

        if(this.reviews() == null || this.reviews().isEmpty()) {
            return 0.0;
        }

        Integer count = 0;
        Integer sum = 0;

        for(Review r: reviews) {
            sum += r.getRating();
            count +=1;
        }

        Double promedio = (double) sum/count ;
        Double prom = Math.round(promedio * 100) / 100d;

        return prom;
    }


}
