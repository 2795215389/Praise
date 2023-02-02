package com.js.main.provider.mapper;

import com.js.api.dto.PraiseRankDto;
import com.js.api.model.Praise;
import com.js.api.model.PraiseExample;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;


@Mapper
public interface PraiseMapper {
    @SelectProvider(type=PraiseSqlProvider.class, method="countByExample")
    long countByExample(PraiseExample example);

    @DeleteProvider(type=PraiseSqlProvider.class, method="deleteByExample")
    int deleteByExample(PraiseExample example);

    @Delete({
        "delete from praise",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);


    @Delete({
            "delete from praise",
            "where blog_id = #{blogId,jdbcType=VARCHAR}",
            "and user_id=#{userId,jdbcType=VARCHAR}"
    })
    int deleteByBUId(String blogId, String userId);

    @Insert({
        "insert into praise (id, blog_id, ",
        "user_id, praise_time, ",
        "STATUS, is_active, ",
        "create_time, update_time)",
        "values (#{id,jdbcType=INTEGER}, #{blogId,jdbcType=VARCHAR}, ",
        "#{userId,jdbcType=VARCHAR}, #{praiseTime,jdbcType=TIMESTAMP}, ",
        "#{status,jdbcType=SMALLINT}, #{isActive,jdbcType=SMALLINT}, ",
        "#{createTime,jdbcType=TIMESTAMP}, #{updateTime,jdbcType=TIMESTAMP})"
    })
    int insert(Praise record);

    @InsertProvider(type=PraiseSqlProvider.class, method="insertSelective")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    int insertSelective(Praise record);

    @SelectProvider(type=PraiseSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="blog_id", property="blogId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="praise_time", property="praiseTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="STATUS", property="status", jdbcType=JdbcType.SMALLINT),
        @Result(column="is_active", property="isActive", jdbcType=JdbcType.SMALLINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<Praise> selectByExample(PraiseExample example);

    @Select({
        "select",
        "id, blog_id, user_id, praise_time, STATUS, is_active, create_time, update_time",
        "from praise",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="blog_id", property="blogId", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="praise_time", property="praiseTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="STATUS", property="status", jdbcType=JdbcType.SMALLINT),
        @Result(column="is_active", property="isActive", jdbcType=JdbcType.SMALLINT),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Praise selectByPrimaryKey(Integer id);




    @Select({
            "select",
            "blog_id as blogId,",
            "count(*) as total",
            "from praise",
            "where STATUS=1 ",
            "group by blog_id",
            "order by total desc"
    })
    @Results({
            //@Result(column="blog_id", property="blogId", jdbcType=JdbcType.VARCHAR),
            //如果使用别名，column必须按别名走
            @Result(column="blogId", property="blogId", jdbcType=JdbcType.VARCHAR),
            @Result(column = "total",property = "total",jdbcType = JdbcType.INTEGER)
    })
    List<PraiseRankDto> getPraiseRank();



    @Select({
            "select",
            "count(*)",
            "from praise",
            "where blog_id = #{blogId,jdbcType=VARCHAR}",
            "and STATUS=1"
    })
    int countBlogPraise(String blogId);



    @Select({
            "select",
            "id, blog_id, user_id, praise_time, STATUS, is_active, create_time, update_time",
            "from praise",
            "where blog_id = #{blogId,jdbcType=VARCHAR}",
            "and user_id=#{userId,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="blog_id", property="blogId", jdbcType=JdbcType.VARCHAR),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
            @Result(column="praise_time", property="praiseTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="STATUS", property="status", jdbcType=JdbcType.SMALLINT),
            @Result(column="is_active", property="isActive", jdbcType=JdbcType.SMALLINT),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="update_time", property="updateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    Praise selectByBUId(String blogId, String userId);

    @UpdateProvider(type=PraiseSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Praise record, @Param("example") PraiseExample example);

    @UpdateProvider(type=PraiseSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Praise record, @Param("example") PraiseExample example);

    @UpdateProvider(type=PraiseSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Praise record);

    @Update({
        "update praise",
        "set blog_id = #{blogId,jdbcType=VARCHAR},",
          "user_id = #{userId,jdbcType=VARCHAR},",
          "praise_time = #{praiseTime,jdbcType=TIMESTAMP},",
          "STATUS = #{status,jdbcType=SMALLINT},",
          "is_active = #{isActive,jdbcType=SMALLINT},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Praise record);
}