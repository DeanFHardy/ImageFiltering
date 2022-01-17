package ie.gmit.dip;

import java.util.Scanner;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumSet;
import javax.imageio.ImageIO;

public class Menu {
	private Scanner scan = new Scanner(System.in);

	public static Kernel k = Kernel.IDENTITY; // Sets my default value for Kernel (Using the original image)

	public static EnumSet<Kernel> kernelSet = EnumSet.allOf(Kernel.class); // Enum set of all Kernels

	private boolean keepRunning = true; // Boolean loop to keep menu operating

	public void startMenu() throws Exception {
		while (keepRunning) {
			menuHeader(); // Display Menu Heading
			showOptions(); // Display Menu options for User input

			do {

				int choice = getInput();

				switch (choice) {

				case 1: // Enter Image
						//System.out.println(ConsoleColour.GREEN_BOLD_BRIGHT);	
						//System.out.println("Please enter in your file:"); << Does this belong here or in your File method?
						//System.out.println(ConsoleColour.RESET);
					try {

						BufferedImage inputImage = ImageIO.read(new File(FileRetriever.inputFile()));

						Convolute.readImage(inputImage, k);

					} catch (Exception e) {

					}

					break;

				case 2: // Filter Options
					kernelSet.forEach(System.out::println); // Double colon to call and display all elements of a static
															// class. In this case the enum Kernel.
															// https://www.geeksforgeeks.org/double-colon-operator-in-java/
					break;

				case 3: // Select Filter
						// The user needs to be able to make a selection of a filter, so scan for a
						// String input.
						// If the user enters a spelling mistake or an unavailable Kernel, they will be
						// prompted to retry.
						// An option to return to Main Menu to recheck Filter Options Is possible by
						// entering '0'

					boolean pickKernel = false;
					String s;

					do {
						System.out.println("To select Kernel, type name from 'Filter Options' : ");
						System.out.println(ConsoleColour.RED_BOLD);
						System.out.println("Enter '0' to return to Menu Options.");
						System.out.println(ConsoleColour.RESET);
						s = getKernelSelect();
						try {
							if (s.equals("0")) {
								System.out.println("Returning to Menu Options");
								break;
							}
							setKernel(s);
							pickKernel = true;
						} catch (Exception e) {
							System.out.println(ConsoleColour.YELLOW);
							System.out.println("The Filter selected is not available, please select again.");
							System.out.println(ConsoleColour.RESET);
							pickKernel = false;
						}

					} while (!pickKernel);
					if (pickKernel) {
						System.out.println(ConsoleColour.GREEN);
						System.out.println("You have chosen " + s);
						System.out.println(ConsoleColour.RESET);

					}

					break;

					
				case 4: // Quit Application
					System.out.println("You have Quit application. Shutting Down....");
					keepRunning = false;
					break;

				default:
					System.out.println(ConsoleColour.YELLOW);
					System.out.println("Not a valid input, Select a Valid Input");
					System.out.println(ConsoleColour.RESET);
				}

			} while (true);

		}

	}

	private void menuHeader() { // Displays Menu banner
		System.out.println(ConsoleColour.BLUE_BRIGHT);
		System.out.println("***************************************************");
		System.out.println("* GMIT - Dept. Computer Science & Applied Physics *");
		System.out.println("*                                                 *");
		System.out.println("*           Image Filtering System V0.1           *");
		System.out.println("*     H.Dip in Science (Software Development)     *");
		System.out.println("*                                                 *");
		System.out.println("***************************************************");
		System.out.print(ConsoleColour.RESET);
	}

	private void showOptions() { // Displays menu
		System.out.println("1) Enter Image File Name");
		System.out.println("2) Filter Options");
		System.out.println("3) Select Filter");
		System.out.println("4) Quit");
		

	}

	private int getInput() { // Method to receive a selection input that also guards against invalid input by
								// returning to menu.
								// https://www.youtube.com/watch?v=25kUc_ammbw
		int choice = 0;
		while (choice < 1 || choice > 5) {
			try {
				System.out.print("\nPlease select an option:");
				choice = Integer.parseInt(scan.next());
			} catch (NumberFormatException e) {
				System.out.println(ConsoleColour.YELLOW);
				System.out.println("Invalid selection. Try Again.");
				System.out.println(ConsoleColour.RESET);
			}
		}

		return choice;
	}

	@SuppressWarnings("resource")
	private static String getKernelSelect() {
		Scanner scanStringInput = new Scanner(System.in);
		String s = scanStringInput.nextLine().toUpperCase();
		return s;
	}

	
	private static void setKernel(String s) {
		k = Kernel.valueOf(s);

	}
	
	
}
