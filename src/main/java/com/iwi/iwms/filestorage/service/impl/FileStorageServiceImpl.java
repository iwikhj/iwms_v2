package com.iwi.iwms.filestorage.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.iwi.iwms.api.common.errors.CommonException;
import com.iwi.iwms.api.common.errors.ErrorCode;
import com.iwi.iwms.filestorage.service.FileStorageService;
import com.iwi.iwms.utils.FilePolicy;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
	
	private Path storagePath;
	
    @Value("${storage.file.path}")
    private void setUploadPath(String uploadPath) throws IOException {
		this.storagePath = Paths.get(uploadPath).toAbsolutePath();
		//this.rootPath = Paths.get(System.getProperty("user.dir")).resolve(uploadPath.substring(1)).toAbsolutePath());
		if (!Files.exists(this.storagePath)) {
    		Files.createDirectories(this.storagePath);
    	}
    }
	
	@Override
	public File store(MultipartFile multipartFile, Path path) {
        try {
        	Path forder = this.storagePath.resolve(path).normalize();
        	if (!Files.exists(forder)) {
        		Files.createDirectories(forder);
        	}
        	
        	String filename = FilePolicy.rename(multipartFile.getOriginalFilename());
        	
        	Path target = forder.resolve(filename);
        	multipartFile.transferTo(target);
        	
        	return target.toFile();
        	
        } catch (IOException e) {
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "파일 스토리지 오류. " + e.getMessage());
        }
	}
	
	@Override
	public Path move(Path source, Path target) {
		try {
        	source = this.storagePath.resolve(source).normalize();
        	if (!Files.exists(source)) {
            	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "원본 파일이 존재하지 않거나 확인할 수 없습니다.");
        	}
        	
        	target = this.storagePath.resolve(target).normalize();
        	
        	return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        	
        } catch (IOException e) {
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "파일 스토리지 오류. " + e.getMessage());
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
		Path target = this.storagePath.resolve(path).normalize();
    	if (!Files.exists(target)) {
    		throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "파일이 존재하지 않거나 확인할 수 없습니다.");
    	}
    	
    	try {
			Resource resource = new UrlResource(target.toUri());
	        if(resource.exists() || resource.isReadable()) {
	            return resource;
	        } else {
            	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "파일 리소스가 존재하지 않거나 읽을 수 없습니다.");
	        }
		} catch (MalformedURLException e) {
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "파일 스토리지 오류. " + e.getMessage());
		}
	}
	
	@Override
	public void delete(final Path path) {
		Path target = this.storagePath.resolve(path).normalize();
    	if (Files.exists(target)) {
			try {
				Files.delete(target);
	        } catch (IOException e) {
	        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "파일 스토리지 오류. " + e.getMessage());
	        }		
    	} else {
    		//throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "파일이 존재하지 않거나 확인할 수 없습니다.");
    	}
	}
	
	@Override
	public void deleteAll(final Path path) {
		Path target = this.storagePath.resolve(path).normalize();
    	if (Files.exists(target)) {
			try {
				Files.walk(target)
					.sorted(Comparator.reverseOrder())
					.map(Path::toFile)
					.forEach(File::delete);
	        } catch (IOException e) {
	        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "파일 스토리지 오류. " + e.getMessage());
	        }		
    	} else {
    		//throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "디렉토리가 존재하지 않거나 확인할 수 없습니다.");
    	}
	}
}
