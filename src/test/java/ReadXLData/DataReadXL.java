package ReadXLData;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class DataReadXL {
    public static String createJsonBody (String name, int year, String CreditCardNumber, double limit,String expdate, String CardType){
        String body = "{\n" +
                "    \"name\": \"" + name + "\",\n" +
                "    \"data\": {\n" +
                "        \"year\": " + year + ",\n" +
                "        \"Credit Card Number\": \"" + CreditCardNumber + "\",\n" +
                "        \"Limit\": \"" + limit + "\",\n" +
                "        \"EXP Date\": \"" +expdate+ "\",\n" +
                "        \"Card Type\": \"" + CardType + "\"\n" +
                "    }\n" +
                "}";
        return body;
    }
    public static List<String> readingXLFileData() {
        List<String> CreditCardnumbers =new ArrayList<>();
        try{
            String xlFile ="Data/CreditCardData.xlsx";
            FileInputStream File = new FileInputStream(xlFile);
            Workbook workbook = new XSSFWorkbook(File);
            Sheet sheet =workbook.getSheet("Sheet1");

            for (int i =0; i <=sheet.getLastRowNum();i++){
                Row row = sheet.getRow(i);
                Cell cell =row.getCell(0);
                String CreditCardNumber =cell.getStringCellValue().trim();
                CreditCardnumbers.add(CreditCardNumber);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return CreditCardnumbers;
    }
}
