package com.LvlUp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_badges",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "badge_type"})
        }
)
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_type", nullable = false)
    private BadgeType badgeType;

    @Column(nullable = false)
    private LocalDateTime earnedAt = LocalDateTime.now();

    // Constructors
    public UserBadge() {}

    public UserBadge(User user, BadgeType badgeType) {
        this.user = user;
        this.badgeType = badgeType;
        this.earnedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BadgeType getBadgeType() { return badgeType; }
    public void setBadgeType(BadgeType badgeType) { this.badgeType = badgeType; }
    public LocalDateTime getEarnedAt() { return earnedAt; }
}