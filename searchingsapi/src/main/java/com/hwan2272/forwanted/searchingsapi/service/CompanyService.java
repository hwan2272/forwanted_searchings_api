package com.hwan2272.forwanted.searchingsapi.service;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.entity.CompanyExtendEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyExtendDataRepository;
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

    @Autowired
    CompanyExtendDataRepository companyExtendDataRepository;

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
        query = replaceQuery(query);

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

    public CompanyEntity addCompExt(CompanyEntity ce) throws Exception {
        companyExtendDataRepository.save(ce.getCompanyExtendEntity());
        return ce;
    }

    public List<ResponseVO> convertCompEntityToResponseVO(
            String requestLanguage, CompanyEntity ce) throws Exception {
        List<CompanyEntity> ceList = new ArrayList<>();
        ceList.add(ce);
        return this.convertCompEntityToResponseVO(requestLanguage, ceList);
    }

    public List<ResponseVO> convertCompEntityToResponseVO(
            String requestLanguage, List<CompanyEntity> ceList) throws Exception {
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
            else if(requestLanguage.equals(LanguageEnum.ja.getName()) || requestLanguage.equals(LanguageEnum.jp.getName())) {
                resVO.setCompany_name(
                        ce.getCompanyJa().isEmpty() ?
                                ce.getCompanyKo().isEmpty() ?
                                        ce.getCompanyEn() : ce.getCompanyKo() : ce.getCompanyJa());
                resVO.setTags(Arrays.asList(ce.getTag().replaceAll(LanguageEnum.tagKo.getName(), LanguageEnum.tagJa.getName()).split("\\|")));
            }
            else { //lang이 ko,en,ja도 아니라면 extend에서 조회
                CompanyExtendEntity cee = companyExtendDataRepository.findCompSeqAndLang(ce.getSeq(), requestLanguage);
                resVO.setCompany_name(cee.getCompany());
                resVO.setTags(Arrays.asList(cee.getTag().split("\\|")));
            }
            resList.add(resVO);
        }
        return resList;
    }

    public CompanyEntity convertRequestVOToCompEntity(RequestVO reqVO) throws Exception {
        CompanyEntity ce = new CompanyEntity();
        CompanyExtendEntity cee = new CompanyExtendEntity();

        Map<String, String> companyMap = reqVO.getCompany_name();
        if(!ObjectUtils.isEmpty(companyMap)) {
            Iterator<String> companyIter = companyMap.keySet().iterator();

            while (companyIter.hasNext()) {
                String lang = companyIter.next();
                if (lang.equals(LanguageEnum.ko.getName())) {
                    ce.setCompanyKo(companyMap.get(lang));
                }
                else if (lang.equals(LanguageEnum.en.getName())) {
                    ce.setCompanyEn(companyMap.get(lang));
                }
                else if (lang.equals(LanguageEnum.ja.getName()) || lang.equals(LanguageEnum.jp.getName())) {
                    ce.setCompanyJa(companyMap.get(lang));
                }
                else {
                    cee.setLang(lang);
                    cee.setCompany(companyMap.get(lang));
                    ce.setCompanyExtendEntity(cee);
                }
            }

            if (!StringUtils.hasText(ce.getCompanyKo())) ce.setCompanyKo("");
            else if (!StringUtils.hasText(ce.getCompanyEn())) ce.setCompanyEn("");
            else if (!StringUtils.hasText(ce.getCompanyJa())) ce.setCompanyJa("");
        }

        List<Map<String, Map<String, String>>> tagsList = reqVO.getTags();
        Set<String> tagSets = new LinkedHashSet<>();
        Set<String> tagEtcSets = new LinkedHashSet<>();
        for(int i=0; i<tagsList.size(); i++) {
            Map tagMap =  tagsList.get(i).get("tag_name");
            Iterator<String> tagIter = tagMap.keySet().iterator();
            while (tagIter.hasNext()) {
                String lang = tagIter.next();
                if (lang.equals(LanguageEnum.ko.getName())
                        || lang.equals(LanguageEnum.en.getName())
                        || lang.equals(LanguageEnum.ja.getName())
                        || lang.equals(LanguageEnum.jp.getName())) {
                    if (lang.equals(LanguageEnum.ko.getName())) {
                        tagSets.add(tagMap.get(lang).toString());

                        String tagString = convertSetsToTag(tagSets);
                        ce.setTag(tagString);
                    }
                } else { //lang이 ko,en,ja도 아니라면 extend에 save
                    cee.setLang(lang);
                    tagEtcSets.add(tagMap.get(lang).toString());

                    String tagString = convertSetsToTag(tagEtcSets);
                    cee.setTag(tagString);
                    ce.setCompanyExtendEntity(cee);
                }
            }
        }

        return ce;
    }

    public CompanyEntity convertTagRequestVOToCompEntity(List<Map<String, Map<String, String>>> reqList) throws Exception {
        CompanyEntity ce = new CompanyEntity();
        CompanyExtendEntity cee = new CompanyExtendEntity();

        Set<String> tagSets = new LinkedHashSet<>();
        Set<String> tagEtcSets = new LinkedHashSet<>();
        for(int i=0; i<reqList.size(); i++) {
            Map tagMap =  reqList.get(i).get("tag_name");
            Iterator<String> tagIter = tagMap.keySet().iterator();
            while (tagIter.hasNext()) {
                String lang = tagIter.next();
                if (lang.equals(LanguageEnum.ko.getName())
                        || lang.equals(LanguageEnum.en.getName())
                        || lang.equals(LanguageEnum.ja.getName())
                        || lang.equals(LanguageEnum.jp.getName())) {
                    if (lang.equals(LanguageEnum.ko.getName())) {
                        tagSets.add(tagMap.get(lang).toString());

                        String tagString = convertSetsToTag(tagSets);
                        ce.setTag(tagString);
                    }
                } else { //lang이 ko,en,ja도 아니라면 extend에 save
                    cee.setLang(lang);
                    tagEtcSets.add(tagMap.get(lang).toString());

                    String tagString = convertSetsToTag(tagEtcSets);
                    cee.setTag(tagString);
                    ce.setCompanyExtendEntity(cee);
                }
            }
        }

        return ce;
    }

}
