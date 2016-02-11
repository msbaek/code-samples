import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class StackTest {
	@Test public void afterPushingX_willPopX() {
		stack.push(99);
		assertThat(stack.pop(), is(99));
	}
}
