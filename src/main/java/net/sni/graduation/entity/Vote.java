package net.sni.graduation.entity;

import lombok.Getter;
import lombok.Setter;
import net.sni.graduation.constant.VoteEnum;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tb_vote")
@Getter
@Setter
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.ORDINAL)
    private VoteEnum vote;
    @ManyToOne
    @JoinColumn(name = "voter_id")
    private Profile voter;
    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;
    @ManyToOne
    @JoinColumn(name = "character_id")
    private Character character;
}
