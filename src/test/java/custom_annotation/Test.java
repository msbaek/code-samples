package custom_annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://www.mkyong.com/java/java-custom-annotations-example/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // can use in method only
public @interface Test {
    // should ignore this test ?
    public boolean enabled() default true;
    /**
     * Method declarations must not have any parameters or a throws clause. Return types are restricted to primitives, String, Class, enums,
     * annotations, and arrays of the preceding types.
     */
}
