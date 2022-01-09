package cz.dangelcz.print.grfgen.gui.window;

import java.awt.image.BufferedImage;

import cz.dangelcz.print.grfgen.libs.IoHelper;
import cz.dangelcz.print.grfgen.logic.ImageProcessing;

/*
 * Tranformation workflow
 * =======================
 * 
 * Source image -> rotated image -> scaled image -> output image
 */
public class GrfGeneratorWindowData
{
	private BufferedImage sourceImage;

	private BufferedImage rotatedImage;
	private BufferedImage scaledImage;

	private BufferedImage outputImage;
	private String outputCode;

	private String inputFilePath;
	private String outputFilePath;
	private boolean compress;
	private int blackness;

	private int newWidth;
	private int newHeight;

	// width / height
	private double aspectRatio;

	private boolean keepAspectRatio;

	public void loadSourceImage(String inputImagePath)
	{
		this.inputFilePath = inputImagePath;
		sourceImage = IoHelper.loadImage(inputImagePath);
		sourceImage = ImageProcessing.transparencyToWhite(sourceImage);
		keepAspectRatio = true;

		updateFromSourceImage();
	}

	public BufferedImage getSourceImage()
	{
		return sourceImage;
	}

	public void setSourceImage(BufferedImage sourceImage)
	{
		this.sourceImage = sourceImage;
		updateFromSourceImage();
	}

	public BufferedImage getOutputImage()
	{
		return outputImage;
	}

	public void setOutputImage(BufferedImage outputImage)
	{
		this.outputImage = outputImage;
	}

	public String getOutputCode()
	{
		return outputCode;
	}

	public void setOutputCode(String outputCode)
	{
		this.outputCode = outputCode;
	}

	public String getInputFilePath()
	{
		return inputFilePath;
	}

	public void setInputFilePath(String inputFilePath)
	{
		this.inputFilePath = inputFilePath;
	}

	public String getOutputFilePath()
	{
		return outputFilePath;
	}

	public void setOutputFilePath(String outputFilePath)
	{
		this.outputFilePath = outputFilePath;
	}

	public boolean isCompress()
	{
		return compress;
	}

	public void setCompress(boolean compress)
	{
		this.compress = compress;
	}

	public int getBlackness()
	{
		return blackness;
	}

	public void setBlackness(int blackness)
	{
		this.blackness = blackness;
	}

	public void resetTransformation()
	{
		loadSourceImage(this.inputFilePath);
	}

	public void updateFromSourceImage()
	{
		newWidth = sourceImage.getWidth();
		newHeight = sourceImage.getHeight();

		aspectRatio = ((double) newWidth / (double) newHeight);

		rotatedImage = ImageProcessing.cloneImage(sourceImage);
		scaledImage = ImageProcessing.cloneImage(sourceImage);
	}

	public int getNewWidth()
	{
		return newWidth;
	}

	public void setNewWidth(int newWidth)
	{
		this.newWidth = newWidth;
	}

	public int getNewHeight()
	{
		return newHeight;
	}

	public void setNewHeight(int newHeight)
	{
		this.newHeight = newHeight;
	}

	public boolean keepAspectRatio()
	{
		return keepAspectRatio;
	}

	public void setKeepAspectRatio(boolean keepAspectRatio)
	{
		this.keepAspectRatio = keepAspectRatio;
	}

	public double getAspectRatio()
	{
		return aspectRatio;
	}

	public BufferedImage getScaledImage()
	{
		return scaledImage;
	}

	public void setRotatedImageWithScaleSwitch(BufferedImage rotated)
	{
		// switch dimensions
		newWidth = scaledImage.getHeight();
		newHeight = scaledImage.getWidth();
		aspectRatio = ((double) newWidth / (double) newHeight);

		setRotatedImage(rotated);
	}

	public void setRotatedImage(BufferedImage rotated)
	{
		this.rotatedImage = rotated;
		updateScaledImage();
	}

	public void updateScaledImage()
	{
		BufferedImage newScaledImage = ImageProcessing.resizeImage(rotatedImage, newWidth, newHeight);
		setScaledImage(newScaledImage);
	}

	public void setScaledImage(BufferedImage scaled)
	{
		this.scaledImage = scaled;
		newWidth = scaledImage.getWidth();
		newHeight = scaledImage.getHeight();
	}

	public BufferedImage getRotatedImage()
	{
		return this.rotatedImage;
	}
}
