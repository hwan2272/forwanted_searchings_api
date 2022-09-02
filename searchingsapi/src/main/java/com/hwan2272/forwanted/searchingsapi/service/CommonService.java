package com.hwan2272.forwanted.searchingsapi.service;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.entity.CompanyExtendEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CommonService {

    @Autowired
    CompanyDataRepository companyDataRepository;

    public static final String equals = "equals";
    public static final String like = "like";


    public String replaceQuery(String query) {
        //언어에 따라 바꿔주는 로직 추가
        query = query
                .replaceAll(LanguageEnum.tagEn.getName(), LanguageEnum.tagKo.getName())
                .replaceAll(LanguageEnum.tagJa.getName(), LanguageEnum.tagKo.getName());
        return query;
    }

    public String convertSetsToTag(Set<String> sets) {
        String tagString = sets.toString();
        tagString = tagString.replaceAll(", ", "|")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .trim();
        return tagString;
    }

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
        List<CompanyEntity> ceList = (List) companyDataRepository.findCompByTagContaining(query);
        List<CompanyEntity> ceList2 = new ArrayList<>();

        for(CompanyEntity ce : ceList) {
            String[] tags = ce.getTag().split("\\|");

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
