package cz.dangelcz.print.grfgen.logic;

public class GrfGeneratorInputParameters
{
	public static GrfGeneratorInputParameters parseArguments(String[] args)
	{
		GrfGeneratorInputParameters parameters = new GrfGeneratorInputParameters();
		int i = 0;

		try
		{
			for (i = 0; i < args.length; i++)
			{
				String argument = args[i];

				switch (argument)
				{
					case "-c":
						parameters.compress = true;
						break;
					case "-b":
						parameters.blackness = Integer.parseInt(args[i + 1]);
						i++;
						break;
					case "-i":
						parameters.inputFilePath = argument;
						break;
					case "-o":
						parameters.outputFilePath = argument;
						break;

				}
			}
		} catch (Exception e)
		{
			System.err.println("Invalid argument " + args[i]);
			System.exit(1);
		}

		// remove extesion whether it was specified
		parameters.outputFilePath = getBaseName(parameters.outputFilePath);

		return parameters;
	}

	// helper method for removing file name extension
	private static String getBaseName(String fileName)
	{
		int index = fileName.lastIndexOf('.');
		if (index == -1)
		{
			return fileName;
		} else
		{
			return fileName.substring(0, index);
		}
	}

	private String inputFilePath = "";
	private String outputFilePath = "";
	private boolean compress = false;
	private int blackness = 50;

	public String getInputFilePath()
	{
		return inputFilePath;
	}

	public String getOutputFilePath()
	{
		return outputFilePath;
	}

	public boolean isCompress()
	{
		return compress;
	}

	public int getBlackness()
	{
		return blackness;
	}

}
