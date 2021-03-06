package java_stream_api_extentions;

import lombok.Getter;

@Getter
class Person {
    private final int id;
    private final String firstName;
    private final String lastName;
    private final int age;

    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = 0;
    }

    public Person(int id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}
