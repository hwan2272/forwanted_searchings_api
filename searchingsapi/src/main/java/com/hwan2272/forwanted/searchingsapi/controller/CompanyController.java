package com.hwan2272.forwanted.searchingsapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.service.CompanyService;
import com.hwan2272.forwanted.searchingsapi.service.LanguageEnum;
import com.hwan2272.forwanted.searchingsapi.vo.RequestVO;
import com.hwan2272.forwanted.searchingsapi.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Slf4j
public class CompanyController {
    @Autowired
    CompanyService companyService;

    @GetMapping("/search")
    public ResponseEntity company_search_like(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @RequestParam String query) {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = new ArrayList<>();

            try {
                ceList = companyService.searchComp("like", query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ceList);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @GetMapping("/companies/{compName}")
    public ResponseEntity company_search_equals(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @PathVariable String compName) {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = new ArrayList<>();

            try {
                ceList = companyService.searchComp("equals", compName);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ceList);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @PostMapping("/companies")
    public ResponseEntity company_add(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @RequestBody RequestVO reqVO) {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            CompanyEntity ce = companyService.convertRequestVOToCompEntity(reqVO);

            try {
                companyService.addComp(ce);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            //리턴 리스폰스
            //리스폰스 요구에 맞게 수정 필요
            //List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ceList);
            returnMap.put("companies", ce);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @GetMapping("/tags")
    public ResponseEntity tag_search(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @RequestParam String query) {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = new ArrayList<>();

            try {
                //언어에 따라 바꿔주는 로직 추가
                query = query
                        .replaceAll(LanguageEnum.tagEn.getName(), LanguageEnum.tagKo.getName())
                        .replaceAll(LanguageEnum.tagJa.getName(), LanguageEnum.tagKo.getName());

                ceList = companyService.searchTag(query);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ceList);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @PutMapping("/companies/{compName}/tags")
    public ResponseEntity tag_add(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @PathVariable String compName,
            @RequestBody String reqVO) {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = new ArrayList<>();

            try {
                ceList = companyService.searchComp("equals", compName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            CompanyEntity ce = ceList.get(0);
            Set<String> sets = new LinkedHashSet<>(Arrays.asList(ce.getTag().split("\\|")));

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String,Map<String,String>>> tagReqList = null;
            try {
                tagReqList = objectMapper.readValue(reqVO, ArrayList.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            CompanyEntity convertCe = companyService.convertTagRequestVOToCompEntity(tagReqList);
            Set<String> reqTagAddSets = new LinkedHashSet<>(Arrays.asList(convertCe.getTag().split("\\|")));

            sets.addAll(reqTagAddSets);

            String tagString = sets.toString();
            tagString = tagString.replaceAll(", ", "|")
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .trim();

            ce.setTag(tagString);
            try {
                companyService.addComp(ce);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            //리턴 리스폰스
            //정렬? 고민
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ceList);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @DeleteMapping("/companies/{compName}/tags/{tagName}")
    public ResponseEntity tag_delete(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @PathVariable String compName,
            @PathVariable String tagName) {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = new ArrayList<>();

            try {
                ceList = companyService.searchComp("equals", compName);
            } catch (Exception e) {
                e.printStackTrace();
            }

            CompanyEntity ce = ceList.get(0);
            Set<String> sets = new LinkedHashSet<>(Arrays.asList(ce.getTag().split("\\|")));

            //언어에 따라 바꿔주는 로직 추가
            tagName = tagName
                    .replaceAll(LanguageEnum.tagEn.getName(), LanguageEnum.tagKo.getName())
                    .replaceAll(LanguageEnum.tagJa.getName(), LanguageEnum.tagKo.getName());

            sets.remove(tagName);
            String tagString = sets.toString();
            tagString = tagString.replaceAll(", ", "|")
                    .replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .trim();

            ce.setTag(tagString);

            try {
                companyService.addComp(ce);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ceList);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }
}
