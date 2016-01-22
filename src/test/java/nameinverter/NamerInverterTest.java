package nameinverter;

import org.junit.Test;

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
		assertThat(invert("Mrs. First Last"), is("Last, First")); // single assert rule. assert should be logical
	}

	@Test
	public void given_post_nominals_exists___post_nominals_stays_at_end() throws Exception {
		assertThat(invert("First Last Sr."), is("Last, First Sr."));
		assertThat(invert("First Last BS. Phd."), is("Last, First BS. Phd."));
	}

	@Test
	public void integrated_case() {
		assertThat(invert("   Rober Martin III esq.  "), is("Martin, Rober III esq."));
	}

	private String invert(String name) {
		return new NameInverter().invert(name);
	}

}
