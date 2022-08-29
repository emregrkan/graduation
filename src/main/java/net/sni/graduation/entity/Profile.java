package net.sni.graduation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_profile")
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    private String username;
    @OneToMany(mappedBy = "author")
    private Set<Story> stories;
    @OneToMany(mappedBy = "voter")
    private Set<Vote> votes;
    @OneToMany(mappedBy = "author")
    private Set<Comment> comments;
}
