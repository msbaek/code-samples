package dynamic_casting;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://blog.frankel.ch/dynamic-casting-java/
 */

abstract class Animal {
    abstract protected String getType();
}

class Cat extends Animal {
    @Override
    protected String getType() {
        return "Cat";
    }
}

class Dog extends Animal {
    @Override
    protected String getType() {
        return "Dog";
    }
}

public class CatFilterTest {
    @Test
    public void filter_should_return_specified_types_only() {
        List<Animal> animals = Arrays.asList(
                new Cat(),
                new Dog(),
                new Cat(),
                new Dog(),
                new Cat(),
                new Dog()
        );

        filter(Cat.class, animals)
                .forEach(cat -> assertThat(cat.getType()).isEqualTo("Cat"));

        filter(Dog.class, animals)
                .forEach(dog -> assertThat(dog.getType()).isEqualTo("Dog"));
    }

    <T> List<T> filter(Class<T> clazz, List<?> items) {
        return items.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }
}
