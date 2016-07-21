package mocking_part2;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BasicMathTest implements Adder {
	private BasicMath basicMath;

	@Before
	public void setUp() throws Exception {
		basicMath = new BasicMath();
		basicMath.setAdder(this);
	}

	@Test
	public void test_interaction_not_state() {
		int a = 3;
		int b = 4;
		basicMath.multiply(a, b);

		assertThat(traceList.toString(), is("[ 0 +  4,  4 +  4,  8 +  4]"));
	}

	@Override
	public int add(int p, int b) {
		traceList.add(String.format("%2d + %2d", p, b));
		return p + b;
	}

	List<String> traceList = new ArrayList<>();
}

class BasicMath {

	private Adder adder;

	public BasicMath() {
		this.adder = (p, b) -> p + b;
	}

	public int multiply(int a, int b) {
		boolean posA = a >= 0;
		boolean posB = b >= 0;
		boolean negP = posA != posB;

		a = posA ? a : -a;
		b = posB ? b : -b;

		int p = 0;
		while (a-- > 0)
			p = adder.add(p, b);

		return negP ? -p : p;
	}

	public void setAdder(Adder adder) {
		this.adder = adder;
	}

}

interface Adder {
	int add(int p, int b);

}
