package com.js.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Praise implements Serializable {
    private Integer id;

    private String blogId;

    private String userId;

    private Date praiseTime;

    private Short status;

    private Short isActive;

    private Date createTime;

    private Date updateTime;

}