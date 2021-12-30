package cz.dangelcz.print.grfgen.logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class ImageProcessing
{
	public static BufferedImage convertToBlackMask(BufferedImage inputImage, int blackness)
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

		int[] inputPixels = inputImage.getRGB(0, 0, width, height, null, 0, width);
		int[] outputPixels = new int[inputPixels.length];

		for (int i = 0; i < inputPixels.length; i++)
		{
			inputRgb = inputPixels[i];
			red = (inputRgb >> 16) & 0x000000FF;
			green = (inputRgb >> 8) & 0x000000FF;
			blue = (inputRgb) & 0x000000FF;

			colorSum = red + green + blue;

			outputRgb = colorSum <= colorThreshold ? BLACK : WHITE;
			outputPixels[i] = outputRgb;
		}

		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
		outputImage.setRGB(0, 0, width, height, outputPixels, 0, width);

		return outputImage;
	}

	public static BufferedImage convertToARGB(BufferedImage image)
	{
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage convertToRGB(BufferedImage image)
	{
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage transparencyToWhite(BufferedImage image)
	{
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = newImage.createGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, newImage.getWidth(), newImage.getHeight());
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static BufferedImage copyImage(BufferedImage source)
	{
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}

	public static BufferedImage cloneImage(BufferedImage source)
	{
		ColorModel colorModel = source.getColorModel();
		WritableRaster raster = source.copyData(null);
		boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();
		return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
	}

	private static BufferedImage rotate90or180(BufferedImage inputImage, double radian)
	{
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();
		double translationOffset = 0;

		// change of dimensions for 90 degrees
		if (Math.PI / 2 == Math.abs(radian))
		{
			width = inputImage.getHeight();
			height = inputImage.getWidth();
			translationOffset = Math.signum(radian) * (width - height) / 2;
		}

		double cx = width / 2;
		double cy = height / 2;

		BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
		Graphics2D g = outputImage.createGraphics();

		g.translate(translationOffset, translationOffset);
		g.rotate(radian, cx, cy);
		g.drawRenderedImage(inputImage, null);

		return outputImage;
	}

	public static BufferedImage rotate90Left(BufferedImage inputImage)
	{
		return rotate90or180(inputImage, -Math.PI / 2);
	}

	public static BufferedImage rotate90Right(BufferedImage inputImage)
	{
		return rotate90or180(inputImage, Math.PI / 2);
	}

	public static BufferedImage rotate180(BufferedImage inputImage)
	{
		return rotate90or180(inputImage, Math.PI);
	}

	public static BufferedImage flipHorizontaly(BufferedImage inputImage)
	{
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		BufferedImage b = new BufferedImage(width, height, inputImage.getType());
		Graphics g = b.getGraphics();
		g.drawImage(inputImage, 0 + width, 0, -width, height, null);
		g.dispose();
		return b;
	}

	public static BufferedImage flipVerticaly(BufferedImage inputImage)
	{
		int width = inputImage.getWidth();
		int height = inputImage.getHeight();

		BufferedImage b = new BufferedImage(width, height, inputImage.getType());
		Graphics g = b.getGraphics();
		g.drawImage(inputImage, 0, 0 + height, width, -height, null);
		g.dispose();
		return b;
	}

}
