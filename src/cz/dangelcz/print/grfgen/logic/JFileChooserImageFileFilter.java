package cz.dangelcz.print.grfgen.logic;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class JFileChooserImageFileFilter extends FileFilter
{
	private static final String[] FILE_FORMATS = { "jpg", "png", "gif", "bmp", "jpeg" };

	public boolean accept(File f)
	{
		if (f.isDirectory())
		{
			return true;
		}

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

	@Override
	public String getDescription()
	{
		return "*.jpg *.png *.gif";
	}
}
