package caexbot;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class TestingGround {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Pair<String, String> p1 = new ImmutablePair<>("test", "hello");
		Pair<String, String> p2 = new ImmutablePair<>("test", "hello");
		
		System.out.println(p1.hashCode()==p2.hashCode());
	}

}
