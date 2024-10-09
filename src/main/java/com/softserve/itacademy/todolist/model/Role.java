package com.softserve.itacademy.todolist.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@ToString
@Getter @Setter
@NoArgsConstructor
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The 'name' cannot be empty")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return getId() != null && getId().equals(role.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

//    public Set<SimpleGrantedAuthority> getAuthorities() {
//        if (getName().equals("ADMIN")) {
//            return Set.of(new SimpleGrantedAuthority(getName()), new SimpleGrantedAuthority("USER"));
//        }
//        return Set.of(new SimpleGrantedAuthority("USER"));
//    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
