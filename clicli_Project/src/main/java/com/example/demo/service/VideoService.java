package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class VideoService {
    
<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
}