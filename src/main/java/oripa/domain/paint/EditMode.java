package oripa.domain.paint;

/**
 * different edit mode states used by GraphicMouseAction
 *
 */
public enum EditMode {
	NONE, INPUT, SELECT, CHANGE_TYPE, DELETE_LINE,
	// ADD_VERTEX, DELETE_VERTEX,
	VERTEX, OTHER, COPY, CUT, EDIT_BG
}