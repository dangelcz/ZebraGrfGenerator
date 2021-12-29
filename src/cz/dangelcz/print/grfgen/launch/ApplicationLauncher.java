
package cz.dangelcz.print.grfgen.launch;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.*;

import cz.dangelcz.print.grfgen.gui.window.GrfGeneratorWindow;
import cz.dangelcz.print.grfgen.gui.window.GrfGeneratorWindowData;

public class ApplicationLauncher
{
	public static void main(String[] args)
	{
		FlatLightLaf.setup();
		
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				GrfGeneratorWindow aw = new GrfGeneratorWindow();
				
				GrfGeneratorWindowData data = new GrfGeneratorWindowData();
				data.setBlackness(ApplicationConfig.DEFAULT_BLACKNESS);
				aw.setModelInstance(data);
				
				aw.setVisible(true);
			}
		});
	}
}
