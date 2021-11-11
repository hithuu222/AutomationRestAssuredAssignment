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

//Generating access_token
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

//Fetching and displaying user details
    @Test(priority=3)
    public void AdminUserDetails() throws ParseException {
        String access_token = TokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response AdminUser= RestAssured.given().auth()
                .oauth2(access_token).get("http://rest-api.upskills.In/api/rest_admin/user");

        String AdminUserData = AdminUser.getBody().asString();
        Pattern user_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher user_m = user_p.matcher(AdminUserData);
        List<String> user_l = new ArrayList<String>();
        while (user_m.find()) {
            user_l.add(user_m.group(1));
        }
        String AdminUserjson = user_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminUserjsonObject = new JsonParser().parse(AdminUserjson).getAsJsonObject();
        String username=AdminUserjsonObject.get("username").getAsString();
        System.out.println(AdminUserData);
        int statusCode= LoginResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

        Assert.assertEquals(statusCode,200,"Correct status code displayed");
        Assert.assertEquals(username,"upskills_admin","Correct username displayed");

    }

    //Adding new product
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
        ProductResponse.prettyPrint();
        Pattern product_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher product_m = product_p.matcher(AdminProduct);
        List<String> user_l = new ArrayList<String>();
        while (product_m.find()) {
            user_l.add(product_m.group(1));
        }
        String AdminProductjson = user_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();

    }
    //Fetching product details of particular id
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
        Pattern getproduct_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher getproduct_m = getproduct_p.matcher(AdminProduct);
        List<String> getproduct_l = new ArrayList<String>();
        while (getproduct_m.find()) {
            getproduct_l.add(getproduct_m.group(1));
        }
        String GetProductjson = getproduct_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject GetProductjsonObject = new JsonParser().parse(GetProductjson).getAsJsonObject();

        int statusCode= LoginResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);
        Assert.assertEquals(statusCode,200,"Correct status code displayed");

    }

    //adding new category details
    @Test(priority=6)
    public void PostCategory() throws ParseException {
        String access_token = TokenPost();

        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response CategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        String AdminProduct = CategoryResponse.getBody().asString();
        CategoryResponse.prettyPrint();
        Pattern category_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher category_m = category_p.matcher(AdminProduct);
        List<String> category_l = new ArrayList<String>();
        while (category_m.find()) {
            category_l.add(category_m.group(1));
        }
        String Categoryjson = category_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject CategoryjsonObject = new JsonParser().parse(Categoryjson).getAsJsonObject();

    }

    //Fetching category details of particular id
    @Test(priority=7)
    public void CategoryId() throws ParseException {
        String access_token = TokenPost();

        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response CategoryIdResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");

        String AdminProduct = CategoryIdResponse.getBody().asString();
        Pattern CategoryId_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher CategoryId_m = CategoryId_p.matcher(AdminProduct);
        List<String> CategoryId_l = new ArrayList<String>();
        while (CategoryId_m.find()) {
            CategoryId_l.add(CategoryId_m.group(1));
        }
        String AdminProductjson = CategoryId_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();

        String id = AdminProductjsonObject.get("id").getAsString();
        Response GetResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/categories"+id);
        GetResponse.prettyPrint();
    }
    //Deleting category details of particular id and checking whether id has been deleted
    @Test(priority=8)
    public void DeleteCategoryId() throws ParseException {
        String access_token = TokenPost();


        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        Response DeleteIdResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(category).when().post("http://rest-api.upskills.in/api/rest_admin/categories");


        String AdminProduct = DeleteIdResponse.getBody().asString();
        Pattern DeleteId_p = Pattern.compile("\\{([^{}]*)\\}");
        Matcher login_m = DeleteId_p.matcher(AdminProduct);
        List<String> DeleteId_l = new ArrayList<String>();
        while (login_m.find()) {
            DeleteId_l.add(login_m.group(1));
        }
        String AdminProductjson = DeleteId_l.stream().map(n -> String.valueOf(n)).collect(Collectors.joining(",", "{", "}"));
        JsonObject AdminProductjsonObject = new JsonParser().parse(AdminProductjson).getAsJsonObject();

        String Id = AdminProductjsonObject.get("id").getAsString();
        Response DeleteCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().delete("http://rest-api.upskills.in/api/rest_admin/categories"+Id);
        DeleteCategoryResponse.prettyPrint();

        Response GetCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/categories"+Id);

        GetCategoryResponse.prettyPrint();

        int statusCode= GetCategoryResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

        Assert.assertEquals(statusCode,404,"Correct status code displayed");

    }

    //logout
    @Test(priority=9)
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



