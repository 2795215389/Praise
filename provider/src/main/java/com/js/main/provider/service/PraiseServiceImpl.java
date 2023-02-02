package com.js.main.provider.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.js.api.dto.PraiseDto;
import com.js.api.dto.PraiseRankDto;
import com.js.api.model.Praise;
import com.js.api.service.IPraiseRecordService;
import com.js.api.service.IPraiseService;
import com.js.api.service.IRedisPraiseSevice;
import com.js.main.provider.mapper.PraiseMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service(
        version = "1.0.0",interfaceName = "IPraiseService",
        interfaceClass = IPraiseService.class,
        timeout = 120000
)
public class PraiseServiceImpl implements IPraiseService {
    private static final Logger log= LoggerFactory.getLogger(PraiseServiceImpl.class);
    @Autowired
    PraiseMapper praiseMapper;
    @Autowired
    IRedisPraiseSevice redisPraiseSevice;
    @Autowired
    IPraiseRecordService recordService;
    @Autowired
    RedissonClient redissonClient;


    private static final String lockPrefix="redissonLock:";

    @Override
    public boolean addPraise(PraiseDto dto) {
        String userId=dto.getUserId();
        String blogId=dto.getBlogId();
        Praise praise=praiseMapper.selectByBUId(blogId,userId);
        if(praise!=null && praise.getStatus()==1){
            log.info("博客:{},原已被用户{},点赞,不得重复点赞！",blogId,userId);
            return false;
        }else{
            boolean flag=recordService.insertPraiseDB(dto);
            if(flag){
                log.info("用户{}，对博客{}，点赞成功！",userId,blogId);
            }else{
                log.info("用户{}，对博客{}，点赞失败！",userId,blogId);
            }
            return flag;
        }
    }

    @Override
    public boolean addPraiseLock(PraiseDto dto) {
        String blogId=dto.getBlogId();
        String userId=dto.getUserId();
        final String lockName=lockPrefix+blogId+userId;
        RLock rLock=redissonClient.getLock(lockName);
        try {
            boolean lockFlag=rLock.tryLock(10, TimeUnit.SECONDS);
            if(lockFlag){
                Praise praise=praiseMapper.selectByBUId(blogId,userId);
                if(praise!=null && praise.getStatus()==1){
                    log.info("博客:{},原已被用户{},点赞,不得重复点赞！",blogId,userId);
                    return false;
                }else{
                    boolean flag=recordService.insertPraiseDB(dto);
                    redisPraiseSevice.cachePraiseBlog(blogId,userId,(short)1);
                    if(flag){
                        log.info("用户{}，对博客{}，点赞成功！",userId,blogId);
                    }else{
                        log.info("用户{}，对博客{}，点赞失败！",userId,blogId);
                    }
                    return flag;
                }
            }
        }catch (Exception e){
            log.info("点赞服务对象，加锁失败！");
        }finally {
            if(rLock!=null){
                rLock.unlock();
            }
        }
        return false;
    }

    @Override
    public boolean cancelPraise(PraiseDto dto) {
        String blogId=dto.getBlogId();
        String userId=dto.getUserId();
        boolean flag=recordService.delPraiseDB(blogId,userId);
        if(flag){
            log.info("删除点赞记录{}---{}成功！",dto.getBlogId(),dto.getUserId());
            return true;
        }else{
            log.info("删除点赞记录{}---{}失败！",dto.getBlogId(),dto.getUserId());
        }
        return false;
    }

    @Override
    public boolean cancelPraiseLock(PraiseDto dto) {
        String blogId=dto.getBlogId();
        String userId=dto.getUserId();
        final String lockName=lockPrefix+blogId+userId;
        RLock rLock=redissonClient.getLock(lockName);
        try{
            boolean lockFlag=rLock.tryLock(10, TimeUnit.SECONDS);
            if(lockFlag){
                boolean flag=recordService.delPraiseDB(blogId,userId);
                if(flag){
                    log.info("删除点赞记录{}---{}成功！",dto.getBlogId(),dto.getUserId());
                    return true;
                }else{
                    log.info("删除点赞记录{}---{}失败！",dto.getBlogId(),dto.getUserId());
                }
            }
        }catch (Exception e){
            log.info("加锁失败！");
        }finally {
            if(rLock!=null){
                rLock.unlock();
            }
        }
        return false;
    }

    @Override
    public int countBlogPraise(String blogId) {
        redisPraiseSevice.countPraiseByBid(blogId);
        return praiseMapper.countBlogPraise(blogId);
    }

    @Override
    public List<PraiseRankDto> cacheRank() {
        return redisPraiseSevice.getPraiseRankCache();
    }
}
