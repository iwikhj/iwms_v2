package com.iwi.iwms.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class FilePolicy {
	
	 /**
     * File rename (UUID without hyphen)
     */
	public static String rename(String filename) {
		String body = UUID.randomUUID().toString().replace("-", "");
		String ext = "";

		int dot = filename.lastIndexOf(".");
		if (dot != -1) {
			ext = filename.substring(dot); // includes "."
		}
		
		return body + ext;
	}
	
    /**
     * File Checksum (MD5)
     */
	public String md5Checksum(File f) throws IOException, NoSuchAlgorithmException {
		Path path = Paths.get(f.getPath());
		byte[] bytes = Files.readAllBytes(path);
		return digest(bytes, Algorithm.MD5);
	}
	
    /**
     * File Checksum (SHA256)
     */
	public String sha256Checksum(File f) throws IOException, NoSuchAlgorithmException {
		Path path = Paths.get(f.getPath());
		byte[] bytes = Files.readAllBytes(path);
		return digest(bytes, Algorithm.SHA256);
	}
	
	private String digest(byte[] bytes, Algorithm algorithm) throws IOException, NoSuchAlgorithmException {
		byte[] hash = MessageDigest.getInstance(algorithm.value).digest(bytes);
		
		//return bytesToHex(hash);
		return new BigInteger(1, hash).toString(16);
	}
	
	private String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b: bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
    }
	
	private enum Algorithm {
		MD5("MD5"),
		SHA256("SHA-256")
		;
		
		private String value;
		
		private Algorithm(String value) {
			this.value = value;
		}
	};
}
