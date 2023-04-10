package cz.dangelcz.print.grfgen.gui.window.handlers;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.TransferHandler;

public abstract class FileDropHandler extends TransferHandler
{
	@Override
	public boolean canImport(TransferSupport support)
	{
		return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean importData(TransferSupport support)
	{
		if (!canImport(support))
		{
			return false;
		}

		Transferable transferable = support.getTransferable();
		try
		{
			List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
			for (File file : files)
			{
				processInputFile(file);
			}
		} catch (UnsupportedFlavorException | IOException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public abstract void processInputFile(File file);
}