package com.otmetkaX.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Entity
@Getter
@Setter
@Table(name = "notihub_user")
public class Security {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "status", nullable = false)
    boolean status;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "token", nullable = false)
    private String token;

    public Security() {}

    public Security(String role, String login, String password, boolean status, String token) {
        this.role = role;
        this.login = login;
        this.status = status;
        this.token = token;
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public String encrypt(String rawPassword) {
        return passwordEncoder.encode(password);
    }

}
