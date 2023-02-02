package com.js.main.provider.service;

import com.js.api.dto.PraiseRankDto;
import com.js.api.service.IRedisPraiseSevice;
import com.js.main.provider.mapper.PraiseMapper;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisPraiseServiceImpl implements IRedisPraiseSevice {
    private final Logger log= LoggerFactory.getLogger(RedisPraiseServiceImpl.class);

    @Autowired
    PraiseMapper praiseMapper;
    @Autowired
    RedissonClient redissonClient;


    private final String redissonCacheKey="RedissonCacheMap";
    private static final String cacheRank="RedisRankList";

    @Override //只是为了练习锁的使用，由于PraiseService中已经对该数据加了锁，缓存调用在服务之后，缓存中没必要加锁
    public void cachePraiseBlog(String blogId, String userId, short status) {
        RMap rMap=redissonClient.getMap(redissonCacheKey);
        final String lockName="redissonCacheLock:"+blogId+userId;
        RLock rLock=redissonClient.getLock(lockName);
        try{
            boolean lockFlag=rLock.tryLock(10, TimeUnit.SECONDS);
            if(lockFlag){
                final String mapKey=blogId+":"+userId;
                if(status==1){
                    rMap.put(mapKey,status);
                }else{
                    rMap.remove(mapKey);
                }
            }
        }catch (Exception e){
            log.info("缓存加锁失败！");
        }finally {
            if(rLock!=null){
                rLock.unlock();
            }
        }
    }

    @Override
    public int countPraiseByBid(String blogId) {
        int res=0;
        //得到缓存，然后分割它的键与形参比较做统计即可
        //缓存中键的形式为，blogId+":"+userId;
        RMap rMap=redissonClient.getMap(redissonCacheKey);
        Set<String> cacheKeySet=rMap.keySet();
        for(String key:cacheKeySet){
            int ind=key.indexOf(':');
            String bId=key.substring(0,ind);
            if(bId.equals(blogId)){
                res++;
            }
        }
        return res;
    }

    @Override
    public List<PraiseRankDto> getPraiseRankCache() {
        //从数据库中查询，并放入缓存
        List<PraiseRankDto> list=praiseMapper.getPraiseRank();
        if(list!=null && list.size()>0){
            RList rList=redissonClient.getList(cacheRank);
            rList.clear();
            rList.addAll(list);
        }
        return list;
    }
}
