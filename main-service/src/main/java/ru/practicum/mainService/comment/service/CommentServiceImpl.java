package ru.practicum.mainService.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainService.comment.Comment;
import ru.practicum.mainService.comment.CommentState;
import ru.practicum.mainService.comment.CommentStateAction;
import ru.practicum.mainService.comment.dto.CommentDto;
import ru.practicum.mainService.comment.dto.NewCommentDto;
import ru.practicum.mainService.comment.dto.PartialCommentDto;
import ru.practicum.mainService.comment.mapper.CommentMapper;
import ru.practicum.mainService.comment.repository.CommentRepository;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.event.repository.EventRepository;
import ru.practicum.mainService.exception.BadRequestException;
import ru.practicum.mainService.exception.ConflictException;
import ru.practicum.mainService.exception.NotFoundException;
import ru.practicum.mainService.user.User;
import ru.practicum.mainService.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl {

    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentDto addComment(NewCommentDto newCommentDto, Long userId, Long eventId) {
        User author = getUserById(userId);
        Event event = getEventById(eventId);
        Comment comment = CommentMapper.toComment(newCommentDto, author, event);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public CommentDto updateComment(PartialCommentDto partialCommentDto, Long userId, Long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        checkUserPermission(comment, userId);
        if (partialCommentDto.getText() != null) {
            comment.setText(partialCommentDto.getText());
        }
        updateStatusComment(comment, partialCommentDto.getState());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    public CommentDto getCommentByUser(Long userId, Long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        checkUserPermission(comment, userId);
        return CommentMapper.toCommentDto(comment);
    }

    public void deleteCommentByUser(Long userId, Long commentId) {
        getUserById(userId);
        Comment comment = getCommentById(commentId);
        checkUserPermission(comment, userId);
        commentRepository.delete(comment);
    }

    public CommentDto updateCommentByAdmin(Long commentId, PartialCommentDto partialCommentDto) {
        Comment comment = getCommentById(commentId);
        if (partialCommentDto.getText() != null) {
            comment.setText(partialCommentDto.getText());
        }
        updateStatusComment(comment, partialCommentDto.getState());
        return CommentMapper.toCommentDto(comment);
    }

    public void deleteCommentByAdmin(Long commentId) {
        getCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getAllPublishedComments(Long eventId) {
        getEventById(eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .filter(commentDto -> commentDto.getState().equals(CommentState.PUBLISHED))
                .collect(Collectors.toList());
    }

    public List<CommentDto> getAllComments(Long eventId) {
        getEventById(eventId);
        List<Comment> comments = commentRepository.findAllByEventId(eventId);
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден " + userId));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие не найдено " + eventId));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Комментарий не найден " + commentId));
    }

    private void checkUserPermission(Comment comment, Long userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Недостаточно прав для просмотра/редактирования/удаления чужого комментария");
        }
    }

    private void updateStatusComment(Comment comment, CommentStateAction stateAction) {
        CommentState newState;
        switch (stateAction) {
            case CANCEL_COMMENT_TO_REVIEW:
            case REJECT_COMMENT:
                newState = CommentState.CANCELED;
                break;
            case SEND_COMMENT_TO_REVIEW:
                newState = CommentState.PENDING;
                break;
            case PUBLISH_COMMENT:
                newState = CommentState.PUBLISHED;
                break;
            default:
                throw new BadRequestException("Некорректный статус");
        }
        comment.setState(newState);
        if (stateAction == CommentStateAction.PUBLISH_COMMENT) {
            comment.setPublishedOn(LocalDateTime.now());
        }
    }

}