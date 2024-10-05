package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;
    @JoinColumn(name = "author_id")
    @ManyToOne
    private User author;
    @Column(insertable = false, updatable = false)
    private Boolean created;
}
