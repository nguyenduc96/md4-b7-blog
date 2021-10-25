package com.nguyenduc.controller;

import com.nguyenduc.model.Blog;
import com.nguyenduc.model.BlogForm;
import com.nguyenduc.model.Image;
import com.nguyenduc.service.blog.IBlogService;
import com.nguyenduc.service.image.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
public class BlogController {
    @Value("${file-upload}")
    private String fileUpload;

    @Autowired
    private IBlogService blogService;

    @Autowired
    private IImageService imageService;

    @GetMapping("/home")
    public ModelAndView home(@PageableDefault(size = 2) Pageable pageable,
                             @RequestParam(name = "q", required = false) String search) {
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Blog> blogs;
        if (search == null || search.equals("")) {
            blogs = blogService.findAll(pageable);
        } else {
            blogs = blogService.findAllByTitleContaining(search,pageable);
            modelAndView.addObject("q", search);
        }
        List<Image> images = (List<Image>) imageService.findAll();
        modelAndView.addObject("blogs", blogs);
        modelAndView.addObject("images", images);
        return modelAndView;
    }

    @GetMapping("/status")
    public ModelAndView statusForm() {
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("blogForm", new BlogForm());
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        Blog blog = blogService.findById(id).get();
        BlogForm blogForm = new BlogForm();
        blogForm.setName(blog.getName());
        blogForm.setId(blog.getId());
        blogForm.setContent(blog.getContent());
        blogForm.setTitle(blog.getTitle());
        blogForm.setDate(blog.getDate());
        modelAndView.addObject("blogForm", blogForm);
        return modelAndView;
    }

    @PostMapping("/status")
    public String status(@ModelAttribute("blogForm") BlogForm blogForm) throws IOException {
        List<MultipartFile> multipartFiles = blogForm.getImages();
        Blog blog = new Blog();
        blog.setDate(new Date());
        if (blogForm.getId() != null) {
            blog.setId(blogForm.getId());
        }
        blog.setContent(blogForm.getContent());
        blog.setName(blogForm.getName());
        blog.setTitle(blogForm.getTitle());
        blogService.save(blog);
        if (multipartFiles != null) {
            for (MultipartFile multipartFile : multipartFiles) {
                Image image = new Image();
                image.setBlog(blog);
                FileCopyUtils.copy(multipartFile.getBytes(), new File(fileUpload + multipartFile.getOriginalFilename()));
                image.setFile(multipartFile.getOriginalFilename());
                imageService.save(image);
            }
        } else {
            Image image = new Image();
            image.setBlog(blog);
            imageService.save(image);
        }
       return "redirect:/home" ;
    }
}
