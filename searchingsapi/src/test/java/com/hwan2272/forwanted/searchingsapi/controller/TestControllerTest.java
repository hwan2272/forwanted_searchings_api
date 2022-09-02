package com.hwan2272.forwanted.searchingsapi.controller;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import com.hwan2272.forwanted.searchingsapi.service.CompanyService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestControllerTest {

    @Autowired
    CompanyDataRepository companyDataRepository;

    @Autowired
    CompanyService companyService;

    @Test
    @Order(1)
    public void test_company_name_autocomplete() {
        System.out.println("[GET] /search?query={검색어}");
        //List<CompanyEntity> ceList = (List) companyDataRepository.findCompByCompanyKoContaining("링크");
        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("like", "링크");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        for(CompanyEntity ce : ceList) {
            System.out.println(ce.toString());
        }

    }

    @Test
    @Order(2)
    public void test_company_search() {
        System.out.println("[GET] /companies/{회사명}");
        //CompanyEntity ce = companyDataRepository.findCompByCompanyEn("Wantedlab");
        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("equals", "Wantedlab");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ceList.get(0).toString());

    }

    @Test
    @Order(3)
    public void test_new_company() {
        System.out.println("[POST] /companies");

        CompanyEntity ce = new CompanyEntity();
        ce.setCompanyKo("라인 프레쉬");
        ce.setCompanyEn("LINE FRESH");
        ce.setCompanyJa("");

        ce.setTagKo("태그_1|태그_8|태그_15");
        ce.setTagEn("tag_1|tag_8|tag_15");
        try {
            companyService.addComp(ce);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ce.toString());

    }

    @Test
    @Order(4)
    public void test_search_tag_name() {
        System.out.println("[GET] /tags?query={검색어}");
        //List<CompanyEntity> ceList = (List) companyDataRepository.findCompByTagJaContaining("タグ_6");
        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchTag("タグ_22");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for(CompanyEntity newCe : ceList) {
            System.out.println(newCe.toString());
        }
    }

    @Test
    @Order(5)
    public void test_new_tag() {
        System.out.println("[PUT] /companies/{회사명}/tags");

        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("equals", "원티드랩");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        CompanyEntity ce = ceList.get(0);
        //ce.setSeq(ceList.get(0).getSeq());
        //ce.setCompanyKo("원티드랩");

        ce.setTagKo(ceList.get(0).getTagKo() + "|태그_50|태그_4");
        ce.setTagEn(ceList.get(0).getTagEn() + "|tag_50|tag_4");
        ce.setTagJa(ceList.get(0).getTagJa() + "|タグ_50|タグ_4");
        try {
            companyService.addComp(ce);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ce.toString());
    }

    @Test
    @Order(6)
    public void test_delete_tag() {
        System.out.println("[DELETE] /companies/{회사명}/tags/{태그명}");

        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("equals", "원티드랩");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        CompanyEntity ce = ceList.get(0);
        //ce.setSeq(ceList.get(0).getSeq());
        //ce.setCompanyKo("원티드랩");

        ce.setTagKo(ceList.get(0).getTagKo().replaceAll("태그_16", ""));
        try {
            companyService.addComp(ce);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ce.toString());
    }
}