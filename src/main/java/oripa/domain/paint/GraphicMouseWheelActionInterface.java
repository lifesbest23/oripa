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
package oripa.domain.paint;

import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;

/**
 * Used if the mouse Action is supposed to handle mouse wheel input as well
 *
 * @author lifesbest23
 *
 */
public interface GraphicMouseWheelActionInterface extends GraphicMouseActionInterface {

	public abstract void onWheelMoved(PaintContextInterface context,
			AffineTransform affine, MouseWheelEvent e);
}
