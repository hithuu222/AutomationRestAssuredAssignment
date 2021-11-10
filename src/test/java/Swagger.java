import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.assertEquals;

public class Swagger {
    String auth = "Authorization";
    String base = "Basic dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9jbGllbnQ6dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9zZWNyZXQ=";
    public String access_token;

    @Test
    public String AdminTokenPost() {
        Response TokenResponse = RestAssured.given().header(auth, base).when().post("http://rest-api.upskills.in/api/rest_admin/oauth2/token/client_credentials");
        String body = TokenResponse.getBody().asString();
        Pattern p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher m = p.matcher(body);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            l.add(m.group(1));

        }
        String result = l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        System.out.println(result);
        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String access_token=jsonObject.get("access_token").getAsString();
        System.out.println((access_token));
        System.out.println("StatusCode:" + TokenResponse.getStatusCode());
        int statusCode = TokenResponse.getStatusCode();
        return access_token;
    }
//admin login
    @Test
    public void Login() {
        String access_token=AdminTokenPost();
        String requestBody = "{\n" +
                "  \"username\": \"upskills_admin\",\n" +
                "  \"password\": \"Talent4$$\"\n" +
                "}";
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        String AdminLoginbody = LoginResponse.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminLoginbody);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminLoginjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminLoginjsonObject = new JsonParser().parse(AdminLoginjson).getAsJsonObject();
        String Adminusername = AdminLoginjsonObject.get("username").getAsString();
        System.out.println(Adminusername);
    }

}
