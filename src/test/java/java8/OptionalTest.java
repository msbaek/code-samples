package java8;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.security.Key;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

/**
 * https://dzone.com/articles/java-8-top-tips
 */
@Slf4j
public class OptionalTest {
    @Test
    public void optional_should_only_be_used_for_return_types() throws Exception {
        /**
         * not parameters and not fields
         * http://blog.joda.org/2015/08/java-se-8-optional-pragmatic-approach.html
         */
    }

    @Test
    public void you_should_not_simply_call_get() throws Exception {
        Optional<Key> key = Optional.ofNullable(null);
        Map<Key, Object> map = new Hashtable<>();
        Object value = "value";

        /**
         * Simply calling get() without checking isPresent() first is likely to lead to a null pointer at some point.
         */
//        map.put(key.get(), value);

        /**
         * More Elegant Way
         */
        if(key.isPresent())
            map.put(key.get(), value);
        else
            map.put(createNewKey(), value);

        key = Optional.of(createNewKey());
        /**
         * More More Elegant Way
         */
        map.put(key.orElse(createNewKey()), value); // createNewKey() called all the times
        map.put(key.orElseGet(this::createNewKey), value); // createNewKey() called only if key is null
    }

    private Key createNewKey() {
        log.info("createKey");
        return new Key() {
            @Override
            public String getAlgorithm() {
                return null;
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public byte[] getEncoded() {
                return new byte[0];
            }
        };
    }
}
