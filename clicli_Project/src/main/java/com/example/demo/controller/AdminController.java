package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.service.UserService;
import com.example.demo.service.VideoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private VideoService videoService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String adminId,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        if ("admin".equals(adminId) && "admin123".equals(password)) {
            // 创建管理员 User 对象存入 Session
            User adminUser = new User();
            adminUser.setId("admin001");
            adminUser.setNickname("系统管理员");
            adminUser.setRole("admin");
            
            session.setAttribute("loginUser", adminUser);
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("error", "账号或密码错误");
            return "admin/login";
        }
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // 检查是否管理员登录
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            return "redirect:/admin/login";
        }
        
        List<Video> videos = videoService.findAll();
        List<User> users = userService.getAllUsers();
        model.addAttribute("videos", videos);
        model.addAttribute("users", users);
        model.addAttribute("currentAdminId", loginUser.getId());
        return "admin/dashboard";
    }
    
    @PostMapping("/video/add")
    public String addVideo(@RequestParam String title,
                           @RequestParam String category,
                           @RequestParam(required = false) String description,
                           @RequestParam("cover") MultipartFile cover,
                           @RequestParam("video") MultipartFile video,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        // 检查是否管理员登录
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            return "redirect:/admin/login";
        }
        
        Video newVideo = new Video();
        newVideo.setTitle(title);
        newVideo.setCategory(category);
        newVideo.setDescription(description);
        try {
            videoService.upload(newVideo, video, cover);
            redirectAttributes.addFlashAttribute("successMsg", "视频上传成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/admin/dashboard";
    }
    
    @GetMapping("/video/delete/{id}")
    public String deleteVideo(@PathVariable Long id, 
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        // 检查是否管理员登录
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            return "redirect:/admin/login";
        }
        
        videoService.delete(id);
        redirectAttributes.addFlashAttribute("successMsg", "视频删除成功！");
        return "redirect:/admin/dashboard";
    }
}