package com.hwan2272.forwanted.searchingsapi.service;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import com.hwan2272.forwanted.searchingsapi.vo.RequestVO;
import com.hwan2272.forwanted.searchingsapi.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class CompanyService extends CommonService {

    @Autowired
    CompanyDataRepository companyDataRepository;

    public List<CompanyEntity> searchComp(String subFlag, String query) throws Exception {
        List<CompanyEntity> ceList = null;
        if(subFlag.equals(equals)) {
            ceList = searchCompCondition(equals, query);
        }
        else if(subFlag.equals(like)) {
            ceList = searchCompCondition(like, query);
        }
        else {
            throw new Exception();
        }
        if(ceList.isEmpty() || ceList.get(0).getSeq() < 0) {
            throw  new Exception();
        }
        return ceList;
    }

    public List<CompanyEntity> searchTag(String query) throws Exception {
        List<CompanyEntity> ceList = searchTagCondition(query);
        if(ceList.isEmpty() || ceList.get(0).getSeq() < 0) {
            throw  new Exception();
        }
        return ceList;
    }

    public CompanyEntity addComp(CompanyEntity ce) throws Exception {
        companyDataRepository.save(ce);
        return ce;
    }

    public List<ResponseVO> convertCompEntityToResponseVO(
            String requestLanguage, List<CompanyEntity> ceList) {
        List<ResponseVO> resList = new ArrayList<>();
        for(CompanyEntity ce : ceList) {
            ResponseVO resVO = new ResponseVO();
            if(requestLanguage.equals(LanguageEnum.ko.getName())) {
                resVO.setCompany_name(
                        ce.getCompanyKo().isEmpty() ?
                                ce.getCompanyEn().isEmpty() ?
                                        ce.getCompanyJa() : ce.getCompanyEn() : ce.getCompanyKo());
                resVO.setTags(Arrays.asList(ce.getTag().split("\\|")));
            }
            else if(requestLanguage.equals(LanguageEnum.en.getName())) {
                resVO.setCompany_name(
                        ce.getCompanyEn().isEmpty() ?
                                ce.getCompanyJa().isEmpty() ?
                                        ce.getCompanyKo() : ce.getCompanyJa() : ce.getCompanyEn());
                resVO.setTags(Arrays.asList(ce.getTag().replaceAll(LanguageEnum.tagKo.getName(), LanguageEnum.tagEn.getName()).split("\\|")));
            }
            else if(requestLanguage.equals(LanguageEnum.ja.getName())) {
                resVO.setCompany_name(
                        ce.getCompanyJa().isEmpty() ?
                                ce.getCompanyKo().isEmpty() ?
                                        ce.getCompanyEn() : ce.getCompanyKo() : ce.getCompanyJa());
                resVO.setTags(Arrays.asList(ce.getTag().replaceAll(LanguageEnum.tagKo.getName(), LanguageEnum.tagJa.getName()).split("\\|")));
            }
            resList.add(resVO);
        }
        return resList;
    }

    public CompanyEntity convertRequestVOToCompEntity(RequestVO reqVO) {
        CompanyEntity ce = new CompanyEntity();
        Map<String, String> companyMap = reqVO.getCompany_name();
        if(!ObjectUtils.isEmpty(companyMap)) {
            Iterator<String> companyIter = companyMap.keySet().iterator();

            while (companyIter.hasNext()) {
                String lang = companyIter.next();
                if (lang.equals(LanguageEnum.ko.getName())) {
                    ce.setCompanyKo(companyMap.get(lang));
                } else if (lang.equals(LanguageEnum.en.getName())) {
                    ce.setCompanyEn(companyMap.get(lang));
                } else if (lang.equals(LanguageEnum.ja.getName())) {
                    ce.setCompanyJa(companyMap.get(lang));
                }
            }

            if (!StringUtils.hasText(ce.getCompanyKo())) ce.setCompanyKo("");
            else if (!StringUtils.hasText(ce.getCompanyEn())) ce.setCompanyEn("");
            else if (!StringUtils.hasText(ce.getCompanyJa())) ce.setCompanyJa("");
        }

        List<Map<String, Map<String, String>>> tagsList = reqVO.getTags();
        Set<String> tagSets = new LinkedHashSet<>();
        for(int i=0; i<tagsList.size(); i++) {
            Map tagMap =  tagsList.get(i).get("tag_name");
            Iterator<String> tagIter = tagMap.keySet().iterator();
            while (tagIter.hasNext()) {
                String lang = tagIter.next();
                if (lang.equals(LanguageEnum.ko.getName()) || lang.equals(LanguageEnum.en.getName()) || lang.equals(LanguageEnum.ja.getName())) {
                    if (lang.equals(LanguageEnum.ko.getName())) {
                        tagSets.add(tagMap.get(lang).toString());
                    }
                } else { //확장
                    //ce.setTag(companyMap.get(lang));
                }
            }
        }

        String tagString = tagSets.toString();
        tagString = tagString.replaceAll(", ", "|")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .trim();

        ce.setTag(tagString);
        return ce;
    }

    public CompanyEntity convertTagRequestVOToCompEntity(List<Map<String, Map<String, String>>> reqList) {
        CompanyEntity ce = new CompanyEntity();
        Set<String> tagSets = new LinkedHashSet<>();
        for(int i=0; i<reqList.size(); i++) {
            Map tagMap =  reqList.get(i).get("tag_name");
            Iterator<String> tagIter = tagMap.keySet().iterator();
            while (tagIter.hasNext()) {
                String lang = tagIter.next();
                if (lang.equals(LanguageEnum.ko.getName()) || lang.equals(LanguageEnum.en.getName()) || lang.equals(LanguageEnum.ja.getName())) {
                    if (lang.equals(LanguageEnum.ko.getName())) {
                        tagSets.add(tagMap.get(lang).toString());
                    }
                } else { //확장
                    //ce.setTag(companyMap.get(lang));
                }
            }
        }

        String tagString = tagSets.toString();
        tagString = tagString.replaceAll(", ", "|")
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .trim();

        ce.setTag(tagString);
        return ce;
    }

}
