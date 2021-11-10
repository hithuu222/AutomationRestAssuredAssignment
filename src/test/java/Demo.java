import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Demo {
    String authorization = "Authorization";
    String Basic = "Basic dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9jbGllbnQ6dXBza2lsbHNfcmVzdF9hZG1pbl9vYXV0aF9zZWNyZXQ=";
    String requestBody = "{\n" +
            "  \"username\": \"upskills_admin\",\n" +
            "  \"password\": \"Talent4$$\"\n" +
            "}";
    String product="{\n" +
            "\"model\": \"Lenovo Ideapad Laptop\",\n" +
            " \"quantity\": \"1000\",\n" +
            " \"price\": \"44000.00\",\n" +
            " \"product_description\": [\n" +
            "    {\n" +
            "      \"name\": \"Lenovo IdeaPad S100\", \n" +
            "      \"meta_title\": \"Lenovo IdeaPad S100\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    String category="{\n" +
            "\"category_description\": [\n" +
            "    {\n" +
            "      \"name\": \"Computers & Accessories\",\n" +
            "      \"meta_title\": \"Computers & Accessories\",\n" +
            "      \"description\": \"Description of the Computers & Accessories\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    @Test(priority=1)
    public String TokenPost() throws ParseException {
        Response TokenResponse = RestAssured.given().header(authorization,Basic).when().post("http://rest-api.upskills.in/api/rest_admin/oauth2/token/client_credentials");
        String body = TokenResponse.getBody().asString();
        Pattern Token_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher Token_m = Token_p.matcher(body);
        List<String> Token_l = new ArrayList<String>();
        while (Token_m.find()) {
            Token_l.add(Token_m.group(1));

        }
        String result = Token_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));

        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String access_token = jsonObject.get("access_token").getAsString();


        int statusCode = TokenResponse.getStatusCode();
        Assert.assertEquals(statusCode, 200, "Correct status code displayed");
        return access_token;
    }

    // admin login
    @Test(priority=2)
    public void Login() throws ParseException {
        String access_token = TokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        String AdminLoginbody = LoginResponse.getBody().asString();
        LoginResponse.prettyPrint();
        int statusCode= LoginResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminLoginbody);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminLoginjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminLoginjsonObject = new JsonParser().parse(AdminLoginjson).getAsJsonObject();
        String username=AdminLoginjsonObject.get("username").getAsString();

        Assert.assertEquals(statusCode,200,"Correct status code displayed");
        Assert.assertEquals(username,"upskills_admin","Correct username displayed");


    }

//get user details
    @Test(priority=3)
    public void AdminUserDetails() throws ParseException {
        String access_token = TokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response AdminUser= RestAssured.given().auth()
                .oauth2(access_token).get("http://rest-api.upskills.In/api/rest_admin/user");

        String AdminUserData = AdminUser.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminUserData);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminUserjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminUserjsonObject = new JsonParser().parse(AdminUserjson).getAsJsonObject();
        String username=AdminUserjsonObject.get("username").getAsString();
        System.out.println(AdminUserData);
        int statusCode= LoginResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

        Assert.assertEquals(statusCode,200,"Correct status code displayed");
        Assert.assertEquals(username,"upskills_admin","Correct username displayed");

    }

    //post product
    @Test(priority=4)
    public void Product() throws ParseException {
        String access_token = TokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response ProductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json").
                body(product).when().post("http://rest-api.upskills.in/api/rest_admin/products");

        String AdminProduct = ProductResponse.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminProduct);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminProductjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();
        System.out.println(AdminProductjsonObject);
    }
    @Test(priority=5)
    public void ProductByid() throws ParseException {
        String access_token = TokenPost();

        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        int Id=58;
        Response GetProductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/products"+Id);

        String AdminProduct = GetProductResponse.getBody().asString();
        GetProductResponse.prettyPrint();

        int statusCode= LoginResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminProduct);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminProductjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();



        Assert.assertEquals(statusCode,200,"Correct status code displayed");
        //Assert.assertEquals(product_Id,"58","Correct product id displayed");
    }

    @Test(priority=6)
    public void PostCategory() throws ParseException {
        String access_token = TokenPost();

        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response ProductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        String AdminProduct = ProductResponse.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminProduct);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminProductjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();
        System.out.println(AdminProductjsonObject);
    }

    @Test(priority=7)
    public void CategoryId() throws ParseException {
        String access_token = TokenPost();

        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response ProductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");



        String AdminProduct = ProductResponse.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminProduct);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminProductjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();

        String id = AdminProductjsonObject.get("id").getAsString();
        Response GetResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/categories"+id);
        GetResponse.prettyPrint();
    }
    @Test(priority=8)
    public void DeleteCategoryId() throws ParseException {
        String access_token = TokenPost();


        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response ProductResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");


        String AdminProduct = ProductResponse.getBody().asString();
        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminProduct);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminProductjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();

        String delete_id = AdminProductjsonObject.get("id").getAsString();
        Response DeleteCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().delete("http://rest-api.upskills.in/api/rest_admin/categories"+delete_id);
        DeleteCategoryResponse.prettyPrint();

    }
    @Test(priority=9)
    public void GetCategoryId() throws ParseException {
        String access_token = TokenPost();

        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        int Id = 2707;
        Response GetCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/categories"+Id);


        String AdminProduct = GetCategoryResponse.getBody().asString();

        Pattern login_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = login_p.matcher(AdminProduct);
        List<String> login_l = new ArrayList<String>();
        while (login_m.find()) {
            login_l.add(login_m.group(1));
        }
        String AdminProductjson = login_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();
        System.out.println(AdminProductjsonObject);


        int statusCode= LoginResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

       Assert.assertEquals(statusCode,200,"Correct status code displayed");
    }

    //post for logout
    @Test(priority=10)
    public void AdminLogoutPost() throws ParseException {

        //Login
        String access_token = TokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");


        Response AdminLogoutResponse = RestAssured.given().auth()
                .oauth2(access_token).post("http://rest-api.upskills.in/api/rest_admin/logout");
        AdminLogoutResponse.prettyPrint();

        int statusCode = AdminLogoutResponse.getStatusCode();
        System.out.println("Status code: "+statusCode);
        Assert.assertEquals(statusCode, 200, "Correct status code displayed");


    }
}



