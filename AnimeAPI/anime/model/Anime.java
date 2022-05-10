package anime.model;

import java.util.ArrayList;
import java.util.List;


public class Anime {
	
	private String id;
	private String titulo;
	private String anyo;
	private Integer temporada;
	private Integer n_capitulos;
	private TipoFormato formato;
	private List<Review> reviews;
	
	public Anime() {}
	
	public Anime(String titulo) {
		this.titulo = titulo;
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
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getAnyo() {
		return anyo;
	}
	
	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}
	
	public Integer getTemporada() {
		return temporada;
	}
	
	public void setTemporada(Integer temporada) {
		this.temporada = temporada;
	}
	
	public Integer getN_capitulos() {
		return n_capitulos;
	}
	
	public void setN_capitulos(Integer n_capitulos) {
		this.n_capitulos = n_capitulos;
	}
	
	public TipoFormato getTipoFormato() {
		return formato;
	}
	
	public void setTipoFormato(TipoFormato formato) {
		this.formato = formato;
	}
	
	public List<Review> getReviews() {
		return reviews;
	}
	
	public Review getReview(String id) {
		if (reviews==null)
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

}
