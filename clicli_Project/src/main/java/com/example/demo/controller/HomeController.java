package com.example.demo.controller;

import java.util.List;

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

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private VideoService videoService;
    
    @GetMapping("/")
    public String index(@RequestParam(required = false) String category, 
                        Model model, 
                        HttpSession session) {
        List<Video> videos;
        if (category == null || category.isEmpty() || "全部".equals(category)) {
            videos = videoService.findAll();
        } else {
            videos = videoService.getVideosByCategory(category);
        }
        
        model.addAttribute("videos", videos);
        model.addAttribute("currentCategory", category);

        // videos不为空时才设置推荐视频
        if (videos != null && !videos.isEmpty()) {
            model.addAttribute("video", videos.get(0));
        }

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("user", loginUser);
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
        // 密码格式验证
        if (user.getPassword() == null || !user.getPassword().matches("^[a-zA-Z0-9]{8,16}$")) {
            model.addAttribute("error", "密码必须是8-16位字母或数字");
            return "register";
        }
        
        boolean success = userService.register(user);
        if (success) {
            return "redirect:/login";
        }

        model.addAttribute("error", "邮箱已存在");
        return "register";
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