package com.loam.stoody.service.documentation;

import com.loam.stoody.dto.api.request.documentation.DocumentationCategoryDTO;
import com.loam.stoody.dto.api.request.documentation.DocumentationLectureDTO;
import com.loam.stoody.dto.api.request.documentation.DocumentationSectionDTO;
import com.loam.stoody.model.documentation.DocumentationCategory;
import com.loam.stoody.model.documentation.DocumentationLecture;
import com.loam.stoody.model.documentation.DocumentationSection;
import com.loam.stoody.repository.documentation.DocumentationCategoryRepository;
import com.loam.stoody.repository.documentation.DocumentationLectureRepository;
import com.loam.stoody.repository.documentation.DocumentationSectionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DocumentationService {

    private DocumentationCategoryRepository documentationCategoryRepository;
    private DocumentationSectionRepository documentationSectionRepository;
    private DocumentationLectureRepository documentationLectureRepository;

    public DocumentationCategory mapDocumentationCategoryDTOToEntity(DocumentationCategoryDTO dto) {
        DocumentationCategory entity = new DocumentationCategory();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public DocumentationCategoryDTO mapDocumentationCategoryEntityToDTO(DocumentationCategory entity) {
        DocumentationCategoryDTO dto = new DocumentationCategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public DocumentationCategory saveDocumentationCategory(DocumentationCategory documentationCategory){
        return documentationCategoryRepository.save(documentationCategory);
    }

    public DocumentationCategoryDTO saveDocumentationCategory(DocumentationCategoryDTO documentationCategoryDTO){
        return mapDocumentationCategoryEntityToDTO(documentationCategoryRepository.save(mapDocumentationCategoryDTOToEntity(documentationCategoryDTO)));
    }

    public List<DocumentationCategoryDTO> getDocumentationCategoryAll(){
        return documentationCategoryRepository.findAll().stream().map(this::mapDocumentationCategoryEntityToDTO).toList();
    }

    public void deleteDocumentationCategoryById(Long id){
         documentationCategoryRepository.deleteById(id);
    }

    public DocumentationCategoryDTO getDocumentationCategoryById(Long id){
        DocumentationCategory documentationCategory = documentationCategoryRepository.findById(id).orElse(null);
        if(documentationCategory != null)
            return mapDocumentationCategoryEntityToDTO(documentationCategory);
        else
            return null;
    }

    // -> DocumentationSection Service
    public DocumentationSectionDTO mapDocumentationSectionEntityToDTO(DocumentationSection entity) {
        DocumentationSectionDTO dto = new DocumentationSectionDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setThumbnailUrl(entity.getThumbnailUrl());
        dto.setVisitCount(entity.getVisitCount());
        if (entity.getCategory() != null)
            dto.setCategoryId(entity.getCategory().getId());
        return dto;
    }

    public DocumentationSection mapDocumentationSectionDTOToEntity(DocumentationSectionDTO dto) {
        DocumentationSection entity = new DocumentationSection();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setThumbnailUrl(dto.getThumbnailUrl());
        entity.setVisitCount(dto.getVisitCount());
        entity.setCategory(dto.getCategoryId() == null ? null : documentationCategoryRepository.findById(dto.getCategoryId()).orElse(null));
        return entity;
    }

    public DocumentationSectionDTO saveDocumentationSection(DocumentationSectionDTO documentationSectionDTO) {
        return mapDocumentationSectionEntityToDTO(documentationSectionRepository.save(mapDocumentationSectionDTOToEntity(documentationSectionDTO)));
    }

    public List<DocumentationSectionDTO> getDocumentationSectionAll() {
        return documentationSectionRepository.findAll().stream().map(this::mapDocumentationSectionEntityToDTO).toList();
    }

    public DocumentationSectionDTO getDocumentationSectionById(Long id) {
        DocumentationSection documentationSection = documentationSectionRepository.findById(id).orElse(null);
        if (documentationSection != null)
            return mapDocumentationSectionEntityToDTO(documentationSection);
        else
            return null;
    }

    public void deleteDocumentationSectionById(Long id) {
        documentationSectionRepository.deleteById(id);
    }

    // -> DocumentationLecture Service
    public DocumentationLectureDTO mapDocumentationLectureEntityToDTO(DocumentationLecture entity) {
        DocumentationLectureDTO dto = new DocumentationLectureDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        dto.setName(entity.getName());
        dto.setSectionId(entity.getSection().getId());

        return dto;
    }
    public DocumentationLecture mapDocumentationLectureDTOToEntity(DocumentationLectureDTO dto) {
        DocumentationLecture entity = new DocumentationLecture();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());
        entity.setName(dto.getName());
        entity.setSection(dto.getSectionId() == null ? null : documentationSectionRepository.findById(dto.getSectionId()).orElse(null));

        return entity;
    }

    public DocumentationLectureDTO saveDocumentationLecture(DocumentationLectureDTO documentationLectureDTO) {
        return mapDocumentationLectureEntityToDTO(documentationLectureRepository.save(mapDocumentationLectureDTOToEntity(documentationLectureDTO)));
    }

    public List<DocumentationLectureDTO> getDocumentationLectureAll() {
        return documentationLectureRepository.findAll().stream().map(this::mapDocumentationLectureEntityToDTO).toList();
    }

    public DocumentationLectureDTO getDocumentationLectureById(Long id) {
        DocumentationLecture documentationLecture = documentationLectureRepository.findById(id).orElse(null);
        if (documentationLecture != null)
            return mapDocumentationLectureEntityToDTO(documentationLecture);
        else
            return null;
    }

    public void deleteDocumentationLectureById(Long id) {
        documentationLectureRepository.deleteById(id);
    }
}
