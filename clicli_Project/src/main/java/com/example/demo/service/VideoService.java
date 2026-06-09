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

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/jpg", "image/gif");
    private static final List<String> ALLOWED_VIDEO_TYPES = List.of("video/mp4", "video/mpeg", "video/avi", "video/quicktime");

    @Autowired
    private VideoMapper videoMapper;

    private void validateFile(MultipartFile file, List<String> allowedTypes, String typeName) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException(typeName + "文件不能为空");
        }
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new RuntimeException(typeName + "文件格式不支持");
        }
    }

    private String sanitizeFilename(String filename) {
        if (filename == null) return "file";
        return filename.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    public void upload(Video video, MultipartFile file, MultipartFile cover) {
        try {
            validateFile(file, ALLOWED_VIDEO_TYPES, "视频");
            validateFile(cover, ALLOWED_IMAGE_TYPES, "封面");
            
            String videoDir = System.getProperty("user.dir") + "/uploads/videos/";
            File videoFolder = new File(videoDir);
            if (!videoFolder.exists()) videoFolder.mkdirs();
            String videoName = UUID.randomUUID() + "_" + sanitizeFilename(file.getOriginalFilename());
            file.transferTo(new File(videoDir + videoName));
            
            String coverDir = System.getProperty("user.dir") + "/uploads/covers/";
            File coverFolder = new File(coverDir);
            if (!coverFolder.exists()) coverFolder.mkdirs();
            String coverName = UUID.randomUUID() + "_" + sanitizeFilename(cover.getOriginalFilename());
            cover.transferTo(new File(coverDir + coverName));
            
            video.setVideoPath("/uploads/videos/" + videoName);
            video.setCoverPath("/uploads/covers/" + coverName);
            video.setCreatedAt(LocalDateTime.now());
            videoMapper.insert(video);
        } catch (Exception e) {
            throw new RuntimeException("上传失败: " + e.getMessage());
        }
    }
    
    public void update(Long id, String title, String category, String description,
                       MultipartFile cover, MultipartFile video) throws Exception {
        
        Video existing = videoMapper.findById(id);
        if (existing == null) {
            throw new RuntimeException("视频不存在");
        }
        
        existing.setTitle(title);
        existing.setCategory(category);
        existing.setDescription(description);
        
        if (cover != null && !cover.isEmpty()) {
            validateFile(cover, ALLOWED_IMAGE_TYPES, "封面");
            
            String coverDir = System.getProperty("user.dir") + "/uploads/covers/";
            File coverFolder = new File(coverDir);
            if (!coverFolder.exists()) coverFolder.mkdirs();

            if (existing.getCoverPath() != null) {
                File oldCover = new File(System.getProperty("user.dir") + existing.getCoverPath());
                if (oldCover.exists()) oldCover.delete();
            }
            
            String coverName = UUID.randomUUID() + "_" + sanitizeFilename(cover.getOriginalFilename());
            cover.transferTo(new File(coverDir + coverName));
            existing.setCoverPath("/uploads/covers/" + coverName);
        }

        if (video != null && !video.isEmpty()) {
            validateFile(video, ALLOWED_VIDEO_TYPES, "视频");
            
            String videoDir = System.getProperty("user.dir") + "/uploads/videos/";
            File videoFolder = new File(videoDir);
            if (!videoFolder.exists()) videoFolder.mkdirs();

            if (existing.getVideoPath() != null) {
                File oldVideo = new File(System.getProperty("user.dir") + existing.getVideoPath());
                if (oldVideo.exists()) oldVideo.delete();
            }
            
            String videoName = UUID.randomUUID() + "_" + sanitizeFilename(video.getOriginalFilename());
            video.transferTo(new File(videoDir + videoName));
            existing.setVideoPath("/uploads/videos/" + videoName);
        }
        
        videoMapper.update(existing);
    }
    
    public List<Video> findAll() {
        return videoMapper.findAll();
    }
    
    public List<Video> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return videoMapper.searchByTitle(keyword.trim());
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
        if (category == null || category.isEmpty() || "全部".equals(category)) {
            return findAll();
        }
        return videoMapper.findByCategory(category);
    }
    
    public List<Video> findLatestVideos() {
        return videoMapper.findLatestVideos();
    }
}