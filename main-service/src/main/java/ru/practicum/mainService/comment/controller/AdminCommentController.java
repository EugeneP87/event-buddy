package ru.practicum.mainService.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainService.comment.dto.CommentDto;
import ru.practicum.mainService.comment.dto.PartialCommentDto;
import ru.practicum.mainService.comment.service.CommentServiceImpl;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class AdminCommentController {

    private final CommentServiceImpl commentServiceImpl;

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentByAdmin(@Valid @RequestBody PartialCommentDto commentDto,
                                           @PathVariable(value = "commentId") Long commentId) {
        log.info("Обновление комментария администратором");
        return commentServiceImpl.updateCommentByAdmin(commentId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable(value = "commentId") Long commentId) {
        log.info("Удаление комментария с ID {} администратором", commentId);
        commentServiceImpl.deleteCommentByAdmin(commentId);
    }

    @GetMapping("/{eventId}")
    public List<CommentDto> getAllComments(@PathVariable(value = "eventId") Long eventId) {
        log.info("Получение всех комментариев к мероприятию с ID {}", eventId);
        return commentServiceImpl.getAllComments(eventId);
    }

}