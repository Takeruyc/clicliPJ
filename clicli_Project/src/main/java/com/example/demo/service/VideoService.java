package com.example.demo.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.mapper.VideoMapper;
import com.example.demo.model.Video;

@Service
public class VideoService {

    @Autowired
    private VideoMapper videoMapper;

    public void upload(Video video, MultipartFile file, MultipartFile cover) {
        try {
            // 保存视频
            String videoDir = System.getProperty("user.dir") + "/uploads/videos/";
            File videoFolder = new File(videoDir);
            if (!videoFolder.exists()) videoFolder.mkdirs();
            String videoName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(new File(videoDir + videoName));
            
            // 保存封面
            String coverDir = System.getProperty("user.dir") + "/uploads/covers/";
            File coverFolder = new File(coverDir);
            if (!coverFolder.exists()) coverFolder.mkdirs();
            String coverName = UUID.randomUUID() + "_" + cover.getOriginalFilename();
            cover.transferTo(new File(coverDir + coverName));
            
            video.setVideoPath("/uploads/videos/" + videoName);
            video.setCoverPath("/uploads/covers/" + coverName);
            video.setCreatedAt(LocalDateTime.now());
            videoMapper.insert(video);
        } catch (Exception e) {
            throw new RuntimeException("上传失败: " + e.getMessage());
        }
    }
    
    public List<Video> findAll() {
        return videoMapper.findAll();
    }
    
    public Video findById(Long id) {
        return videoMapper.findById(id);
    }
    
    public void delete(Long id) {
        Video video = videoMapper.findById(id);
        if (video == null) return;
        
        if (video.getVideoPath() != null) {
            File f = new File(System.getProperty("user.dir") + video.getVideoPath());
            if (f.exists()) f.delete();
        }
        if (video.getCoverPath() != null) {
            File f = new File(System.getProperty("user.dir") + video.getCoverPath());
            if (f.exists()) f.delete();
        }
        videoMapper.delete(id);
    }
    
    // 按分类获取视频列表
    public List<Video> getVideosByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return findAll();
        }
        return videoMapper.findByCategory(category);
    }
}