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
import org.springframework.util.ObjectUtils;
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
            @RequestParam String query) throws Exception {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = companyService.searchComp("like", query);

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
            @PathVariable String compName) throws Exception {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = companyService.searchComp("equals", compName);

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
            @RequestBody RequestVO reqVO) throws Exception {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            CompanyEntity ce = companyService.convertRequestVOToCompEntity(reqVO);
            ce = companyService.addComp(ce);
            if(!ObjectUtils.isEmpty(ce.getCompanyExtendEntity())) {
                ce.getCompanyExtendEntity().setCompSeq(ce.getSeq());
                companyService.addCompExt(ce);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ce);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @GetMapping("/tags")
    public ResponseEntity tag_search(
            @RequestHeader("x-wanted-language") String requestLanguage,
            @RequestParam String query) throws Exception {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = companyService.searchTag(query);

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
            @RequestBody String reqVO) throws Exception {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = companyService.searchComp("equals", compName);

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

            String tagString = companyService.convertSetsToTag(sets);
            ce.setTag(tagString);
            ce.setCompanyExtendEntity(convertCe.getCompanyExtendEntity());
            ce = companyService.addComp(ce);
            if(!ObjectUtils.isEmpty(ce.getCompanyExtendEntity())) {
                ce.getCompanyExtendEntity().setCompSeq(ce.getSeq());
                companyService.addCompExt(ce);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ce);
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
            @PathVariable String tagName) throws Exception {
        Map<String, Object> returnMap = new LinkedHashMap<>();

        //공통 헤더 체크
        if (!requestLanguage.isEmpty()) {
            List<CompanyEntity> ceList = new ArrayList<>();
            ceList = companyService.searchComp("equals", compName);

            CompanyEntity ce = ceList.get(0);
            Set<String> sets = new LinkedHashSet<>(Arrays.asList(ce.getTag().split("\\|")));

            tagName = companyService.replaceQuery(tagName);
            sets.remove(tagName);
            String tagString = companyService.convertSetsToTag(sets);
            ce.setTag(tagString);
            ce = companyService.addComp(ce);
            if(!ObjectUtils.isEmpty(ce.getCompanyExtendEntity())) {
                ce.getCompanyExtendEntity().setCompSeq(ce.getSeq());
                companyService.addCompExt(ce);
            }

            //리턴 리스폰스
            List<ResponseVO> resList =  companyService.convertCompEntityToResponseVO(requestLanguage, ce);
            returnMap.put("companies", resList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null);
    }
}
