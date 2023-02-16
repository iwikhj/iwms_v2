package com.iwi.iwms.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class FilePolicy {
	
	// This method does not need to be synchronized because createNewFile()
	// is atomic and used here to mark when a file name is chosen
	public File renameByOrder(File f) {
		if (createNewFile(f)) {
			return f;
		}
		String name = f.getName();
		String body = null;
		String ext = null;

		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			body = name.substring(0, dot);
			ext = name.substring(dot); // includes "."
		} else {
			body = name;
			ext = "";
		}

		// Increase the count until an empty spot is found.
		// Max out at 9999 to avoid an infinite loop caused by a persistent
		// IOException, like when the destination dir becomes non-writable.
		// We don't pass the exception up because our job is just to rename,
		// and the caller will hit any IOException in normal processing.
		int count = 0;
		while (!createNewFile(f) && count < 9999) {
			count++;
			String newName = body + "(" + count + ")" + ext;
			f = new File(f.getParent(), newName);
		}

		return f;
	}

	public File renameByUUID(File f) {
		String name = f.getName();
		String body = UUID.randomUUID().toString().replace("-", "");
		String ext = null;

		int dot = name.lastIndexOf(".");
		if (dot != -1) {
			ext = name.substring(dot); // includes "."
		} else {
			ext = "";
		}
		
		String newName = body + ext;
		
		return new File(f.getParent(), newName);
	}
	
	private boolean createNewFile(File f) {
		try {
			return f.createNewFile();
		} catch (IOException ignored) {
			return false;
		}
	}
	
	public String md5DigestAsHex(File f) {
		BufferedInputStream fis = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(f));
			return DigestUtils.md5DigestAsHex(fis);// .sha256Hex(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return null;
	}
}
