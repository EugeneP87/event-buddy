package ru.practicum.mainService.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.mainService.common.DateAndTimeFormatter;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "text")
    private String text;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime created;
    @Column(name = "published_on")
    @JsonFormat(pattern = DateAndTimeFormatter.DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private CommentState state;

}