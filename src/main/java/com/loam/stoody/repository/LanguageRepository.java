package com.loam.stoody.repository;

import com.loam.stoody.model.LanguageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageModel, Integer> {
    LanguageModel findByKeyAndLocale(String key, String locale);

    @Query(value = "select * from languages lang where lang.locale like %:filter% or lang.messagekey like %:filter% or lang.messagecontent like %:filter%", nativeQuery = true)
    List<LanguageModel> findBySearchKey(@Param("filter") String filter);

}