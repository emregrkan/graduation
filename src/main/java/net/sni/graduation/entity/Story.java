package net.sni.graduation.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_story")
@Getter
@Setter
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String title;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;
    @OneToMany(mappedBy = "story")
    private Set<Character> characters;
    @OneToMany(mappedBy = "story")
    private Set<Vote> votes;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Profile author;
    @OneToMany(mappedBy = "story")
    private Set<Comment> comments;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
