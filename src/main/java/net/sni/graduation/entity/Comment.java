package net.sni.graduation.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tb_comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Profile author;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;
}
