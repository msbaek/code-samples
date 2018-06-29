package json;

import com.jayway.jsonpath.JsonPath;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * http://www.baeldung.com/jsonpath-count
 */
public class JsonPathTest {
    private final String json = "{\n" +
            "    \"items\":{\n" +
            "        \"book\":[\n" +
            "            {\n" +
            "                \"author\":\"Arthur Conan Doyle\",\n" +
            "                \"title\":\"Sherlock Holmes\",\n" +
            "                \"price\":8.99\n" +
            "            },\n" +
            "            {\n" +
            "                \"author\":\"J. R. R. Tolkien\",\n" +
            "                \"title\":\"The Lord of the Rings\",\n" +
            "                \"isbn\":\"0-395-19395-8\",\n" +
            "                \"price\":22.99\n" +
            "            }\n" +
            "        ],\n" +
            "        \"bicycle\":{\n" +
            "            \"color\":\"red\",\n" +
            "            \"price\":19.95\n" +
            "        }\n" +
            "    },\n" +
            "    \"url\":\"mystore.com\",\n" +
            "    \"owner\":\"baeldung\"\n" +
            "}";

    @Test
    void count_json_objects() {
        Map<String, String> objectMap = JsonPath.read(json, "$");
        assertThat(objectMap.keySet().size())
                .isEqualTo(3);
    }

//    @Test
//    void count_json_array_size() {
//        JSONArray jsonArray = JsonPath.read(json, "$.items.book[*]");
//        assertThat(jsonArray.length()).isEqualTo(2);
//    }
}
