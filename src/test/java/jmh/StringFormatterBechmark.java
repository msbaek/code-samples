package jmh;

import org.openjdk.jmh.annotations.*;

import java.util.Formatter;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark        Mode  Cnt     Score   Error  Units
 * formatter        avgt        962.231          ns/op
 * stringBuilder    avgt         71.831          ns/op
 * stringFormatter  avgt       1020.619          ns/op
 */
@Fork(value = 1, warmups = 1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
public class StringFormatterBechmark {
    private String name = "name";
    private String lName = "lName";
    private String nick = "nick";

    @Benchmark
    public void stringFormatter() {
        String.format("Contact {name=%s, lastName=%s, nickName=%s}", name, lName, nick);
    }

    @Benchmark
    public void formatter() {
        Formatter formatter = new Formatter();
        formatter.format("Contact {name=%s, lastName=%s, nickName=%s}", name, lName, nick);
    }

    @Benchmark
    public void stringBuilder() {
        StringBuilder sb = new StringBuilder();
        sb.append("Contact {name=");
        sb.append(name);
        sb.append(", lastName=");
        sb.append(lName);
        sb.append("nickName=");
        sb.append(nick);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
