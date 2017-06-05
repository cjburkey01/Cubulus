package com.cjburkey.cubulus.render;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import com.cjburkey.cubulus.Cubulus;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Texture {
	
	private PNGDecoder decoder;
	private int texture;
	
	public Texture(String path) {
		try {
			decoder = new PNGDecoder(Texture.class.getResourceAsStream(path));
			sendToGpu();
		} catch (Exception e) {
			error(e);
		}
	}
	
	private void sendToGpu() throws Exception {
		ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
		decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
		buf.flip();
		
		texture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
	}
	
	public int getTextureId() {
		return texture;
	}
	
	private static void error(Exception e) {
		Cubulus.getInstance().error(-100, true, "Could not load image.");
	}
	
}