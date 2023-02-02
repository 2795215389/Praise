package com.js.api.service;

import com.js.api.dto.PraiseRankDto;

import java.util.List;

public interface IRedisPraiseSevice {
    void cachePraiseBlog(String blogId, String userId, short status);
    int countPraiseByBid(String blogId);
    List<PraiseRankDto> getPraiseRankCache();
}
