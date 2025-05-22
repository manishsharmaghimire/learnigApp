package com.elearn.entity;

import jakarta.persistence.Column;

import java.util.Date;

public class Category {

    private String id;
    private String title;
    @Column(name = "desc")
    private String desc;
    private Date addedDate;
}
