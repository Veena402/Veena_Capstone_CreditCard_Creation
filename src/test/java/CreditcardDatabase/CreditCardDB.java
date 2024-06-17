package CreditcardDatabase;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
public class CreditCardDB {

    private ExtentTest logger;
    private Connection con;
    private String url = "jdbc:mysql://127.0.0.1:3306";
    private String uid = "root";
    private String pass = "root123";

    public void Connect(){
        try{
            System.out.println("Connecting to Database");
            con =DriverManager.getConnection(url,uid,pass);
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void CreateDatabase(){
        try{
            Statement stmt = con.createStatement();
            stmt.execute("CREATE DATABASE IF NOT EXISTS CreditCardDB");
            System.out.println("CreditCardDB Database is Created");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void createnewTable(){
        try{
            Statement stmt = con.createStatement();
            {
                String sql = "CREATE TABLE IF NOT EXISTS CreditCardDB.creditcards (\n" +
                        "  Name VARCHAR(45) NULL,\n" +
                        "  Year INT,\n" +
                        "  CreditCardNumber VARCHAR(45) NULL,\n" +
                        "  CreditLimit DECIMAL(10,2),\n" +
                        "  ExpDate VARCHAR(45),\n" +
                        "  CardType VARCHAR(20) NULL);";
                stmt.executeUpdate(sql);
                System.out.println("Table Created as creditcards");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void InsertData() throws  SQLException{
        Statement stmt = con.createStatement();
        stmt.execute("use CreditCardDB");
        stmt.execute("DELETE FROM CreditCardDB.creditcards");
        System.out.println("Inserting Data into the creditcards Table");
        stmt.execute("INSERT INTO CreditCardDB.creditcards (Name, Year, CreditCardNumber, CreditLimit, ExpDate, CardType) VALUES\n" +
                "('Ravi', 2023, '4111 1111 1111 1111', 5000.00, '2025-06-30', 'Visa'),\n" +
                "('Raju', 2021, '4000 1234 5678 9010', 7500.00, '2024-11-30', 'Visa'),\n" +
                "('Kumar', 2022, '4012 8888 8888 1881', 10000.00, '2023-09-30', 'Visa'),\n" +
                "('Dinesh', 2020, '5555 5555 5555 4444', 6000.00, '2025-04-30', 'Master'),\n" +
                "('Suresh', 2019, '1111 1111 2223 3323', 4000.00, '2024-02-28', 'Master'),\n" +
                "('Kishore', 2021, '7898 4532 1234 5465', 3000.00, '2023-12-31', 'Master'),\n" +
                "('Geeta', 2022, '5645 3423 3456 5678', 5000.00, '2026-08-31', 'Master'),\n" +
                "('Henry', 2023, '1234 5324 4567 7890', 8500.00, '2025-10-31', 'Visa'),\n" +
                "('Mohit', 2020, '2345 4567 4321 1234', 9000.00, '2024-06-30', 'Visa'),\n" +
                "('Preetham', 2022, '8900 9008 7876 5432', 11000.00, '2023-11-30', 'Visa'),\n" +
                "('Suma', 2023, '1234 4567 7654 3452', 7000.00, '2025-07-31', 'Visa'),\n" +
                "('Meena', 2021, '2345 5678 8900 0098', 6500.00, '2024-01-31', 'Visa'),\n" +
                "('Meera', 2019, '2344 0009 8800 7890', 5500.00, '2024-03-31', 'Master'),\n" +
                "('Nayana', 2020, '2900 9008 8009 9007', 8000.00, '2025-09-30', 'Master'),\n" +
                "('Keerthan', 2022, '9999 8456 6789 2345', 12000.00, '2024-11-30', 'Master'),\n" +
                "('Pavithra', 2023, '2345 5678 8900 0098', 9500.00, '2025-05-31', 'Master'),\n" +
                "('Heet', 2021, '1234 4324 3456 6543', 13000.00, '2024-12-31', 'Master'),\n" +
                "('Abishek', 2020, '1233 3334 3334 3344', 7000.00, '2025-04-30', 'Visa'),\n" +
                "('Wilson', 2022, '2344 4445 4444 4444', 8500.00, '2023-10-31', 'Visa'),\n" +
                "('Tina', 2019, '2344 3344 6666 7777', 6000.00, '2024-02-28', 'Visa');");
        System.out.println("Data Sucessfully Inserted into CreditCard");
    }
    public void CreatePanCardTable() throws SQLException{
        Statement stmt = con.createStatement();
        stmt.execute("use CreditCardDB");
        stmt.execute("CREATE TABLE IF NOT EXISTS CreditCardDB.PanNumberTable (\n" +
                "  CardNumber VARCHAR(45) NULL,\n" +
                "  PanNumber VARCHAR(40) NULL);");
        System.out.println("Table Created for PanNumber with Card Number.");
    }
    public void InsertDatapan() throws SQLException{
        Statement stmt = con.createStatement();
        stmt.execute("use CreditCardDB");
        stmt.execute("DELETE FROM CreditCardDB.PanNumberTable");
        stmt.execute("INSERT INTO CreditCardDB.PanNumberTable (CardNumber, PanNumber) VALUES\n" +
                "('4111 1111 1111 1111', 'DNSPA123456'),\n" +
                "('4000 1234 5678 9010', 'FREDF123453'),\n" +
                "('4012 8888 8888 1881', 'YGHJ1234567'),\n" +
                "('5555 5555 5555 4444', 'FTRE2345679'),\n" +
                "('1111 1111 2223 3323', 'ERTY3456790'),\n" +
                "('7898 4532 1234 5465', 'HGFR4567901'),\n" +
                "('5645 3423 3456 5678', 'RRRT0679012'),\n" +
                "('1234 5324 4567 7890', 'KJHG9790123'),\n" +
                "('2345 4567 4321 1234', 'NBGH7901234'),\n" +
                "('8900 9008 7876 5432', 'HRTY8012345'),\n" +
                "('1234 4567 7654 3452', 'ERTY7543210'),\n" +
                "('2345 5678 8900 0098', 'TGFR7654321'),\n" +
                "('2344 0009 8800 7890', 'HRTY9875432'),\n" +
                "('2900 9008 8009 9007', 'WERT0987543'),\n" +
                "('9999 8456 6789 2345', 'BGRF1097654'),\n" +
                "('2345 5678 8900 0098', 'MJKL2198765'),\n" +
                "('1234 4324 3456 6543', 'KJUI3109876'),\n" +
                "('1233 3334 3334 3344', 'WERS4310987'),\n" +
                "('2344 4445 4444 4444', 'NFRT5321098'),\n" +
                "('2344 3344 6666 7777', 'WQSD6532109');");
        System.out.println("data inserted into PanNumberTable successfully.");
    }
}
