package com.js.api.service;

import com.js.api.dto.PraiseDto;
import com.js.api.dto.PraiseRankDto;

import java.util.List;

public interface IPraiseService {
    boolean addPraise(PraiseDto dto);
    boolean addPraiseLock(PraiseDto dto);
    boolean cancelPraise(PraiseDto dto);
    boolean cancelPraiseLock(PraiseDto dto);
    int countBlogPraise(String blogId);
    List<PraiseRankDto> cacheRank();
}
