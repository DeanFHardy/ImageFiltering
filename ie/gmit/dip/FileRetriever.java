package ie.gmit.dip;

import java.util.Scanner;
import java.io.*;

public class FileRetriever { // https://stackoverflow.com/questions/15624226/java-search-for-files-in-a-directory
								// https://www.youtube.com/watch?v=xqKXiU61r-w
									//https://docs.oracle.com/javase/tutorial/essential/io/pathOps.html
	
	private final static Scanner scan = new Scanner(System.in);
	private final static String extension = ".png"; // All our file extensions (suffix) are 'PNG' so declare it final
													// and add it to the end of each filename when entered.

	public static String inputFile() throws Exception {
		// Request user to Enter File name etc so as not to clog your Menu methods.
		// "Single principle rule".
		System.out.println(ConsoleColour.GREEN_BOLD_BRIGHT);
		System.out.println("Enter your File name here: ");
		System.out.println(ConsoleColour.RESET);

		String fileName = scan.nextLine().trim() + extension; //Scanned file entry plus default extension above
		System.out.println(ConsoleColour.GREEN_BOLD_BRIGHT);
		System.out.println("Enter Path:");
		System.out.println(ConsoleColour.RESET);
		
		String userPath = scan.nextLine().trim(); //Directory input to read in file path
		String slash = getOS();
		String loc = userPath + slash + fileName; //Location of file 
		
		
		File location = new File(loc);
		
		if(location.exists()) {
			System.out.println(ConsoleColour.YELLOW_BOLD);
			System.out.println("Your file has been succesfully located!");
			System.out.println(ConsoleColour.RESET);
			return location.getAbsolutePath();
		}
		else {
			System.out.println(ConsoleColour.YELLOW_BOLD);
			System.out.println("[ERROR]: File Not Located. Try Again.");
			System.out.println(ConsoleColour.RESET);
		}
		
		
		return null;
		
		
	}

	
	public static String getOS() {
		//This method will determine the Operating system ('OS') of the user and read the File Path correctly according to the OS
		String os = System.getProperty("os.name");
		String slash;
		if(os.contains("Windows"))
		{
			slash = "\\";
		}
		else  {
			slash = "//";
		}
		return slash;
	}

}
