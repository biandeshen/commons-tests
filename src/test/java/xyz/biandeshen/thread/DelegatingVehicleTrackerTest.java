package xyz.biandeshen.thread;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.mockito.Mockito.*;

/**
 * @author fjp
 * @Title: DelegatingVehicleTrackerTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/8/60:06
 */
public class DelegatingVehicleTrackerTest {
	@Mock
	ConcurrentMap<String, Point> locations;
	@Mock
	Map<String, Point> unmodifiableMap;
	@InjectMocks
	DelegatingVehicleTracker delegatingVehicleTracker;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetLocation() throws Exception {
		Point result = delegatingVehicleTracker.getLocation("id");
		Assert.assertEquals(new Point(0, 0), result);
	}
	
	@Test
	public void testSetLocation() throws Exception {
		delegatingVehicleTracker.setLocation("id", 0, 0);
	}
	
	@Test
	public void testGetLocations2() throws Exception {
		Map<String, Point> result = delegatingVehicleTracker.getLocations2();
		Assert.assertEquals(new HashMap<String, Point>() {{
			put("String", new Point(0, 0));
		}}, result);
	}
	
	@Test
	public void testLocations() {
		DelegatingVehicleTracker delegatingVehicleTracker = new DelegatingVehicleTracker(new HashMap<String, Point>() {{
			put("car1", new Point(0, 0));
		}});
		
		
		System.out.println("=================================================");
		Map<String, Point> locations1 = delegatingVehicleTracker.getLocations1();
		locations1.keySet().forEach(key -> System.out.println(locations1.get(key)));
		System.out.println("=================================================");
		Map<String, Point> locations2 = delegatingVehicleTracker.getLocations2();
		locations2.keySet().forEach(key -> System.out.println(locations2.get(key)));
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		Point car1 = new Point(1,1);
		delegatingVehicleTracker.setLocation("car1",car1.x,car1.y);
		System.out.println("=================================================");
		locations1.keySet().forEach(key -> System.out.println(locations1.get(key)));
		System.out.println("=================================================");
		locations2.keySet().forEach(key -> System.out.println(locations2.get(key)));
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme