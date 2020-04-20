package com.justlikethat.file;

/*
cd // present java file directory 
cd c:\Users\sravanthi\Documents
javac MyFileProcess.java
java MyFileProcess REPLACE "D:\Legacy migration\ScriptCare\RxSense_ScriptCare_201907_No_CF.txt" "D:\Legacy migration\ScriptCare\output.txt"
java MyFileProcess SPLIT "D:\Legacy migration\ScriptCare\RxSense_ScriptCare_201907_No_CF.txt" 15000
java MyFileProcess MERGE "D:\Legacy migration\ScriptCare" CSV "OUTPUT.XML"
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

public class MyFileProcess {

	public static void main(String s[]) {
		
		s = new String[4];
		s[0] = "MERGE";
		s[1] = "C:\\Users\\harsh.LAPTOP-TPCB1OT7\\Desktop";
		s[2] = "csv"; 
		s[3] = "OUTPUT2.TXT"; //*/
		
		String cmd = s[0];

		try {
			if (cmd == "REPLACE") {
				replaceInEachLine(s);
			} else if (cmd == "SPLIT") {
				splitIntoMultipleFiles(s);
			} else if(cmd == "MERGE") {
				mergeAllFiles(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void mergeAllFiles(String[] s) throws IOException {
		String folderLoc = s[1];
		String extn = s[2];
		String outputFileName = s[3];
		
		File folder = new File(folderLoc);
		System.out.println(folder.getAbsolutePath());
		String[] list = folder.list(new MyFileNameFilter(extn));
		
		String output = folderLoc + "\\" + outputFileName; 
		File outputFile = new File(output);
		FileWriter fileWriter = new FileWriter(outputFile, true);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		for(int i=0; i<list.length; i++) {
			System.out.println(list[i]);
			File currentFile = new File(folderLoc + "\\" + list[i]);
			
			
			BufferedReader reader = new BufferedReader(new FileReader(currentFile));
			String str;
			while ((str = reader.readLine()) != null) {
				bufferedWriter.write(str);
				bufferedWriter.write("\n");
			}
			reader.close();
			
		}
		
		bufferedWriter.close();
		fileWriter.close();
		
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

//FileNameFilter implementation
	class MyFileNameFilter implements FilenameFilter {

		private String extension;

		public MyFileNameFilter(String extension) {
			this.extension = extension.toLowerCase();
		}

		@Override
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(extension);
		}

	}
	
