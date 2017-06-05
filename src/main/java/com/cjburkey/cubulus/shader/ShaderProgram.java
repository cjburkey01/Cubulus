package com.cjburkey.cubulus.shader;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import com.cjburkey.cubulus.Cubulus;

public class ShaderProgram {
	
	private final HashMap<String, Integer> uniforms;
	private final int program;
	private int vertexShader;
	private int fragmentShader;
	
	public ShaderProgram() {
		uniforms = new HashMap<>();
		program = GL20.glCreateProgram();
		if(program == 0) {
			Cubulus.getInstance().error(-3, true, "Could not create shader program.");
		}
	}
	
	public void createVertex(String code) {
		vertexShader = createShader(code, GL20.GL_VERTEX_SHADER);
	}
	
	public void createFragment(String code) {
		fragmentShader = createShader(code, GL20.GL_FRAGMENT_SHADER);
	}
	
	public void link() {
		GL20.glLinkProgram(program);
		if(GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) {
			Cubulus.getInstance().error(-6, true, "Could not link program: " + GL20.glGetProgramInfoLog(program, 1024));
		}
		if(vertexShader != 0) {
			GL20.glDetachShader(program, vertexShader);
		}
		if(fragmentShader != 0) {
			GL20.glDetachShader(program, fragmentShader);
		}
		GL20.glValidateProgram(program);
		if(GL20.glGetProgrami(program, GL20.GL_VALIDATE_STATUS) == 0) {
			Cubulus.getInstance().error(-7, false, "Could not validate program: " + GL20.glGetProgramInfoLog(program, 1024));
			return;
		}
	}
	
	public void createUniform(String name) {
		int loc = GL20.glGetUniformLocation(program, name);
		if(loc < 0) {
			Cubulus.getInstance().error(-8, true, "Could not create uniform: " + name);
		}
		uniforms.put(name, loc);
	}
	
	public void setUniform(String name, Matrix4f value) {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			GL20.glUniformMatrix4fv(uniforms.get(name), false, fb);
		}
	}
	
	public void bind() {
		GL20.glUseProgram(program);
	}
	
	public void unbind() {
		GL20.glUseProgram(0);
	}
	
	public void cleanup() {
		unbind();
		if(program != 0) {
			GL20.glDeleteProgram(program);
		}
	}
	
	protected int createShader(String code, int type) {
		int id = GL20.glCreateShader(type);
		if(id == 0) {
			Cubulus.getInstance().error(-4, true, "Could not create shader.");
		}
		GL20.glShaderSource(id, code);
		GL20.glCompileShader(id);
		if(GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == 0) {
			Cubulus.getInstance().error(-5, true, "Could not compile shader: " + GL20.glGetShaderInfoLog(id, 1024));
		}
		GL20.glAttachShader(program, id);
		return id;
	}
	
}