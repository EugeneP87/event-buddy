package ru.practicum.mainService.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.mainService.comment.dto.CommentDto;
import ru.practicum.mainService.comment.service.CommentServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {

    private final CommentServiceImpl commentServiceImpl;

    @GetMapping("/{eventId}")
    public List<CommentDto> getAllPublishedComments(@PathVariable(value = "eventId") Long eventId) {
        log.info("Получение всех опубликованных комментариев к мероприятию с ID {}", eventId);
        return commentServiceImpl.getAllPublishedComments(eventId);
    }

}