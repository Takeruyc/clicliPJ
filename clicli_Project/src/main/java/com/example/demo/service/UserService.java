package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;

@Service
public class UserService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[a-zA-Z0-9]{8,16}$");

    @Autowired
    private UserMapper userMapper;

    public boolean register(User user) {
        // 验证密码格式
        if (user.getPassword() == null || !PASSWORD_PATTERN.matcher(user.getPassword()).matches()) {
            return false;
        }
        
        if (userMapper.existsByEmail(user.getEmail()) > 0) {
            return false;
        }
        
        // 生成8位ID
        String shortId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        user.setId(shortId);
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

    public void deleteById(String id) {
        userMapper.deleteById(id);
    }
    
    public boolean isEmailExists(String email) {
        return userMapper.existsByEmail(email) > 0;
    }
}