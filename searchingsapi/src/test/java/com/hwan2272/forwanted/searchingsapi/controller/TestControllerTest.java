package com.hwan2272.forwanted.searchingsapi.controller;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
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

    @Test
    @Order(1)
    public void test_company_name_autocomplete() {
        System.out.println("[GET] /search?query={검색어}");
        List<CompanyEntity> ceList =
                (List) companyDataRepository.findCompByCompanyKoContaining("링크");

        for(CompanyEntity ce : ceList) {
            System.out.println(ce.toString());
        }

    }

    @Test
    @Order(2)
    public void test_company_search() {
        System.out.println("[GET] /companies/{회사명}");
        CompanyEntity ce = companyDataRepository.findCompByCompanyEn("Wantedlab");
        System.out.println(ce.toString());

    }

    @Test
    @Order(3)
    public void test_new_company() {
        System.out.println("[POST] /companies");
    }

    @Test
    @Order(4)
    public void test_search_tag_name() {
        System.out.println("[GET] /tags?query={검색어}");
        List<CompanyEntity> ceList =
                (List) companyDataRepository.findCompByTagJaContaining("タグ_22");
        List<CompanyEntity> ceList2 = new ArrayList<>();

        for(CompanyEntity ce : ceList) {
            String[] tags = ce.getTagJa().split("\\|");
            for(String tag : tags) {
                if (tag.equals("タグ_22")) {
                    ceList2.add(ce);
                }
                else {
                    continue;
                }
            }
        }

        System.out.println(":::NEW CE:::ListCnt:: " + ceList2.size());
        for(CompanyEntity newCe : ceList2) {
            System.out.println(newCe.toString());
        }
    }

    @Test
    @Order(5)
    public void test_new_tag() {
        System.out.println("[PUT] /companies/{회사명}/tags");
    }

    @Test
    @Order(6)
    public void test_delete_tag() {
        System.out.println("[DELETE] /companies/{회사명}/tags/{태그명}");
    }
}