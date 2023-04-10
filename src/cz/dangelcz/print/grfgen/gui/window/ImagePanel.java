package cz.dangelcz.print.grfgen.gui.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel
{
	private static final long serialVersionUID = 1493070454704311553L;

	private BufferedImage image;

	public ImagePanel()
	{
		super();
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		drawBackground(g);

		if (image == null)
		{
			return;
		}

		int x = getWidth() / 2 - image.getWidth() / 2;
		int y = getHeight() / 2 - image.getHeight() / 2;

		g.drawImage(image, x, y, null);
	}

	private void drawBackground(Graphics g)
	{
		Color background = this.getRootPane().getBackground();
		
		g.setColor(background);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
