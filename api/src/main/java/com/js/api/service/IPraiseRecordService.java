package com.js.api.service;

import com.js.api.dto.PraiseDto;
import com.js.api.model.Praise;

public interface IPraiseRecordService {
    boolean insertPraiseDB(PraiseDto dto);
    boolean delPraiseDB(String blogId,String userId);
}
