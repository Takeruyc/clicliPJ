package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean register(User user) {
        if (userMapper.existsByEmail(user.getEmail()) > 0) {
            return false;
        }
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setRole("user");
        user.setCreatedAt(new Date());
        userMapper.insertUser(user);
        return true;
    }
    
    public User login(String email, String password) {
        User user = userMapper.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }
    
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }
}