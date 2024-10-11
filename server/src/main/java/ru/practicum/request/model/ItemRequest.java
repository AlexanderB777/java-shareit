package ru.practicum.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @JoinColumn(name = "requestor_id")
    @ManyToOne
    private User requestor;
    private LocalDate created;
    @OneToMany(mappedBy = "itemRequest")
    private List<Item> items;
}