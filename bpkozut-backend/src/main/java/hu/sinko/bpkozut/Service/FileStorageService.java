package hu.sinko.bpkozut.Service;

import hu.sinko.bpkozut.Exception.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;
    private final String UPLOAD_DIRECTORY = "../bpkozut-frontend/src/assets/images";


    @Autowired
    public FileStorageService() {
        this.fileStorageLocation = Paths.get(this.UPLOAD_DIRECTORY).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeImage(MultipartFile file) {
        if (!Objects.requireNonNull(file.getContentType()).contains("image")) {
            throw new RuntimeException("Incorrect file format!");
        }
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
        String concatFilename = file.getOriginalFilename();
        if (concatFilename == null || concatFilename.isEmpty()) {
            concatFilename = "";
            concatFilename += LocalDateTime.now().toString() + "jpg";
        }
        String fileName = StringUtils.cleanPath(concatFilename);
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName, ex);
        }
    }

    public void deleteImage(String image) {
        try {
            Path filePath = Paths.get(this.fileStorageLocation + "/" + image).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                File file = new File(filePath.toString());
                file.delete();

            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + image, ex);
        }
    }

    public String updateImage(MultipartFile newImage, String oldFilename) throws IOException {
        String newFilename = this.storeImage(newImage);
        deleteImage(oldFilename);
        return newFilename;
    }

}
