package anime.model;

import java.time.LocalDate;

public class Review {
	private String id;
    private String idAnime;
    private String user;
    private String comment;
    private Integer rating;
    private LocalDate date;

    public Review() {}

    public Review(String comment, Integer rating, LocalDate date) {
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }


    public Review(String id, String comment, Integer rating, LocalDate date) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }


    public String getComment() {
        return comment;
    }


    public void setComment(String comment) {
        this.comment = comment;
    }


    public Integer getRating() {
        return rating;
    }


    public void setRating(Integer rating) {
        this.rating = rating;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdAnime() {
		return idAnime;
	}

	public void setIdAnime(String idAnime) {
		this.idAnime = idAnime;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	

}
