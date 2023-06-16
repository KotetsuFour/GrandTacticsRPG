package sanity_check;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

public class TestMapFunctions {

	@Test
	public void testGetWithEqualArrays() {
		HashMap<int[], String> map = new HashMap<>();
		int[] key1 = {1, 2};
		int[] key2 = {1, 2};
		String works = "This works. You're sane";
		map.put(key1, works);
		assertEquals(works, map.get(key2));
	}

	@Test
	public void testGetWithEqualCharacters() {
		HashMap<Character, String> map = new HashMap<>();
		char key1 = 'a';
		char key2 = 'a';
		String works = "This works. You're sane";
		map.put(key1, works);
		assertEquals(works, map.get(key2));
	}
	@Test
	public void testGetWithEqualStrings() {
		HashMap<String, String> map = new HashMap<>();
		String key1 = "key1";
		String key2 = "key2";
		String works = "This works. You're sane";
		map.put(key1, works);
		assertEquals(works, map.get(key2));
	}

}
