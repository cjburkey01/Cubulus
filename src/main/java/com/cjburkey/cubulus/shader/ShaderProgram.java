package com.cjburkey.cubulus.shader;

import org.lwjgl.opengl.GL20;

public class ShaderProgram {
	
	private final int id;
	private int vertexId;
	private int fragmentId;
	
	public ShaderProgram() {
		id = GL20.glCreateProgram();
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		vertexId = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentId = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}
	
	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = GL20.glCreateShader(shaderType);
		if(shaderId == 0) {
			throw new Exception("Error creating shader. Type: " + shaderType);
		}
		
		GL20.glShaderSource(shaderId, shaderCode);
		GL20.glCompileShader(shaderId);
		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + GL20.glGetShaderInfoLog(shaderId, 1024));
		}
		
		GL20.glAttachShader(id, shaderId);
		return shaderId;
	}
	
	public void link() throws Exception {
		GL20.glLinkProgram(id);
		if(GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + GL20.glGetProgramInfoLog(id, 1024));
		}
		
		if (vertexId != 0) {
			GL20.glDetachShader(id, vertexId);
		}
		
		if (fragmentId != 0) {
			GL20.glDetachShader(id, fragmentId);
		}
		
		GL20.glValidateProgram(id);
		if(GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + GL20.glGetProgramInfoLog(id, 1024));
		}
	}
	
	public void bind() {
		GL20.glUseProgram(id);
	}
	
	public void cleanup() {
		unbind();
		if(id != 0) {
			GL20.glDeleteProgram(id);
		}
	}
	
	// -- STATIC -- //
	
	public static void unbind() {
		GL20.glUseProgram(0);
	}
	
}