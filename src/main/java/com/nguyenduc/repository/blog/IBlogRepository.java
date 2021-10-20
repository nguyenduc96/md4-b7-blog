package com.nguyenduc.repository.blog;
import com.nguyenduc.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface IBlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findAllByTitleContaining(String title, Pageable pageable);
}
