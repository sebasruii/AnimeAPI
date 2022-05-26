package anime.api.resources.comparators;

import java.util.Comparator;

import anime.model.Anime;
import anime.model.Review;

public class ComparatorRatingReversed implements Comparator<Anime>{
	@Override
	public int compare(Anime o1, Anime o2) {
		
		return o2.getAverageRating().compareTo(o1.getAverageRating());
	}
	
}
