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
package oripa.domain.bgimage;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lucas
 *
 */
public class BGImage {

	private static final Logger logger = LoggerFactory.getLogger(BGImage.class);

	private boolean changedValue = false;
	public static String CHANGED_BGIMAGE = "changed bg image";
	public static String CHANGED_BGIMAGE_SCALE = "changed bg image scale";
	public static String CHANGED_BGIMAGE_POSITION = "changed bg image position";
	public static String CHANGED_BGIMAGE_ROTATION = "changed bg image rotation";

	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	private BufferedImage image = null;
	private BufferedImage imageBuffer = null;

	private String path = null;

	public int offsetX = 0;
	public int offsetY = 0;

	public int scaleX = 1000;
	public int scaleY = 1000;

	public int rotation = 0;

	private boolean visible = true;

	public BGImage() {
		/*
		 * try { image = ImageIO.read( getClass().getResource( "/demon1.png" //
		 * "/resources/icon/bisector_p.gif" )); } catch (IOException e) {
		 * logger.error("ERROR BGIMAGE"); }
		 */
		logger.info("SUCCESS");
	}

	public void addPropertyChangeListener(final String propertyName,
			final PropertyChangeListener listener) {
		support.addPropertyChangeListener(propertyName, listener);
	}

	public void moveImage(final int deltaX, final int deltaY) {
		offsetX += deltaX;
		offsetY += deltaY;

		support.firePropertyChange(CHANGED_BGIMAGE_POSITION, offsetX, deltaX);
	}

	public void zoomImage(final int scale) {
		int old = scaleX;
		scaleX += scale;
		scaleY += scale;
		support.firePropertyChange(CHANGED_BGIMAGE_SCALE, old, scaleX);
		setValueChanged(true);
	}

	public void rotateImage(final int delta) {
		int old = rotation;
		rotation += delta;
		support.firePropertyChange(CHANGED_BGIMAGE_ROTATION, old, rotation);
		setValueChanged(true);
	}

	public boolean getValueChanged() {
		return changedValue;
	}

	public void setValueChanged(final boolean ch) {
		changedValue = ch;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public int getScaleX() {
		return scaleX;
	}

	public int getScaleY() {
		return scaleY;
	}

	public int getRotation() {
		return rotation;
	}

	public void setOffsetX(final int x) {
		int old = offsetX;
		offsetX = x;
		support.firePropertyChange(CHANGED_BGIMAGE, old, x);
	}

	public void setOffsetY(final int y) {
		int old = offsetY;
		offsetY = y;
		support.firePropertyChange(CHANGED_BGIMAGE, old, y);
	}

	public void setScale(final int sc) {
		int old = scaleX;
		scaleX = sc;
		scaleY = sc;
		support.firePropertyChange(CHANGED_BGIMAGE, old, sc);
		setValueChanged(true);
	}

	public void setRotation(final int rot) {
		int old = rotation;
		rotation = rot;
		support.firePropertyChange(CHANGED_BGIMAGE, old, rot);
		setValueChanged(true);
	}

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(final boolean vis) {
		logger.info("setVisible " + vis);
		visible = vis;
	}

	public BufferedImage getOriginal() {
		return image;
	}

	public BufferedImage getBuffer() {
		return imageBuffer;
	}

	public void setBuffer(final BufferedImage im) {
		imageBuffer = im;
	}

	public void setImage(final String newPath) {
		path = newPath;

		if (path == null) {
			image = null;
			return;
		}

		try {
			image = ImageIO.read(new File(path));
			imageBuffer = image.getSubimage(0, 0, image.getWidth(), image.getHeight());

			scaleX = 1000;
			scaleY = 1000;
			offsetX = 0;
			offsetY = 0;
			rotation = 0;
			visible = true;
		} catch (IOException e) {
			logger.error("ERROR BGIMAGE setImage");
			return;
		}
		logger.info(
				"loaded new bg image " + path + " @ " + image.getHeight() + "x" + image.getWidth());
	}

	public String getImagePath() {
		return path;
	}

}
