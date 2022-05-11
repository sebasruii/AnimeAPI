package anime.api.resources.comparators;

import java.util.Comparator;

import anime.model.Anime;

public class ComparatorRatingReversed implements Comparator<Anime>{
	@Override
	public int compare(Anime o1, Anime o2) {
		
		return o2.getRating().compareTo(o1.getRating());
	}
}
