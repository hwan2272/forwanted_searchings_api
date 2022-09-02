package com.hwan2272.forwanted.searchingsapi.controller;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import com.hwan2272.forwanted.searchingsapi.service.CompanyService;
import com.hwan2272.forwanted.searchingsapi.service.LanguageEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
@AutoConfigureMockMvc
class TestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @Order(1)
    @DisplayName("[GET] /search?query={검색어}")
    public void test_company_name_autocomplete() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/search?query=링크")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "ko")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companies.[0].company_name", is("주식회사 링크드코리아")))
                .andExpect(jsonPath("$.companies.[1].company_name", is("스피링크")));

    }

    @Test
    @Order(2)
    @DisplayName("[GET] /companies/{회사명}")
    public void test_company_search() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/companies/Wantedlab")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "ko")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companies.[0].company_name", is("원티드랩")))
                .andExpect(jsonPath("$.companies.[0].tags[0]", is("태그_4")));

        result = mockMvc.perform(
                get("/companies/없는회사")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "ko")
        );

        result.andExpect(status().is4xxClientError())
                .andExpect(status().isNotFound());


    }

    @Test
    @Order(3)
    @DisplayName("[POST] /companies")
    public void test_new_company() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "tw")
                        .content("{\n" +
                                "    \"company_name\": {\n" +
                                "        \"ko\": \"라인 프레쉬\",\n" +
                                "        \"tw\": \"LINE FRESH\",\n" +
                                "        \"en\": \"LINE FRESH\"\n" +
                                "    },\n" +
                                "    \"tags\": [\n" +
                                "        {\n" +
                                "            \"tag_name\": {\n" +
                                "                \"ko\": \"태그_1\",\n" +
                                "                \"tw\": \"tag_1\",\n" +
                                "                \"en\": \"tag_1\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"tag_name\": {\n" +
                                "                \"ko\": \"태그_8\",\n" +
                                "                \"tw\": \"tag_8\",\n" +
                                "                \"en\": \"tag_8\"\n" +
                                "            }\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"tag_name\": {\n" +
                                "                \"ko\": \"태그_15\",\n" +
                                "                \"tw\": \"tag_15\",\n" +
                                "                \"en\": \"tag_15\"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companies.[0].company_name", is("LINE FRESH")))
                .andExpect(jsonPath("$.companies.[0].tags[0]", is("tag_1")));
    }

    @Test
    @Order(4)
    @DisplayName("[GET] /tags?query={검색어}")
    public void test_search_tag_name() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/tags?query=タグ_22")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "ko")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companies.[0].company_name", is("딤딤섬 대구점")))
                .andExpect(jsonPath("$.companies.[1].company_name", is("마이셀럽스")))
                .andExpect(jsonPath("$.companies.[2].company_name", is("Rejoice Pregnancy")))
                .andExpect(jsonPath("$.companies.[3].company_name", is("삼일제약")))
                .andExpect(jsonPath("$.companies.[4].company_name", is("투게더앱스")));
    }

    @Test
    @Order(5)
    @DisplayName("[PUT] /companies/{회사명}/tags")
    public void test_new_tag() throws Exception {
        ResultActions result = mockMvc.perform(
                put("/companies/원티드랩/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "en")
                        .content("[\n" +
                                "    {\n" +
                                "        \"tag_name\": {\n" +
                                "            \"ko\": \"태그_50\",\n" +
                                "            \"jp\": \"タグ_50\",\n" +
                                "            \"en\": \"tag_50\"\n" +
                                "        }\n" +
                                "    },\n" +
                                "    {\n" +
                                "        \"tag_name\": {\n" +
                                "            \"ko\": \"태그_4\",\n" +
                                "            \"tw\": \"tag_4\",\n" +
                                "            \"en\": \"tag_4\"\n" +
                                "        }\n" +
                                "    }\n" +
                                "]")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companies.[0].company_name", is("Wantedlab")))
                .andExpect(jsonPath("$.companies.[0].tags[0]", is("tag_4")));
    }

    @Test
    @Order(6)
    @DisplayName("[DELETE] /companies/{회사명}/tags/{태그명}")
    public void test_delete_tag() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/companies/원티드랩/tags/태그_16")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("x-wanted-language", "en")
        );

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.companies.[0].company_name", is("Wantedlab")))
                .andExpect(jsonPath("$.companies.[0].tags[0]", is("tag_4")))
                .andExpect(jsonPath("$.companies.[0].tags[1]", is("tag_20")))
                .andExpect(jsonPath("$.companies.[0].tags[2]", is("tag_50")));
    }
}