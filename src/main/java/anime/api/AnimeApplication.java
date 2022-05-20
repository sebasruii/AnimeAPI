package anime.api;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import anime.api.resources.AnimeResource;
import anime.api.resources.ReviewResource;




public class AnimeApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	// Loads all resources that are implemented in the application
	// so that they can be found by RESTEasy.
	public AnimeApplication() {

		singletons.add(AnimeResource.getInstance());
		singletons.add(ReviewResource.getInstance());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
