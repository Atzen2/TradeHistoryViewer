package playground;

import java.util.ArrayList;
import java.util.List;

import easyCsvHandler.EasyCsvFile;
import easyCsvHandler.EasyCsvHandler;

public class Playground {

	public static void main(String[] args) {
		EasyCsvHandler handler = new EasyCsvHandler();
		
		String[] header = {"h1","h2","h3"};
		List<String[]> records = new ArrayList<>();
		
		String[] record1 = {"r1","r2","r3"};
		records.add(record1);
		
		String[] record2 = {"r4","r5","r6"};
		records.add(record2);
		
		String[] record3 = {"r7","r8","r9"};
		records.add(record3);
		
		
		try {
			handler.createCsvFile("./test.csv", new EasyCsvFile(header, records));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}













