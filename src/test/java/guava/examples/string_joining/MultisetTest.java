package guava.examples.string_joining;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class MultisetTest {

    @Test
    public void multiset() {
        int idx = 0;
        Multiset<String> tags = HashMultiset.create();
        Collection<String>[] tagListSet = new ArrayList[10];
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("java", "c", "c#", "scala", "ruby"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("blog", "cafe", "bbs"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("java", "c"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("java", "c", "c#", "scala"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("mysql", "oracle", "postgresql"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("mysql", "oracle", "postgresql"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("hbase", "cassandra", "mongodb"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("hbase", "cassandra", "mongodb"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("java", "c", "c#", "scala", "ruby"));
        tagListSet[idx++] = new ArrayList<String>(Arrays.asList("java", "c", "c#", "scala", "ruby"));

        for (Collection<String> tagList : tagListSet)
            tags.addAll(tagList);
        System.out.printf("distinct tags: %s\n", tags.elementSet());
        System.out.printf("count for java tag: %d\n", tags.count("java"));
        System.out.printf("total count %d\n", tags.size());
    }
}
