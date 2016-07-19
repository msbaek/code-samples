package mocking_part2;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BasicMathTest {
	private BasicMath basicMath;

	@Before
	public void setUp() throws Exception {
		basicMath = new BasicMath();
	}

	@Test
	public void small_number() {
		int a = 3;
		int b = 4;
		int result = basicMath.multiply(a, b);

		assertThat(result, is(12));
	}

	class BasicMath {
		private int multiply(int a, int b) {
			boolean posA = a >= 0;
			boolean posB = b >= 0;
			boolean negP = posA != posB;

			a = posA ? a : -a;
			b = posB ? b : -b;

			int p = 0;
			while (a-- > 0)
				p = add(p, b);

			return negP ? -p : p;
		}

		private int add(int p, int b) {
			return p + b;
		}
	}
}
