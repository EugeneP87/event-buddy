package ru.practicum.mainService.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private LocalDateTime created;
    @Column(name = "published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private CommentState state;

    public Comment(User author, String text, Event event, LocalDateTime created, CommentState state) {
        this.author = author;
        this.text = text;
        this.event = event;
        this.created = created;
        this.state = state;
    }

}