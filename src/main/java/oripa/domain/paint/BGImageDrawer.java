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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import oripa.domain.bgimage.BGImage;

/**
 * @author lucas
 *
 */
public class BGImageDrawer {

	private BufferedImage cache = null;

	private BufferedImage rotateImageByDegrees(final BufferedImage img, final double angle) {

		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, null);
		g2d.setColor(Color.RED);
		g2d.drawRect(0, 0, newWidth - 1, newHeight - 1);
		g2d.dispose();

		return rotated;
	}

	/**
	 * update local cached version of rotatetd and scaled image
	 *
	 * @param bg
	 *            BGImage instance used
	 */
	public void updateCachedImage(final BGImage bg) {
		int height = bg.getOriginal().getHeight() * bg.getScaleY() / 1000;
		int width = bg.getOriginal().getWidth() * bg.getScaleX() / 1000;

		BufferedImage tmp = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		// Draw the image on to the buffered image
		Graphics2D bGr = tmp.createGraphics();
		bGr.drawImage(
				bg.getOriginal().getScaledInstance(width, height,
						BufferedImage.SCALE_AREA_AVERAGING),
				0, 0, null);
		bGr.dispose();

		cache = rotateImageByDegrees(tmp,
				bg.getRotation());

		bg.setValueChanged(false);
	}

	/**
	 * draws crease pattern according to the context of user interaction.
	 *
	 * @param g2d
	 * @param context
	 */
	public void draw(
			final Graphics2D g2d,
			final PaintContextInterface context) {

		BGImage bg = context.getBGImage();
		if (bg.getImagePath() == null || !bg.getVisible()) {
			return;
		}

		int height = bg.getOriginal().getHeight() * bg.getScaleY() / 1000;
		int width = bg.getOriginal().getWidth() * bg.getScaleX() / 1000;

		if (cache == null) {
			updateCachedImage(bg);
		}

		g2d.drawImage(cache,
				-(width / 2) + bg.offsetX,
				-(height / 2) + bg.offsetY,
				null);

		g2d.setColor(Color.BLACK);
		g2d.fillRect(-1, -1, 2, 2);
	}
}
