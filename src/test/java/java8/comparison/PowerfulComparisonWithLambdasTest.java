package java8.comparison;

import com.google.common.collect.Lists;
import lombok.Data;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * http://www.baeldung.com/java-8-sort-lambda
 */

@Data
class Human {
    private String name;
    private int age;

    public Human() {
        super();
    }

    public Human(String name, int age) {
        super();

        this.name = name;
        this.age = age;
    }

    public static int compareByNameThenAge(Human lhs, Human rhs) {
        if (lhs.name.equals(rhs.name)) {
            return lhs.age - rhs.age;
        } else {
            return lhs.name.compareTo(rhs.name);
        }
    }
}

public class PowerfulComparisonWithLambdasTest {
    private final List<Human> humans = Lists.newArrayList(
            new Human("Sarah", 10),
            new Human("Jack", 12)
    );

    @Test
    public void givenPreLambda_whenSortingEntitiesByName_thenCorrectlySorted() {
        Collections.sort(humans, new Comparator<Human>() {
            @Override
            public int compare(Human h1, Human h2) {
                return h1.getName().compareTo(h2.getName());
            }
        });
        assertThat(humans.get(0), equalTo(new Human("Jack", 12)));
    }

    @Test
    public void whenSortingEntitiesByName_thenCorrectlySorted() {
        humans.sort(
                (Human h1, Human h2) -> h1.getName().compareTo(h2.getName()));

        assertThat(humans.get(0), equalTo(new Human("Jack", 12)));
    }

    @Test
    public void givenLambdaShortForm_whenSortingEntitiesByName_thenCorrectlySorted() {
        humans.sort((h1, h2) -> h1.getName().compareTo(h2.getName()));

        assertThat(humans.get(0), equalTo(new Human("Jack", 12)));
    }

    @Test
    public void givenMethodDefinition_whenSortingEntitiesByNameThenAge_thenCorrectlySorted() {
        humans.sort(Human::compareByNameThenAge);
        assertThat(humans.get(0), equalTo(new Human("Jack", 12)));
    }

    @Test
    public void givenInstanceMethod_whenSortingEntitiesByNameThenAge_thenCorrectlySorted() {
        Collections.sort(humans, Comparator.comparing(Human::getName));
        assertThat(humans.get(0), equalTo(new Human("Jack", 12)));
    }

    @Test
    public void whenSortingEntitiesByNameReversed_thenCorrectlySorted() {
        Comparator<Human> comparator
                = (h1, h2) -> h1.getName().compareTo(h2.getName());

        humans.sort(comparator.reversed());

        assertThat(humans.get(0), equalTo(new Human("Sarah", 10)));
    }

    @Test
    public void whenSortingEntitiesByNameThenAge_thenCorrectlySorted() {
        humans.sort((lhs, rhs) -> {
            if (lhs.getName().equals(rhs.getName())) {
                return lhs.getAge() - rhs.getAge();
            } else {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        assertThat(humans.get(0), equalTo(new Human("Sarah", 10)));
    }

    @Test
    public void
    givenComposition_whenSortingEntitiesByNameThenAge_thenCorrectlySorted() {
        humans.sort(
                Comparator.comparing(Human::getName)
                        .thenComparing(Human::getAge)
        );

        assertThat(humans.get(0), equalTo(new Human("Sarah", 10)));
    }
}
