package cz.dangelcz.print.grfgen.logic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Reads GRF back to graphic image
 */
public class GrfReader
{
	public static final int BLACK = 0xFF000000;
	public static final int WHITE = 0xFFFFFFFF;

	public static BufferedImage readGrf(String grfFilePath) throws IOException
	{
		List<String> allLines = Files.readAllLines(Paths.get(grfFilePath));

		int lineId = 0;
		String line = allLines.get(lineId);
		String[] firstLineData = line.split(",");
		int totalBytes = Integer.valueOf(firstLineData[1]);
		int widthBytes = Integer.valueOf(firstLineData[2]);

		int imageHeight = totalBytes / widthBytes;
		int imageWidth = widthBytes * 8;
		int totalPixels = imageWidth * imageHeight;

		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		line = firstLineData[3];

		int[] imagePixels = new int[totalPixels];
		int pixelArrayIndex = 0;
		byte[] grfBits = new byte[imageWidth];

		while (lineId < allLines.size())
		{
			if (lineId > 0)
			{
				line = allLines.get(lineId);
			}

			// parse line
			parseLine(grfBits, line);

			for (byte b : grfBits)
			{
				imagePixels[pixelArrayIndex++] = b == 0 ? WHITE : BLACK;
			}

			lineId++;
		}

		image.setRGB(0, 0, imageWidth, imageHeight, imagePixels, 0, imageWidth);

		return image;
	}

	private static void parseLine(byte [] bitsArray, String line)
	{
		int readIndex = 0;
		int bitIndex = 0;
		char[] lineCharacters = line.toCharArray();

		while (readIndex < lineCharacters.length)
		{
			String hexString = "" + lineCharacters[readIndex] + lineCharacters[readIndex + 1];
			int valueByte = Integer.parseInt(hexString, 16);

			for (int i = 0; i < 8 && bitIndex < bitsArray.length; i++)
			{
				// read always 8th bit so we will read from the right and then shift to the starting possition so we have 0 / 1
				bitsArray[bitIndex++] = (byte) ((valueByte << i & 0b10000000) >> 7);
			}

			readIndex += 2;
		}
	}
}
