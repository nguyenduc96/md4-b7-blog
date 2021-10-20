package com.nguyenduc.repository.image;

import com.nguyenduc.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface IImageRepository extends JpaRepository<Image, Long> {
}
