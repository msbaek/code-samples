package nameinverter;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class NamerInverterTest {
	@Test
	public void given_null___returns_empty_string() throws Exception {
		assertThat(invert(null), is(""));
	}

	@Test
	public void given_empty_string___returns_empty_string() throws Exception {
		assertThat(invert(""), is(""));
	}

	@Test
	public void given_simple_name___returns_simple_name() throws Exception {
		assertThat(invert("Name"), is("Name"));
	}

	@Test
	public void given_first_last___returns_last_first() throws Exception {
		assertThat(invert("First Last"), is("Last, First"));
	}

	@Test
	public void given_first_last_with_multiple_spaces_between___returns_last_first() throws Exception {
		assertThat(invert("First   Last"), is("Last, First"));
	}

	@Test
	public void given_simple_name_with_leading_spaces___returns_simple_name() throws Exception {
		assertThat(invert(" Name"), is("Name"));
	}

	@Test
	public void given_honorific_and_first_last___returns_last_first() throws Exception {
		assertThat(invert("Mr. First Last"), is("Last, First"));
	}

	private String invert(String name) {
		if (name == null || name.isEmpty())
			return "";
		else {
			List<String> names = Arrays.asList(name.trim().split("\\s+"));
			if (names.size() > 1 && names.get(0).equals("Mr."))
				names.remove(0);
			if (names.size() == 1)
				return names.get(0);
			else
				return String.format("%s, %s", names.get(1), names.get(0));
		}
	}
}
