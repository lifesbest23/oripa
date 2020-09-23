package oripa.persistent.filetool;

import java.awt.Component;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oripa.ORIPA;

/**
 *
 * choose background image dialog
 * 
 * @author lifesbest23
 *
 */

public class ImageChooser<BufferedImage> extends JFileChooser {

	private static Logger logger = LoggerFactory.getLogger(ImageChooser.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 4700305827321319095L;

	ImageChooser() {

		super();
	}

	ImageChooser(final String path) {
		super(path);

		File file = new File(path);
		this.setSelectedFile(file);
	}

	public String loadImage(final Component parent) throws FileChooserCanceledException {
		if (JFileChooser.APPROVE_OPTION != this.showOpenDialog(parent)) {
			throw new FileChooserCanceledException();
		}

		try {
			String imagePath = this.getSelectedFile().getPath();

			if (ImageIO.read(new File(imagePath)) != null) {
				return imagePath;
			}

		} catch (Exception e) {
			logger.error("error on loading a file", e);
			JOptionPane.showMessageDialog(this, e.toString(),
					ORIPA.res.getString("Error_FileLoadFailed"),
					JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

}
