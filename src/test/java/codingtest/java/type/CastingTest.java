package codingtest.java.type;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CastingTest {
    class Animal {
        public void eat() {
        }
    }

    class Cat extends Animal {

        public void eat() {
            // ...
        }

        public void meow() {
            // ...
        }
    }

    class Dog extends Animal {

        public void eat() {
            // ...
        }
    }

    public class AnimalFeederGeneric<T> {
        private Class<T> type;

        public AnimalFeederGeneric(Class<T> type) {
            this.type = type;
        }

        public List<T> feed(List<Animal> animals) {
            List<T> list = new ArrayList<>();
            animals.forEach(animal -> {
                if(type.isInstance(animal)) {
                    T objAsType = type.cast(animal);
                    list.add(objAsType);
                }
            });
            return list;
        }
    }

    @Test
    public void whenParameterCat_thenOnlyCatsFed() {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Cat());
        animals.add(new Dog());
        AnimalFeederGeneric<Cat> catFeeder
                = new AnimalFeederGeneric<>(Cat.class);
        List<Cat> fedAnimals = catFeeder.feed(animals);

        assertTrue(fedAnimals.size() == 1);
        assertTrue(fedAnimals.get(0) instanceof Cat);
    }
}
