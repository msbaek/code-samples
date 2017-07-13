package junit_test;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.rule.OutputCapture;

import static org.hamcrest.Matchers.containsString;

public class OutputCaptureTest {
    @Rule
    public OutputCapture output = new OutputCapture();

    @Test
    public void more_easy_characterization_test() throws Exception {
        System.out.println("hello");
        output.expect(containsString("hello"));
    }
}
