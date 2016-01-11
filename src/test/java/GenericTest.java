import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class GenericTest {
    private void printCollection(Collection<?> c) {
        for (Object o : c)
            System.out.println(o);
    }

    @Test
    public void printIt() {
        Collection<Object> c = new ArrayList<Object>();
        c.add(new Object());
        printCollection(c);
    }

    <T> void fromArrayToCollection(T[] a, Collection<T> c) {
        for (T o : a)
            c.add(o);
    }

    @Test
    public void a2c() {
        Object[] oa = new Object[100];
        Collection<Object> co = new ArrayList<Object>();
        fromArrayToCollection(oa, co);

        String [] sa = new String[100];
        Collection<String> cs = new ArrayList<String>();
        fromArrayToCollection(sa, cs); // T는 String
        fromArrayToCollection(sa, co); // T는 Object

        Integer[] ia = new Integer[100];
        Float[] fa = new Float[100];
        Number[] na = new Number[100];
        Collection<Number> cn = new ArrayList<Number>();
        fromArrayToCollection(ia, cn); // T - Number
        fromArrayToCollection(fa, cn); // T - Number
        fromArrayToCollection(na, cn); // T - Number
        fromArrayToCollection(na, co); // T - Object
//        fromArrayToCollection(na, cs); // compile error
    }
}
