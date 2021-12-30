package cz.dangelcz.print.grfgen.test;

import java.awt.image.BufferedImage;
import java.util.Scanner;

import cz.dangelcz.print.grfgen.libs.IoHelper;
import cz.dangelcz.print.grfgen.logic.ImageProcessing;

public class ImageProcessingTest
{
	public static void main(String[] args)
	{
		System.out.println("Press enter to start ...");
		new Scanner(System.in).nextLine();
		
		BufferedImage testImage = IoHelper.loadImage("data/dishes.png");

		runTest(testImage, 1000);
		runTest(testImage, 100);
		runTest(testImage, 10);
		//runTest(testImage, 10000);
	}

	private static void runTest(BufferedImage testImage, int samples)
	{
		System.out.println("--- Running test with " + samples + " samples ---");
		
		BufferedImage outputImage;
		long start, stop, diff;

		start = System.currentTimeMillis();
		for (int i = 0; i < samples; i++)
		{
			outputImage = ImageProcessing.convertToBlackMask(testImage, 50);
		}
		stop = System.currentTimeMillis();
		diff = stop - start;
		System.out.println("Linear test:\t" + diff + " ms. Average " + diff / samples + " ms");

		start = System.currentTimeMillis();
		for (int i = 0; i < samples; i++)
		{
			outputImage = convertToBlackMaskXY(testImage, 50);
		}
		stop = System.currentTimeMillis();
		diff = stop - start;
		System.out.println("XY test:\t" + diff + " ms. Average " + diff / samples + " ms");
		System.out.println("--- Done ---");
		System.out.println();
	}

	public static BufferedImage convertToBlackMaskXY(BufferedImage inputImage, int blackness)
	{
		final int BLACK = 0xFF000000;
		final int WHITE = 0xFFFFFFFF;

		int height = inputImage.getHeight();
		int width = inputImage.getWidth();

		int colorThreshold = (int) (255 * 3 * ((double) blackness) / 100);
		int colorSum;

		int red, green, blue;
		int inputRgb;
		int outputRgb;

		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());

		for (int h = 0; h < height; h++)
		{
			for (int w = 0; w < width; w++)
			{
				inputRgb = inputImage.getRGB(w, h);
				red = (inputRgb >> 16) & 0x000000FF;
				green = (inputRgb >> 8) & 0x000000FF;
				blue = (inputRgb) & 0x000000FF;

				colorSum = red + green + blue;

				outputRgb = colorSum <= colorThreshold ? BLACK : WHITE;
				outputImage.setRGB(w, h, outputRgb);
			}
		}

		return outputImage;
	}
}
