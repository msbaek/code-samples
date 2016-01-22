package guava.examples.string_joining;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class StopwatchTest {
    @Test
    public void elapsedTime() throws InterruptedException {
        Stopwatch watch = Stopwatch.createStarted();
        Thread.sleep(1 * 100);
        System.out.printf("elapsed time in microsecods = %s", watch.elapsed(TimeUnit.MICROSECONDS));
    }
}
