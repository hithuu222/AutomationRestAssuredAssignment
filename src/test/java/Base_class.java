import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;


public class Base_class {
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
        ExtentSparkReporter htmlReporter;
        ExtentReports extent;

        @BeforeSuite
        public void BSetup(){
                htmlReporter = new ExtentSparkReporter("extentReport.html");
                extent = new ExtentReports();
                extent.attachReporter(htmlReporter);

        }
        @AfterSuite
        public void BSetupEnd() {
                extent.flush();

        }
}