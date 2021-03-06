/*
 * This file is part of Zener.
 *
 * Zener is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Zener is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with Zener.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.n8lm.zener.data;

import java.io.*;
import java.net.URL;

/**
 * A resource loading location that searches somewhere on the classpath
 * 
 * @author kevin
 */
public class FileSystemLocation implements ResourceLocation {
	/** The root of the file system to search */
	private File root;
	
	/**
	 * Create a new resoruce location based on the file system
	 * 
	 * @param root The root of the file system to search
	 */
	public FileSystemLocation(File root) {
		this.root = root;
	}
	
	/**
	 * @see ResourceLocation#getResource(String)
	 */
	public URL getResource(String ref) {
		try {
			File file = new File(root, ref);
			if (!file.exists()) {
				file = new File(ref);
			}
			if (!file.exists()) {
				return null;
			}
			
			return file.toURI().toURL();
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * @see ResourceLocation#getResourceAsStream(String)
	 */
	public InputStream getResourceAsStream(String ref) {
		try {
			File file = new File(root, ref);
			if (!file.exists()) {
				file = new File(ref);
			}
			return new FileInputStream(file);
		} catch (IOException e) {
			return null;
		}
	}

}