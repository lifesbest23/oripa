package oripa.viewsetting.main.uipanel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import oripa.domain.cptool.TypeForChange;
import oripa.domain.paint.EditMode;
import oripa.domain.paint.byvalue.ValueSetting;

public class UIPanelSetting {
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	private boolean lineInputPanelVisible = true;
	public static final String LINE_INPUT_PANEL_VISIBLE = "line-input-panel-visible";

	private boolean editBGImagePanelVisible = false;
	public static final String EDIT_BGIMAGE_PANEL_VISIBLE = "edit-bgimage-panel-visible";

	private boolean byValuePanelVisible = false;
	public static final String BY_VALUE_PANEL_VISIBLE = "by-value panel visible";

	private boolean alterLineTypePanelVisible = true;
	public static final String ALTER_LINE_TYPE_PANEL_VISIBLE = "alter-line-type panel visible";

	private boolean mountainButtonEnabled = true;
	public static final String MOUNTAIN_BUTTON_ENABLED = "mountain button enabled";

	private boolean valleyButtonEnabled = true;
	public static final String VALLEY_BUTTON_ENABLED = "valley button enabled";

	private boolean auxButtonEnabled = true;
	public static final String AUX_BUTTON_ENABLED = "aux button enabled";

	private EditMode selectedMode = EditMode.NONE;
	public static final String SELECTED_MODE = "selected mode";

	private int lineTypeFromIndex;
	public static final String LINE_TYPE_FROM_INDEX = "line type index of 'from' box";

	private int lineTypeToIndex;
	public static final String LINE_TYPE_TO_INDEX = "line type index of 'to' box";

	private TypeForChange typeFrom = TypeForChange.EMPTY;
	public static final String TYPE_FROM = "line type of 'from' box";

	private TypeForChange typeTo = TypeForChange.EMPTY;
	public static final String TYPE_TO = "line type of 'to' box";

	private final ValueSetting valueSetting = new ValueSetting();

	public void addPropertyChangeListener(
			final String propertyName, final PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	public TypeForChange getTypeFrom() {
		return typeFrom;
	}

	public void setTypeFrom(final TypeForChange typeFrom) {
		this.typeFrom = typeFrom;
	}

	public TypeForChange getTypeTo() {
		return typeTo;
	}

	public void setTypeTo(final TypeForChange typeTo) {
		this.typeTo = typeTo;
	}

	public int getLineTypeFromIndex() {
		return lineTypeFromIndex;
	}

	public void setLineTypeFromIndex(final int lineTypeFromIndex) {
		var old = this.lineTypeFromIndex;
		this.lineTypeFromIndex = lineTypeFromIndex;
		support.firePropertyChange(LINE_TYPE_FROM_INDEX, old, lineTypeFromIndex);
	}

	public int getLineTypeToIndex() {
		return lineTypeToIndex;
	}

	public void setLineTypeToIndex(final int lineTypeToIndex) {
		var old = this.lineTypeToIndex;
		this.lineTypeToIndex = lineTypeToIndex;
		support.firePropertyChange(LINE_TYPE_TO_INDEX, old, lineTypeToIndex);
	}

	public boolean isByValuePanelVisible() {
		return byValuePanelVisible;
	}

	public boolean isAlterLineTypePanelVisible() {
		return alterLineTypePanelVisible;
	}

	public boolean isLineInputPanelVisible() {
		return lineInputPanelVisible;
	}

	public boolean isEditBGImagePanelVisible() {
		return editBGImagePanelVisible;
	}

	public boolean isMountainButtonEnabled() {
		return mountainButtonEnabled;
	}

	public boolean isValleyButtonEnabled() {
		return valleyButtonEnabled;
	}

	public boolean isAuxButtonEnabled() {
		return auxButtonEnabled;
	}

	public void setByValuePanelVisible(final boolean byValuePanelVisible) {
		var old = this.byValuePanelVisible;
		this.byValuePanelVisible = byValuePanelVisible;
		support.firePropertyChange(BY_VALUE_PANEL_VISIBLE, old, byValuePanelVisible);
	}

	public void setAlterLineTypePanelVisible(final boolean alterLineTypePanelVisible) {
		var old = this.alterLineTypePanelVisible;
		this.alterLineTypePanelVisible = alterLineTypePanelVisible;
		support.firePropertyChange(ALTER_LINE_TYPE_PANEL_VISIBLE, old, alterLineTypePanelVisible);
	}

	public void setLineInputPanelVisible(final boolean lineInputPanelVisible) {
		var old = this.lineInputPanelVisible;
		this.lineInputPanelVisible = lineInputPanelVisible;
		support.firePropertyChange(LINE_INPUT_PANEL_VISIBLE, old, lineInputPanelVisible);
	}

	public void setEditBGImagePanelVisible(final boolean editBGImagePanelVisible) {
		var old = this.editBGImagePanelVisible;
		this.editBGImagePanelVisible = editBGImagePanelVisible;
		support.firePropertyChange(EDIT_BGIMAGE_PANEL_VISIBLE, old, editBGImagePanelVisible);
	}

	public void setMountainButtonEnabled(final boolean mountainButtonEnabled) {
		var old = this.mountainButtonEnabled;
		this.mountainButtonEnabled = mountainButtonEnabled;
		support.firePropertyChange(MOUNTAIN_BUTTON_ENABLED, old, mountainButtonEnabled);
	}

	public void setValleyButtonEnabled(final boolean valleyButtonEnabled) {
		var old = this.valleyButtonEnabled;
		this.valleyButtonEnabled = valleyButtonEnabled;
		support.firePropertyChange(VALLEY_BUTTON_ENABLED, old, valleyButtonEnabled);
	}

	public void setAuxButtonEnabled(final boolean auxButtonEnabled) {
		var old = this.auxButtonEnabled;
		this.auxButtonEnabled = auxButtonEnabled;
		support.firePropertyChange(AUX_BUTTON_ENABLED, old, auxButtonEnabled);
	}

	private void setSelectedMode(final EditMode mode) {
		var old = selectedMode;
		selectedMode = mode;
		support.firePropertyChange(SELECTED_MODE, old, mode);
	}

	public void selectInputMode() {
		setSelectedMode(EditMode.INPUT);
	}

	public void selectChangeLineTypeMode() {
		setSelectedMode(EditMode.CHANGE_TYPE);
	}

	public void selectSelectMode() {
		setSelectedMode(EditMode.SELECT);
	}

	public void selectDeleteLineMode() {
		setSelectedMode(EditMode.DELETE_LINE);
	}

	public void selectAddVertexMode() {
		setSelectedMode(EditMode.VERTEX);
	}

	public void selectDeleteVertexMode() {
		setSelectedMode(EditMode.VERTEX);
	}

	public void selectEditBGImageMode() {
		setSelectedMode(EditMode.EDIT_BG);
	}

	public EditMode getSelectedMode() {
		EditMode ret = selectedMode;

		selectedMode = EditMode.NONE; // a hack to trigger an update every time.

		return ret;
	}

	public ValueSetting getValueSetting() {
		return valueSetting;
	}
}
