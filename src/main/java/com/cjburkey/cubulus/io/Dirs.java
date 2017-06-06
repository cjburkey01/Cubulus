package com.cjburkey.cubulus.io;

import java.io.File;

public final class Dirs {
	
	public static File getGameDir() {
		return new File(System.getProperty("user.home"), "/Cubulus/");
	}
	
	public static File getTmpDir() {
		return new File(getGameDir(), "/tmp/");
	}
	
	public static File getTmpImgDir() {
		return new File(getTmpDir(), "/img/");
	}
	
	public static void init() {
		if(!getGameDir().exists()) getGameDir().mkdirs();
		if(!getTmpDir().exists()) getTmpDir().mkdirs();
		if(!getTmpImgDir().exists()) getTmpImgDir().mkdirs();
	}
	
	public static void cleanup() {
		deleteDir(getTmpDir());
	}
	
	private static void deleteDir(File dir) {
		if(!dir.isDirectory()) return;
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				deleteDir(f);
			} else {
				f.delete();
			}
		}
		dir.delete();
	}
	
}