package com.purewave.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class AudioService {
    private static final String DRY_AUDIO_PATH = "uploads-audio/dry/";
    private static final String WET_AUDIO_PATH = "uploads-audio/wet/";
    private static final String FLASK_API_URL = "http://localhost:5000/audio";

    public String saveAudio(MultipartFile file) throws IOException {
        // Ensure the upload directories exist
        Path dryPath = Paths.get(DRY_AUDIO_PATH);
        Path wetPath = Paths.get(WET_AUDIO_PATH);

        if (!Files.exists(dryPath)) {
            Files.createDirectories(dryPath);
        }

        if (!Files.exists(wetPath)) {
            Files.createDirectories(wetPath);
        }

        // Generate a unique filename
        String id = String.valueOf(System.currentTimeMillis());
        String originalFilenameDry = Objects.requireNonNull(file.getOriginalFilename());
        String uniqueFilenameDry = id + "_" + originalFilenameDry;

        // Save dry audio file locally
        Path dryFilePath = dryPath.resolve(uniqueFilenameDry);
        Files.copy(file.getInputStream(), dryFilePath, StandardCopyOption.REPLACE_EXISTING);

        // Send the file to Flask API for enhancement
        byte[] wetAudioData = sendToFlask(dryFilePath);

        // Save wet audio file locally
        String uniqueFilenameWet = "enhanced_" + id + "_" + originalFilenameDry;
        Path wetFilePath = wetPath.resolve(uniqueFilenameWet);
        Files.write(wetFilePath, wetAudioData);

        // Return the unique filenames for both dry and wet files
        return uniqueFilenameDry + "," + uniqueFilenameWet;
    }

    private byte[] sendToFlask(Path dryFilePath) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("audio", new FileSystemResource(dryFilePath.toFile()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(FLASK_API_URL, HttpMethod.POST, requestEntity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new IOException("Failed to enhance audio: " + response.getStatusCode());
        }
    }

    public String getAudioDry() {
        return getLatestFilePath(DRY_AUDIO_PATH);
    }

    public String getAudioWet() {
        return getLatestFilePath(WET_AUDIO_PATH);
    }

    private String getLatestFilePath(String directoryPath) {
        try {
            Path dirPath = Paths.get(directoryPath);

            if (!Files.exists(dirPath)) {
                return "Directory does not exist: " + directoryPath;
            }

            return Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .sorted((file1, file2) -> {
                        try {
                            return Files.getLastModifiedTime(file2).compareTo(Files.getLastModifiedTime(file1));
                        } catch (Exception e) {
                            return 0;
                        }
                    })
                    .map(Path::toString)
                    .findFirst()
                    .map(filePath -> "http://localhost:8080/audio/files/" + Paths.get(filePath).getFileName().toString())  // Return a URL
                    .orElse("No audio files found in: " + directoryPath);

        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return "Error retrieving files: " + e.getMessage();
        }
    }

}
