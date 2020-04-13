package com.justlikethat.file;

/*
cd // present java file directory 
cd c:\Users\sravanthi\Documents
javac MyFileProcess.java
java MyFileProcess REPLACE "D:\Legacy migration\ScriptCare\RxSense_ScriptCare_201907_No_CF.txt" "D:\Legacy migration\ScriptCare\output.txt"
java MyFileProcess SPLIT "D:\Legacy migration\ScriptCare\RxSense_ScriptCare_201907_No_CF.txt" 15000
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileProcess {

	public static void main(String s[]) {
		
/*		s = new String[3];
		s[0] = "SPLIT";
		s[1] = "C:\\Users\\harsh.LAPTOP-TPCB1OT7\\Desktop\\random-content.txt";
		s[2] = "2000";*/
		
		String cmd = s[0];

		try {
			if (cmd == "REPLACE") {
				replaceInEachLine(s);
			} else if (cmd == "SPLIT") {
				splitIntoMultipleFiles(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void splitIntoMultipleFiles(String s[]) throws IOException {
		String input = s[1];
		String input2 = s[2];
		int linesInEachFile = Integer.parseInt(input2);

		File inputFile = new File(input);
		String fullFileName = inputFile.getAbsolutePath();
		String fileName = inputFile.getName();
		fileName = fileName.substring(0, fileName.indexOf("."));
		
		String parentFolder = fullFileName.substring(0, fullFileName.lastIndexOf("\\"));
		
		String fileExtn = fullFileName.substring(fullFileName.lastIndexOf(".")+1);
		
		System.out.println(fullFileName);
		System.out.println(parentFolder);
		System.out.println(fileName);
		System.out.println(fileExtn);

		int counter = 0;
		int fileCount = 1;
		File output = new File(parentFolder + "\\" + fileName + fileCount + "." + fileExtn);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));

		String str;
		
		while ((str = reader.readLine()) != null) {
			writer.write(str);
			writer.write("\n");
			counter++;

			if (counter >= linesInEachFile) {
				writer.close();
				fileCount++;
				counter = 0;
				output = new File(parentFolder + "\\" + fileName + fileCount + "." + fileExtn);				
				writer = new BufferedWriter(new FileWriter(output));
			}
		}
		
		if(writer != null) {
			writer.close();
		}
		
		reader.close();

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
			System.out.println(
					"\n\nUsage:\njava MyFileProcess \"full-path-of-input-file-with-extension\" \"full-path-of-output-file-with-extension\"");

		}
	}

	private static String filter(String str) {
		return str.replace("\"", "");
	}

	private static String merge(String[] t, String delim) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < t.length; i++) {
			builder.append(t[i]).append(delim);
		}
		if (builder.length() > 1) {
			builder.setLength(builder.length() - 1);
		}
		return builder.toString();
	}

}
