import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
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

public class Demo extends Base_class {

//Generating access_token
    @Test(priority=1)
    public String TokenPost() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Get Access Token");
        test.log(Status.INFO, "Starting test case");

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
        test.pass("Access token generated successfully.");
        test.pass("Correct status code returned.");
        test.info("Test completed");
        return access_token;
    }

    // admin login
    @Test(priority=2)
    public void Login() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case Admin login");
        test.log(Status.INFO, "Starting test case");

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

        test.pass("Login Successfully");

        Assert.assertEquals(statusCode,200,"Correct status code displayed");
        Assert.assertEquals(username,"upskills_admin","Correct username displayed");
        test.pass("Correct status code and Correct username returned ");
        test.info("Test completed");
    }

//Fetching and displaying user details
    @Test(priority=3)
    public void AdminUserDetails() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Admin login user details");
        test.log(Status.INFO, "Starting test case");

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

        test.pass("Fetched Details Successfully");
        Assert.assertEquals(statusCode,200,"Correct status code displayed");
        Assert.assertEquals(username,"upskills_admin","Correct username displayed");
        test.pass("Correct status code and Correct username returned ");
        test.info("Test completed");

    }

    //Adding new product details
    @Test(priority=4)
    public void Product() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Add new product details ");
        test.log(Status.INFO, "Starting test case");
        String access_token = TokenPost();

        //login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");
        test.pass("Login Successfully");

        //post product
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
        test.pass("Post Product successfully.");
        test.info("Test completed");

    }
    //Fetching details of particular id
    @Test(priority=5)
    public void ProductByid() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case  to Get particular productId with details ");
        test.log(Status.INFO, "Starting test case");
        String access_token = TokenPost();

        //login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        test.pass("Login Successfully");

        int Id=58;
        //get product id with details
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
        test.pass("ProductId with details has been fetched successfully.");
        test.pass("Correct status code displayed ");
        test.info("Test completed");

    }

    //adding new category details
    @Test(priority=6)
    public void PostCategory() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case Add new category details");
        test.log(Status.INFO, "Starting test case");
        String access_token = TokenPost();

        //Login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        test.pass("Login Successfully");

        //post category
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
        test.pass("Post Category successfully.");
        test.info("Test completed");


    }

    //Fetching category details of particular id
    @Test(priority=7)
    public void CategoryId() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case  to Get particular categoryId with details ");
        test.log(Status.INFO, "Starting test case");

        String access_token = TokenPost();

        //login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        test.pass("Login Successfully");

        //post category
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

        //get category id
        Response GetResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/categories"+id);
        GetResponse.prettyPrint();
        test.pass("CategoryId with details has been fetched successfully.");
        test.info("Test completed");
    }

    //Deleting category details of particular id and checking whether id has been deleted
    @Test(priority=8)
    public void DeleteCategoryId() throws ParseException {
        ExtentTest test = extent.createTest("Rest Test Case to Delete particular categoryId with details and check whether it has deleted");
        test.log(Status.INFO, "Starting test case");
        String access_token = TokenPost();
        //login
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        test.pass("Login Successfully");

        //post category
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
        //delete category id with details
        Response DeleteCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().delete("http://rest-api.upskills.in/api/rest_admin/categories"+Id);
        DeleteCategoryResponse.prettyPrint();

        test.pass("CategorytId with details has been deleted successfully.");

        //check  whether details got deleted
        Response GetCategoryResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .when().get("http://rest-api.upskills.in/api/rest_admin/categories"+Id);

        GetCategoryResponse.prettyPrint();

        int statusCode= GetCategoryResponse.getStatusCode();
        System.out.println("Status code:"+statusCode);

        Assert.assertEquals(statusCode,404,"Correct status code displayed");
        test.pass("Category deleted and Category not found");
        test.pass("Correct status code displayed ");
        test.info("Test completed");

    }

    //logout
    @Test(priority=9)
    public void AdminLogoutPost() throws ParseException {

        ExtentTest test = extent.createTest("Rest Test Case to Delete particular categoryId with details and check whether it has deleted");
        test.log(Status.INFO, "Starting test case");

        //Login
        String access_token = TokenPost();
        Response LoginResponse = RestAssured.given().auth()
                .oauth2(access_token).header("Content-Type", "application/json")
                .body(requestBody).when().post("http://rest-api.upskills.in/api/rest_admin/login");

        //Logout
        Response AdminLogoutResponse = RestAssured.given().auth()
                .oauth2(access_token).post("http://rest-api.upskills.in/api/rest_admin/logout");
        AdminLogoutResponse.prettyPrint();

        int statusCode = AdminLogoutResponse.getStatusCode();
        System.out.println("Status code: "+statusCode);
        Assert.assertEquals(statusCode, 200, "Correct status code displayed");

        test.pass("Logout Successful");
        test.pass("Correct status code displayed ");
        test.info("Test completed");

    }
}



