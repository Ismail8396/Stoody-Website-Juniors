package com.loam.stoody.controller.control.documentation;

import com.loam.stoody.dto.api.request.documentation.DocumentationCategoryDTO;
import com.loam.stoody.dto.api.request.documentation.DocumentationLectureDTO;
import com.loam.stoody.dto.api.request.documentation.DocumentationSectionDTO;
import com.loam.stoody.dto.api.response.OutdoorResponse;
import com.loam.stoody.enums.IndoorResponse;
import com.loam.stoody.global.constants.PRL;
import com.loam.stoody.service.documentation.DocumentationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class DocumentationController {
    private final DocumentationService documentationService;

    @GetMapping("/docs")
    public String getDocumentationLanding() {
        return null;
    }

    @GetMapping("/stoody/authorized/docs/category")
    public String getAdminDocsCategory(Model model) {
        model.addAttribute("documentationCategoriesDTO", documentationService.getDocumentationCategoryAll());
        model.addAttribute("documentationCategoryDTO", new DocumentationCategoryDTO());
        return "pages/dashboard/admin-docs-category";
    }

    @PostMapping("/stoody/authorized/docs/category")
    public String postAdminDocsCategory(@ModelAttribute("documentationCategoryDTO") DocumentationCategoryDTO documentationCategoryDTO) {
        documentationService.saveDocumentationCategory(documentationCategoryDTO);
        return "redirect:/stoody/authorized/docs/category";
    }

    @PostMapping("/stoody/authorized/docs/category/{id}")
    public String adminDocsCategoryDelete(@PathVariable("id") Long id) {
        documentationService.deleteDocumentationCategoryById(id);
        return "redirect:/stoody/authorized/docs/category";
    }

    @GetMapping("/stoody/authorized/docs/section")
    public String getAdminDocsSection(Model model) {
        model.addAttribute("documentationSectionsDTO", documentationService.getDocumentationSectionAll());
        model.addAttribute("documentationSectionDTO", new DocumentationSectionDTO());
        model.addAttribute("documentationCategoriesDTO", documentationService.getDocumentationCategoryAll());
        return "pages/dashboard/admin-docs-section";
    }

    @PostMapping("/stoody/authorized/docs/section")
    public String postAdminDocsSection(@ModelAttribute("documentationSectionDTO") DocumentationSectionDTO documentationSectionDTO) {
        documentationService.saveDocumentationSection(documentationSectionDTO);
        return "redirect:/stoody/authorized/docs/section";
    }

    @PostMapping("/stoody/authorized/docs/section/delete/{id}")
    public String adminDocsSectionDelete(@PathVariable("id") Long id) {
        documentationService.deleteDocumentationSectionById(id);
        return "redirect:/stoody/authorized/docs/section";
    }

    // ->  DocumentationCategory RestController
    @PostMapping(PRL.apiPrefix + "/docs/save/category")
    @ResponseBody
    public DocumentationCategoryDTO saveDocumentationCategory(@RequestBody DocumentationCategoryDTO documentationCategoryDTO) {
        return documentationService.saveDocumentationCategory(documentationCategoryDTO);
    }

    @GetMapping(PRL.apiPrefix + "/docs/get/category/all")
    @ResponseBody
    public List<DocumentationCategoryDTO> getDocumentationCategoryAll() {
        return documentationService.getDocumentationCategoryAll();
    }

    @GetMapping(PRL.apiPrefix + "/docs/get/category/{id}")
    @ResponseBody
    public DocumentationCategoryDTO getDocumentationCategoryById(@PathVariable("id") Long id) {
        return documentationService.getDocumentationCategoryById(id);
    }

    @DeleteMapping(PRL.apiPrefix + "/docs/delete/category/{id}")
    @ResponseBody
    public OutdoorResponse<?> deleteDocumentationCategoryById(@PathVariable("id")Long id) {
        documentationService.deleteDocumentationCategoryById(id);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS);
    }

    // ->  DocumentationSection RestController
    @PostMapping(PRL.apiPrefix + "/docs/save/section")
    @ResponseBody
    public DocumentationSectionDTO saveDocumentationSection(@RequestBody DocumentationSectionDTO documentationSectionDTO) {
        return documentationService.saveDocumentationSection(documentationSectionDTO);
    }

    @GetMapping(PRL.apiPrefix + "/docs/get/section/all")
    @ResponseBody
    public List<DocumentationSectionDTO> getDocumentationSectionAll() {
        return documentationService.getDocumentationSectionAll();
    }

    @GetMapping(PRL.apiPrefix + "/docs/get/section/{id}")
    @ResponseBody
    public DocumentationSectionDTO getDocumentationSectionById(@PathVariable("id") Long id) {
        return documentationService.getDocumentationSectionById(id);
    }

    @DeleteMapping(PRL.apiPrefix + "/docs/delete/section/{id}")
    @ResponseBody
    public OutdoorResponse<?> deleteDocumentationSectionById(@PathVariable("id")Long id) {
        documentationService.deleteDocumentationSectionById(id);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS);
    }

    // ->  DocumentationLecture RestController
    @PostMapping(PRL.apiPrefix + "/docs/save/lecture")
    @ResponseBody
    public DocumentationLectureDTO saveDocumentationLecture(@RequestBody DocumentationLectureDTO documentationLectureDTO) {
        return documentationService.saveDocumentationLecture(documentationLectureDTO);
    }

    @GetMapping(PRL.apiPrefix + "/docs/get/lecture/all")
    @ResponseBody
    public List<DocumentationLectureDTO> getDocumentationLectureAll() {
        return documentationService.getDocumentationLectureAll();
    }

    @GetMapping(PRL.apiPrefix + "/docs/get/lecture/{id}")
    @ResponseBody
    public DocumentationLectureDTO getDocumentationLectureById(@PathVariable("id") Long id) {
        return documentationService.getDocumentationLectureById(id);
    }

    @DeleteMapping(PRL.apiPrefix + "/docs/delete/lecture/{id}")
    @ResponseBody
    public OutdoorResponse<?> deleteDocumentationLectureById(@PathVariable("id")Long id) {
        documentationService.deleteDocumentationLectureById(id);
        return new OutdoorResponse<>(IndoorResponse.SUCCESS, IndoorResponse.SUCCESS);
    }

}
