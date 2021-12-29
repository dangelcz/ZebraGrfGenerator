package cz.dangelcz.print.grfgen.gui.window;

import java.awt.image.BufferedImage;

import cz.dangelcz.print.grfgen.libs.IoHelper;
import cz.dangelcz.print.grfgen.logic.ImageProcessing;

public class GrfGeneratorWindowData
{
	private BufferedImage sourceImage;
	private BufferedImage transformedImage;
	private BufferedImage outputImage;
	private String outputCode;

	private String inputFilePath;
	private String outputFilePath;
	private boolean compress;
	private int blackness;

	public void loadSourceImage(String inputImagePath)
	{
		this.inputFilePath = inputImagePath;
		sourceImage = IoHelper.loadImage(inputImagePath);
		sourceImage = ImageProcessing.transparencyToWhite(sourceImage);
		resetTransformation();		
	}

	public BufferedImage getSourceImage()
	{
		return sourceImage;
	}

	public void setSourceImage(BufferedImage sourceImage)
	{
		this.sourceImage = sourceImage;
	}

	public BufferedImage getTransformedImage()
	{
		return transformedImage;
	}

	public void setTransformedImage(BufferedImage transformedImage)
	{
		this.transformedImage = transformedImage;
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
		transformedImage = ImageProcessing.cloneImage(sourceImage);
	}
}