package com.nguyenduc.service.blog;

import com.nguyenduc.model.Blog;
import com.nguyenduc.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBlogService extends IGeneralService<Blog> {
    Page<Blog> findAllByTitleContaining(String title, Pageable pageable);
}
