package networking.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UUIDGenerator {
	
	private static List<Integer> uuids = new ArrayList<>();
	private static Random random = new Random();
	
	public static int generate() {
		
		int uuid = 10000+random.nextInt(90000);
		
		if (uuids.contains(uuid)) return generate();
		else {
			uuids.add(uuid);
			return uuid;
		}
		
	}
	
}
