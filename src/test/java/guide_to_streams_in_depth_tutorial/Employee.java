package guide_to_streams_in_depth_tutorial;

public class Employee {
    private final Integer id;
    private final String name;
    private Double salary;

    public Employee(Integer id, String name, Double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Double getSalary() {
        return salary;
    }

    public void salaryIncrement(Double percentage) {
        Double newSalary = salary + percentage * salary / 100;
        this.salary = newSalary;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
