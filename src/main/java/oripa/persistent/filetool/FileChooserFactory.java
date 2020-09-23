package oripa.persistent.filetool;

import java.awt.image.BufferedImage;

public class FileChooserFactory<Data> {

	@SafeVarargs
	public final FileChooser<Data> createChooser(final String path,
			final FileAccessSupportFilter<Data>... filters) {

		FileChooser<Data> fileChooser;

		if (path != null) {
			fileChooser = new FileChooser<Data>(path);
		} else {
			fileChooser = new FileChooser<Data>();
		}

		for (int i = 0; i < filters.length; i++) {
			fileChooser.addChoosableFileFilter(filters[i]);
		}

		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filters[0]);

		return fileChooser;
	}

	public final ImageChooser<BufferedImage> createImageChooser(final String path) {

		ImageChooser<BufferedImage> imageChooser;

		if (path != null) {
			imageChooser = new ImageChooser<BufferedImage>(path);
		} else {
			imageChooser = new ImageChooser<BufferedImage>();
		}

		/*
		 * for (int i = 0; i < filters.length; i++) {
		 * fileChooser.addChoosableFileFilter(filters[i]); }
		 *
		 * fileChooser.setAcceptAllFileFilterUsed(false);
		 * fileChooser.setFileFilter(filters[0]);
		 */

		return imageChooser;
	}

}
