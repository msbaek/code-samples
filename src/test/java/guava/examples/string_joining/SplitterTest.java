package guava.examples.string_joining;

import com.google.common.base.Splitter;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SplitterTest {
    @Test
    public void split() {
        Iterable<String> results = Splitter.on("|")
                .omitEmptyStrings()
                .split("|Harry||Ron||Hermione  ||");
        assertThat(results.toString(), is("[Harry, Ron, Hermione  ]"));
    }
}
