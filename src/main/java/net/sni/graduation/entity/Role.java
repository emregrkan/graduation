package net.sni.graduation.entity;

import lombok.Getter;
import lombok.Setter;
import net.sni.graduation.constant.RoleEnum;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tb_role")
@Getter
@Setter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleEnum role = RoleEnum.USER;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User> users;

    @Override
    public String getAuthority() {
        return role.getRole().getAuthority();
    }
}
