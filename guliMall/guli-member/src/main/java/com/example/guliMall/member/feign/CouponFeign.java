package com.example.guliMall.member.feign;

import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cc
 * @date 2023年03月04日 20:02
 */
@FeignClient("guli-coupon")
public interface CouponFeign {

    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
