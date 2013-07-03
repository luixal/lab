package es.luixal.ragbag;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class Utils {

	public static String byteArrayToHexString(byte [] inarray) {
	    int i, j, in;
	    String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	    String out= "";
	
	    for(j = 0 ; j < inarray.length ; ++j) {
	        in = (int) inarray[j] & 0xff;
	        i = (in >> 4) & 0x0f;
	        out += hex[i];
	        i = in & 0x0f;
	        out += hex[i];
	    }
	    
	    return out;
	}
	
	public static void exportToExcel(ArrayList<String> items, String filename) throws IOException {
		// creating workbook and sheet:
//		SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory, exceeding rows will be flushed to disk
		Workbook wb = new HSSFWorkbook();
        Sheet sh = wb.createSheet();
        // cell used for every operations (avoiding freeing and reserving memory :P)
        Cell cell = null;
        // creating headers row:
        Row headers = sh.createRow(0);
        cell = headers.createCell(0);
        cell.setCellValue("Tag Núm");
        cell = headers.createCell(1);
        cell.setCellValue("Tag UID");
        // generating rows:
        for (int i = 0; i < items.size(); i++) {
        	// creating new row:
        	Row row = sh.createRow(i+1);
        	// creating new cell with "i" value:
        	cell = row.createCell(0);
        	cell.setCellValue(i+1);
        	// creating new cell with tag UID value:
        	cell = row.createCell(1);
        	cell.setCellValue(items.get(i));
        }
        // writing file:
        FileOutputStream out = new FileOutputStream(filename);
        wb.write(out);
        out.close();
        // dispose of temporary files backing this workbook on disk
//        wb.dispose();
	}
	
	public static void exportToExcel(ArrayList<String> items, int counterOffset, String filename) throws IOException {
		// creating workbook and sheet:
		Workbook wb = new HSSFWorkbook();
        Sheet sh = wb.createSheet();
        // cell used for every operations (avoiding freeing and reserving memory :P)
        Cell cell = null;
        // creating headers row:
        Row headers = sh.createRow(0);
        cell = headers.createCell(0);
        cell.setCellValue("Tag Núm");
        cell = headers.createCell(1);
        cell.setCellValue("Tag UID");
        // generating rows:
        for (int i = 0; i < items.size(); i++) {
        	// creating new row:
        	Row row = sh.createRow(i+1);
        	// creating new cell with "i" value:
        	cell = row.createCell(0);
        	cell.setCellValue(i+counterOffset);
        	// creating new cell with tag UID value:
        	cell = row.createCell(1);
        	cell.setCellValue(items.get(i));
        }
        // writing file:
        FileOutputStream out = new FileOutputStream(filename);
        wb.write(out);
        out.close();
	}
}
