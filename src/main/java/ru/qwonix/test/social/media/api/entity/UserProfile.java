package ru.qwonix.test.social.media.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user_profile")
public class UserProfile implements UserDetails {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ToString.Exclude

    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new HashSet<>();

    @ToString.Exclude

    @ManyToMany
    @JoinTable(name = "relation",
            joinColumns = @JoinColumn(name = "source_user_id"),
            inverseJoinColumns = @JoinColumn(name = "target_user_id"))
    private Set<UserProfile> relations;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return role != Role.BANNED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return role != Role.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return role != Role.BANNED;
    }

    @Override
    public boolean isEnabled() {
        return role != Role.BANNED;
    }
}
