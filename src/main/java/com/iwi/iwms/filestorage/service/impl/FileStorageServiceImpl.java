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

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
	
	@Value("${app.root}/${app.version}")
	private String root;
	
	private final Tika tika = new Tika();
	
	private Path uploadPath;
	
    @Value("${file.storage.upload-path}")
    private void setRootPath(String uploadPath) throws IOException {
		this.uploadPath = Paths.get(uploadPath).toAbsolutePath();
		//this.rootPath = Paths.get(System.getProperty("user.dir")).resolve(uploadPath.substring(1)).toAbsolutePath());
		if (!Files.exists(this.uploadPath)) {
    		Files.createDirectories(this.uploadPath);
    	}
    }
	
	@Override
	public FileStorageResponse store(MultipartFile multipartFile, Path path, String filename) {
        try {
        	Path forder = uploadPath.resolve(path).normalize();
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
        	throw new CommonException(ErrorCode.INTERNAL_SERIVCE_ERROR, "파일 스토리지 오류. " + e.getMessage());
        }
	}
	
	@Override
	public Path move(Path source, Path target) {
		try {
        	source = uploadPath.resolve(source).normalize();
        	if (!Files.exists(source)) {
            	throw new CommonException(ErrorCode.RESOURCES_NOT_EXISTS, "원본 파일이 존재하지 않거나 확인할 수 없습니다.");
        	}
        	
        	target = uploadPath.resolve(target).normalize();
        	
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
		Path target = uploadPath.resolve(path).normalize();
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
		Path target = uploadPath.resolve(path).normalize();
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
		Path target = uploadPath.resolve(path).normalize();
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
