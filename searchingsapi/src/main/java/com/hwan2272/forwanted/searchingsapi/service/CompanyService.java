package com.hwan2272.forwanted.searchingsapi.service;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.repository.CompanyDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService extends CommonService {

    @Autowired
    CompanyDataRepository companyDataRepository;

    public List<CompanyEntity> searchComp(String subFlag, String query) throws Exception {
        List<CompanyEntity> ceList = new ArrayList<>();
        if(subFlag.equals(equals)) {
            ceList = searchCompCondition(equals, query);
        }
        else if(subFlag.equals(like)) {
            ceList = searchCompCondition(like, query);
        }
        else {
            throw new Exception();
        }
        return ceList;
    }

    public List<CompanyEntity> searchTag(String query) throws Exception {
        List<CompanyEntity> ceList = searchTagCondition(query);
        return ceList;
    }

    public CompanyEntity addComp(CompanyEntity ce) throws Exception {
        companyDataRepository.save(ce);
        return ce;
    }

}
