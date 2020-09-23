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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import oripa.domain.bgimage.BGImage;

/**
 * @author lucas
 *
 */
public class BGImageDrawer {

	/**
	 * draws crease pattern according to the context of user interaction.
	 *
	 * @param g2d
	 * @param context
	 * @param forceShowingVertex
	 */
	public void draw(
			final Graphics2D g2d,
			final PaintContextInterface context, final boolean forceShowingVertex) {

		BGImage bg = context.getBGImage();
		if (bg.getImagePath() == null) {
			return;
		}

		// resize and scale
		int cpsize = (int) context.getCreasePattern().getPaperSize();
		Image im = bg.getBuffer().getScaledInstance(cpsize, cpsize, Image.SCALE_SMOOTH);

		g2d.drawImage(im,
				-(cpsize / 2) + bg.offsetX,
				-(cpsize / 2) + bg.offsetY, null);

		g2d.setColor(Color.BLACK);
		g2d.fillRect(-1, -1, 2, 2);
	}
}
