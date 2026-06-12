package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.service.VideoService;

@Controller
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/video/{id}")
    public String play(@PathVariable Long id, Model model, HttpSession session) {
        Video video = videoService.findById(id);
        if (video == null) {
            return "redirect:/";
        }

        List<Video> recommendVideos = videoService.findAll()
                .stream()
                .filter(item -> !id.equals(item.getId()))
                .limit(4)
                .toList();

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("nickname", loginUser.getNickname());
        }

        model.addAttribute("video", video);
        model.addAttribute("recommendVideos", recommendVideos);
        return "video";
    }
    
}