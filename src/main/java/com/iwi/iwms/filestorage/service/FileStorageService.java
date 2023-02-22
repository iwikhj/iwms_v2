package com.iwi.iwms.filestorage.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.filestorage.FileStorageResponse;

public interface FileStorageService {
	
	FileStorageResponse store(final MultipartFile multipartFile, final Path path, final String filename);

	Path move(final Path source, final Path target);
	
    Stream<Path> loadAll();

    Path load(final Path path, final String filename);

    Resource loadAsResource(final Path path);

    void delete(final Path path);
    
    void deleteAll(final Path path);
}
