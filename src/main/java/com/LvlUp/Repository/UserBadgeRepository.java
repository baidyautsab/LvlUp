package com.LvlUp.Repository;

import com.LvlUp.Entity.BadgeType;
import com.LvlUp.Entity.UserBadge;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBadgeRepository {
    public boolean existsByUserIdAndBadgeType(Long userId, BadgeType badgeType);

    void save(UserBadge newBadge);
}
