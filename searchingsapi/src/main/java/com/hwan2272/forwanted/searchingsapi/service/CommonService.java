package com.hwan2272.forwanted.searchingsapi.service;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommonService {

    @Autowired
    CompanyDataRepository companyDataRepository;

    public static final String equals = "equals";
    public static final String like = "like";
    public static final String ko = "ko";
    public static final String en = "en";
    public static final String ja = "ja";

    /*public List<CompanyEntity> searchConditions(String flag, String subFlag, String query) {
        switch (flag) {
            case "comp":
                this.searchCompCondition(subFlag, query);
                break;
            case "tag":
                this.searchTagCondition(subFlag, query);
                break;
            default:
                break;
        }
        return new ArrayList<>();
    }*/

    public List<CompanyEntity> searchCompCondition(String subFlag, String query) throws Exception {
        List<CompanyEntity> ceList = new ArrayList<>();
        switch (subFlag) {
            case equals:
                CompanyEntity companyEntity = companyDataRepository.findCompByCompanyKo(query);
                if(ObjectUtils.isEmpty(companyEntity) || companyEntity.getSeq() <= 0) companyEntity = companyDataRepository.findCompByCompanyEn(query);
                if(ObjectUtils.isEmpty(companyEntity) || companyEntity.getSeq() <= 0) companyEntity = companyDataRepository.findCompByCompanyJa(query);
                ceList.add(companyEntity);
                break;
            case like:
                ceList = (List) companyDataRepository.findCompByCompanyKoContaining(query);
                if(ObjectUtils.isEmpty(ceList) || ceList.get(0).getSeq() <= 0) ceList = (List) companyDataRepository.findCompByCompanyEnContaining(query);
                if(ObjectUtils.isEmpty(ceList) || ceList.get(0).getSeq() <= 0) ceList = (List) companyDataRepository.findCompByCompanyJaContaining(query);
                break;
            default:
                break;
        }

        return  ceList;
    }

    public List<CompanyEntity> searchTagCondition(String query) throws Exception {
        String lang = ko;
        List<CompanyEntity> ceList = (List) companyDataRepository.findCompByTagKoContaining(query);
        if(ObjectUtils.isEmpty(ceList) || ceList.get(0).getSeq() <= 0) {
            lang = en;
            ceList = (List) companyDataRepository.findCompByTagEnContaining(query);
        }
        if(ObjectUtils.isEmpty(ceList) || ceList.get(0).getSeq() <= 0) {
            lang = ja;
            ceList = (List) companyDataRepository.findCompByTagJaContaining(query);
        }

        List<CompanyEntity> ceList2 = new ArrayList<>();

        for(CompanyEntity ce : ceList) {
            String[] tags = null;
            if(lang.equals(ko)) tags = ce.getTagKo().split("\\|");
            if(lang.equals(en)) tags = ce.getTagEn().split("\\|");
            if(lang.equals(ja)) tags = ce.getTagJa().split("\\|");

            for(String tag : tags) {
                if (tag.equals(query)) {
                    ceList2.add(ce);
                }
                else {
                    continue;
                }
            }
        }

        return  ceList2;
    }
}
