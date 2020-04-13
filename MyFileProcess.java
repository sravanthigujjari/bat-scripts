/*
cd // present java file directory 
cd c:\Users\sravanthi\Documents
javac MyFileProcess.java
java MyFileProcess REPLACE "D:\Legacy migration\ScriptCare\RxSense_ScriptCare_201907_No_CF.txt" "D:\Legacy migration\ScriptCare\output.txt"
java MyFileProcess SPLIT "D:\Legacy migration\ScriptCare\RxSense_ScriptCare_201907_No_CF.txt" 15000

*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class MyFileProcess {

	public static void main(String s[]) {
		String cmd = s[0];
		if(cmd == "REPLACE") {
			replaceInEachLine(s);
		} else if(cmd == "SPLIT") {
			splitIntoMultipleFiles(s);
		}
	}
  
  private static void splitIntoMultipleFiles(String s[]) {
    	String input = s[1];
			String input2 = s[2];
    int linesInEachFile = Integer.parse(input2);
	
	BufferedReader reader = new BufferedReader(new FileReader(input));
	BufferedWriter writer = new BufferedWriter(new FileWriter(output));

	String str;
	int counter = 0;
	int fileCount = 1;
	while ((str = reader.readLine()) != null) {
		writer.write(str);
		writer.write("\n");
		counter++;
		
		if(counter >= linesInEachFile) {
			writer.close();
			writer = new BufferedWriter(new FileWriter(output));
		}
	}
				
  }
  
  private static void replaceInEachLine(String s[]) {
    try {
			String input = s[1];
			String output = s[2];

			BufferedReader reader = new BufferedReader(new FileReader(input));
			BufferedWriter writer = new BufferedWriter(new FileWriter(output));

			String str;
			String[] tokens;
			while ((str = reader.readLine()) != null) {
				writer.write(filter(merge(str.split(","), "|")));
				writer.write("\n");
			}

			writer.close();
			reader.close();
		} catch (Exception e) {
			System.out.println("Exception in running: " + e.getMessage());
			System.out.println("\n\nUsage:\njava MyFileProcess \"full-path-of-input-file-with-extension\" \"full-path-of-output-file-with-extension\"");
			
		}
  }
	
	private static String filter(String str) {		
		return str.replace ( "\"", "" ); 
	}
	
	private static String merge(String[] t, String delim) {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<t.length; i++) {
			builder.append(t[i]).append(delim);
		}
		if(builder.length() > 1) {
			builder.setLength(builder.length()-1);
		}
		return builder.toString();
	}

}
