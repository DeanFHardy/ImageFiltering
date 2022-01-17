package ie.gmit.dip;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

import javax.imageio.ImageIO;

public class Convolute { // http://tech.abdulfatir.com/2014/05/kernel-image-processing.html?m=1
							// https://lodev.org/cgtutor/filtering.html
							// https://blog.idrsolutions.com/2018/08/how-to-read-and-write-images-in-java/
							// https://stackoverflow.com/questions/9132149/how-to-convert-buffered-image-to-image-and-vice-versa

	private static Scanner scan = new Scanner(System.in);
	public static Kernel k;
	private static double mult_factor = 1.0d;
	private static double bias = 0d;
	private final static String extension = ".png";

	public static void readImage(BufferedImage inputImage, Kernel kernel) throws IOException { // Can't change to
																								// Private because not
																								// used locally
		k = kernel;
		writeIoImage(inputImage);

	}

	private static void writeIoImage(BufferedImage inputImage) throws IOException {
		// Method that receives the user image and outputs a new file image with the
		// added filter
		BufferedImage outputImage = imageConvolution(inputImage);

		if (tryNewFilter()) {
			Kernel newFilter = getNewFilter();
			readImage(outputImage, newFilter);
		} else

			try {

				ImageIO.write(outputImage, "PNG", new File(outputFileName() + extension));

			} catch (FileNotFoundException e) {
				System.out.println(ConsoleColour.YELLOW_BOLD);
				System.out.println("[ERROR]");
				System.out.println(ConsoleColour.RESET);
				ImageIO.write(outputImage, "PNG", new File("output.png"));
			}

	}

	private static boolean tryNewFilter() {	//Method which gives a user a choice to apply a filter or continue to outputting the new image
		System.out.println(ConsoleColour.GREEN_BOLD);
		System.out.println("1) Add New Filter");
		System.out.println("2) Continue to Output Entry");
		System.out.println(ConsoleColour.RESET);
		int choice = Integer.parseInt(scan.next());
		if (choice == 1) {
			return true;

		} else

			return false;
	}

	private static Kernel getNewFilter() { //Allows the user to select and apply a new filter from the default Kernel is use
		Kernel additionalFilter = null;
		System.out.println(ConsoleColour.GREEN_BOLD);
		Menu.kernelSet.forEach(System.out::println); //Recalls menu option to display Filter names 
		System.out.println(ConsoleColour.RESET);

		boolean loop;
		do {
			System.out.println(ConsoleColour.GREEN_BOLD);
			System.out.println("To select Kernel, type name from 'Filter Options' : ");
			System.out.println(ConsoleColour.RESET);
			String newFilter = scan.next();
			try {
				additionalFilter = Kernel.valueOf(newFilter.toUpperCase());
																			
				loop = true;
			} catch (Exception e) {
				System.out.println(ConsoleColour.YELLOW_BOLD);
				System.out.println("The Filter selected is not available, please select again.");
				System.out.println(ConsoleColour.RESET);
				loop = false;
			}
		} while (!loop);
		if (loop) {
			System.out.println(ConsoleColour.GREEN_BOLD);
			System.out.println("Confirmed: adding filter " + additionalFilter.toString());

			System.out.println(ConsoleColour.RESET);
			System.out.println();
		}
		return additionalFilter;
	}

	private static String outputFileName() throws FileNotFoundException { // This will get the name and location of
																			// where to store the convoluted
		// image
		Scanner scan2 = new Scanner(System.in);
		System.out.println(ConsoleColour.GREEN);
		System.out.println("Enter File Name for Output:");
		System.out.println(ConsoleColour.RESET);
		String outputImageName = scan2.nextLine().trim();
		System.out.println(ConsoleColour.GREEN);
		System.out.println("Enter Directory Path for Output:");
		System.out.println(ConsoleColour.RESET);
		String outputDirectory = scan2.nextLine().trim();
		String slash = FileRetriever.getOS();

		File f = new File(outputDirectory);
		if (!(f).exists()) {
			System.out.println(ConsoleColour.YELLOW_BOLD);
			System.out.println("[ERROR]");
			System.out.println(ConsoleColour.RESET);
			return null;
		} else {
			return outputDirectory + slash + outputImageName;

		}

	}

	private static BufferedImage imageConvolution(BufferedImage image) { // This was an invaluable source in
																			// understanding the logic behind the Pixels
																			// in relation to the image
																			// tech.abdulfatir.com/2014/05/kernel-image-processing.html?m=1
		BufferedImage imageIn = userImage(image);
		BufferedImage imageOut = new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), imageIn.getType());

		int HEIGHT = image.getHeight();
		int WIDTH = image.getWidth();

		// Nested for Loops for 'imageOut'
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {

				// Values of RED GREEN & BLUE
				int outputRed, outputGreen, outputBlue;
				double red = 0.0, green = 0.0, blue = 0.0;

				int outputRGB = 0;

				try {
					for (int xOffset = Math.negateExact(k.getKernels().length / 2); xOffset <= k.getKernels().length
							/ 2; xOffset++) {
						for (int yOffset = Math.negateExact(k.getKernels().length / 2); yOffset <= k.getKernels().length
								/ 2; yOffset++) {

							// Calculation for X and Y coordinates of the pixel that will be multiplied with
							// Kernel
							// Modulo (%) deals with the outer edges of the image and allows the pixel to
							// wrap from the opposite edge
							int imageX = (x - k.getKernels().length / 2 + xOffset + WIDTH) % WIDTH;
							int imageY = (y - k.getKernels().length / 2 + yOffset + HEIGHT) % HEIGHT;

							int RGB = image.getRGB((imageX), (imageY));
							int R = (RGB >> 16) & 0xFF; // Bitshift 16 to get Red Value
							int G = (RGB >> 8) & 0xFF; // Bit Shift 8 to get Green Value
							int B = (RGB) & 0xFF;

							// The value is truncated to 0 and 255 if it goes beyond
							red += (R * (k.getKernels()[yOffset + k.getKernels().length / 2])[xOffset
									+ k.getKernels().length / 2] * mult_factor);
							green += (G * k.getKernels()[yOffset + k.getKernels().length / 2][xOffset
									+ k.getKernels().length / 2] * mult_factor);
							blue += (B * k.getKernels()[yOffset + k.getKernels().length / 2][xOffset
									+ k.getKernels().length / 2] * mult_factor);

						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("[ERROR!!]");
				}

				// The value is truncated to 0 and 255 if it goes beyond
				outputRed = (int) Math.min(Math.max((red + bias), 0), 255);
				outputGreen = (int) Math.min(Math.max((green + bias), 0), 255);
				outputBlue = (int) Math.min(Math.max((blue + bias), 0), 255);
				// The Pixel is then written to the output Image
				imageOut.setRGB(x, y, new Color(outputRed, outputGreen, outputBlue).getRGB());

				//

				outputRGB = outputRGB | (outputRed << 16);
				outputRGB = outputRGB | (outputGreen << 8);
				outputRGB = outputRGB | outputBlue;

			}
		}
		System.out.println(ConsoleColour.GREEN_BOLD);
		System.out.println("Convolution Successful!");
		System.out.println(ConsoleColour.RESET);

		return imageOut;
	}

	private static BufferedImage userImage(BufferedImage image) {
		
		//https://eclipse.github.io/imagen/guide/color/
		//www.tabnine.com/code/java/methods/java.awt.image.BufferedImage/getColorModel
		int imgType = image.getColorModel().getColorSpace().getType();
		boolean greyscale = (imgType == ColorSpace.TYPE_GRAY || imgType == ColorSpace.CS_GRAY);
		if (greyscale) {
			System.out.println(ConsoleColour.RED);
			System.out.println("Image found is Greyscale");
			System.out.println(ConsoleColour.RESET);
			return image;
		} else {
			System.out.println(ConsoleColour.GREEN_BOLD);
			System.out.println("Image Found..");
			System.out.println("Select from the output Options below:");
			System.out.println("1) Colour");
			System.out.println("2) GreyScale");
			System.out.println(ConsoleColour.RESET);
			int imgTypeChoice = scan.nextInt();
			while (imgTypeChoice > 2 || imgTypeChoice < 1) {
				try {
					System.out.println(ConsoleColour.YELLOW_BOLD);
					System.out.println("Input not Valid - Please Select 1 or 2");
					System.out.println(ConsoleColour.RESET);
					imgTypeChoice = Integer.parseInt(scan.nextLine());
				} catch (Exception e) {

				}
			}
			switch (imgTypeChoice) {
			case 1: //Return the current image selected with applied filter
				return image;
			case 2: // Convert the image to greyscale 
				return (toGreyScale(image));
			}
		}
		return image;
	}

	private static BufferedImage toGreyScale(BufferedImage image) {

		int WIDTH = image.getWidth();
		int HEIGHT = image.getHeight();

		// Nested For Loop to cover each pixel
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				int pixel = image.getRGB((x), (y));
				int R = (pixel >> 16) & 0xFF;
				int G = (pixel >> 8) & 0xFF;

				int B = (pixel) & 0xFF;

				int average = (R + G + B) / 3;

				pixel = (average << 16) | (average << 8) | average;

				image.setRGB(x, y, pixel);
			}
		}

		return image;
	}

	public static double getBias() {
		return bias;
	}

	public static void setBias(double bias) {
		Convolute.bias = bias;
	}

	// MultiFactor multiplies each cell of the kernel to increase / decrease the
	// kernels effect. Also affects brightness so may need to be offset with bias
	public static double getMultiFactor() {
		return mult_factor;
	}

	public static void setMultiFactor(double multiFactor) {
		Convolute.mult_factor = multiFactor;
	}

}
