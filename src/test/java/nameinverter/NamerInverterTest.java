package nameinverter;

import org.junit.Test;

public class NamerInverterTest {
	@Test
	public void given_null___returns_empty_string() throws Exception {
		assertThat(invert(null), is(""));
	}
}
