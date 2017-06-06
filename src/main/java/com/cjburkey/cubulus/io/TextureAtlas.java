package com.cjburkey.cubulus.io;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.joml.Vector2i;
import com.cjburkey.cubulus.Cubulus;
import com.cjburkey.cubulus.block.Block;
import com.cjburkey.cubulus.render.Texture;
import com.cjburkey.cubulus.resource.ResourceHandler;

public class TextureAtlas {

	private final int size = 64;
	private final Map<Block, Vector2i> inAtlas;
	private final Block[] blocks;
	private int width = 0;
	private Texture texture;
	
	public TextureAtlas(Block[] blocks) {
		this.blocks = blocks.clone();
		inAtlas = new HashMap<>();
	}
	
	public void init() {
		try {
			Cubulus.info("Building texture atlas...");
			while(Math.pow(width, 2) < blocks.length) {
				width ++;
			}
			final int aSize = size * width;
			Cubulus.info("  Size: " + aSize + "x" + aSize);
			BufferedImage img = new BufferedImage(aSize, aSize, BufferedImage.TYPE_INT_RGB);
			addToAtlas(img.createGraphics());
			writeToFile(img);
			Cubulus.info("Finished texture atlas.");
		} catch(Exception e) {
			Cubulus.getInstance().error(-903, true, e);
		}
	}
	
	public void cleanup() {
		if(!getTempFile().delete()) {
			getTempFile().deleteOnExit();
		}
	}
	
	private void addToAtlas(Graphics2D g) throws Exception {
		int i = 0;
		Cubulus.info("  Adding blocks to atlas...");
		for(int x = 0; x < width; x ++) {
			for(int y = 0; y < width; y ++) {
				inAtlas.put(blocks[i], new Vector2i(x, y));
				writeImageToAtlas(g, blocks[i].getTextureLocation(), x, y);
				i ++;
				if(i >= blocks.length) return;
			}
		}
		Cubulus.info("  Added blocks to atlas.");
	}
	
	private void writeImageToAtlas(Graphics2D g, String loc, int x, int y) throws Exception {
		BufferedImage at = ImageIO.read(ResourceHandler.getInstance().getStream(loc));
		if(at != null && at.getWidth() == size && at.getHeight() == size) {
			g.drawImage(at, x * size, y * size, null);
		} else {
			Cubulus.info("  Skipping image: " + loc + ". It was either missing or the wrong size. It should be " + size + "x" + size + ".");
		}
	}
	
	private void writeToFile(BufferedImage img) {
		Cubulus.info("  Writing atlas to file...");
		try {
			if(getTempFile().exists()) {
				getTempFile().delete();
			}
			ImageIO.write(img, "png", getTempFile());
			Cubulus.info("  Wrote atlas to file.");
		} catch (Exception e) {
			Cubulus.getInstance().error(-902, true, "Could not write texture atlas to file.");
		}
	}
	
	public Texture getTexture() {
		if(texture == null) {
			try {
				texture = new Texture(getTempFile().getAbsolutePath(), getTempFile().toURI().toURL().openStream());
			} catch (Exception e) {
				Cubulus.getInstance().error(-901, true, "Could not create texture for texture atlas.");
			}
		}
		return texture;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Vector2i forBlock(Block block) {
		return inAtlas.get(block);
	}
	
	private File getTempFile() {
		return new File(Dirs.getTmpImgDir(), "/atlas.png");
	}
	
}