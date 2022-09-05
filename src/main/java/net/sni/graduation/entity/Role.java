package net.sni.graduation.entity;

import lombok.Getter;
import lombok.Setter;
import net.sni.graduation.constant.AuthorityEnum;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_role")
@Getter
@Setter
public class Role {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private AuthorityEnum authority = AuthorityEnum.USER;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;
}
