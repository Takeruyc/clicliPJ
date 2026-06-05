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
            String videoDir = System.getProperty("user.dir") + "/uploads/videos/";
            File videoFolder = new File(videoDir);
            if (!videoFolder.exists()) videoFolder.mkdirs();
            String videoName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            file.transferTo(new File(videoDir + videoName));
            
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
    
    // ========== 新增：更新视频方法 ==========
    public void update(Long id, String title, String category, String description,
                       MultipartFile cover, MultipartFile video) throws Exception {
        
        Video existing = videoMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("视频不存在");
        }
        
        existing.setTitle(title);
        existing.setCategory(category);
        existing.setDescription(description);
        
        // 如果上传了新封面
        if (cover != null && !cover.isEmpty()) {
            String coverDir = System.getProperty("user.dir") + "/uploads/covers/";
            File coverFolder = new File(coverDir);
            if (!coverFolder.exists()) coverFolder.mkdirs();
            
            // 删除旧封面
            if (existing.getCoverPath() != null) {
                File oldCover = new File(System.getProperty("user.dir") + existing.getCoverPath());
                if (oldCover.exists()) oldCover.delete();
            }
            
            String coverName = UUID.randomUUID() + "_" + cover.getOriginalFilename();
            cover.transferTo(new File(coverDir + coverName));
            existing.setCoverPath("/uploads/covers/" + coverName);
        }
        
        // 如果上传了新视频
        if (video != null && !video.isEmpty()) {
            String videoDir = System.getProperty("user.dir") + "/uploads/videos/";
            File videoFolder = new File(videoDir);
            if (!videoFolder.exists()) videoFolder.mkdirs();
            
            // 删除旧视频
            if (existing.getVideoPath() != null) {
                File oldVideo = new File(System.getProperty("user.dir") + existing.getVideoPath());
                if (oldVideo.exists()) oldVideo.delete();
            }
            
            String videoName = UUID.randomUUID() + "_" + video.getOriginalFilename();
            video.transferTo(new File(videoDir + videoName));
            existing.setVideoPath("/uploads/videos/" + videoName);
        }
        
        videoMapper.update(existing);
    }
    // =====================================
    
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
    
    public List<Video> getVideosByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return findAll();
        }
        return videoMapper.findByCategory(category);
    }
}