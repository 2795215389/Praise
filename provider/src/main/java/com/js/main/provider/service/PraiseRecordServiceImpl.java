package com.js.main.provider.service;

import com.js.api.dto.PraiseDto;
import com.js.api.model.Praise;
import com.js.api.service.IPraiseRecordService;
import com.js.main.provider.mapper.PraiseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class PraiseRecordServiceImpl implements IPraiseRecordService {

    @Autowired
    private PraiseMapper praiseMapper;
    @Override
    public boolean insertPraiseDB(PraiseDto dto) {
        Praise praise=new Praise();
        BeanUtils.copyProperties(dto,praise);
        praise.setStatus((short)1);
        praise.setUpdateTime(new Date());
        praise.setPraiseTime(new Date());
        int flag=praiseMapper.insertSelective(praise);
        return flag>0;
    }

    @Override
    public boolean delPraiseDB(String blogId,String userId) {
        int flag=praiseMapper.deleteByBUId(blogId,userId);
        return flag>0;
    }

}
