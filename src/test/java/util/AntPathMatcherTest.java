package util;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * http://javacan.tistory.com/entry/Tip-Using-Spring-AntPathMatcher
 */
public class AntPathMatcherTest {
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Test
    public void match_path() throws Exception {
        assertThat(pathMatcher.match("/a/**/b", "/a/1/2/3/b"), is(true));
        assertThat(pathMatcher.match("/a/**/b", "/a/1/2/3/b/c"), is(false));
    }

    @Test
    public void extract_path() throws Exception {
        assertThat(pathMatcher.extractPathWithinPattern("/a/*", "/a/1"), is("1"));
        assertThat(pathMatcher.extractPathWithinPattern("/a/*", "/a/1/b"), is("1/b"));
        assertThat(pathMatcher.extractPathWithinPattern("/a/b/*", "/a/b/c"), is("c"));
        assertThat(pathMatcher.extractPathWithinPattern("/a/b/*.xml", "/a/b/c.xml"), is("c.xml"));

        assertThat(pathMatcher.extractPathWithinPattern("/*", "/a/b/c.xml"), is("a/b/c.xml"));
        assertThat(pathMatcher.extractPathWithinPattern("/*.xml", "/a/b/c.xml"), is("a/b/c.xml"));

        assertThat(pathMatcher.extractPathWithinPattern("/a/*", "/a/1/b"), is("1/b"));
        assertThat(pathMatcher.extractPathWithinPattern("/a/**", "/a/1/b"), is("1/b"));

        assertThat(pathMatcher.extractPathWithinPattern("/a/*", "/a/1/b.xml"), is("1/b.xml"));
        assertThat(pathMatcher.extractPathWithinPattern("/a/**", "/a/1/b.xml"), is("1/b.xml"));

        assertThat(pathMatcher.extractPathWithinPattern("/a/*/b", "/a/1/b"), is("1"));
        assertThat(pathMatcher.extractPathWithinPattern("/a/**/b", "/a/1/b"), is("1"));

//        assertThat(pathMatcher.extractPathWithinPattern("/a/**/b.xml", "/a/1/b.xml"), is("1"));
//        assertThat(pathMatcher.extractPathWithinPattern("/a/**/*.xml", "/a/1/b.xml"), is("1/b.xml"));
    }
}
