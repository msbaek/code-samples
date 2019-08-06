package codota;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class EmployeeResource {
    private long id;
    private String name;
    private long salary;
    private int age;
    private String profileImage;
}
// http://dummy.restapiexample.com/api/v1/employee/15647
