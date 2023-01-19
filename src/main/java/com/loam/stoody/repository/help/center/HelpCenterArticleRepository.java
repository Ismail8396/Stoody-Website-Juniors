package com.loam.stoody.repository.help.center;


import com.loam.stoody.model.help.center.HelpCenterArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpCenterArticleRepository extends JpaRepository<HelpCenterArticle, Long> {
}
