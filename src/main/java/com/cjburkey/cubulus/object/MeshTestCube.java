package com.cjburkey.cubulus.object;

import com.cjburkey.cubulus.render.Texture;

public final class MeshTestCube extends Mesh {
	
	private static final float[] verts = new float[] {
			-0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f,
			0.5f, -0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
			-0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, -0.5f,
			-0.5f, 0.5f, -0.5f,
			0.5f, 0.5f, -0.5f,
			-0.5f, 0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,
			0.5f, -0.5f, 0.5f,
			-0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f, 0.5f,
			-0.5f, -0.5f, -0.5f,
			0.5f, -0.5f, -0.5f,
			-0.5f, -0.5f, 0.5f,
			0.5f, -0.5f, 0.5f,
	};

	private static final float[] uvs = new float[] {
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			0.0f, 0.0f,
			0.5f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 0.5f,
			0.5f, 0.5f,
			0.0f, 1.0f,
			0.5f, 1.0f,
			0.0f, 0.0f,
			0.0f, 0.5f,
			0.5f, 0.0f,
			0.5f, 0.5f,
			0.5f, 0.0f,
			1.0f, 0.0f,
			0.5f, 0.5f,
			1.0f, 0.5f
	};
	
	private static final int[] inds = new int[] {
			0, 1, 3, 3, 1, 2,
			8, 10, 11, 9, 8, 11,
			12, 13, 7, 5, 12, 7,
			14, 15, 6, 4, 14, 6,
			16, 18, 19, 17, 16, 19,
			4, 6, 7, 5, 4, 7
	};

	public MeshTestCube() {
		super(verts, uvs, inds, new Texture("/texture/basic/stone.png"));
	}
	
}