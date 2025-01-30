package com.purewave.controller;

import com.purewave.service.AudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/audio")
public class AudioController {
    @Autowired
    private AudioService audioService;

    @PostMapping
    public String uploadAudio(@RequestParam("audio") MultipartFile file) {
        try {
            return audioService.saveAudio(file);
        } catch (IOException e) {
            return "Failed to save audio: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/dry")
    public String getAudioDry(){
        return audioService.getAudioDry();
    }

    @GetMapping("/wet")
    public String getAudioWet(){
        return audioService.getAudioWet();
    }
}
