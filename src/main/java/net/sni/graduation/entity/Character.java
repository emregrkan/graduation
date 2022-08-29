package net.sni.graduation.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_character")
@Getter
@Setter
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;
    @OneToMany(mappedBy = "character")
    private Set<Vote> votes;
}
