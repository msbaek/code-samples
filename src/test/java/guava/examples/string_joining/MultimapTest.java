package guava.examples.string_joining;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MultimapTest {
    private Multimap<String, String> multimap;

    @Before
    public void setUp() {
        multimap = ArrayListMultimap.create();
        addSomeKeyValues();
    }

    private void addSomeKeyValues() {
        multimap.put("Fruits", "Bannana");
        multimap.put("Fruits", "Apple");
        multimap.put("Fruits", "Pear");
        multimap.put("Vegetables", "Carrot");
    }

    @Test
    public void size_should_be_4() {
        assertThat(multimap.size(), equalTo(4));
    }

    @Test
    public void should_return_appropriate_list() {
        assertThat(multimap.get("Fruits").toArray(new String[3]), equalTo(new String[]{"Bannana", "Apple", "Pear"}));
        assertThat(multimap.get("Vegetables").toArray(new String[1]), equalTo(new String[]{"Carrot"}));
    }

    @Test
    public void should_return_all_values() {
        assertThat(multimap.values().toArray(new String[4]), equalTo(new String[]{"Bannana", "Apple", "Pear", "Carrot"}));
    }

    @Test
    public void should_not_return_removed_key_value() {
        multimap.remove("Fruits", "Pear");
        assertThat(multimap.get("Fruits").toArray(new String[2]), equalTo(new String[]{"Bannana", "Apple"}));
    }

    @Test
    public void should_be_empty_when_removed_all() {
        multimap.removeAll("Fruits");
        assertThat(multimap.get("Fruits").isEmpty(), equalTo(true));
    }
}
