package ru.practicum.mainService.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainService.comment.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId);

}