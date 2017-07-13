package regexp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class NegativeLookaheadTest {
    String patterns = "^(?!hystrixStreamOutput)([a-z0-9.]+)$";

    @Test
    public void regex_negative_lookahead() throws Exception {
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher("a11");
        assertThat(matcher.matches(), is(true));
        matcher = pattern.matcher("hystrixStreamOutputa11");
        assertThat(matcher.matches(), is(false));
    }
}
