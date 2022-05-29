package anime.api.resources;

import static org.junit.Assert.*;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import anime.model.events.Event;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventResourceTest {
	static EventResource er;
	static Event e1;
	static Event e2;
	static Event e3;
	@BeforeClass
	public static void setUp() throws Exception {
		er = new EventResource();
		
		e1 = er.addEvent("1234", 21);
		e2 = er.addEvent("1234", 20);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		er.removeEvent("1234", e1.getId().toString());
		er.removeEvent("1234", e2.getId().toString());
	}

	@Test
	public void test1GetEvents() {
		Collection<Event> events = er.getEvents("1234");
		
		assertNotNull("La lista de eventos es null", events);
		assertEquals("El tamaño del conjunto de eventos no es 2", 2, events.size());
		
	}
	
	@Test
	public void test2AddEvent() {
		e3 = er.addEvent("1234", 1);
		
		assertNotNull("El evento no ha sido creado correctamente.",e3);
	}
	
	@Test
	public void test3RemoveEvent() {
		Response r = er.removeEvent("1234", e3.getId().toString());
		
		
		assertEquals("El evento no ha sido eliminado correctamente.", r.getStatus(), 204);
		
	}
	
	

}
