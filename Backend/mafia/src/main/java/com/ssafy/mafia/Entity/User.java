package com.ssafy.mafia.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import net.bytebuddy.asm.Advice;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    public User(String email, String password){
        this.email = email;
        this.password = password;
        isAuth = false;
        isRedUser = false;
        winCount = 0;
        loseCount = 0;
        isLogin = false;
        reportedCount = 0;
        rankPoint = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int UserSeq;

    @Column(nullable = false, length = 255)
    private String email;

    private boolean isAuth;

    @Column(nullable = false, length = 1023)
    private String password;

    @Column(nullable = true)
    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;


    private boolean isRedUser;

    private boolean isAdmin;

    private int winCount;

    private int loseCount;

    private boolean isLogin;

    private int reportedCount;

    private int rankPoint;

    @JsonIgnore
    @OneToMany(mappedBy = "userSeq")
    private List<NoticeBoard> noticeBoardList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reporting")
    private List<Report> reporting = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "reported")
    private List<Report> reported = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "friendFrom")
    private List<Friend> friendFrom = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "friendTo")
    private List<Friend> friendTo = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "hostUser", fetch = FetchType.LAZY)
    private RoomInfo hostUser;

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private RoomUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}