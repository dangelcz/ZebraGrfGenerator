package cz.dangelcz.print.grfgen.gui.window;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.Cursor;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import cz.dangelcz.print.grfgen.launch.ApplicationConfig;
import cz.dangelcz.print.grfgen.libs.IoHelper;
import cz.dangelcz.print.grfgen.logic.GrfGenerator;
import cz.dangelcz.print.grfgen.logic.ImageProcessing;
import cz.dangelcz.print.grfgen.logic.JFileChooserImageFileFilter;

import javax.swing.event.ChangeEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;

public class GrfGeneratorWindow
{
	private JFrame grfGeneratorFrame;

	private JTextField filePathInputField;
	private JSpinner blacknessSpinner;
	private JSlider blacknessSlider;
	private JCheckBox useCompressionCheckBox;
	private JButton turnLeftButton;
	private JButton turnRightButton;
	private JButton turn180Button;
	private JButton flipHorizontalyButton;
	private JButton flipVerticallyButton;
	private JButton resetButton;

	private JButton btnSaveGrf;
	private JButton btnSaveZpl;

	private ImagePanel originalImagePanel;
	private ImagePanel transformedImagePanel;

	private GrfGeneratorWindowData windowData;
	private JSpinner newWidthSpinner;
	private JLabel originalSizeLabel;
	private JSpinner newHeightSpinner;
	private JCheckBox aspectRatioCheckBox;
	
	private boolean resolutionsSpinnerLock = false;

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
		grfGeneratorFrame.setTitle("GRF generator");
		grfGeneratorFrame.setBounds(100, 100, 990, 742);
		grfGeneratorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		grfGeneratorFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		
				
				
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(190, 0));
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
						JFileChooser jf = new JFileChooser(ApplicationConfig.DEFAULT_FILE_PATH);
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

							updateElementsFromModelData();
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

						JFileChooser jf = new JFileChooser(ApplicationConfig.DEFAULT_FILE_PATH);
						jf.setDialogTitle("Set grf file name");
						jf.setSelectedFile(new File(outputFileName));

						if (jf.showSaveDialog(grfGeneratorFrame) == JFileChooser.APPROVE_OPTION)
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

						JFileChooser jf = new JFileChooser(ApplicationConfig.DEFAULT_FILE_PATH);
						jf.setDialogTitle("Set grf file name");
						jf.setSelectedFile(new File(outputFileName));

						if (jf.showSaveDialog(grfGeneratorFrame) == JFileChooser.APPROVE_OPTION)
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

		JSeparator separator = new JSeparator();
		sl_controlPanel.putConstraint(SpringLayout.NORTH, lblRotate, 10, SpringLayout.SOUTH, separator);
		sl_controlPanel.putConstraint(SpringLayout.WEST, separator, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, separator, -10, SpringLayout.EAST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, separator, 6, SpringLayout.SOUTH, useCompressionCheckBox);
		controlPanel.add(separator);

		JSeparator separator_1 = new JSeparator();
		sl_controlPanel.putConstraint(SpringLayout.WEST, separator_1, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, separator_1, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(separator_1);

		JSeparator separator_2 = new JSeparator();
		sl_controlPanel.putConstraint(SpringLayout.WEST, separator_2, 16, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, separator_2, -10, SpringLayout.NORTH, btnSaveGrf);
		sl_controlPanel.putConstraint(SpringLayout.EAST, separator_2, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(separator_2);

		resetButton = new JButton("Reset transformation");
		sl_controlPanel.putConstraint(SpringLayout.WEST, resetButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, resetButton, -10, SpringLayout.SOUTH, separator_2);
		sl_controlPanel.putConstraint(SpringLayout.EAST, resetButton, -10, SpringLayout.EAST, controlPanel);
		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				windowData.resetTransformation();
				updateOutputImage();
			}
		});
		resetButton.setEnabled(false);
		controlPanel.add(resetButton);

		JPanel rotateButtonsPanel = new JPanel();
		sl_controlPanel.putConstraint(SpringLayout.NORTH, separator_1, 10, SpringLayout.SOUTH, rotateButtonsPanel);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, rotateButtonsPanel, 6, SpringLayout.SOUTH, lblRotate);
		sl_controlPanel.putConstraint(SpringLayout.WEST, rotateButtonsPanel, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, rotateButtonsPanel, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(rotateButtonsPanel);

		GridBagLayout gbl_rotateButtonsPanel = new GridBagLayout();
		gbl_rotateButtonsPanel.columnWidths = new int[] { 40 };
		gbl_rotateButtonsPanel.rowWeights = new double[] { 0.0, 0.0 };
		rotateButtonsPanel.setLayout(gbl_rotateButtonsPanel);

		turnLeftButton = new JButton("90\u00B0 left");
		GridBagConstraints gbc_turnLeftButton = new GridBagConstraints();
		gbc_turnLeftButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_turnLeftButton.insets = new Insets(0, 0, 5, 5);
		gbc_turnLeftButton.gridx = 0;
		gbc_turnLeftButton.gridy = 0;
		rotateButtonsPanel.add(turnLeftButton, gbc_turnLeftButton);
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

		turnRightButton = new JButton("90\u00B0 right");
		GridBagConstraints gbc_turnRightButton = new GridBagConstraints();
		gbc_turnRightButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_turnRightButton.insets = new Insets(0, 0, 5, 0);
		gbc_turnRightButton.gridx = 1;
		gbc_turnRightButton.gridy = 0;
		rotateButtonsPanel.add(turnRightButton, gbc_turnRightButton);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turnRightButton, 46, SpringLayout.SOUTH, lblRotate);
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

		turn180Button = new JButton("180\u00B0");
		GridBagConstraints gbc_turn180Button = new GridBagConstraints();
		gbc_turn180Button.fill = GridBagConstraints.HORIZONTAL;
		gbc_turn180Button.gridwidth = 2;
		gbc_turn180Button.insets = new Insets(0, 0, 5, 0);
		gbc_turn180Button.gridx = 0;
		gbc_turn180Button.gridy = 1;
		rotateButtonsPanel.add(turn180Button, gbc_turn180Button);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turn180Button, 82, SpringLayout.SOUTH, lblRotate);
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

		flipHorizontalyButton = new JButton("Flip horz.");
		GridBagConstraints gbc_flipHorizontalyButton = new GridBagConstraints();
		gbc_flipHorizontalyButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_flipHorizontalyButton.insets = new Insets(0, 0, 0, 5);
		gbc_flipHorizontalyButton.gridx = 0;
		gbc_flipHorizontalyButton.gridy = 2;
		rotateButtonsPanel.add(flipHorizontalyButton, gbc_flipHorizontalyButton);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, flipHorizontalyButton, 118, SpringLayout.SOUTH, lblRotate);
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

		flipVerticallyButton = new JButton("Flip vert.");
		GridBagConstraints gbc_flipVerticallyButton = new GridBagConstraints();
		gbc_flipVerticallyButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_flipVerticallyButton.gridx = 1;
		gbc_flipVerticallyButton.gridy = 2;
		rotateButtonsPanel.add(flipVerticallyButton, gbc_flipVerticallyButton);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, flipVerticallyButton, 154, SpringLayout.SOUTH, lblRotate);
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

		JLabel lblNewLabel_2 = new JLabel("Resize");
		sl_controlPanel.putConstraint(SpringLayout.NORTH, lblNewLabel_2, 6, SpringLayout.SOUTH, separator_1);
		sl_controlPanel.putConstraint(SpringLayout.WEST, lblNewLabel_2, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, lblNewLabel_2, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(lblNewLabel_2);

		JPanel resizePanel = new JPanel();
		sl_controlPanel.putConstraint(SpringLayout.NORTH, resizePanel, 6, SpringLayout.SOUTH, lblNewLabel_2);
		sl_controlPanel.putConstraint(SpringLayout.WEST, resizePanel, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, resizePanel, 180, SpringLayout.WEST, controlPanel);
		controlPanel.add(resizePanel);
		GridBagLayout gbl_resizePanel = new GridBagLayout();
		gbl_resizePanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_resizePanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_resizePanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_resizePanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		resizePanel.setLayout(gbl_resizePanel);

		originalSizeLabel = new JLabel("Original size");
		GridBagConstraints gbc_originalSizeLabel = new GridBagConstraints();
		gbc_originalSizeLabel.anchor = GridBagConstraints.WEST;
		gbc_originalSizeLabel.gridwidth = 2;
		gbc_originalSizeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_originalSizeLabel.gridx = 0;
		gbc_originalSizeLabel.gridy = 0;
		resizePanel.add(originalSizeLabel, gbc_originalSizeLabel);

		JLabel lblNewLabel_3 = new JLabel("Width");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		resizePanel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		newWidthSpinner = new JSpinner();
		newWidthSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				if (resolutionsSpinnerLock)
				{
					return;
				}

				resolutionsSpinnerLock = true;

				int newWidth = (int) newWidthSpinner.getValue();
				windowData.setNewWidth(newWidth);

				if (windowData.keepAspectRatio())
				{
					int newHeight = (int) (newWidth / windowData.getAspectRatio());
					windowData.setNewHeight(newHeight);
					newHeightSpinner.setValue(newHeight);
				}

				applySizeChanges();

				resolutionsSpinnerLock = false;
			}
		});

		newWidthSpinner.setEnabled(false);
		GridBagConstraints gbc_newWidthSpinner = new GridBagConstraints();
		gbc_newWidthSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_newWidthSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_newWidthSpinner.gridx = 1;
		gbc_newWidthSpinner.gridy = 1;
		resizePanel.add(newWidthSpinner, gbc_newWidthSpinner);

		JLabel lblNewLabel_4 = new JLabel("Height");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 2;
		resizePanel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		newHeightSpinner = new JSpinner();

		newHeightSpinner.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				if (resolutionsSpinnerLock)
				{
					return;
				}

				resolutionsSpinnerLock = true;

				int newHeight = (int) newHeightSpinner.getValue();
				windowData.setNewHeight(newHeight);

				if (windowData.keepAspectRatio())
				{
					int newWidth = (int) (newHeight * windowData.getAspectRatio());
					windowData.setNewWidth(newWidth);
					newWidthSpinner.setValue(newWidth);
				}

				applySizeChanges();
				resolutionsSpinnerLock = false;
			}
		});

		newHeightSpinner.setEnabled(false);
		GridBagConstraints gbc_newHeightSpinner = new GridBagConstraints();
		gbc_newHeightSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_newHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_newHeightSpinner.gridx = 1;
		gbc_newHeightSpinner.gridy = 2;
		resizePanel.add(newHeightSpinner, gbc_newHeightSpinner);

		aspectRatioCheckBox = new JCheckBox("Keep aspect ratio");
		aspectRatioCheckBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				windowData.setKeepAspectRatio(aspectRatioCheckBox.isSelected());
			}
		});

		aspectRatioCheckBox.setEnabled(false);
		GridBagConstraints gbc_aspectRatioCheckBox = new GridBagConstraints();
		gbc_aspectRatioCheckBox.anchor = GridBagConstraints.EAST;
		gbc_aspectRatioCheckBox.gridwidth = 2;
		gbc_aspectRatioCheckBox.gridx = 0;
		gbc_aspectRatioCheckBox.gridy = 3;
		resizePanel.add(aspectRatioCheckBox, gbc_aspectRatioCheckBox);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setMinimumSize(new Dimension(250, 30));
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
		newWidthSpinner.setEnabled(true);
		newHeightSpinner.setEnabled(true);
		aspectRatioCheckBox.setEnabled(true);
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

	public void updateOriginalSizeLabel()
	{
		int width = windowData.getSourceImage().getWidth();
		int height = windowData.getSourceImage().getHeight();
		originalSizeLabel.setText("Original size " + width + " x " + height);
	}

	public void updateElementsFromModelData()
	{
		updateOriginalSizeLabel();
		aspectRatioCheckBox.setSelected(windowData.keepAspectRatio());
		newWidthSpinner.setValue(windowData.getNewWidth());
		newHeightSpinner.setValue(windowData.getNewHeight());
	}

	public void applySizeChanges()
	{
		int newWidth = windowData.getNewWidth();
		int newHeight = windowData.getNewHeight();

		if (windowData.getTransformedImage() != null && newWidth > 0 && newHeight > 0)
		{
			BufferedImage transformedImage = ImageProcessing.resizeImage(windowData.getSourceImage(), newWidth, newHeight);
			windowData.setTransformedImage(transformedImage);

			updateOutputImage();
		}
	}
}
