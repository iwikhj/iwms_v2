package com.iwi.iwms.filestorage.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.stream.Stream;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.iwi.iwms.filestorage.FileStorageResponse;
import com.iwi.iwms.filestorage.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
	
	private final Tika tika = new Tika();
	
	private Path rootPath;
	
    @Value("${file.storage.root-path}")
    private void setRootPath(String path) {
		this.rootPath = Paths.get(path).toAbsolutePath();
        try {
        	if (!Files.exists(this.rootPath)) {
        		Files.createDirectories(this.rootPath);
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
	
	@Override
	public FileStorageResponse store(MultipartFile multipartFile, Path path, String filename) {
        try {
        	Path forder = rootPath.resolve(path).normalize();
        	if (!Files.exists(forder)) {
        		Files.createDirectories(forder);
        	}
        	
        	String originalFilename = multipartFile.getOriginalFilename();
        	
        	Path target = forder.resolve(filename);
        	File file = target.toFile();
        	
        	multipartFile.transferTo(file);
        	
          	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
        	return FileStorageResponse.builder()
        		.originalFilename(originalFilename)
        		.filename(filename)
        		.link("/iwms/file/link/" + path.toString().replace("\\", "/") + "/" + filename)
        		.type(tika.detect(file))
        		.size(file.length())
        		.lastModified(sdf.format(file.lastModified()))
        		.build();
        	
        } catch (IOException e) {
    		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
	}
	
	@Override
	public Path move(Path source, Path target) {
		try {
        	source = rootPath.resolve(source).normalize();
        	if (!Files.exists(source)) {
        		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Source file not found");
        	}
        	
        	target = rootPath.resolve(target).normalize();
        	
        	return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        	
        } catch (IOException e) {
    		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
	}
	
	@Override
	public Stream<Path> loadAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Path load(final Path path, final String filename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource loadAsResource(final Path path) {
		Path target = rootPath.resolve(path).normalize();
    	if (!Files.exists(target)) {
    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found.");
    	}
    	
    	try {
			Resource resource = new UrlResource(target.toUri());
	        if(resource.exists() || resource.isReadable()) {
	            return resource;
	        } else {
	    		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not read file.");

	        }
		} catch (MalformedURLException e) {
    		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

		}
    	//return new InputStreamResource(getClass().getResourceAsStream(target.toString()));
	}
	
	@Override
	public void delete(final Path path) {
		Path target = rootPath.resolve(path).normalize();
    	if (Files.exists(target)) {
			try {
				Files.delete(target);
	        } catch (IOException e) {
	    		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	        }		
    	} else {
    		//throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The file does not exist. " + target.toString());
    	}
	}
	
	@Override
	public void deleteAll(final Path path) {
		Path target = rootPath.resolve(path).normalize();
    	if (Files.exists(target)) {
			try {
				Files.walk(target)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
	        } catch (IOException e) {
	    		throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	        	
	        }		
    	} else {
    		//throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The directory does not exist. " + target.toString());
    	}
	}
}
