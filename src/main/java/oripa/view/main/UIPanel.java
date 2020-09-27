/**
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2005-2009 Jun Mitani http://mitani.cs.tsukuba.ac.jp/

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package oripa.view.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oripa.appstate.InputCommandStatePopper;
import oripa.appstate.StateManager;
import oripa.bind.ButtonFactory;
import oripa.bind.PaintActionButtonFactory;
import oripa.bind.binder.BinderInterface;
import oripa.bind.binder.ViewChangeBinder;
import oripa.bind.state.PaintBoundStateFactory;
import oripa.bind.state.action.PaintActionSetterFactory;
import oripa.domain.cptool.TypeForChange;
import oripa.domain.creasepattern.CreasePatternInterface;
import oripa.domain.cutmodel.CutModelOutlinesHolder;
import oripa.domain.fold.BoundBox;
import oripa.domain.fold.FoldedModelInfo;
import oripa.domain.fold.Folder;
import oripa.domain.fold.OrigamiModel;
import oripa.domain.fold.OrigamiModelFactory;
import oripa.domain.paint.MouseActionHolder;
import oripa.domain.paint.PaintContextInterface;
import oripa.domain.paint.byvalue.AngleMeasuringAction;
import oripa.domain.paint.byvalue.AngleValueInputListener;
import oripa.domain.paint.byvalue.LengthMeasuringAction;
import oripa.domain.paint.byvalue.LengthValueInputListener;
import oripa.domain.paint.byvalue.ValueSetting;
import oripa.file.ImageResourceLoader;
import oripa.resource.ResourceHolder;
import oripa.resource.ResourceKey;
import oripa.resource.StringID;
import oripa.util.gui.ChildFrameManager;
import oripa.value.OriLine;
import oripa.view.estimation.EstimationResultFrameFactory;
import oripa.view.foldability.FoldabilityCheckFrameFactory;
import oripa.view.model.ModelViewFrameFactory;
import oripa.viewsetting.ChangeViewSetting;
import oripa.viewsetting.ViewScreenUpdater;
import oripa.viewsetting.main.MainFrameSetting;
import oripa.viewsetting.main.MainScreenSetting;
import oripa.viewsetting.main.uipanel.ChangeOnPaintInputButtonSelected;
import oripa.viewsetting.main.uipanel.FromLineTypeItemListener;
import oripa.viewsetting.main.uipanel.ToLineTypeItemListener;
import oripa.viewsetting.main.uipanel.UIPanelSetting;

public class UIPanel extends JPanel {

	private static final Logger logger = LoggerFactory.getLogger(UIPanel.class);

	private final UIPanelSetting setting = new UIPanelSetting();
	private final ValueSetting valueSetting = setting.getValueSetting();

	private ChildFrameManager childFrameManager;

	private final ResourceHolder resources = ResourceHolder.getInstance();

	private final ViewScreenUpdater screenUpdater;

	private final PaintContextInterface paintContext;

	private boolean fullEstimation = true;

//	 * mainPanel
//	 * . input line button
//	 * * lineTypePanel
//	 * 	 . mountain button
//	 *   . valley button
//	 *   . aux button
//	 * -------------
//	 * . select line button
//	 * . delete line button
//	 * . change line type
//	 * * alterLineTypePanel
//	 *   alter_line_combo_from/to
//	 * -------------
//	 * . add vertex button
//	 * . delete vertex button
//	 * . edit bgimage button
//	 * * editBGImagePanel
//	 *   checkbox display image
//	 *   button selectImage
//	 *   offsetX input
//	 *   offsetY input
//	 *   scaleX input
//	 *   scaleY input
//	 *   tilt input
//	 *   rotate45deg button
//	 * --------------
//	 * 	 ... all the commands with images
//	 * ---------
//	 * * byValueLengthPanel
//	 * 	 textFieldLength
//	 *   buttonLength
//	 * * byValueAnglePanel
//	 *   textFieldAngle
//	 *   buttonAngle
//	 * * gridPanel
//	 *   show grid checkbox
//	 *   and so on
//	 * o MV checkbox
//	 * o aux checkbox
//	 * o show vertices checkbox
//	 * checkWindowButton
//	 * FoldWindowButton
//	 * o fullEstimationCheckbox
//	 *
	// ---------------------------------------------------------------------------------------------------------------------------
	// Panels to be used
	private final JPanel mainPanel = new JPanel();

	// ---------------------------------------------------------------------------------------------------------------------------
	// Binding edit mode
	private ButtonGroup editModeGroup;

	private JRadioButton editModeInputLineButton;
	private JRadioButton editModePickLineButton;
	private JRadioButton editModeDeleteLineButton;
	private JRadioButton editModeLineTypeButton;
	private JRadioButton editModeAddVertex;
	private JRadioButton editModeDeleteVertex;
	private JRadioButton editModeBGImage;

	// ---------------------------------------------------------------------------------------------------------------------------
	// Panel Components

	// Insert Line Tools Panel
	private final JPanel lineInputPanel = new JPanel();

	private JRadioButton lineInputDirectVButton;
	private JRadioButton lineInputOnVButton;
	private JRadioButton lineInputVerticalLineButton;
	private JRadioButton lineInputAngleBisectorButton;
	private JRadioButton lineInputTriangleSplitButton;
	private JRadioButton lineInputSymmetricButton;
	private JRadioButton lineInputMirrorButton;
	private JRadioButton lineInputByValueButton;
	private JRadioButton lineInputPBisectorButton;

	// lineTypePanel
	private final JPanel lineTypePanel = new JPanel();

	private final JRadioButton lineTypeAuxButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.AUX_ID));
	private final JRadioButton lineTypeMountainButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.MOUNTAIN_ID));
	private final JRadioButton lineTypeValleyButton = new JRadioButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.VALLEY_ID));

	// ActionButtons Panel
	private final JPanel buttonsPanel = new JPanel();
	private final JButton buildButton = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.FOLD_ID));
	private final JButton buttonCheckWindow = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.CHECK_WINDOW_ID));

	private final JCheckBox dispMVLinesCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_MV_ID),
			true);
	private final JCheckBox dispAuxLinesCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_AUX_ID),
			true);
	private final JCheckBox dispVertexCheckBox = new JCheckBox(
			resources
					.getString(ResourceKey.LABEL, StringID.UI.SHOW_VERTICES_ID),
			false);
	private final JCheckBox doFullEstimationCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL,
					StringID.UI.FULL_ESTIMATION_ID),
			false);

	// contains all the different Panels changing dependant on what edit mode
	// selected
	private final JPanel editModeSettingsPanel = new JPanel();
	private final JPanel generalSettingsPanel = new JPanel();

	// editBGImagePanel
	private final JPanel editBGImagePanel = new JPanel();
	private final JCheckBox showBGImage = new JCheckBox("Disply BG Image", true);
	private JFormattedTextField textFieldBGposX;
	private JFormattedTextField textFieldBGposY;
	private JFormattedTextField textFieldBGscaleX;
	private JFormattedTextField textFieldBGscaleY;
	private JFormattedTextField textFieldBGrotation;
	private final JButton setBGsettings = new JButton("set image");

	// byValuePanel for length and angle
	private final JPanel byValuePanel = new JPanel();
	private JFormattedTextField textFieldLength;
	private final JButton buttonLength = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.MEASURE_ID));
	private JFormattedTextField textFieldAngle;
	private final JButton buttonAngle = new JButton(
			resources.getString(ResourceKey.LABEL, StringID.UI.MEASURE_ID));

	// gridPanel
	private final JPanel gridPanel = new JPanel();
	private final JCheckBox dispGridCheckBox = new JCheckBox(
			resources.getString(ResourceKey.LABEL, StringID.UI.SHOW_GRID_ID),
			true);
	private JFormattedTextField textFieldGrid;
	private final JButton gridSmallButton = new JButton("x2");
	private final JButton gridLargeButton = new JButton("x1/2");
	private final JButton gridChangeButton = new JButton(
			resources.getString(ResourceKey.LABEL,
					StringID.UI.GRID_SIZE_CHANGE_ID));

	// AlterLineTypePanel
	private final JPanel alterLineTypePanel = new JPanel();
	private final TypeForChange[] alterLine_comboData_from = {
			TypeForChange.EMPTY, TypeForChange.RIDGE, TypeForChange.VALLEY };
	private final TypeForChange[] alterLine_comboData_to = {
			TypeForChange.RIDGE, TypeForChange.VALLEY, TypeForChange.AUX,
			TypeForChange.CUT, TypeForChange.DELETE, TypeForChange.FLIP };

	private final JComboBox<TypeForChange> alterLine_combo_from = new JComboBox<>(
			alterLine_comboData_from);
	private final JComboBox<TypeForChange> alterLine_combo_to = new JComboBox<>(
			alterLine_comboData_to);

	public UIPanel(
			final StateManager stateManager,
			final ViewScreenUpdater screenUpdater,
			final MouseActionHolder actionHolder,
			final PaintContextInterface aContext,
			final CutModelOutlinesHolder cutOutlinesHolder,
			final MainFrameSetting mainFrameSetting,
			final MainScreenSetting mainScreenSetting) {

		this.screenUpdater = screenUpdater;

		paintContext = aContext;

		constructButtons(stateManager, actionHolder, mainFrameSetting, mainScreenSetting);

		// setPreferredSize(new Dimension(210, 400));

		// create all the Components
		createEditActionPanel();
		createLineInputPanel();
		createAlterLineTypePanel();
		createEditBGImagePanel();
		createEditByValuePanel();
		createGridPanel();
		createButtonsPanel();

		// editMode settings panel
		editModeSettingsPanel.setLayout(new BoxLayout(editModeSettingsPanel, BoxLayout.PAGE_AXIS));
		TitledBorder settingsBorder = new TitledBorder("Tool Settings");
		settingsBorder.setBorder(new LineBorder(getBackground().darker().darker()));
		editModeSettingsPanel.setBorder(settingsBorder);

		editModeSettingsPanel.add(lineInputPanel);
		editModeSettingsPanel.add(alterLineTypePanel);
		editModeSettingsPanel.add(byValuePanel);
		editModeSettingsPanel.add(editBGImagePanel);

		// general settings panel
		generalSettingsPanel.setLayout(new BoxLayout(generalSettingsPanel, BoxLayout.PAGE_AXIS));
		TitledBorder generalSettingsBorder = new TitledBorder("General Settings");
		generalSettingsBorder.setBorder(new LineBorder(getBackground().darker().darker()));
		generalSettingsPanel.setBorder(generalSettingsBorder);

		generalSettingsPanel.add(gridPanel);
		generalSettingsPanel.add(buttonsPanel);

		setLayout(new GridBagLayout());
		GridBagConstraints c = createGridBagConstraints(0, 0, 1);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.NONE;
		c.weighty = 0;
		add(mainPanel, c);

		c.gridy++;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(editModeSettingsPanel, c);

		c.gridy++;
		c.weighty = 0;
		add(generalSettingsPanel, c);

		// Shortcut
		// How to enter the line
		lineInputDirectVButton.setMnemonic(KeyEvent.VK_1);
		lineInputOnVButton.setMnemonic(KeyEvent.VK_2);
		lineInputPBisectorButton.setMnemonic(KeyEvent.VK_3);
		lineInputAngleBisectorButton.setMnemonic(KeyEvent.VK_4);
		lineInputTriangleSplitButton.setMnemonic(KeyEvent.VK_5);
		lineInputVerticalLineButton.setMnemonic(KeyEvent.VK_6);
		lineInputSymmetricButton.setMnemonic(KeyEvent.VK_7);
		lineInputMirrorButton.setMnemonic(KeyEvent.VK_8);
		lineInputByValueButton.setMnemonic(KeyEvent.VK_9);

		editModeInputLineButton.setMnemonic(KeyEvent.VK_I);
		editModePickLineButton.setMnemonic(KeyEvent.VK_S);
		editModeDeleteLineButton.setMnemonic(KeyEvent.VK_D);
		editModeLineTypeButton.setMnemonic(KeyEvent.VK_T);
		editModeDeleteVertex.setMnemonic(KeyEvent.VK_L);
		editModeBGImage.setMnemonic(KeyEvent.VK_B);
		lineTypeAuxButton.setMnemonic(KeyEvent.VK_A);
		lineTypeMountainButton.setMnemonic(KeyEvent.VK_M);
		lineTypeValleyButton.setMnemonic(KeyEvent.VK_V);

		addPropertyChangeListenersToSetting(mainScreenSetting);
		addActionListenersToComponents(stateManager, actionHolder, cutOutlinesHolder,
				mainScreenSetting);

		// -------------------------------------------------
		// Initialize selection
		// -------------------------------------------------
		editModeInputLineButton.setSelected(true);

		// of paint command
		lineInputDirectVButton.doClick();

		// of line type on setting
		setting.setTypeFrom((TypeForChange) alterLine_combo_from
				.getSelectedItem());
		setting.setTypeTo((TypeForChange) alterLine_combo_to
				.getSelectedItem());

		doFullEstimationCheckBox.setSelected(true);
		lineTypeMountainButton.doClick();
	}

	public void setChildFrameManager(final ChildFrameManager childFrameManager) {
		this.childFrameManager = childFrameManager;
	}

	private JFormattedTextField createNumTextField() {
		NumberFormat bgNumberFormat = NumberFormat
				.getNumberInstance(Locale.US);
		bgNumberFormat.setMinimumFractionDigits(6);

		JFormattedTextField field = new JFormattedTextField(bgNumberFormat);

		field.setColumns(8);
		field.setHorizontalAlignment(JTextField.LEFT);

		return field;
	}

	private TitledBorder createTitledBorder(final String text) {
		TitledBorder border = new TitledBorder(text);
		border.setBorder(new LineBorder(getBackground().darker().darker(), 2));
		border.setBorder(new MatteBorder(1, 0, 0, 0,
				getBackground().darker().darker()));
		return border;
	}

	private void constructButtons(final StateManager stateManager,
			final MouseActionHolder actionHolder,
			final MainFrameSetting mainFrameSetting,
			final MainScreenSetting mainScreenSetting) {

		BinderInterface<ChangeViewSetting> viewChangeBinder = new ViewChangeBinder();

		var stateFactory = new PaintBoundStateFactory(stateManager, mainFrameSetting, setting,
				mainScreenSetting.getSelectionOriginHolder());

		ButtonFactory buttonFactory = new PaintActionButtonFactory(stateFactory, paintContext);

		editModeInputLineButton = (JRadioButton) viewChangeBinder
				.createButton(
						JRadioButton.class, new ChangeOnPaintInputButtonSelected(setting),
						StringID.UI.INPUT_LINE_ID,
						screenUpdater.getKeyListener());

		editModePickLineButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.SELECT_ID,
				screenUpdater.getKeyListener());

		editModeDeleteLineButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater,
				StringID.DELETE_LINE_ID,
				screenUpdater.getKeyListener());

		editModeLineTypeButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.CHANGE_LINE_TYPE_ID,
				screenUpdater.getKeyListener());

		editModeAddVertex = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.ADD_VERTEX_ID,
				screenUpdater.getKeyListener());

		editModeDeleteVertex = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.DELETE_VERTEX_ID,
				screenUpdater.getKeyListener());

		editModeBGImage = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.EDIT_BGIMAGE_ID,
				screenUpdater.getKeyListener());

		// ---------------------------------------------------------------------------------------------------------------------------
		// Binding how to enter the line

		lineInputDirectVButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.DIRECT_V_ID,
				screenUpdater.getKeyListener());

		lineInputOnVButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.ON_V_ID,
				screenUpdater.getKeyListener());

		lineInputVerticalLineButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.VERTICAL_ID,
				screenUpdater.getKeyListener());

		lineInputAngleBisectorButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.BISECTOR_ID,
				screenUpdater.getKeyListener());

		lineInputTriangleSplitButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.TRIANGLE_ID,
				screenUpdater.getKeyListener());

		lineInputSymmetricButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater,
				StringID.SYMMETRIC_ID,
				screenUpdater.getKeyListener());

		lineInputMirrorButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.MIRROR_ID,
				screenUpdater.getKeyListener());

		lineInputByValueButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater, StringID.BY_VALUE_ID,
				screenUpdater.getKeyListener());

		lineInputPBisectorButton = (JRadioButton) buttonFactory.create(
				this, JRadioButton.class, actionHolder, screenUpdater,
				StringID.PERPENDICULAR_BISECTOR_ID,
				screenUpdater.getKeyListener());
	}

	private void createEditActionPanel() {
		// Edit mode
		editModeGroup = new ButtonGroup();
		editModeGroup.add(editModeInputLineButton);
		editModeGroup.add(editModePickLineButton);
		editModeGroup.add(editModeDeleteLineButton);
		editModeGroup.add(editModeLineTypeButton);
		editModeGroup.add(editModeAddVertex);
		editModeGroup.add(editModeDeleteVertex);
		editModeGroup.add(editModeBGImage);

		// How to enter the line
		ButtonGroup lineInputGroup = new ButtonGroup();
		lineInputGroup.add(lineInputDirectVButton);
		lineInputGroup.add(lineInputOnVButton);
		lineInputGroup.add(lineInputTriangleSplitButton);
		lineInputGroup.add(lineInputAngleBisectorButton);
		lineInputGroup.add(lineInputVerticalLineButton);
		lineInputGroup.add(lineInputSymmetricButton);
		lineInputGroup.add(lineInputMirrorButton);
		lineInputGroup.add(lineInputByValueButton);
		lineInputGroup.add(lineInputPBisectorButton);

		mainPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
		mainPanel.setLayout(new GridBagLayout());

		int n = 0;
		int gridX = 0;
		int gridY = 0;
		int gridWidth = 1;

		mainPanel.add(editModeInputLineButton, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));

		mainPanel.add(editModePickLineButton, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));

		mainPanel.add(editModeDeleteLineButton, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));

		mainPanel.add(editModeLineTypeButton, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));

		mainPanel.add(editModeAddVertex, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));

		mainPanel.add(editModeDeleteVertex, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));

		mainPanel.add(editModeBGImage, createMainPanelGridBagConstraints(
				gridX, gridY++, gridWidth));
	}

	private void createLineInputPanel() {

		ButtonGroup lineTypeGroup = new ButtonGroup();
		lineTypeGroup.add(lineTypeMountainButton);
		lineTypeGroup.add(lineTypeValleyButton);
		lineTypeGroup.add(lineTypeAuxButton);

		lineTypePanel.setLayout(new BoxLayout(lineTypePanel, BoxLayout.LINE_AXIS));
		lineTypePanel.add(lineTypeMountainButton);
		lineTypePanel.add(lineTypeValleyButton);
		lineTypePanel.add(lineTypeAuxButton);

		lineInputPanel.setLayout(new GridBagLayout());
		lineInputPanel.setBorder(createTitledBorder("Line Input"));

		int gridX = 0;
		int gridY = 0;
		int gridWidth = 4;

		JLabel label0 = new JLabel("Line Type");
		label0.setHorizontalAlignment(JLabel.CENTER);
		GridBagConstraints c = createGridBagConstraints(
				0, gridY++, gridWidth);
		c.anchor = GridBagConstraints.CENTER;
		c.weighty = 1.0;
		lineInputPanel.add(label0, c);

		c.gridy++;
		c.weighty = 0;
		lineInputPanel.add(lineTypePanel, c);

		JLabel label1 = new JLabel("Command (Alt + 1...9)");
		label1.setHorizontalAlignment(JLabel.CENTER);
		c.weighty = 1.0;
		c.gridy++;
		lineInputPanel.add(label1, c);

		gridY = c.gridy + 1;
		c.weighty = 0;
		// put operation buttons in order
		lineInputPanel.add(lineInputDirectVButton, createGridBagConstraints(
				0, gridY, 1));
		lineInputPanel.add(lineInputOnVButton, createGridBagConstraints(
				1, gridY, 1));
		lineInputPanel.add(lineInputPBisectorButton, createGridBagConstraints(
				2, gridY, 1));
		lineInputPanel.add(lineInputAngleBisectorButton, createGridBagConstraints(
				3, gridY++, 1));
		lineInputPanel.add(lineInputTriangleSplitButton, createGridBagConstraints(
				0, gridY, 1));
		lineInputPanel.add(lineInputVerticalLineButton, createGridBagConstraints(
				1, gridY, 1));
		lineInputPanel.add(lineInputSymmetricButton, createGridBagConstraints(
				2, gridY, 1));
		lineInputPanel.add(lineInputMirrorButton, createGridBagConstraints(
				3, gridY++, 1));
		lineInputPanel.add(lineInputByValueButton, createGridBagConstraints(
				0, gridY, 1));

		setButtonIcons();

	}

	private void createAlterLineTypePanel() {
		// alter line type panel setup
		JLabel l1 = new JLabel(
				resources.getString(ResourceKey.LABEL,
						StringID.UI.CHANGE_LINE_TYPE_FROM_ID));

		JLabel l2 = new JLabel(
				resources.getString(ResourceKey.LABEL,
						StringID.UI.CHANGE_LINE_TYPE_TO_ID));

		alterLineTypePanel.setLayout(new GridBagLayout());
		alterLineTypePanel.setBorder(createTitledBorder("Alter Line Type"));

		alterLineTypePanel.add(l1, createGridBagConstraints(0, 0, 1));
		alterLineTypePanel.add(alterLine_combo_from, createGridBagConstraints(1, 0, 1));
		alterLineTypePanel.add(l2, createGridBagConstraints(0, 1, 1));
		alterLineTypePanel.add(alterLine_combo_to, createGridBagConstraints(1, 1, 1));
		alterLineTypePanel.setVisible(false);
	}

	private void createEditBGImagePanel() {
		// editBGImagePanel
		textFieldBGposX = createNumTextField();
		textFieldBGposY = createNumTextField();
		textFieldBGscaleX = createNumTextField();
		textFieldBGscaleY = createNumTextField();
		textFieldBGrotation = createNumTextField();

		// put it all together
		editBGImagePanel.setLayout(new GridBagLayout());
		editBGImagePanel.setBorder(createTitledBorder("BGImage"));
		editBGImagePanel.setVisible(false);

		// editBGImagePanel.setVisible(false);
		int gridX = 0;
		int gridY = 0;
		int gridWidth = 1;

		editBGImagePanel.add(showBGImage, createGridBagConstraints(
				gridX, gridY++, 2));
		editBGImagePanel.add(new JLabel("posX:"), createGridBagConstraints(
				gridX, gridY, gridWidth));
		editBGImagePanel.add(textFieldBGposX, createGridBagConstraints(
				gridX + 1, gridY++, gridWidth));
		editBGImagePanel.add(new JLabel("posY:"), createGridBagConstraints(
				gridX, gridY, gridWidth));
		editBGImagePanel.add(textFieldBGposY, createGridBagConstraints(
				gridX + 1, gridY++, gridWidth));
		editBGImagePanel.add(new JLabel("scaleX:"), createGridBagConstraints(
				gridX, gridY, gridWidth));
		editBGImagePanel.add(textFieldBGscaleX, createGridBagConstraints(
				gridX + 1, gridY++, gridWidth));
		editBGImagePanel.add(new JLabel("scaleY:"), createGridBagConstraints(
				gridX, gridY, gridWidth));
		editBGImagePanel.add(textFieldBGscaleY, createGridBagConstraints(
				gridX + 1, gridY++, gridWidth));
		editBGImagePanel.add(new JLabel("tilt:"), createGridBagConstraints(
				gridX, gridY, gridWidth));
		editBGImagePanel.add(textFieldBGrotation, createGridBagConstraints(
				gridX + 1, gridY++, gridWidth));
		editBGImagePanel.add(setBGsettings, createGridBagConstraints(
				gridX, gridY++, 2));

	}

	private void createEditByValuePanel() {
		JLabel subLabel1 = new JLabel(
				resources.getString(ResourceKey.LABEL, StringID.UI.LENGTH_ID));

		JLabel subLabel2 = new JLabel(
				resources.getString(ResourceKey.LABEL, StringID.UI.ANGLE_ID));

		NumberFormat doubleValueFormat = NumberFormat
				.getNumberInstance(Locale.US);
		doubleValueFormat.setMinimumFractionDigits(6);

		textFieldLength = new JFormattedTextField(doubleValueFormat);
		textFieldAngle = new JFormattedTextField(doubleValueFormat);

		textFieldLength.setColumns(4);
		textFieldAngle.setColumns(4);
		textFieldLength.setValue(java.lang.Double.valueOf(0.0));
		textFieldAngle.setValue(java.lang.Double.valueOf(0.0));

		textFieldLength.setHorizontalAlignment(JTextField.RIGHT);
		textFieldAngle.setHorizontalAlignment(JTextField.RIGHT);

		byValuePanel.setLayout(new GridBagLayout());
		byValuePanel.setBorder(createTitledBorder("By Value"));
		byValuePanel.setVisible(false);

		byValuePanel.add(subLabel1, createGridBagConstraints(0, 0, 1));
		byValuePanel.add(textFieldLength, createGridBagConstraints(1, 0, 1));
		byValuePanel.add(buttonLength, createGridBagConstraints(2, 0, 1));
		byValuePanel.add(subLabel2, createGridBagConstraints(0, 1, 1));
		byValuePanel.add(textFieldAngle, createGridBagConstraints(1, 1, 1));
		byValuePanel.add(buttonAngle, createGridBagConstraints(2, 1, 1));
	}

	private void createGridPanel() {
		JLabel gridLabel1 = new JLabel(
				resources.getString(ResourceKey.LABEL,
						StringID.UI.GRID_DIVIDE_NUM_ID));

		textFieldGrid = new JFormattedTextField(new DecimalFormat("#"));
		textFieldGrid.setColumns(2);
		textFieldGrid.setValue(Integer.valueOf(paintContext.getGridDivNum()));
		textFieldGrid.setHorizontalAlignment(JTextField.RIGHT);

		gridPanel.setLayout(new GridBagLayout());
		gridPanel.setBorder(createTitledBorder("Grid"));

		gridPanel.add(dispGridCheckBox, createGridBagConstraints(0, 0, 3));

		gridPanel.add(gridLabel1, createGridBagConstraints(0, 1, 1));
		gridPanel.add(textFieldGrid, createGridBagConstraints(1, 1, 1));
		gridPanel.add(gridChangeButton, createGridBagConstraints(2, 1, 1));

		gridPanel.add(gridSmallButton, createGridBagConstraints(0, 2, 1));
		gridPanel.add(gridLargeButton, createGridBagConstraints(1, 2, 1));

	}

	private void createButtonsPanel() {
		int n = 0;
		buttonsPanel.add(dispMVLinesCheckBox);
		n++;
		buttonsPanel.add(dispAuxLinesCheckBox);
		n++;
		buttonsPanel.add(dispVertexCheckBox);
		n++;
		buttonsPanel.add(buttonCheckWindow);
		n++;
		buttonsPanel.add(buildButton);
		n++;
		buttonsPanel.add(doFullEstimationCheckBox);
		n++;

		buttonsPanel.setBorder(new MatteBorder(1, 0, 0, 0,
				getBackground().darker().darker()));
		buttonsPanel.setLayout(new GridLayout(n, 1, 10, 2));
	}

	private GridBagConstraints createMainPanelGridBagConstraints(final int gridX, final int gridY,
			final int gridWidth) {
		var gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = gridX;
		gridBagConstraints.gridy = gridY;
		gridBagConstraints.gridwidth = gridWidth;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;

		return gridBagConstraints;
	}

	private GridBagConstraints createGridBagConstraints(final int gridX, final int gridY,
			final int gridWidth) {
		var gridBagConstraints = new GridBagConstraints();

		// padding
		// gridBagConstraints.insets = new Insets(5, 5, 5, 5);

		gridBagConstraints.weightx = 0.5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = gridX;
		gridBagConstraints.gridy = gridY;
		// number of columns spanned
		gridBagConstraints.gridwidth = gridWidth;

		// left anchor
		gridBagConstraints.anchor = GridBagConstraints.LINE_START;

		return gridBagConstraints;
	}

	private void setButtonIcons() {
		setButtonIcon(lineInputDirectVButton, "icon/segment.gif", "icon/segment_p.gif");

		setButtonIcon(lineInputOnVButton, "icon/line.gif", "icon/line_p.gif");

		setButtonIcon(lineInputPBisectorButton, "icon/pbisector.gif", "icon/pbisector_p.gif");

		setButtonIcon(lineInputAngleBisectorButton, "icon/bisector.gif", "icon/bisector_p.gif");

		setButtonIcon(lineInputTriangleSplitButton, "icon/incenter.gif", "icon/incenter_p.gif");

		setButtonIcon(lineInputVerticalLineButton, "icon/vertical.gif", "icon/vertical_p.gif");

		setButtonIcon(lineInputSymmetricButton, "icon/symmetry.gif", "icon/symmetry_p.gif");

		setButtonIcon(lineInputMirrorButton, "icon/mirror.gif", "icon/mirror_p.gif");

		setButtonIcon(lineInputByValueButton, "icon/by_value.gif", "icon/by_value_p.gif");
	}

	private void setButtonIcon(final AbstractButton button, final String iconPath,
			final String selectedIconPath) {
		ImageResourceLoader imgLoader = new ImageResourceLoader();
		button.setIcon(imgLoader.loadAsIcon(iconPath));
		button.setSelectedIcon(imgLoader.loadAsIcon(selectedIconPath));
	}

	private void addActionListenersToComponents(final StateManager stateManager,
			final MouseActionHolder actionHolder,
			final CutModelOutlinesHolder cutOutlinesHolder,
			final MainScreenSetting mainScreenSetting) {

		alterLine_combo_from.addItemListener(new FromLineTypeItemListener(setting));
		alterLine_combo_to.addItemListener(new ToLineTypeItemListener(setting));

		PaintActionSetterFactory setterFactory = new PaintActionSetterFactory(
				actionHolder, screenUpdater, paintContext);

		buttonLength.addActionListener(
				setterFactory.create(new LengthMeasuringAction(valueSetting)));

		buttonAngle.addActionListener(
				setterFactory.create(new AngleMeasuringAction(valueSetting)));

		lineTypeMountainButton.addActionListener(
				e -> paintContext.setLineTypeOfNewLines(OriLine.Type.RIDGE));

		lineTypeValleyButton.addActionListener(
				e -> paintContext.setLineTypeOfNewLines(OriLine.Type.VALLEY));

		lineTypeAuxButton.addActionListener(
				e -> paintContext.setLineTypeOfNewLines(OriLine.Type.NONE));

		editModeInputLineButton
				.addActionListener(new InputCommandStatePopper(stateManager));

		textFieldLength.getDocument().addDocumentListener(
				new LengthValueInputListener(valueSetting));
		textFieldAngle.getDocument().addDocumentListener(
				new AngleValueInputListener(valueSetting));

		dispGridCheckBox.addActionListener(e -> {
			mainScreenSetting.setGridVisible(dispGridCheckBox.isSelected());
			screenUpdater.updateScreen();
		});

		gridSmallButton.addActionListener(e -> makeGridSizeHalf());

		gridLargeButton.addActionListener(e -> makeGridSizeTwiceLarge());

		gridChangeButton.addActionListener(e -> setGridDivNum());

		textFieldGrid.addActionListener(e -> setGridDivNum());

		dispVertexCheckBox.addActionListener(e -> {
			paintContext.setVertexVisible(dispVertexCheckBox.isSelected());
			screenUpdater.updateScreen();
		});
		dispVertexCheckBox.setSelected(true);
		paintContext.setVertexVisible(true);

		dispMVLinesCheckBox
				.addActionListener(e -> {
					paintContext.setMVLineVisible(dispMVLinesCheckBox.isSelected());
					screenUpdater.updateScreen();
				});
		dispAuxLinesCheckBox
				.addActionListener(e -> {
					paintContext.setAuxLineVisible(dispAuxLinesCheckBox.isSelected());
					screenUpdater.updateScreen();
				});

		doFullEstimationCheckBox
				.addActionListener(e -> {
					fullEstimation = doFullEstimationCheckBox.isSelected();
				});

		buttonCheckWindow.addActionListener(e -> showCheckerWindow(paintContext));

		buildButton.addActionListener(
				e -> showFoldedModelWindows(cutOutlinesHolder, mainScreenSetting));
	}

	private void showCheckerWindow(final PaintContextInterface context) {
		OrigamiModel origamiModel;
		CreasePatternInterface creasePattern = context.getCreasePattern();

		OrigamiModelFactory modelFactory = new OrigamiModelFactory();
		origamiModel = modelFactory.createOrigamiModel(
				creasePattern, creasePattern.getPaperSize());

		FoldabilityCheckFrameFactory checkerFactory = new FoldabilityCheckFrameFactory(
				childFrameManager);
		JFrame checker = checkerFactory.createFrame(
				UIPanel.this, origamiModel, creasePattern);
		checker.repaint();
		checker.setVisible(true);
	}

	private void makeGridSizeHalf() {
		if (paintContext.getGridDivNum() < 65) {
			paintContext.setGridDivNum(paintContext.getGridDivNum() * 2);
			textFieldGrid.setValue(Integer.valueOf(paintContext.getGridDivNum()));

			screenUpdater.updateScreen();
		}
	}

	private void makeGridSizeTwiceLarge() {
		if (paintContext.getGridDivNum() > 3) {
			paintContext.setGridDivNum(paintContext.getGridDivNum() / 2);
			textFieldGrid.setValue(Integer.valueOf(paintContext.getGridDivNum()));

			screenUpdater.updateScreen();
		}
	}

	private void setGridDivNum() {
		int value;
		try {
			value = Integer.valueOf(textFieldGrid.getText());
			logger.debug("gird division num: " + value);

			if (value < 128 && value > 2) {
				paintContext.setGridDivNum(value);
				screenUpdater.updateScreen();
			}
		} catch (Exception ex) {
			logger.error("failed to get grid division num.", ex);
		}
	}

	private void showFoldedModelWindows(
			final CutModelOutlinesHolder cutOutlinesHolder,
			final MainScreenSetting mainScreenSetting) {
		CreasePatternInterface creasePattern = paintContext.getCreasePattern();
		FoldedModelInfo foldedModelInfo = new FoldedModelInfo();

		Folder folder = new Folder();

		OrigamiModel origamiModel = buildOrigamiModel(creasePattern);

		if (origamiModel.isProbablyFoldable()) {
			final int foldableModelCount = folder.fold(
					origamiModel, foldedModelInfo, fullEstimation);

			if (foldableModelCount == -1) {

			} else if (foldableModelCount == 0) {
				JOptionPane.showMessageDialog(
						null, "No answer was found", "ORIPA",
						JOptionPane.DEFAULT_OPTION);
			} else if (foldableModelCount > 0) {
				logger.info("foldable layer layout is found.");

				EstimationResultFrameFactory resultFrameFactory = new EstimationResultFrameFactory(
						childFrameManager);
				JFrame frame = resultFrameFactory.createFrame(this,
						origamiModel, foldedModelInfo);
				frame.repaint();
				frame.setVisible(true);
			}
		} else {
			BoundBox boundBox = folder.foldWithoutLineType(origamiModel);
			foldedModelInfo.setBoundBox(boundBox);
		}

		ModelViewFrameFactory modelViewFactory = new ModelViewFrameFactory(
				mainScreenSetting,
				childFrameManager);
		JFrame modelView = modelViewFactory.createFrame(this, origamiModel,
				cutOutlinesHolder, () -> screenUpdater.updateScreen());

		modelView.repaint();
		modelView.setVisible(true);
	}

	private OrigamiModel buildOrigamiModel(final CreasePatternInterface creasePattern) {
		OrigamiModelFactory modelFactory = new OrigamiModelFactory();
		OrigamiModel origamiModel = modelFactory.createOrigamiModel(
				creasePattern, creasePattern.getPaperSize());

		if (origamiModel.isProbablyFoldable()) {
			return origamiModel;
		}

		if (JOptionPane.showConfirmDialog(
				this, resources.getString(
						ResourceKey.WARNING,
						StringID.Warning.FOLD_FAILED_DUPLICATION_ID),
				"Failed", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.NO_OPTION) {
			return origamiModel;
		}

		origamiModel = modelFactory
				.createOrigamiModelNoDuplicateLines(
						creasePattern, creasePattern.getPaperSize());
		if (origamiModel.isProbablyFoldable()) {
			return origamiModel;
		}

		JOptionPane.showMessageDialog(
				this,
				resources.getString(
						ResourceKey.WARNING,
						StringID.Warning.FOLD_FAILED_WRONG_STRUCTURE_ID),
				"Failed Level1",
				JOptionPane.INFORMATION_MESSAGE);

		return origamiModel;
	}

	private void addPropertyChangeListenersToSetting(final MainScreenSetting mainScreenSetting) {
		mainScreenSetting.addPropertyChangeListener(
				MainScreenSetting.GRID_VISIBLE, e -> {
					dispGridCheckBox.setSelected((boolean) e.getNewValue());
					repaint();
				});

		valueSetting.addPropertyChangeListener(
				ValueSetting.ANGLE, e -> textFieldAngle.setValue(e.getNewValue()));

		valueSetting.addPropertyChangeListener(
				ValueSetting.LENGTH, e -> textFieldLength.setValue(e.getNewValue()));

		setting.addPropertyChangeListener(
				UIPanelSetting.SELECTED_MODE, this::onChangeEditModeButtonSelection);

		setting.addPropertyChangeListener(
				UIPanelSetting.BY_VALUE_PANEL_VISIBLE, e -> {
					byValuePanel.setVisible((boolean) e.getNewValue());
				});

		setting.addPropertyChangeListener(
				UIPanelSetting.ALTER_LINE_TYPE_PANEL_VISIBLE,
				e -> alterLineTypePanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(UIPanelSetting.LINE_INPUT_PANEL_VISIBLE,
				e -> lineInputPanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(UIPanelSetting.EDIT_BGIMAGE_PANEL_VISIBLE,
				e -> editBGImagePanel.setVisible((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(
				UIPanelSetting.MOUNTAIN_BUTTON_ENABLED,
				e -> lineTypeMountainButton.setEnabled((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(
				UIPanelSetting.VALLEY_BUTTON_ENABLED,
				e -> lineTypeValleyButton.setEnabled((boolean) e.getNewValue()));

		setting.addPropertyChangeListener(
				UIPanelSetting.AUX_BUTTON_ENABLED,
				e -> lineTypeAuxButton.setEnabled((boolean) e.getNewValue()));
	}

	private void onChangeEditModeButtonSelection(final PropertyChangeEvent e) {
		switch (setting.getSelectedMode()) {
		case INPUT:
			selectEditModeButton(editModeInputLineButton);
			break;
		case SELECT:
			selectEditModeButton(editModePickLineButton);
			break;
		case MOVE_BG:
			selectEditModeButton(editModeBGImage);
			break;
		default:
			break;
		}
	}

	private void selectEditModeButton(final AbstractButton modeButton) {
		editModeGroup.setSelected(modeButton.getModel(), true);
	}

	public UIPanelSetting getUIPanelSetting() {
		return setting;
	}
}
