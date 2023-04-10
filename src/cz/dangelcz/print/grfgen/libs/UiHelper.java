package cz.dangelcz.print.grfgen.libs;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import cz.dangelcz.print.grfgen.launch.ApplicationConfig;

public class UiHelper
{

	public static void showInfo(Component parent, String message)
	{
		JOptionPane.showMessageDialog(parent, message.split("\n"), "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showWarning(Component parent, String message)
	{
		JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public static void showError(Component parent, String message)
	{
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static boolean showConfirmDialog(Component parent, String message)
	{
		int result = JOptionPane.showConfirmDialog(parent, "Please, confirm.", message, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		return result == JOptionPane.YES_OPTION;
	}
	
	public static File getSaveFileChooser(Component parent, String fileName)
	{
		JFileChooser jf = new JFileChooser(ApplicationConfig.DEFAULT_FILE_PATH);
		jf.setDialogTitle("Choose file name");
		jf.setSelectedFile(new File(fileName));
		
		if (jf.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
		{
			return null;
		}
		
		File file = jf.getSelectedFile();
		
		if (file.exists() && !showConfirmDialog(jf, "Do you want to replace existing file?"))
		{
			return null;
		}
		
		return file;
	}
}
