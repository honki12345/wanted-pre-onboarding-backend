package me.honki12345.wantedassignment.repository;

import me.honki12345.wantedassignment.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
