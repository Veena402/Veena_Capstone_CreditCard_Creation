package CreditCard;

import CreditcardDatabase.CreditCardDB;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.sql.*;
import java.util.List;
import java.util.Map;

import static ReadXLData.DataReadXL.createJsonBody ;

import static ReadXLData.DataReadXL.readingXLFileData;

public class Creditcard {
    CreditCardDB DB;
    private Connection con;
    private ExtentSparkReporter spark;
    private ExtentReports extent;
    private ExtentTest logger;

    @BeforeClass
    public void init() {
        DB = new CreditCardDB();

        spark = new ExtentSparkReporter(System.getProperty("user.dir") + "/Report/validation.html");
        spark.config().setDocumentTitle("Get the details of credit card user");
        spark.config().setReportName("Credit card report");
        spark.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("OS", "Windows 10");
        extent.setSystemInfo("Build Tester", "Veena N");
        extent.setSystemInfo("Environment Name", "QA");
    }

    @Test(priority = 0)
    public void DatabaseValidation() throws SQLException {
        DB.Connect();
        DB.CreateDatabase();
        DB.createnewTable();
        DB.InsertData();
        DB.CreatePanCardTable();
        DB.InsertDatapan();
    }

    @Parameters({"url"})
    @Test(priority = 1)
    public void GetCreditCardDetails(String url) throws SQLException {
        List<String> CreditCardNumbers = readingXLFileData();
        ValidateCardDetailsWithDB(url,CreditCardNumbers);

    }
    public void ValidateCardDetailsWithDB(String url,List<String >CreditCardNumbers) throws SQLException {
        DB.Connect();
        System.out.println("Database server is connected");
        con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306", "root", "root123");
        Statement stmt = con.createStatement();
        stmt.execute("use CreditCardDB");
        String selectQuery = "SELECT cc.Name, cc.Year, cc.CreditCardNumber, cc.CreditLimit, cc.ExpDate, cc.CardType, pn.PanNumber " +
                "FROM CreditCardDB.creditcards cc " +
                "JOIN PanNumberTable pn ON cc.CreditCardNumber = pn.CardNumber " +
                "WHERE cc.CreditCardNumber = ?";
        PreparedStatement prepstmt =con.prepareStatement(selectQuery);

        for (String cardnumber : CreditCardNumbers){
            prepstmt.setString(1,cardnumber);
            try (ResultSet resultSet = prepstmt.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("Credit card details not found: " + cardnumber);
                }

                String name = resultSet.getString("Name");
                int year = resultSet.getInt("Year");
                String creditcardNo = resultSet.getString("CreditCardNumber");
                double creditLimit = resultSet.getDouble("CreditLimit");
                String expdate = resultSet.getString("ExpDate");
                String cardType = resultSet.getString("CardType");
                String panNumber = resultSet.getString("PanNumber");

                System.out.println("Retrieved details from database: " + "Name: " + name + ", " + "Year: " + year + ", " + "Credit Card Number: " + creditcardNo + ", " +
                        "Credit Limit: " + creditLimit + ", " + "EXP Date: " + expdate + ", " + "Card Type: " + cardType + ", " + "PAN Number: " + panNumber);

                PostCallRequestCreditCardCreation(url, name, year, creditcardNo, creditLimit, expdate, cardType, panNumber);
            } catch (Exception e) {
                System.out.println("Exception is " + e.getMessage());
            }
        }
    }
    public void PostCallRequestCreditCardCreation(String url, String Name, int year, String CreditCardNumber, double limit, String expdate, String CardType, String PanNumber){
        Response response = given()
                .contentType(ContentType.JSON)
                .body(createJsonBody(Name, year, CreditCardNumber, limit, expdate, CardType))
                .when()
                .post(url);

        int statuscode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        System.out.println("Status code:" + statuscode);
        System.out.println("Response from API:" + responseBody);

        logger = extent.createTest("Credit Card - " + CreditCardNumber);
        logger.info("API Request: " + createJsonBody(Name, year, CreditCardNumber, limit, expdate, CardType));
        logger.info("API Response: " + responseBody);

        JsonPath jsonPath = JsonPath.from(responseBody);
        String cardHoldername = jsonPath.getString("name");
        Map<String, Object> responseData = response.jsonPath().getMap("data");

        CompareDataResonseWithDB(responseData, cardHoldername,Name, year, CreditCardNumber, limit, expdate, CardType, PanNumber);
    }
    public void CompareDataResonseWithDB(Map<String, Object> responseData,String cardHoldername,String name, int year, String creditCardNumber, double limit, String expDate, String cardType, String panNumber){
        try {
            Assert.assertEquals(cardHoldername,name);
            Assert.assertEquals(responseData.get("year"), year);
            Assert.assertEquals(responseData.get("Credit Card Number"), creditCardNumber);
            double actualLimit = Double.parseDouble((String) responseData.get("Limit"));
            Assert.assertEquals(actualLimit, limit, 0.001);
            Assert.assertEquals(responseData.get("EXP Date"), expDate);
            Assert.assertEquals(responseData.get("Card Type"), cardType);

        } catch (AssertionError e) {
            logger.fail(e.getMessage());
        }

        ValidateCreditNumberMappedToPanInDB(creditCardNumber);
    }
    public void ValidateCreditNumberMappedToPanInDB(String CreditCardNumber){
        try {
            Statement statement = con.createStatement();
            statement.execute("use CreditCardDB");

            String selectQuery = "SELECT * FROM PanNumberTable WHERE CardNumber = ?";
            PreparedStatement preparedStatement = con.prepareStatement(selectQuery);
            preparedStatement.setString(1, CreditCardNumber);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String panNumber = resultSet.getString("PanNumber");

                System.out.println("Card Number " + CreditCardNumber + " is mapped with PAN Number " + panNumber + " in the database.");
                logger.info("Card Number " + CreditCardNumber + " is mapped with PAN Number " + panNumber + " in the database.");
            } else {
                System.out.println("No PAN Card mapping found:"+CreditCardNumber);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @AfterClass
    public void tearDown(){
        extent.flush();
    }
}

