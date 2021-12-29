package cz.dangelcz.print.grfgen.libs;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

public class IoHelper
{
	public static List<String> getFolderContent(String folderPath)
	{
		return getFolderContent(folderPath, "");
	}

	public static List<String> getFolderContent(String folderPath, String fileType)
	{
		List<String> types = new ArrayList<>();
		if (fileType != null && fileType.length() > 1)
		{
			types.add(fileType);
		}
		return getFolderContent(folderPath, types);
	}

	public static List<String> getFolderContent(String folderPath, List<String> fileTypes)
	{
		List<String> files = new ArrayList<>(10);

		File folder = new File(folderPath);
		if (!folder.exists() || !folder.isDirectory())
		{
			System.err.println("Directory '" + folder.getName() + "' is unreachable.");
			return files;
		}

		if (fileTypes == null || fileTypes.size() == 0)
		{
			return Arrays.asList(folder.list());
		}

		FileFilter filter = new FileFilter()
		{
			public boolean accept(File f)
			{
				if (f.isDirectory())
				{
					return false;
				}

				String typ = getFileType(f.getName());
				if (typ.length() > 0)
				{
					for (String format : fileTypes)
					{
						if (typ.compareTo(format) == 0)
						{
							return true;
						}
					}

				}

				return false;
			}
		};

		for (File f : folder.listFiles(filter))
		{
			files.add(f.getName());
		}

		return files;
	}
	
	public static String getRandomFile(String baseFolderPath, FileFilter fileFilter)
	{
		return getRandomFile(new File(baseFolderPath), fileFilter);
	}

	public static String getRandomFile(File baseFile, FileFilter fileFilter)
	{
		if (!(baseFile.exists()))
		{
			return null;
		}

		File[] fileList = baseFile.listFiles(fileFilter);

		if (fileList == null || fileList.length == 0)
		{
			return null;
		}

		File choosenFile = fileList[GeneralHelper.getRandomInt(0, fileList.length - 1)];

		if (choosenFile.isDirectory())
		{
			return getRandomFile(choosenFile, fileFilter);
		}

		return choosenFile.getAbsolutePath();
	}

	public static void saveDataFile(String path, byte[] content, boolean rewrite)
	{
		File file = new File(path);

		if (file.exists() && !rewrite)
		{
			return;
		}

		try
		{
			file.getParentFile().mkdirs();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(content);
			bos.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static byte[] loadDataFile(String path)
	{
		File file = new File(path);
		byte[] data = new byte[(int) file.length()];

		try
		{
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
			bis.read(data);
			bis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

	public static void saveTextFile(String path, String content, boolean rewrite)
	{
		File file = new File(path);
		saveTextFile(file, content, rewrite);
	}
	
	public static void saveTextFile(File file, String content, boolean rewrite)
	{

		if (file.exists() && !rewrite)
		{
			return;
		}

		try
		{
			File parent = file.getParentFile();
			if(parent != null)
			{
				parent.mkdirs();
			}

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			bw.write(content);
			bw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static String loadTextFile(String path)
	{
		StringBuffer sb = new StringBuffer();

		File file = new File(path);

		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			int tmp;

			while ((tmp = br.read()) != -1)
			{
				sb.append((char) tmp);
			}

			br.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static Map<String, String> loadProperiesFile(String path)
	{
		Map<String, String> data = new HashMap<>();

		File file = new File(path);
		String line, key, value;
		String[] parts;

		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			while ((line = br.readLine()) != null)
			{
				if (line.length() > 0 && !line.startsWith("#"))
				{
					parts = line.split("=");
					key = parts[0].trim();
					value = parts.length == 2 ? parts[1].trim() : "";

					data.put(key, value);
				}
			}

			br.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return data;
	}

	public static String fillTypeIfMissing(String fileName, String type)
	{
		if (!hasType(fileName))
		{
			fileName += "." + type;
		}

		return fileName;
	}

	public static boolean hasType(String fileName)
	{
		return getFileType(fileName).length() > 0;
	}

	public static String getFileType(String fileName)
	{
		String type = "";

		int i = fileName.lastIndexOf(".");
		if (i > 0)
		{
			type = fileName.substring(i + 1).toLowerCase();
		}

		return type;
	}

	public static void copyFile(String pathFrom, String pathTo)
	{
		try
		{
			File from = new File(pathFrom);
			File to = new File(pathTo);
			to.getParentFile().mkdirs();

			BufferedInputStream in = new BufferedInputStream(new FileInputStream(from));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(to));

			int i;
			while ((i = in.read()) != -1)
			{
				out.write(i);
			}

			in.close();
			out.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getFileNameWithoutType(String fileName)
	{
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	public static List<String> getFileNamesWithoutTypes(List<String> fileNameList)
	{
		for (int i = 0; i < fileNameList.size(); i++)
		{
			fileNameList.set(i, getFileNameWithoutType(fileNameList.get(i)));
		}

		return fileNameList;
	}

	public static void makeDirs(String filePath)
	{
		makeDirs(new File(filePath));
	}

	public static void makeDirs(File file)
	{
		if (file.isFile())
		{
			file = file.getParentFile();
		}

		file.mkdirs();
	}

	public static void deleteFile(String filePath)
	{
		File file = new File(filePath);

		if (file.exists() && file.isFile())
		{
			file.delete();
		}
	}

	public static String readFromZip(String archive, String desiredFileName)
	{
		String content = "";
		try
		{
			File f = new File(archive);
			if (!f.exists())
			{
				throw new IOException("File " + f.getName() + "not found");
			}

			ZipInputStream zin = new ZipInputStream(new FileInputStream(f));
			InputStreamReader ir = new InputStreamReader(zin);
			BufferedReader br = new BufferedReader(ir);

			ZipEntry entry;
			while ((entry = zin.getNextEntry()) != null)
			{
				String entryFileName = entry.getName();
				if (entryFileName.compareTo(desiredFileName) == 0)
				{
					String l = "";
					while ((l = br.readLine()) != null)
					{
						content = content + l;
					}
				}
			}
			br.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return content;
	}

	public static void copyFromZip(String archive, String desiredFileName)
	{
		File f;
		try
		{
			f = new File(archive);
			File out = new File(desiredFileName);
			ZipInputStream zis = new ZipInputStream(new FileInputStream(f));
			FileOutputStream fos = new FileOutputStream(out);

			ZipEntry zipEntry;
			while ((zipEntry = zis.getNextEntry()) != null)
			{
				String entryFileName = zipEntry.getName();
				if (entryFileName.compareTo(out.getName()) == 0)
				{
					byte[] buffer = new byte[1024];

					int len;

					while ((len = zis.read(buffer, 0, 1024)) > 0)
					{
						fos.write(buffer, 0, len);
					}
					fos.flush();
					fos.close();
					break;
				}
			}
			zis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void writeZip(String zipFileName, String saveFilePath, String textToSave)
	{
		try
		{
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName, true));
			out.putNextEntry(new ZipEntry(saveFilePath));
			out.write(textToSave.getBytes());
			out.closeEntry();
			out.close();
		} catch (IOException e)
		{
			System.out.println("Problem writing ZIP file: " + e);
		}
	}
	
	public static BufferedImage loadImage(String filePath)
	{
		return loadImage(new File(filePath));
	}

	public static BufferedImage loadImage(File file)
	{
		try
		{
			if (!file.exists())
			{
				return null;
			}

			BufferedImage bi = ImageIO.read(file);

			// int alpha = (colour>>24) & 0xff;
			return bi;
		} catch (IOException e)
		{
			// e.printStackTrace();
			System.err.println("Can't load image: " + file.getPath());
		}

		return null;
	}
}
