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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.filestorage.FileStorageResponse;
import com.iwi.iwms.filestorage.service.FileStorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
	
	@Value("${app.root}/${app.version}")
	private String root;
	
	private final Tika tika = new Tika();
	
	private Path rootPath;
	
    @Value("${file.storage.root-path}")
    private void setRootPath(String rootPath) {
		this.rootPath = Paths.get(rootPath).toAbsolutePath();
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
        	
        	multipartFile.transferTo(target);
        	
          	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		
        	return FileStorageResponse.builder()
        		.originalFilename(originalFilename)
        		.filename(filename)
        		.link(root + "/files/link/" + path.toString().replace("\\", "/") + "/" + filename)
        		.type(tika.detect(file))
        		.size(file.length())
        		.lastModified(sdf.format(file.lastModified()))
        		.build();
        	
        } catch (IOException e) {
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "File storage");
        }
	}
	
	@Override
	public Path move(Path source, Path target) {
		try {
        	source = rootPath.resolve(source).normalize();
        	if (!Files.exists(source)) {
            	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "원본 파일이 존재하지 않거나 확인할 수 없습니다.");
        	}
        	
        	target = rootPath.resolve(target).normalize();
        	
        	return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        	
        } catch (IOException e) {
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "File storage");
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
    		throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "파일이 존재하지 않거나 확인할 수 없습니다.");
    	}
    	
    	try {
			Resource resource = new UrlResource(target.toUri());
	        if(resource.exists() || resource.isReadable()) {
	            return resource;
	        } else {
            	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "파일을 읽을 수 없습니다.");
	        }
		} catch (MalformedURLException e) {
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "File storage");

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
	        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "File storage");
	        }		
    	} else {
    		//throw new CommonException(ErrorCode.TARGET_DATA_NOT_EXISTS, "파일이 존재하지 않거나 확인할 수 없습니다.");
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
	        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "File storage");
	        	
	        }		
    	} else {
    		//throw new CommonException(ErrorCode.TARGET_DATA_NOT_EXISTS, "디렉토리가 존재하지 않거나 확인할 수 없습니다.");
    	}
	}
}
