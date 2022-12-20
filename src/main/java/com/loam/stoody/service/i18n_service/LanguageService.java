package com.loam.stoody.service.i18n_service;

import com.loam.stoody.model.i18n_models.LanguageModel;
import com.loam.stoody.repository.i18n_repo.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    private final LanguageRepository languageRepository;

    @Autowired
    public LanguageService(LanguageRepository languageRepository){
        this.languageRepository = languageRepository;
    }

    public void addLanguageModel(LanguageModel languageModel){
        languageRepository.save(languageModel);
    }

    public void removeLanguageModel(LanguageModel languageModel){
        languageRepository.delete(languageModel);
    }

    public List<LanguageModel> getAllLanguageModels(){
        return languageRepository.findAll();
    }

    public List<LanguageModel> getAllLanguagesBySearchKey(String filter){
        return languageRepository.findBySearchKey(filter);
    }

    @Deprecated
    public List<LanguageModel> getLanguageModelsSorted(String field){
        return languageRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    @Deprecated
    public Page<LanguageModel> getLanguageModelsPaginated(int offset, int pageSize){
        return languageRepository.findAll(PageRequest.of(offset,pageSize));
    }
}
