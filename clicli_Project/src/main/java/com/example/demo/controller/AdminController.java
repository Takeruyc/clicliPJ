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

        User adminUser = userService.login(adminId + "@example.com", password);
        if (adminUser != null && "admin".equals(adminUser.getRole())) {
            session.setAttribute("loginUser", adminUser);
            return "redirect:/admin/dashboard";
        }
        
        if ("admin".equals(adminId) && "admin123".equals(password)) {
            User hardcodedAdmin = userService.login("admin@example.com", "admin123");
            if (hardcodedAdmin != null) {
                session.setAttribute("loginUser", hardcodedAdmin);
                return "redirect:/admin/dashboard";
            }
        }

        model.addAttribute("error", "账号或密码错误");
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) Long editId,
                            @RequestParam(defaultValue = "videos") String tab,
                            HttpSession session,
                            Model model) {
        String authRedirect = checkAdmin(session);
        if (authRedirect != null) {
            return authRedirect;
        }

        User loginUser = (User) session.getAttribute("loginUser");
        List<Video> videos = videoService.findAll();
        List<User> users = userService.getAllUsers();

        model.addAttribute("videos", videos);
        model.addAttribute("users", users);
        model.addAttribute("currentAdminId", loginUser.getId());
        model.addAttribute("activeTab", tab);

        if (editId != null) {
            Video editVideo = videoService.findById(editId);
            model.addAttribute("editVideo", editVideo);
        }

        return "admin/dashboard";
    }

    @GetMapping("/upload")
    public String uploadPage(HttpSession session) {
        String authRedirect = checkAdmin(session);
        if (authRedirect != null) {
            return authRedirect;
        }
        return "admin/upload";
    }

    @PostMapping("/video/add")
    public String addVideo(@RequestParam String title,
                           @RequestParam String category,
                           @RequestParam(required = false) String description,
                           @RequestParam("cover") MultipartFile cover,
                           @RequestParam("video") MultipartFile video,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        String authRedirect = checkAdmin(session);
        if (authRedirect != null) {
            return authRedirect;
        }

        if (cover == null || cover.isEmpty() || video == null || video.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "请同时选择封面图和视频文件！");
            return "redirect:/admin/upload";
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

    @PostMapping("/video/edit")
    public String editVideo(@RequestParam Long id,
                            @RequestParam String title,
                            @RequestParam String category,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) MultipartFile cover,
                            @RequestParam(required = false) MultipartFile video,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        String authRedirect = checkAdmin(session);
        if (authRedirect != null) {
            return authRedirect;
        }

        try {
            videoService.update(id, title, category, description, cover, video);
            redirectAttributes.addFlashAttribute("successMsg", "视频修改成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", e.getMessage());
        }

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/video/delete/{id}")
    public String deleteVideo(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        String authRedirect = checkAdmin(session);
        if (authRedirect != null) {
            return authRedirect;
        }

        videoService.delete(id);
        redirectAttributes.addFlashAttribute("successMsg", "视频删除成功！");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable String id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String authRedirect = checkAdmin(session);
        if (authRedirect != null) {
            return authRedirect;
        }

        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMsg", "管理员账户不可删除");
            return "redirect:/admin/dashboard?tab=users";
        }

        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMsg", "用户删除成功！");
        return "redirect:/admin/dashboard?tab=users";
    }

    private String checkAdmin(HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/admin/login";
        }
        if (!"admin".equals(loginUser.getRole())) {
            return "redirect:/";
        }
        return null;
    }
}