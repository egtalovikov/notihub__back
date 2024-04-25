package com.otmetkaX.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "notihub_notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false)
    String text;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Security sender;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public Notification(String text, Security sender) {
        this.text = text;
        this.sender = sender;
        this.createdAt = new Date();
    }

    public Notification() {}
}
