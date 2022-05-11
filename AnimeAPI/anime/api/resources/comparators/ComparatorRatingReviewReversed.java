package anime.api.resources.comparators;

import java.util.Comparator;

import anime.model.Review;

public class ComparatorRatingReviewReversed implements Comparator<Review>{
	
	public int compare(Review o1, Review o2) {
		
		return o2.getRating().compareTo(o1.getRating());
	}

}
