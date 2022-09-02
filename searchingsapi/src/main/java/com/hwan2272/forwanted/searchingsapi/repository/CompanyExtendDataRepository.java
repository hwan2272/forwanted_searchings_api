package com.hwan2272.forwanted.searchingsapi.repository;

import com.hwan2272.forwanted.searchingsapi.entity.CompanyEntity;
import com.hwan2272.forwanted.searchingsapi.entity.CompanyExtendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CompanyExtendDataRepository extends JpaRepository<CompanyExtendEntity, Long> {
    public CompanyExtendEntity findCompExtBySeq(Integer seq);
    public CompanyExtendEntity findCompExtByCompSeq(Integer compSeq);

    @Query(value = "select * from comp_ext cee where cee.comp_seq= :compSeq and cee.lang= :lang", nativeQuery = true)
    public CompanyExtendEntity findCompSeqAndLang(
            @Param("compSeq") Integer compSeq, @Param("lang") String lang);

}
