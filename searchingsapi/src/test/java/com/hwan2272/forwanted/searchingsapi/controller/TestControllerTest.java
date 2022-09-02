package com.hwan2272.forwanted.searchingsapi.controller;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import com.hwan2272.forwanted.searchingsapi.service.CompanyService;
import com.hwan2272.forwanted.searchingsapi.service.LanguageEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class TestControllerTest {

    @Autowired
    CompanyDataRepository companyDataRepository;

    @Autowired
    CompanyService companyService;

    @Test
    @Order(1)
    public void test_company_name_autocomplete() {
        log.info("[GET] /search?query={검색어}");
        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("like", "링크");
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        for(CompanyEntity ce : ceList) {
            log.info(ce.toString());
        }

    }

    @Test
    @Order(2)
    public void test_company_search() {
        log.info("[GET] /companies/{회사명}");
        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("equals", "Wantedlab");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(ceList.get(0).toString());

    }

    @Test
    @Order(3)
    public void test_new_company() {
        log.info("[POST] /companies");

        CompanyEntity ce = new CompanyEntity();
        ce.setCompanyKo("라인 프레쉬");
        ce.setCompanyEn("LINE FRESH");
        ce.setCompanyJa("");

        ce.setTag("태그_1|태그_8|태그_15");
        try {
            companyService.addComp(ce);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(ce.toString());

    }

    @Test
    @Order(4)
    public void test_search_tag_name() {
        log.info("[GET] /tags?query={검색어}");
        //List<CompanyEntity> ceList = (List) companyDataRepository.findCompByTagJaContaining("タグ_6");
        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            String query = "タグ_22";
            //String query = "tag_22";
            //언어에 따라 바꿔주는 로직 추가
            query = query
                    .replaceAll(LanguageEnum.tagEn.getName(), LanguageEnum.tagKo.getName())
                    .replaceAll(LanguageEnum.tagJa.getName(), LanguageEnum.tagKo.getName());

            log.info(":::::" + query);
            ceList = companyService.searchTag(query);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        for(CompanyEntity newCe : ceList) {
            log.info(newCe.toString());
        }
    }

    @Test
    @Order(5)
    public void test_new_tag() {
        log.info("[PUT] /companies/{회사명}/tags");
        String compName = "원티드랩";

        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("equals", compName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        CompanyEntity ce = ceList.get(0);
        Set<String> sets = new LinkedHashSet<>(Arrays.asList(ce.getTag().split("\\|")));
        sets.add("태그_50");
        sets.add("태그_4");

        String tagString = sets.toString();
        tagString = tagString.replaceAll(", ", "|")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .trim();

        ce.setTag(tagString);
        try {
            companyService.addComp(ce);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(ce.toString());
    }

    @Test
    @Order(6)
    public void test_delete_tag() {
        log.info("[DELETE] /companies/{회사명}/tags/{태그명}");
        String compName = "원티드랩";
        String tag = "태그_16";

        List<CompanyEntity> ceList = new ArrayList<>();

        try {
            ceList = companyService.searchComp("equals", compName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        CompanyEntity ce = ceList.get(0);
        Set<String> sets = new LinkedHashSet<>(Arrays.asList(ce.getTag().split("\\|")));

        //언어에 따라 바꿔주는 로직 추가
        tag = tag
                .replaceAll(LanguageEnum.tagEn.getName(), LanguageEnum.tagKo.getName())
                .replaceAll(LanguageEnum.tagJa.getName(), LanguageEnum.tagKo.getName());

        sets.remove(tag);
        String tagString = sets.toString();
        tagString = tagString.replaceAll(", ", "|")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .trim();

        log.info(tagString.toString());
        ce.setTag(tagString);

        try {
            companyService.addComp(ce);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        log.info(ce.toString());
    }
}