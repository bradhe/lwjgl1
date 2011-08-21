import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;


public class Texture {
	private int textureId;
	private int target;
	private int height;
	private int width;
	private int imageHeight;
	private int imageWidth;
	private ByteBuffer imageData;
	
	public ByteBuffer getImageData() {
		return imageData;
	}

	public void setImageData(ByteBuffer imageData) {
		this.imageData = imageData;
	}

	public int getTextureId() {
		return textureId;
	}

	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	public Texture(int textureId, int target) {
		this.textureId = textureId;
		this.target = target;
	}
	
	public void bind() {
		GL11.glBindTexture(target, textureId);
	}
	
	public static Texture load(String fileName) {
		int textureId = generateTextureId();
		int target = GL11.GL_TEXTURE_2D;
		Texture texture = null;
		
		try {
			BufferedImage imageData = loadImage(fileName);
			texture = new Texture(textureId, target);
			texture.setImageWidth(imageData.getWidth());
			texture.setImageHeight(imageData.getHeight());
			
			rasterizeImageToTexture(imageData, texture);
			
			int colorFormat = GL11.GL_RGBA;
			
			if(!imageData.getColorModel().hasAlpha()) {
				colorFormat = GL11.GL_RGB;	
			}
			
			// Go!
			GL11.glBindTexture(target, textureId);
			GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); 
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexImage2D(target, 
            		0,
            		colorFormat,
            		getNearestPowerOfTwo(imageData.getWidth()), 
            		getNearestPowerOfTwo(imageData.getHeight()), 
            		0, 
            		colorFormat, 
            		GL11.GL_UNSIGNED_BYTE, 
            		texture.getImageData());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return texture;
	}
	
	private static ColorModel alphaColorModel = null;
	private static ColorModel getAlphaColorModel() {
		if(alphaColorModel == null) {
			alphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);
		}
		
		return alphaColorModel;
	}
	
	private static ColorModel colorModel = null;
	private static ColorModel getColorModel() {
		if(colorModel == null) { 
			colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
		}
		
		return colorModel;
	}
	
	private static int generateTextureId() {
		ByteBuffer temp = ByteBuffer.allocateDirect(4);
		temp.order(ByteOrder.nativeOrder());
		GL11.glGenTextures(temp.asIntBuffer()); 
		return temp.get(0);
	}
	
	private static BufferedImage loadImage(String fileName) throws FileNotFoundException, IOException {
        return ImageIO.read(new BufferedInputStream(new FileInputStream(fileName)));
	}
	
	private static void rasterizeImageToTexture(BufferedImage image, Texture texture) {
		ByteBuffer imageBuffer = null; 
        WritableRaster raster;
        BufferedImage texImage;
        
        // find the closest power of 2 for the width and height
        // of the produced texture    
        int texWidth = getNearestPowerOfTwo(image.getWidth());
        int texHeight = getNearestPowerOfTwo(image.getHeight());

        // This is the height and width of the texture
        texture.setHeight(texHeight);
        texture.setWidth(texWidth);
        
        // create a raster that can be used by OpenGL as a source
        if (image.getColorModel().hasAlpha()) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
            texImage = new BufferedImage(getAlphaColorModel(), raster, false, new Hashtable());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(getColorModel() ,raster, false, new Hashtable());
        }
            
        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f,0f,0f,0f));
        g.fillRect(0,0,texWidth,texHeight);
        g.drawImage(image,0,0,null);
        
        // build a byte buffer from the temporary image 
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData(); 

        imageBuffer = ByteBuffer.allocateDirect(data.length); 
        imageBuffer.order(ByteOrder.nativeOrder()); 
        imageBuffer.put(data, 0, data.length); 
        imageBuffer.flip();
        
        texture.setImageData(imageBuffer);
	}
	
	private static int getNearestPowerOfTwo(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    } 
}
