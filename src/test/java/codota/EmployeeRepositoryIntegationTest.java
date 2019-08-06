package codota;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EmployeeRepositoryIntegationTest {
    @Test
    @DisplayName("id로 get하면 특정 직원의 정보를 json으로 반환한다")
    void getEmployeeById() throws IOException, JSONException {
        String url = "http://dummy.restapiexample.com/api/v1/employee/15647";
        Request request = new Request.Builder().url(url).build();

        OkHttpClient client = new OkHttpClient.Builder().build();
        try (Response response = client.newCall(request).execute()) {
           if(!response.isSuccessful())
               return;

           try (ResponseBody responseBody = response.body()) {
               String source = responseBody.string();
               JSONObject jsonObject = new JSONObject(source);
               EmployeeResource result = new EmployeeResource();
               result.setId(jsonObject.getLong("id"));
               result.setName(jsonObject.getString("employee_name"));
               result.setSalary(jsonObject.getLong("employee_salary"));
               result.setAge(jsonObject.getInt("employee_age"));
               result.setProfileImage(jsonObject.getString("profile_image"));

               System.out.println(source);
               System.out.println(result);
           }
        }
    }
}
