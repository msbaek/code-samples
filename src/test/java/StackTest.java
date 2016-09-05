import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StackTest {
	private MyStack stack;

	@Before
	public void setUp() throws Exception {
		stack = new MyStack();
	}

	@Test public void afterPushingX_willPopX() {
		stack.push(99);
		assertThat(stack.pop(), is(99));
	}

	@Test public void afterPushingX_willPopX__andPushingY_willPopY() {
		stack.push(99);
		assertThat(stack.pop(), is(99));
		stack.push(1);
		assertThat(stack.pop(), is(1));
	}

	@Test public void afterPushingX_and_Y_willPopY__andX() {
		stack.push(99);
		stack.push(1);
		assertThat(stack.pop(), is(1));
		assertThat(stack.pop(), is(99));
	}
}
