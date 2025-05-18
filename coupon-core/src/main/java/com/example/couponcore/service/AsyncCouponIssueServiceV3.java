package com.example.couponcore.service;


import com.example.couponcore.repository.redis.RedisRepository;
import com.example.couponcore.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AsyncCouponIssueServiceV3 {

    private final RedisRepository redisRepository;
    private final CouponCacheService couponCacheService;

    // V3: Local Cache 사용
    public void issue(long couponId, long userId) {
        CouponRedisEntity coupon = couponCacheService.getCouponLocalCache(couponId);        
        coupon.checkIssuableCoupon();
        issueRequest(couponId, userId, coupon.totalQuantity());
    }

    public void issueRequest(long couponId, long userCouponId, Integer totalIssueQuantity) {
        if (totalIssueQuantity == null) {
            redisRepository.issueRequest(couponId, userCouponId, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId, userCouponId, totalIssueQuantity);
    }
}

