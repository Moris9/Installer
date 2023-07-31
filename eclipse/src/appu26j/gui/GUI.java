package appu26j.gui;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import appu26j.gui.textures.Texture;

public class GUI
{
	public static void drawRect(float x, float y, float width, float height, Color color)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.alphaFunc(516, 0);
		GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
		glBegin(GL_TRIANGLES);
		glVertex2f(x, y);
		glVertex2f(x, height);
		glVertex2f(width, height);
		glVertex2f(width, height);
		glVertex2f(width, y);
		glVertex2f(x, y);
		glEnd();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
	}


	public static void drawImage(Texture texture, float x, float y, float u, float v, float width, float height, float renderWidth, float renderHeight)
	{
		width += x;
		height += y;
		width -= u;
		height -= v;
		
		if (texture != null)
		{
			glEnable(GL_SCISSOR_TEST);
			glScissor((int) x, (int) (720 - renderHeight), (int) (renderWidth - x), (int) (renderHeight - y));
			glBindTexture(GL_TEXTURE_2D, texture.getId());
			GlStateManager.enableTexture2D();
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			GlStateManager.alphaFunc(516, 0);
			GlStateManager.color(1, 1, 1, 1);
			glBegin(GL_TRIANGLES);
			glTexCoord2f(u / texture.getWidth(), v / texture.getHeight());
			glVertex2f(x, y);
			glTexCoord2f(u / texture.getWidth(), 1);
			glVertex2f(x, height);
			glTexCoord2f(1, 1);
			glVertex2f(width, height);
			glTexCoord2f(1, 1);
			glVertex2f(width, height);
			glTexCoord2f(1, v / texture.getHeight());
			glVertex2f(width, y);
			glTexCoord2f(u / texture.getWidth(), v / texture.getHeight());
			glVertex2f(x, y);
			glEnd();
			GlStateManager.disableBlend();
			GlStateManager.disableAlpha();
			GlStateManager.disableTexture2D();
			glDisable(GL_SCISSOR_TEST);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}
	
	public static Texture getTexture(File image)
	{
		try
		{
			BufferedImage bufferedImage = ImageIO.read(image);
			ByteBuffer byteBuffer = getByteBuffer(bufferedImage);
		    int id = glGenTextures();
		    glBindTexture(GL_TEXTURE_2D, id);
		    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		    glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(), bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
		    return new Texture(id, bufferedImage.getWidth(), bufferedImage.getHeight());
		}
		
		catch (Exception e)
		{
			return null;
		}
	}
	
	private static ByteBuffer getByteBuffer(BufferedImage bufferedImage)
	{
        int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
        bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer(4 * bufferedImage.getWidth() * bufferedImage.getHeight());
 
        for (int y = 0; y < bufferedImage.getHeight(); y++)
        {
            for (int x = 0; x < bufferedImage.getWidth(); x++)
            {
                int pixel = pixels[y * bufferedImage.getWidth() + x];
                byteBuffer.put((byte) ((pixel >> 16) & 0xFF));
                byteBuffer.put((byte) ((pixel >> 8) & 0xFF));
                byteBuffer.put((byte) (pixel & 0xFF));
                byteBuffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        
        byteBuffer.flip();
        return byteBuffer;
    }
}