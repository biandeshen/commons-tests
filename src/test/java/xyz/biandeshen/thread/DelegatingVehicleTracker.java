package xyz.biandeshen.thread;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author fjp
 * @Title: DelegatingVehicleTracker
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/8/523:45
 */
public class DelegatingVehicleTracker {
	private final ConcurrentMap<String, Point> locations;
	private final Map<String, Point> unmodifiableMap;
	
	public DelegatingVehicleTracker(Map<String, Point> points) {
		locations = new ConcurrentHashMap<>(points);
		unmodifiableMap = Collections.unmodifiableMap(locations);
	}
	
	public Point getLocation(String id) {
		return locations.get(id);
	}
	
	public void setLocation(String id, int x, int y) {
		if (locations.replace(id, new Point(x, y)) == null) {
			throw new IllegalArgumentException("invalid vehicle name: " + id);
		}
	}
	
	public Map<String, Point> getLocations1() {
		return unmodifiableMap;
	}
	
	public Map<String, Point> getLocations2() {
		return Collections.unmodifiableMap(new HashMap<>(locations));
	}
	
	
}

class Point {
	public final int x, y;
	
	Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"x\":").append(x);
		sb.append(",\"y\":").append(y);
		sb.append('}');
		sb.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode()));
		return sb.toString();
	}
}