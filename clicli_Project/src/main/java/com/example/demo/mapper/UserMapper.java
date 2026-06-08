package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.model.User;
import java.util.List;

@Mapper
public interface UserMapper {
    void insertUser(User user);
    User findByEmail(@Param("email") String email);
    int existsByEmail(@Param("email") String email);
    List<User> findAll();
    void deleteById(@Param("id") String id);
}