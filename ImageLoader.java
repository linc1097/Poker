import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.net.URL;
/**
 * responsible for loading images
 * 
 * @Lincoln Updike
 * @6.10.17
 */
public class ImageLoader
{
    private BufferedImage image;
    /**
     * takes a filepath and returns a buffered image. Filepath must be to a picture (PNG, JPG, GIF, etc.)
     */
    public BufferedImage loadImage(String path) throws IOException
    {
        File file = new File(path);
        image = ImageIO.read(file);
        return image;
    }
}
