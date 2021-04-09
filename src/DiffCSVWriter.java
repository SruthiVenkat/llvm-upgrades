import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import xmlreader.domain.LibDiffsModel;

public class DiffCSVWriter {
	public static Map<String, String> problemIDsToMsgsMap;
	public static long lib_versions_id;
	public static DatabaseConnector connector = DatabaseConnector.buildDatabaseConnector(true);

	public static void main(String[] args) {
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter old version");
		int old_version = myObj.nextInt();
		System.out.println("Enter new version");
		int new_version = myObj.nextInt();
		myObj.close();
		
		lib_versions_id = connector.getIdFromLibVersions(old_version, new_version);
		System.out.println(connector.getLibDiffs("Overridden_Virtual_Method", lib_versions_id));
		
		problemIDsToMsgsMap = readProblems(new File(".").getAbsolutePath()+File.separator+"problems.csv");
		writeData(new File(".").getAbsolutePath()+File.separator+"llvm-"+old_version+"-"+new_version+"-diffs.csv", lib_versions_id);
	}
	
	public static Map<String, String> readProblems(String filePath)
	{
		Map<String, String> problemIDsToMsgsMap = new HashMap<String, String>();
		File file = new File(filePath);
		try {
			FileReader filereader = new FileReader(file);
	        CSVReader csvReader = new CSVReaderBuilder(filereader)
	                                  .build();
	        List<String[]> allData = csvReader.readAll();
	  
	        for (String[] row : allData) {
	        	problemIDsToMsgsMap.put(row[0], row[1]);
	        }
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    }
		return problemIDsToMsgsMap;
	}

	public static void writeData(String filePath, long lib_versions_id)
	{
	    File file = new File(filePath);
	    try {
	        FileWriter outputfile = new FileWriter(file);
	        CSVWriter writer = new CSVWriter(outputfile);
	  
	        String[] header = { "Problem ID", "Old", "New", "Header", "Type"};
	        writer.writeNext(header);
	        
	        List<LibDiffsModel> renamedFieldsProblemsList = connector.getLibDiffs("Renamed_Field", lib_versions_id);
	        for (LibDiffsModel libDiffs: renamedFieldsProblemsList) {
	        	String[] data1 = {problemIDsToMsgsMap.get("Renamed_Field"), libDiffs.getTarget(), libDiffs.getNew_value(), libDiffs.getHeader(), libDiffs.getType() };
		        writer.writeNext(data1);
	        }
	        
	        List<LibDiffsModel> overridenVirtualMethodsFieldsProblemsList = connector.getLibDiffs("Overridden_Virtual_Method", lib_versions_id);
	        for (LibDiffsModel libDiffs: overridenVirtualMethodsFieldsProblemsList) {
	        	String[] data1 = {problemIDsToMsgsMap.get("Overridden_Virtual_Method"), libDiffs.getOld_value(), libDiffs.getNew_value(), libDiffs.getHeader(), libDiffs.getType() };
		        writer.writeNext(data1);
	        }
	        
	        writer.close();
	    }
	    catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	}
}
