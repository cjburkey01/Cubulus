package com.cjburkey.cubulus.render;

import java.io.InputStream;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.resource.ResourceHandler;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public final class Texture {
	
	private PNGDecoder decoder;
	private int texture;
	
	public Texture(String path) {
		this(path, ResourceHandler.getInstance().getStream(path));
	}
	
	public Texture(String path, InputStream stream) {
		try {
			decoder = new PNGDecoder(stream);
			Cubulus.getInstance().runLater(() -> {
				try {
					sendToGpu();
				} catch (Exception e) { error(path, e); }
			});
		} catch (Exception e) {
			error(path, e);
		}
	}
	
	public void cleanup() {
		GL11.glDeleteTextures(texture);
	}
	
	public int getTextureId() {
		return texture;
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
	
	private static void error(String image, Exception e) {
		Cubulus.getInstance().error(-100, true, "Could not load image: " + image);
	}
	
}