/**
 * ORIPA - Origami Pattern Editor
 * Copyright (C) 2013-     ORIPA OSS Project  https://github.com/oripa/oripa
 * Copyright (C) 2005-2009 Jun Mitani         http://mitani.cs.tsukuba.ac.jp/

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
package oripa.domain.paint.editbgimage;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oripa.domain.bgimage.BGImage;
import oripa.domain.paint.EditMode;
import oripa.domain.paint.PaintContextInterface;
import oripa.domain.paint.core.GraphicMouseAction;

/**
 * @author lucas
 *
 */
public class EditBGImageAction extends GraphicMouseAction {

	private static final Logger logger = LoggerFactory.getLogger(EditBGImageAction.class);

	private Point2D.Double startPoint = null;
	private Point2D.Double draggingPoint = null;

	public EditBGImageAction() {
		setEditMode(EditMode.MOVE_BG);

		setActionState(new EditBGImage());
	}

	/*
	 * (non Javadoc)
	 *
	 * @see
	 * oripa.domain.paint.core.GraphicMouseAction#onPress(oripa.domain.paint.
	 * PaintContextInterface, java.awt.geom.AffineTransform, boolean)
	 */
	@Override
	public void onPress(final PaintContextInterface context, final AffineTransform affine,
			final boolean differentAction) {
		startPoint = context.getLogicalMousePoint();
		logger.info(startPoint.toString());
	}

	/*
	 * (non Javadoc)
	 *
	 * @see
	 * oripa.domain.paint.core.GraphicMouseAction#onDrag(oripa.domain.paint.
	 * PaintContextInterface, java.awt.geom.AffineTransform, boolean)
	 */
	@Override
	public void onDrag(final PaintContextInterface context, final AffineTransform affine,
			final boolean differentAction) {
		draggingPoint = context.getLogicalMousePoint();

		if (startPoint != null && draggingPoint != null) {
			moveBGImage(context);
		}
		startPoint = draggingPoint;
	}

	/*
	 * (non Javadoc)
	 *
	 * @see
	 * oripa.domain.paint.core.GraphicMouseAction#onRelease(oripa.domain.paint.
	 * PaintContextInterface, java.awt.geom.AffineTransform, boolean)
	 */
	@Override
	public void onRelease(final PaintContextInterface context, final AffineTransform affine,
			final boolean differentAction) {
		if (startPoint != null && draggingPoint != null) {
			moveBGImage(context);
		}

		startPoint = null;
		draggingPoint = null;
	}

	private void moveBGImage(final PaintContextInterface context) {
		BGImage bg = context.getBGImage();

		double dx = startPoint.x - draggingPoint.x;
		double dy = startPoint.y - draggingPoint.y;

		bg.offsetX -= dx;
		bg.offsetY -= dy;
	}

}
