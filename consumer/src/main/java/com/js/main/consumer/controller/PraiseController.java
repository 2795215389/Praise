package com.js.main.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.base.Strings;
import com.js.api.dto.PraiseDto;
import com.js.api.dto.PraiseRankDto;
import com.js.api.response.BaseResponse;
import com.js.api.response.StatusCode;
import com.js.api.service.IPraiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PraiseController {
    private static final Logger log = LoggerFactory.getLogger(PraiseController.class);
    private static final String prefix="/blog";


    @Reference(
            version = "1.0.0",interfaceName = "IPraiseService",
            interfaceClass = IPraiseService.class
    )
    private IPraiseService praiseService;

    @GetMapping(value = prefix+"/praise")
    public BaseResponse praise(PraiseDto dto){
        String userId=dto.getUserId();
        String blogId=dto.getBlogId();
        BaseResponse response=new BaseResponse(StatusCode.Failed);
        if(Strings.isNullOrEmpty(userId) ||Strings.isNullOrEmpty(blogId)){
            log.info("输入信息不符合要求!");
            response=new BaseResponse(StatusCode.InvalidParam);
            return response;
        }
        boolean flag=praiseService.addPraise(dto);
        if(flag){
            log.info("点赞成功！");
            response=new BaseResponse(StatusCode.Success);
            int res=praiseService.countBlogPraise(blogId);
            response.setData("当前博客点赞数为:"+res);
            return response;
        }else{
            log.info("点赞失败！");
        }
        return response;
    }


    @GetMapping(value = prefix+"/praiseLock")
    public BaseResponse praiseLock(PraiseDto dto){
        String userId=dto.getUserId();
        String blogId=dto.getBlogId();
        BaseResponse response=new BaseResponse(StatusCode.Failed);
        if(Strings.isNullOrEmpty(userId) ||Strings.isNullOrEmpty(blogId)){
            log.info("输入信息不符合要求!不能为空！");
            response=new BaseResponse(StatusCode.InvalidParam);
            return response;
        }
        boolean flag=praiseService.addPraiseLock(dto);
        if(flag){
            log.info("加锁方式，点赞成功！");
            response=new BaseResponse(StatusCode.Success);
            int res=praiseService.countBlogPraise(blogId);
            response.setData("当前博客点赞数为:"+res);
            return response;
        }else{
            log.info("点赞失败！");
        }
        return response;
    }



    @GetMapping(value = prefix+"/del")
    public BaseResponse cancel(PraiseDto dto){
        String userId=dto.getUserId();
        String blogId=dto.getBlogId();
        BaseResponse response=new BaseResponse(StatusCode.Failed);
        if(Strings.isNullOrEmpty(userId) ||Strings.isNullOrEmpty(blogId)){
            log.info("输入信息不符合要求!不能为空！");
            response=new BaseResponse(StatusCode.InvalidParam);
            return response;
        }
        boolean flag=praiseService.cancelPraise(dto);
        if(flag){
            log.info("删除点赞成功！");
            response=new BaseResponse(StatusCode.Success);
            int res=praiseService.countBlogPraise(blogId);
            response.setData("当前博客点赞数为:"+res);
            return response;
        }else{
            log.info("删除点赞失败！");
        }
        return response;
    }



    @GetMapping(value = prefix+"/delLock")
    public BaseResponse cancelLock(PraiseDto dto){
        String userId=dto.getUserId();
        String blogId=dto.getBlogId();
        BaseResponse response=new BaseResponse(StatusCode.Failed);
        if(Strings.isNullOrEmpty(userId) ||Strings.isNullOrEmpty(blogId)){
            log.info("输入信息不符合要求!不能为空！");
            response=new BaseResponse(StatusCode.InvalidParam);
            return response;
        }
        boolean flag=praiseService.cancelPraiseLock(dto);
        if(flag){
            log.info("删除点赞成功！");
            response=new BaseResponse(StatusCode.Success);
            int res=praiseService.countBlogPraise(blogId);
            response.setData("当前博客点赞数为:"+res);
            return response;
        }else{
            log.info("删除点赞失败！");
        }
        return response;
    }


    @GetMapping(value = prefix+"/rank")
    public BaseResponse getRank(){
        BaseResponse response=new BaseResponse(StatusCode.Failed);
        List<PraiseRankDto> praiseRankDtoList=praiseService.cacheRank();
        if(praiseRankDtoList!=null && praiseRankDtoList.size()>0){
            response=new BaseResponse(StatusCode.Success);
            response.setData(praiseRankDtoList);
            return response;
        }
        return response;
    }





}
