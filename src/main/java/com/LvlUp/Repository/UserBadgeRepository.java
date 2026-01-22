package com.LvlUp.Repository;

import com.LvlUp.Entity.BadgeType;
import com.LvlUp.Entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository; // Import this
import org.springframework.stereotype.Repository;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> { // <--- THIS WAS MISSING

    // Spring generates the logic for this automatically
    boolean existsByUserIdAndBadgeType(Long userId, BadgeType badgeType);
}