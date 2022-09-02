package com.hwan2272.forwanted.searchingsapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="comp")
public class CompanyEntity {

    @Id
    @GeneratedValue
    private Integer seq;

    @Column(name="company_ko", nullable = true, length = 50)
    private String companyKo;

    @Column(name="company_en", nullable = true, length = 50)
    private String companyEn;

    @Column(name="company_ja", nullable = true, length = 50)
    private String companyJa;

    @Column(name="tag_ko", nullable = true, length = 100)
    private String tagKo;

    @Column(name="tag_en", nullable = true, length = 100)
    private String tagEn;

    @Column(name="tag_ja", nullable = true, length = 100)
    private String tagJa;
}
