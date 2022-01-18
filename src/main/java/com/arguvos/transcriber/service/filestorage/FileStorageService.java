package com.arguvos.transcriber.service.filestorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class FileStorageService {

    private final Path rootLocation;

    @Autowired
    public FileStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public void store(MultipartFile file) {
        storeWithName(file, file.getOriginalFilename());
    }

    public String storeWithUniqueName(MultipartFile file) {
        String uniqueFileName = UUID.randomUUID().toString();
        storeWithName(file, uniqueFileName);
        return uniqueFileName;
    }

    public void storeWithName(MultipartFile file, String name) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(name)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public void storeWithName(InputStream inputStream, String name) {
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            String filePath = this.rootLocation.resolve(Paths.get(name)).normalize().toAbsolutePath().toString();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            int inByte;
            while((inByte = bis.read()) != -1) bos.write(inByte);
            bis.close();
            bos.close();
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public File getFile(String filename) {
        return rootLocation.resolve(filename).toFile();
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}