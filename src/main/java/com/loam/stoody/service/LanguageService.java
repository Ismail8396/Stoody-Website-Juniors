package com.loam.stoody.service;

import com.loam.stoody.model.LanguageModel;
import com.loam.stoody.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageRepository languageRepository;

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

    public List<LanguageModel> getLanguageModelsSorted(String field){
        return languageRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Page<LanguageModel> getLanguageModelsPaginated(int offset, int pageSize){
        Page<LanguageModel> paginatedLanguageModelList = languageRepository.findAll(PageRequest.of(offset,pageSize));
        return paginatedLanguageModelList;
    }
}
