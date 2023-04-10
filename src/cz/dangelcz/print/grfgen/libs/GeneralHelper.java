package cz.dangelcz.print.grfgen.libs;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralHelper
{
	private static Random random = new Random();
	public static final char STRING_END = '\0';

	// is null length
	public static boolean INL(String s)
	{
		return s == null || s.length() == 0;
	}

	// is not null length
	public static boolean INNL(String s)
	{
		return s != null && s.length() > 0;
	}

	public static boolean INLs(String... strings)
	{
		return Arrays.stream(strings).allMatch(s -> INL(s));
	}

	public static boolean INNLs(String... strings)
	{
		return Arrays.stream(strings).allMatch(s -> INNL(s));
	}

	public static boolean compareStrings(String a, String b)
	{
		if (INL(a))
		{
			return INL(b);
		}

		return a.equals(b);
	}

	public static String getGuid(int length)
	{
		String s = "";
		length--;
		s += (char) (random.nextInt(25) + 65);

		while (length-- > 0)
		{
			if (random.nextBoolean())
				s += random.nextInt(10);
			else
				s += (char) (random.nextInt(25) + 65);
		}

		return s;
	}

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		} catch (InterruptedException e)
		{
		}
	}

	public static void sleepSeconds(long seconds)
	{
		sleep(seconds * 1000);
	}

	public static String substringRegex(String input, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find())
		{
			return matcher.group(0);
		}

		return "";
	}

	public static String getTimeString()
	{
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");

		return ft.format(dNow);
	}

	public static String getDateString()
	{
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy");

		return ft.format(dNow);
	}

	/**
	 * Returns date string in format dd.MM.yyyy HH:mm:ss
	 * 
	 * @return
	 */
	public static String getDateTimeString()
	{
		return getDateString() + " " + getTimeString();
	}

	public static int getRandomInt(int from, int to)
	{
		if(from == to)
		{
			return from;
		}
		
		return random.nextInt(to - from) + from;
	}

	public static void writeString(ByteBuffer bb, String s)
	{
		for (char c : s.toCharArray())
		{
			// osetreni proti preteceni bufferu
			if (bb.remaining() > 6)
			{
				bb.putChar(c);
			}
		}

		bb.putChar(STRING_END);
	}

	public static String readString(ByteBuffer bb)
	{
		StringBuffer sb = new StringBuffer(20);
		char c;
		while ((c = bb.getChar()) != STRING_END)
		{
			sb.append(c);
		}

		return sb.toString();
	}

	public static void writeArray(byte[] array, ByteBuffer bb)
	{
		bb.putInt(array.length);
		bb.put(array);
	}

	public static byte[] readArray(ByteBuffer bb)
	{
		int len = bb.getInt();
		byte[] array = new byte[len];
		bb.get(array);

		return array;
	}

	public static void sleepWithPrint(int seconds)
	{
		System.out.print("Sleep countdown: ");

		while (seconds-- >= 0)
		{
			System.out.print((1 + seconds) + " ");
			sleepSeconds(1);
		}

		System.out.println("done");
	}

	public static String readInput()
	{
		return new String(System.console().readLine());
	}

	public static String readPassword()
	{
		return new String(System.console().readPassword());
	}

	public static boolean isIp(String ip)
	{
		if (INL(ip))
		{
			return false;
		}

		String[] parts = ip.split("\\.");

		if (parts.length != 4)
		{
			return false;
		}

		try
		{
			for (String part : parts)
			{
				int i = Integer.parseInt(part);
				if (i < 0 || i > 255)
				{
					return false;
				}
			}
		} catch (NumberFormatException e)
		{
			return false;
		}

		return true;
	}

	public static <T> T getFirstItemOrNull(List<T> list)
	{
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	public static boolean runningFromJar()
	{
		URI uri = null;
		try
		{
			URL url = GeneralHelper.class.getResource(GeneralHelper.class.getPackage().toString());
			
			if(url == null)
			{
				return false;
			}
			
			uri = url.toURI();
		} catch (URISyntaxException e)
		{
			// really nothing
		}
		return uri != null && uri.getScheme().equals("jar");
	}

	public static String runCommand(String commandLine) throws Exception
	{
		System.out.println(commandLine);

		Process process = Runtime.getRuntime().exec(commandLine);

		BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

		String trecEvalOutput = null;
		StringBuilder output = new StringBuilder("Console output:\n");

		for (String line = null; (line = stdout.readLine()) != null;)
		{
			output.append(line).append("\n");
		}

		trecEvalOutput = output.toString();

		int exitStatus = 0;
		try
		{
			exitStatus = process.waitFor();
		} catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}

		// log.info(exitStatus);

		stdout.close();
		stderr.close();

		return trecEvalOutput;
	}

	public static void openFilePathBySystem(String filePath)
	{
		try
		{
			Desktop.getDesktop().open(new File(filePath));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getPomXmlVersion()
	{
		Package mainPackage = GeneralHelper.class.getPackage();
		String version = mainPackage.getImplementationVersion();
		//String groupId = mainPackage.getName();
		//String artifactId = mainPackage.getImplementationTitle();
		
		return version;
	}
	
}
