package com.cjburkey.cubulus.render;

import org.lwjgl.opengl.GL11;

public class Renderer {
	
	public void init() {
		GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
	}
	
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
}