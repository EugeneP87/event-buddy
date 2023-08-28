package ru.practicum.mainService.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.comment.dto.CommentDto;
import ru.practicum.mainService.comment.dto.NewCommentDto;
import ru.practicum.mainService.comment.dto.PartialCommentDto;
import ru.practicum.mainService.comment.service.CommentServiceImpl;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {

    private final CommentServiceImpl commentServiceImpl;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@Valid @RequestBody NewCommentDto newCommentDto,
                                 @PathVariable(value = "userId") Long userId,
                                 @PathVariable(value = "eventId") Long eventId) {
        log.info("Добавление комментария пользователем с ID {} к событию с ID {}", userId, eventId);
        return commentServiceImpl.addComment(newCommentDto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@Valid @RequestBody PartialCommentDto partialCommentDto,
                                    @PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "commentId") Long commentId) {
        log.info("Изменение комментария пользователем с ID {}", userId);
        return commentServiceImpl.updateComment(partialCommentDto, userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentByUser(@PathVariable(value = "userId") Long userId,
                                       @PathVariable(value = "commentId") Long commentId) {
        log.info("Получение комментария пользователем с ID {}", userId);
        return commentServiceImpl.getCommentByUser(userId, commentId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByUser(@PathVariable(value = "userId") Long userId,
                                    @PathVariable(value = "commentId") Long commentId) {
        log.info("Удаление комментария пользователем с ID {}", userId);
        commentServiceImpl.deleteCommentByUser(userId, commentId);
    }

}