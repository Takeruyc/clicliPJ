package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.Video;
import com.example.demo.model.User;
import com.example.demo.service.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;
    
    @GetMapping("/video/{id}")
    public String play(@PathVariable Long id, Model model, HttpSession session) {
        Video video = videoService.findById(id);
        model.addAttribute("video", video);
        
        // 获取昵称用于页面显示
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }
        
        return "video";
    }
}