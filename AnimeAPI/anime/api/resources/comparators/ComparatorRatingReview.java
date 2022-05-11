package anime.api.resources.comparators;

import java.util.Comparator;

import anime.model.Review;

public class ComparatorRatingReview implements Comparator<Review>{
	
public int compare(Review o1, Review o2) {
		
		return o1.getRating().compareTo(o2.getRating());
	}

}
