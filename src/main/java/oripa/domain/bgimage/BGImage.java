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

	private BufferedImage image = null;

	private String path = "";

	public int offsetX = 0;
	public int offsetY = 0;

	public int scaleX = -1;
	public int scaleY = -1;

	public BGImage() {
		/*
		 * try { image = ImageIO.read( getClass().getResource( "/demon1.png" //
		 * "/resources/icon/bisector_p.gif" )); } catch (IOException e) {
		 * logger.error("ERROR BGIMAGE"); }
		 */
		logger.info("SUCCESS");
	}

	public BufferedImage getBuffer() {
		return image;
	}

	public void setImage(final String newPath) {
		path = newPath;

		if (path == null) {
			image = null;
			return;
		}

		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			logger.error("ERROR BGIMAGE");
			return;
		}
		logger.info(
				"loaded new bg image " + path + " @ " + image.getHeight() + "x" + image.getWidth());
	}

	public String getImagePath() {
		return path;
	}

}
