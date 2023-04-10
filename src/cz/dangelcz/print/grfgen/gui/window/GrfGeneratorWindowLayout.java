package cz.dangelcz.print.grfgen.gui.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class GrfGeneratorWindowLayout
{
	protected GrfGeneratorWindow window;
	
	public GrfGeneratorWindowLayout(GrfGeneratorWindow window)
	{
		this.window = window;
	}

	public void layoutComponents()
	{
		JPanel controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(190, 0));
		SpringLayout sl_controlPanel = new SpringLayout();
		controlPanel.setLayout(sl_controlPanel);

		JLabel lblInputFile = new JLabel("Input file");
		sl_controlPanel.putConstraint(SpringLayout.NORTH, lblInputFile, 10, SpringLayout.NORTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.WEST, lblInputFile, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, lblInputFile, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(lblInputFile);

		JTextField filePathInputField = window.filePathInputField;
		filePathInputField.setToolTipText("Ctrl+O");
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

		JSpinner blacknessSpinner = window.blacknessSpinner;
		sl_controlPanel.putConstraint(SpringLayout.NORTH, blacknessSpinner, 6, SpringLayout.SOUTH, lblBlackness);
		blacknessSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		blacknessSpinner.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.WEST, blacknessSpinner, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, blacknessSpinner, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(blacknessSpinner);

		JSlider blacknessSlider = window.blacknessSlider;
		blacknessSlider.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, blacknessSlider, 12, SpringLayout.SOUTH, blacknessSpinner);
		sl_controlPanel.putConstraint(SpringLayout.WEST, blacknessSlider, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, blacknessSlider, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(blacknessSlider);

		JCheckBox useCompressionCheckBox = window.useCompressionCheckBox; 
		useCompressionCheckBox.setText("Use ZPL compression");
		useCompressionCheckBox.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, useCompressionCheckBox, 6, SpringLayout.SOUTH, blacknessSlider);
		sl_controlPanel.putConstraint(SpringLayout.WEST, useCompressionCheckBox, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, useCompressionCheckBox, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(useCompressionCheckBox);

		JButton btnSaveGrf = window.btnSaveGrf;
		btnSaveGrf.setText("Save GRF");
		btnSaveGrf.setToolTipText("Ctrl+S");
		btnSaveGrf.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnSaveGrf, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnSaveGrf, -10, SpringLayout.EAST, controlPanel);
		controlPanel.add(btnSaveGrf);

		JButton btnSaveZpl = window.btnSaveZpl;
		btnSaveZpl.setText("Save ZPL");
		btnSaveZpl.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnSaveGrf, -10, SpringLayout.NORTH, btnSaveZpl);
		sl_controlPanel.putConstraint(SpringLayout.WEST, btnSaveZpl, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, btnSaveZpl, -10, SpringLayout.SOUTH, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.EAST, btnSaveZpl, -10, SpringLayout.EAST, controlPanel);

		controlPanel.add(btnSaveZpl);

		window.grfGeneratorFrame.getContentPane().add(controlPanel, BorderLayout.WEST);

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

		JButton resetButton = window.resetButton;
        resetButton.setText("Reset transformation");
		sl_controlPanel.putConstraint(SpringLayout.WEST, resetButton, 10, SpringLayout.WEST, controlPanel);
		sl_controlPanel.putConstraint(SpringLayout.SOUTH, resetButton, -10, SpringLayout.SOUTH, separator_2);
		sl_controlPanel.putConstraint(SpringLayout.EAST, resetButton, -10, SpringLayout.EAST, controlPanel);

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

		JButton turnLeftButton = window.turnLeftButton;
        turnLeftButton.setText("90\u00B0 left");
		GridBagConstraints gbc_turnLeftButton = new GridBagConstraints();
		gbc_turnLeftButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_turnLeftButton.insets = new Insets(0, 0, 5, 5);
		gbc_turnLeftButton.gridx = 0;
		gbc_turnLeftButton.gridy = 0;
		rotateButtonsPanel.add(turnLeftButton, gbc_turnLeftButton);
		turnLeftButton.setEnabled(false);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turnLeftButton, 10, SpringLayout.SOUTH, lblRotate);

		JButton turnRightButton = window.turnRightButton;
        turnRightButton.setText("90\u00B0 right");
		GridBagConstraints gbc_turnRightButton = new GridBagConstraints();
		gbc_turnRightButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_turnRightButton.insets = new Insets(0, 0, 5, 0);
		gbc_turnRightButton.gridx = 1;
		gbc_turnRightButton.gridy = 0;
		rotateButtonsPanel.add(turnRightButton, gbc_turnRightButton);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turnRightButton, 46, SpringLayout.SOUTH, lblRotate);

		turnRightButton.setEnabled(false);

		JButton turn180Button = window.turn180Button;
        turn180Button.setText("180\u00B0");
		GridBagConstraints gbc_turn180Button = new GridBagConstraints();
		gbc_turn180Button.fill = GridBagConstraints.HORIZONTAL;
		gbc_turn180Button.gridwidth = 2;
		gbc_turn180Button.insets = new Insets(0, 0, 5, 0);
		gbc_turn180Button.gridx = 0;
		gbc_turn180Button.gridy = 1;
		rotateButtonsPanel.add(turn180Button, gbc_turn180Button);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, turn180Button, 82, SpringLayout.SOUTH, lblRotate);

		turn180Button.setEnabled(false);

		JButton flipHorizontalyButton = window.flipHorizontalyButton;
        flipHorizontalyButton.setText("Flip horz.");
		GridBagConstraints gbc_flipHorizontalyButton = new GridBagConstraints();
		gbc_flipHorizontalyButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_flipHorizontalyButton.insets = new Insets(0, 0, 0, 5);
		gbc_flipHorizontalyButton.gridx = 0;
		gbc_flipHorizontalyButton.gridy = 2;
		rotateButtonsPanel.add(flipHorizontalyButton, gbc_flipHorizontalyButton);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, flipHorizontalyButton, 118, SpringLayout.SOUTH, lblRotate);

		flipHorizontalyButton.setEnabled(false);

		JButton flipVerticallyButton = window.flipVerticallyButton;
        flipVerticallyButton.setText("Flip vert.");
		GridBagConstraints gbc_flipVerticallyButton = new GridBagConstraints();
		gbc_flipVerticallyButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_flipVerticallyButton.gridx = 1;
		gbc_flipVerticallyButton.gridy = 2;
		rotateButtonsPanel.add(flipVerticallyButton, gbc_flipVerticallyButton);
		sl_controlPanel.putConstraint(SpringLayout.NORTH, flipVerticallyButton, 154, SpringLayout.SOUTH, lblRotate);

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

		JLabel originalSizeLabel = window.originalSizeLabel;
        originalSizeLabel.setText("Original size");
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

		JSpinner newWidthSpinner = window.newWidthSpinner;

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

		JSpinner newHeightSpinner = window.newHeightSpinner;
		newHeightSpinner.setEnabled(false);
		GridBagConstraints gbc_newHeightSpinner = new GridBagConstraints();
		gbc_newHeightSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_newHeightSpinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_newHeightSpinner.gridx = 1;
		gbc_newHeightSpinner.gridy = 2;
		resizePanel.add(newHeightSpinner, gbc_newHeightSpinner);

		JCheckBox aspectRatioCheckBox = window.aspectRatioCheckBox;
        aspectRatioCheckBox.setText("Keep aspect ratio");
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
		window.grfGeneratorFrame.getContentPane().add(splitPane, BorderLayout.CENTER);

		ImagePanel originalImagePanel = window.originalImagePanel;
		originalImagePanel.setBackground(Color.WHITE);
		splitPane.setLeftComponent(originalImagePanel);
		originalImagePanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Original image");
		lblNewLabel.setBounds(12, 12, 109, 16);
		originalImagePanel.add(lblNewLabel);

		ImagePanel outputImagePanel = window.outputImagePanel;
		outputImagePanel.setBackground(Color.WHITE);
		splitPane.setRightComponent(outputImagePanel);
		outputImagePanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Output image");
		lblNewLabel_1.setBounds(12, 12, 99, 16);
		outputImagePanel.add(lblNewLabel_1);

		splitPane.setResizeWeight(.5d);
	}
}
