package com.nguyenduc.controller;

import com.nguyenduc.model.Blog;
import com.nguyenduc.service.blog.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/blogs")
public class BlogRestController {
    @Autowired
    private IBlogService blogService;

    @GetMapping
    public ResponseEntity<Iterable<Blog>> home() {
        return new ResponseEntity<>(blogService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Blog> create(@RequestBody Blog blog) {
        if (blog == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(blogService.save(blog), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Blog> update(@PathVariable Long id, @RequestBody Blog blog) {
        Optional<Blog> blog1 = blogService.findById(id);
        if (!blog1.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        blog.setId(id);
        return new ResponseEntity<>(blogService.save(blog), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Blog> delete(@PathVariable Long id) {
        Optional<Blog> blog = blogService.findById(id);
        if (!blog.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        blogService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
