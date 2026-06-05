package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.Video;

@Mapper
public interface VideoMapper {
    void insert(Video video);
    List<Video> findAll();
    Video findById(@Param("id") Long id);
    void delete(@Param("id") Long id);
    List<Video> findByCategory(@Param("category") String category);
}