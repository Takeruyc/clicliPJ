package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private VideoService videoService;
    
    @GetMapping("/")
    public String index(@RequestParam(required = false) String category, Model model, HttpSession session) {
        // 获取视频列表（支持分类筛选）
        List<Video> videos;
        if (category == null || category.isEmpty() || "全部".equals(category)) {
            videos = videoService.findAll();
        } else {
            videos = videoService.getVideosByCategory(category);
        }
        model.addAttribute("videos", videos);
        model.addAttribute("currentCategory", category);
        
        // 获取登录状态
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("loggedIn", true);
            model.addAttribute("nickname", loginUser.getNickname());
            model.addAttribute("userRole", loginUser.getRole());
        } else {
            model.addAttribute("loggedIn", false);
        }
        
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        boolean success = userService.register(user);
        if (success) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "邮箱已存在");
            return "register";
        }
    }
    
    @PostMapping("/login")
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpSession session,
                            Model model) {
        User loginUser = userService.login(email, password);
        if (loginUser == null) {
            model.addAttribute("error", "邮箱或密码错误");
            return "login";
        }
        session.setAttribute("loginUser", loginUser);
        return "redirect:/";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}