package com.hwan2272.forwanted.searchingsapi.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="comp_ext")
public class CompanyExtendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @JoinColumn(name = "comp_seq", nullable = false)
    private Integer compSeq;

    @Column(nullable = true, length = 2)
    private String lang;

    @Column(nullable = true, length = 50)
    private String company;

    @Column(nullable = true, length = 100)
    private String tag;

    //@OneToOne(cascade = CascadeType.ALL)
    //private CompanyEntity companyEntity;

}
