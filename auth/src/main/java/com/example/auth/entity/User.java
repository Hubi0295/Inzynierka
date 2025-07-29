package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails{
    @Id
    @GeneratedValue(generator = "users_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @Getter
    private long id;
    private String uuid;
    private String username;
    private String password;
    @Getter
    private String email;
    @Getter
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name="isenable")
    private boolean isEnable;
    @Column(name="islock")
    private boolean isLock;
    public User(long id, String uuid, String username, String password, String email, UserRole role, boolean isEnable, boolean isLock) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isEnable = isEnable;
        this.isLock = isLock;
        generateUuid();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLock;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
    public void generateUuid(){
        if(uuid==null || uuid.equals("")){
            setUuid(UUID.randomUUID().toString());
        }
    }
}
