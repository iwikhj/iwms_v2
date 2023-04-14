package com.iwi.iwms.filestorage.transaction;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.util.StringUtils;

public class FileTransactionListener implements TransactionSynchronization {
	   
	private List<File> files;
	
	public FileTransactionListener() {
		this.files = new ArrayList<>();
	}
	
	public void add(File file) {
		this.files.add(file);
	}
	
	@Override
	public void afterCompletion(int status) {
		if(STATUS_COMMITTED != status) {
			AtomicReference<String> folderPath = new AtomicReference<String>();
			
			files.stream()
				.forEach(v -> {
					if(v.exists()) {
						if(folderPath.get() == null) {
							folderPath.set(v.getParent());
						}
						v.delete();
					}
				});
			
			if(folderPath.get() != null && StringUtils.hasText(folderPath.get())) {
				try {
					Files.walk(Paths.get(folderPath.get()))
						.sorted(Comparator.reverseOrder())
						.findFirst()
						.filter(v -> v.toString().equals(folderPath.get()))
						.map(Path::toFile)
						.ifPresent(File::delete);
		        } catch (IOException e) {
		        	e.printStackTrace();
		        }				
			}
		}
	}
}
