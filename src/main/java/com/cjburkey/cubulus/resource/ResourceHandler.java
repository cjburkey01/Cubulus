package com.cjburkey.cubulus.resource;

import java.io.InputStream;

public class ResourceHandler {
	
	private static ResourceHandler instance;
	
	public ResourceHandler() {
		instance = this;
	}
	
	public InputStream getStream(String domain, String path) {
		ResourceLocation loc = new ResourceLocation(domain, path);
		return loc.getStream();
	}
	
	public String getDomain(String location) {
		return new ResourceLocation(location).getDomain();
	}
	
	public String getPath(String location) {
		return new ResourceLocation(location).getPath();
	}
	
	public String getLocation(String domain, String path) {
		return new ResourceLocation(domain, path).toString();
	}
	
	public InputStream getStream(String location) {
		return new ResourceLocation(location).getStream();
	}
	
	public static ResourceHandler getInstance() {
		return instance;
	}
	
	private static class ResourceLocation {
		private String domain;
		private String path;
		
		public ResourceLocation(String domain, String path) {
			this.domain = domain;
			this.path = path;
		}
		
		public ResourceLocation(String loc) {
			String[] split = loc.split(":");
			if(split.length == 2) {
				domain = split[0];
				path = split[1];
			}
		}
		
		public String getDomain() {
			return domain;
		}
		
		public String getPath() {
			return path;
		}
		
		public String toString() {
			return domain + ":" + path;
		}
		
		public InputStream getStream() {
			return getClass().getResourceAsStream("/" + domain + "/" + path);
		}
		
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((domain == null) ? 0 : domain.hashCode());
			result = prime * result + ((path == null) ? 0 : path.hashCode());
			return result;
		}
		
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ResourceLocation other = (ResourceLocation) obj;
			if (domain == null) {
				if (other.domain != null)
					return false;
			} else if (!domain.equals(other.domain))
				return false;
			if (path == null) {
				if (other.path != null)
					return false;
			} else if (!path.equals(other.path))
				return false;
			return true;
		}
	}
	
}