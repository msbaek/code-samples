package java8.grouping_by_collector;

// http://www.baeldung.com/java-groupingby-collector

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

@Builder
@ToString
@Getter
class BlogPost {
    String title;
    String author;
    BlogPostType type;
    int likes;
}

enum BlogPostType {
    NEWS,
    REVIEW,
    GUIDE
}

@ToString
@EqualsAndHashCode
class Tuple {
    BlogPostType type;
    String author;

    public Tuple(BlogPostType type, String author) {
        this.type = type;
        this.author = author;
    }
}

public class GuideToJava8GroupingByCollectorTest {
    List<BlogPost> posts = new ArrayList<>();

    @BeforeEach
    public void setUp() throws Exception {
        System.out.println("BeforeEach");
        IntStream.range(1, 10).forEach(idx ->
                {
                    BlogPost post = BlogPost.builder()
                            .title("title" + idx)
                            .author("author" + idx % 3)
                            .type(idx % 3 == 0 ? BlogPostType.NEWS : idx % 3 == 1 ? BlogPostType.REVIEW : BlogPostType.GUIDE)
                            .likes(idx)
                            .build();
                    posts.add(post);
                }
        );
    }

    @Test
    @DisplayName("하나의 컬럼으로 단순 그룹핑하기")
    void simple_Grouping_by_a_Single_Column() {
        Map<BlogPostType, List<BlogPost>> postsPerType = posts.stream()
                .collect(groupingBy(BlogPost::getType));

        System.out.println(postsPerType);
    }

    @Test
    void grouping_by_with_a_complex_map_key_type() {
        Map<Tuple, List<BlogPost>> postsPerTypeAndAuthor = posts.stream()
                .collect(groupingBy(post -> new Tuple(post.getType(), post.getAuthor())));

        System.out.println(postsPerTypeAndAuthor);
    }

    @Test
    void modifying_the_returned_map_value_type() {
        Map<BlogPostType, Set<BlogPost>> postsPerType = posts.stream()
                .collect(groupingBy(BlogPost::getType, toSet()));

        System.out.println(postsPerType);
    }

    @Test
    void providing_a_secondary_group_by_collector() {
        Map<String, Map<BlogPostType, List<BlogPost>>> map = posts.stream()
                .collect(groupingBy(BlogPost::getAuthor, groupingBy(BlogPost::getType)));

        System.out.println(map);
    }

    @Test
    void getting_the_average_from_grouped_results() {
        Map<BlogPostType, Double> collect = posts.stream()
                .collect(groupingBy(BlogPost::getType, averagingInt(BlogPost::getLikes)));

        System.out.println(collect);
    }

    @Test
    void getting_the_sum_from_grouped_results() {
        Map<BlogPostType, Integer> collect = posts.stream()
                .collect(groupingBy(BlogPost::getType, summingInt(BlogPost::getLikes)));

        System.out.println(collect);
    }

    @Test
    void getting_the_maximum_or_minimum_from_grouped_results() {
        Map<BlogPostType, Optional<BlogPost>> collect = posts.stream()
                .collect(groupingBy(BlogPost::getType, maxBy(Comparator.comparingInt(BlogPost::getLikes))));

        System.out.println(collect);
    }

    @Test
    void getting_a_summary_for_an_attribute_of_grouped_results() {
        Map<BlogPostType, IntSummaryStatistics> collect = posts.stream()
                .collect(groupingBy(BlogPost::getType, summarizingInt(BlogPost::getLikes)));

        System.out.println(collect);
    }

    @Test
    void mapping_grouped_results_to_a_different_type() {
        Map<BlogPostType, String> collect = posts.stream()
                .collect(groupingBy(BlogPost::getType, mapping(BlogPost::getTitle, joining(", ", "Post Titles: [", "]"))));

        System.out.println(collect);
    }

    @Test
    void modifying_the_return_map_type() {
        EnumMap<BlogPostType, List<BlogPost>> collect = posts.stream()
                .collect(groupingBy(BlogPost::getType, () -> new EnumMap<>(BlogPostType.class), toList()));

        System.out.println(collect);
    }

    @Test
    void concurrent_grouping_by_collector() {
        ConcurrentMap<BlogPostType, List<BlogPost>> collect = posts.parallelStream()
                .collect(groupingByConcurrent(BlogPost::getType));

        System.out.println(collect);
    }
}
