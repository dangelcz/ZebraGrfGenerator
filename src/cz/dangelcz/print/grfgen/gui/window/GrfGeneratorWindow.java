package cz.dangelcz.print.grfgen.gui.window;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Cursor;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Frame;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import cz.dangelcz.print.grfgen.libs.IoHelper;
import cz.dangelcz.print.grfgen.logic.GrfGenerator;
import cz.dangelcz.print.grfgen.logic.ImageProcessing;
import cz.dangelcz.print.grfgen.logic.JFileChooserImageFileFilter;

import javax.swing.event.ChangeEvent;

public class GrfGeneratorWindow
{
	private JFrame grfGeneratorFrame;
	private JTextField filePathInputField;
	private JSpinner blacknessSpinner;
	private JSlider blacknessSlider;
	private JCheckBox useCompressionCheckBox;
	private JButton turnLeftButton;
	private JButton turnRightButton;
	private JButton flipHorizontalyButton;
	private JButton flipVerticallyButton;
	private JButton btnSaveGrf;
	private JButton btnSaveZpl;
	private ImagePanel originalImagePanel;
	private ImagePanel transformedImagePanel;
	private JButton turn180Button;

	private GrfGeneratorWindowData windowData;
	private JButton resetButton;

	public GrfGeneratorWindow()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		grfGeneratorFrame = new JFrame();
		grfGeneratorFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		grfGeneratorFrame.setTitle("GRF generator");
		grfGeneratorFrame.setBounds(100, 100, 774, 616);
		grfGeneratorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		grfGeneratorFrame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(180, 0));
		SpringLayout sl_controlPanel = new SpringLayout();
		controlPanel.setLayout(sl_controlPanel);

		JLabel lblInputFile = new JLabel("Input file");
		sl_controlPanel.putConstraint(SpringLayout.NORTH, lblInputFile, 10, SpringLayout.NORTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.WEST, lblInputFile, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, lblInputFile, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(lblInputFile);

		filePathInputField = new JTextField();
		filePathInputField.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						JFileChooser jf = new JFileChooser("./data");
						jf.setDialogTitle("Select image");
						jf.setFileFilter(new JFileChooserImageFileFilter());

						if (jf.showOpenDialog(grfGeneratorFrame) == JFileChooser.APPROVE_OPTION)
						{
							File file = jf.getSelectedFile();
							filePathInputField.setText(file.getName());
							filePathInputField.setToolTipText(file.getAbsolutePath());

							windowData.loadSourceImage(file.getAbsolutePath());
							updateOutputImage();
							enableComponents();
						}
					}
				});
			}
		});

		filePathInputField.setHorizontalAlignment(SwingConstants.CENTER);
		filePathInputField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		filePathInputField.setText("<click to enter the file>");
		filePathInputField.setEditable(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, filePathInputField, 6, SpringLayout.SOUTH, lblInputFile);
		sl_controlPanel.putConstraint(SpringLayout.WEST, filePathInputField, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, filePathInputField, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(filePathInputField);
		filePathInputField.setColumns(10);

		JLabel lblBlackness = new JLabel("Color threshold");
		sl_controlPanel.putConstraint(SpringLayout.NORTH, lblBlackness, 6, SpringLayout.SOUTH, filePathInputField);
		sl_controlPanel.putConstraint(SpringLayout.WEST, lblBlackness, 0, SpringLayout.WEST, lblInputFile);
		sl_controlPanel.putConstraint(SpringLayout.EAST, lblBlackness, 0, SpringLayout.EAST, lblInputFile);
		controlPanel.add(lblBlackness);

		blacknessSpinner = new JSpinner();
		sl_controlPanel.putConstraint(SpringLayout.NORTH, blacknessSpinner, 6, SpringLayout.SOUTH, lblBlackness);
		blacknessSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				setBlackness((int) blacknessSpinner.getValue());
			}
		});
		blacknessSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		blacknessSpinner.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.WEST, blacknessSpinner, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, blacknessSpinner, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(blacknessSpinner);

		blacknessSlider = new JSlider();
		blacknessSlider.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				setBlackness((int) blacknessSlider.getValue());
			}
		});
		blacknessSlider.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, blacknessSlider, 12, SpringLayout.SOUTH, blacknessSpinner);
		sl_controlPanel.putConstraint(SpringLayout.WEST, blacknessSlider, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, blacknessSlider, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(blacknessSlider);

		useCompressionCheckBox = new JCheckBox("Use ZPL compression");
		useCompressionCheckBox.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, useCompressionCheckBox, 6, SpringLayout.SOUTH, blacknessSlider);
		sl_controlPanel.putConstraint(SpringLayout.WEST, useCompressionCheckBox, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, useCompressionCheckBox, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(useCompressionCheckBox);

		btnSaveGrf = new JButton("Save GRF");
		btnSaveGrf.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnSaveGrf, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnSaveGrf, -10, SpringLayout.EAST, controlPanel);
		btnSaveGrf.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						String outputFileName = windowData.getInputFilePath();
						outputFileName = IoHelper.getFileNameWithoutType(outputFileName) + ".grf";

						JFileChooser jf = new JFileChooser("./data");
						jf.setDialogTitle("Set grf file name");
						jf.setSelectedFile(new File(outputFileName));

						if (jf.showOpenDialog(grfGeneratorFrame) == JFileChooser.APPROVE_OPTION)
						{
							File file = jf.getSelectedFile();

							GrfGenerator grf = new GrfGenerator();
							grf.setCompressHex(windowData.isCompress());
							grf.setBlacknessLimitPercentage(windowData.getBlackness());
							grf.loadImage(windowData.getOutputImage());

							String grfData = grf.getGrf(file.getName());
							IoHelper.saveTextFile(file, grfData, true);
						}
					}
				});
			}
		});

		controlPanel.add(btnSaveGrf);

		btnSaveZpl = new JButton("Save ZPL");
		btnSaveZpl.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnSaveGrf, -10, SpringLayout.NORTH, btnSaveZpl);
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnSaveZpl, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnSaveZpl, -10, SpringLayout.SOUTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnSaveZpl, -10, SpringLayout.EAST, controlPanel);
		btnSaveZpl.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				SwingUtilities.invokeLater(new Runnable()
				{
					public void run()
					{
						String outputFileName = windowData.getInputFilePath();
						outputFileName = IoHelper.getFileNameWithoutType(outputFileName) + ".zpl";

						JFileChooser jf = new JFileChooser("./data");
						jf.setDialogTitle("Set grf file name");
						jf.setSelectedFile(new File(outputFileName));

						if (jf.showOpenDialog(grfGeneratorFrame) == JFileChooser.APPROVE_OPTION)
						{
							File file = jf.getSelectedFile();

							GrfGenerator grf = new GrfGenerator();
							grf.setCompressHex(windowData.isCompress());
							grf.setBlacknessLimitPercentage(windowData.getBlackness());
							grf.loadImage(windowData.getOutputImage());

							String grfData = grf.getZpl();
							IoHelper.saveTextFile(file, grfData, true);
						}
					}
				});
			}
		});
		controlPanel.add(btnSaveZpl);

		grfGeneratorFrame.getContentPane().add(controlPanel, BorderLayout.WEST);

		JLabel lblRotate = new JLabel("Rotate");
		sl_controlPanel.putConstraint(SpringLayout.WEST, lblRotate, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, lblRotate, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(lblRotate);

		turnLeftButton = new JButton("90\u00B0 left");
		turnLeftButton.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turnLeftButton, 10, SpringLayout.SOUTH, lblRotate);
		turnLeftButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				BufferedImage transformed = ImageProcessing.rotate90Left(windowData.getTransformedImage());
				windowData.setTransformedImage(transformed);
				updateOutputImage();
			}
		});
		sl_controlPanel.putConstraint(SpringLayout.WEST, turnLeftButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, turnLeftButton, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(turnLeftButton);

		turnRightButton = new JButton("90\u00B0 right");
		turnRightButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				BufferedImage transformed = ImageProcessing.rotate90Right(windowData.getTransformedImage());
				windowData.setTransformedImage(transformed);
				updateOutputImage();
			}
		});
		turnRightButton.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turnRightButton, 10, SpringLayout.SOUTH, turnLeftButton);
		sl_controlPanel.putConstraint(SpringLayout.WEST, turnRightButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, turnRightButton, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(turnRightButton);

		turn180Button = new JButton("180\u00B0");
		turn180Button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				BufferedImage transformed = ImageProcessing.rotate180(windowData.getTransformedImage());
				windowData.setTransformedImage(transformed);
				updateOutputImage();
			}
		});
		turn180Button.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turn180Button, 10, SpringLayout.SOUTH, turnRightButton);
		sl_controlPanel.putConstraint(SpringLayout.WEST, turn180Button, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, turn180Button, -10, SpringLayout.EAST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.WEST, turnRightButton, 0, SpringLayout.WEST, turn180Button);
		controlPanel.add(turn180Button);

		flipHorizontalyButton = new JButton("Flip horizontaly");
		flipHorizontalyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				BufferedImage transformed = ImageProcessing.flipHorizontaly(windowData.getTransformedImage());
				windowData.setTransformedImage(transformed);
				updateOutputImage();
			}
		});
		flipHorizontalyButton.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, flipHorizontalyButton, 10, SpringLayout.SOUTH, turn180Button);
		sl_controlPanel.putConstraint(SpringLayout.WEST, flipHorizontalyButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, flipHorizontalyButton, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(flipHorizontalyButton);

		flipVerticallyButton = new JButton("Flip verticaly");
		flipVerticallyButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				BufferedImage transformed = ImageProcessing.flipVerticaly(windowData.getTransformedImage());
				windowData.setTransformedImage(transformed);
				updateOutputImage();
			}
		});
		flipVerticallyButton.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, flipVerticallyButton, 10, SpringLayout.SOUTH, flipHorizontalyButton);
		sl_controlPanel.putConstraint(SpringLayout.WEST, flipVerticallyButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, flipVerticallyButton, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(flipVerticallyButton);

		JSeparator separator = new JSeparator();
		sl_controlPanel.putConstraint(SpringLayout.NORTH, lblRotate, 10, SpringLayout.SOUTH, separator);
		sl_controlPanel.putConstraint(SpringLayout.WEST, separator, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, separator, -10, SpringLayout.EAST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, separator, 6, SpringLayout.SOUTH, useCompressionCheckBox);
		controlPanel.add(separator);

		JSeparator separator_1 = new JSeparator();
		sl_controlPanel.putConstraint(SpringLayout.WEST, separator_1, 16, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, separator_1, -10, SpringLayout.NORTH, btnSaveGrf);
		sl_controlPanel.putConstraint(SpringLayout.EAST, separator_1, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(separator_1);

		resetButton = new JButton("Reset");
		sl_controlPanel.putConstraint(SpringLayout.WEST, resetButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, resetButton, -10, SpringLayout.EAST, controlPanel);
		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				windowData.resetTransformation();
				updateOutputImage();
			}
		});
		sl_controlPanel.putConstraint(SpringLayout.NORTH, resetButton, 6, SpringLayout.SOUTH, flipVerticallyButton);
		resetButton.setEnabled(false);
		controlPanel.add(resetButton);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(5);
		grfGeneratorFrame.getContentPane().add(splitPane, BorderLayout.CENTER);

		originalImagePanel = new ImagePanel();
		originalImagePanel.setBackground(Color.WHITE);
		splitPane.setLeftComponent(originalImagePanel);
		originalImagePanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Original image");
		lblNewLabel.setBounds(12, 12, 109, 16);
		originalImagePanel.add(lblNewLabel);

		transformedImagePanel = new ImagePanel();
		transformedImagePanel.setBackground(Color.WHITE);
		splitPane.setRightComponent(transformedImagePanel);
		transformedImagePanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Output image");
		lblNewLabel_1.setBounds(12, 12, 99, 16);
		transformedImagePanel.add(lblNewLabel_1);

		splitPane.setResizeWeight(.5d);
	}

	public void enableComponents()
	{
		blacknessSpinner.setEnabled(true);
		blacknessSlider.setEnabled(true);
		useCompressionCheckBox.setEnabled(true);
		turnLeftButton.setEnabled(true);
		turnRightButton.setEnabled(true);
		turn180Button.setEnabled(true);
		flipHorizontalyButton.setEnabled(true);
		flipVerticallyButton.setEnabled(true);
		btnSaveGrf.setEnabled(true);
		btnSaveZpl.setEnabled(true);
		resetButton.setEnabled(true);
	}

	public void setBlackness(int value)
	{
		blacknessSlider.setValue(value);
		blacknessSpinner.setValue(value);
		windowData.setBlackness(value);

		if (windowData.getTransformedImage() != null)
		{
			updateOutputImage();
		}
	}

	public void setVisible(boolean b)
	{
		grfGeneratorFrame.setVisible(b);
	}

	public void updateOutputImage()
	{
		BufferedImage outputImage = ImageProcessing.convertToBlackMask(windowData.getTransformedImage(), windowData.getBlackness());
		windowData.setOutputImage(outputImage);

		originalImagePanel.setImage(windowData.getSourceImage());
		transformedImagePanel.setImage(windowData.getOutputImage());

		repaintImages();
	}

	private void repaintImages()
	{
		originalImagePanel.repaint();
		transformedImagePanel.repaint();
	}

	public void setModelInstance(GrfGeneratorWindowData data)
	{
		this.windowData = data;

		blacknessSlider.setValue(this.windowData.getBlackness());
		blacknessSpinner.setValue(this.windowData.getBlackness());
	}
}
