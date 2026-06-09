package com.example.demo.mapper;

public interface VideoMapper {
<<<<<<< Updated upstream
    
=======
    void insert(Video video);
    List<Video> findAll();
    Video findById(@Param("id") Long id);
    void delete(@Param("id") Long id);
    List<Video> findByCategory(@Param("category") String category);
    void update(Video video); 
    List<Video> searchByTitle(String keyword);
	List<Video> findLatestVideos();
>>>>>>> Stashed changes
}