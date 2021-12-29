package cz.dangelcz.print.grfgen.logic;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter
{
	private static final String[] FILE_FORMATS = { "jpg", "png", "gif", "jpeg" };

	public boolean accept(File f)
	{
		String fileType = f.getName();
		int i = fileType.lastIndexOf(".");
		if (i > 0)
		{
			fileType = fileType.substring(i + 1);

			for (String format : FILE_FORMATS)
			{
				if (fileType.equalsIgnoreCase(format))
				{
					return true;
				}
			}

		}
		return false;
	}
}
