package com.hwan2272.forwanted.searchingsapi.repository;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyEntity, Long> {
    public CompanyEntity findCompBySeq(Integer compSeq);
    public CompanyEntity findCompByCompanyKo(String companyKo);
    public CompanyEntity findCompByCompanyEn(String companyEn);
    public CompanyEntity findCompByCompanyJa(String companyJa);

    //// compName Search
    public Iterable<CompanyEntity> findCompByCompanyKoContaining(String companyKo);
    public Iterable<CompanyEntity> findCompByCompanyEnContaining(String companyEn);
    public Iterable<CompanyEntity> findCompByCompanyJaContaining(String companyJa);

    //// tagName Search
    public Iterable<CompanyEntity> findCompByTagKoContaining(String companyKo);
    public Iterable<CompanyEntity> findCompByTagEnContaining(String companyEn);
    public Iterable<CompanyEntity> findCompByTagJaContaining(String companyJa);

}
