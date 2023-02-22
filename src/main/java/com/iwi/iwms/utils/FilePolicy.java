package com.iwi.iwms.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

public class FilePolicy {

	public static String rename(String filename) {
		String body = UUID.randomUUID().toString().replace("-", "");
		String ext = "";

		int dot = filename.lastIndexOf(".");
		if (dot != -1) {
			ext = filename.substring(dot); // includes "."
		}
		
		return body + ext;
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
