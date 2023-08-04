package com.sparta.lv2.repository;

import com.sparta.lv2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository // 안달아도 되나??
public interface PostRepository extends JpaRepository<Post, Long> {
}
