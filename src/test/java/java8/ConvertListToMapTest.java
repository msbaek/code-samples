package java8;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

class Animal {
    private int id;
    private String name;

    public Animal(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    //  constructor/getters/setters
};

public class ConvertListToMapTest {
    List<Animal> animals;

    @BeforeEach
    void setUp() {
        animals = new ArrayList<>();
        IntStream.range(1, 10)
                .forEach(idx -> animals.add(new Animal(idx, String.format("name%2d", idx))));
    }

    public Map<Integer, Animal> convertListBeforeJava8(List<Animal> list) {
        Map<Integer, Animal> map = new HashMap<>();
        for (Animal animal : list)
            map.put(animal.getId(), animal);
        return map;
    }

    public Map<Integer, Animal> convertListAfterJava8(List<Animal> list) {
        return list.stream()
                .collect(Collectors.toMap(Animal::getId, animal -> animal));
    }

    @Test
    void shouldContainsAllValues() {
        assertThat(convertListAfterJava8(animals).values(), containsInAnyOrder(animals.toArray()));
    }
}
