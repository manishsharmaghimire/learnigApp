package com.elearn.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name="categories")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Category {
    private String id;
    private String title;
    @Column(name = "desc")
    private String desc;
    private Date addedDate;
}
