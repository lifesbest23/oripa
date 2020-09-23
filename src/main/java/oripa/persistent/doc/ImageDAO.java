package oripa.persistent.doc;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;

import oripa.persistent.filetool.FileChooserCanceledException;
import oripa.persistent.filetool.FileChooserFactory;
import oripa.persistent.filetool.FileVersionError;
import oripa.persistent.filetool.ImageChooser;
import oripa.persistent.filetool.WrongDataFormatException;

public class ImageDAO {

	/**
	 *
	 * @param doc
	 * @param homePath
	 * @param parent
	 * @param filters
	 * @return chosen path
	 */
	public String loadUsingGUI(final String homePath, final Component parent)
			throws FileVersionError, FileChooserCanceledException, IOException,
			WrongDataFormatException {
		FileChooserFactory<BufferedImage> factory = new FileChooserFactory<>();
		ImageChooser<BufferedImage> imageChooser = factory.createImageChooser(
				homePath);

		return imageChooser.loadImage(parent);

	}
}
