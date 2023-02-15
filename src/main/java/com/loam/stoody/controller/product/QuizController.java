package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.request.quiz.QuizRequestDTO;
import com.loam.stoody.model.product.course.quiz.Quiz;
import com.loam.stoody.service.product.quiz.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/stoody/api/v1/quizzes")
    @ResponseBody
    public ResponseEntity<QuizRequestDTO> saveQuiz(@RequestBody QuizRequestDTO quizRequestDTO) {
        return ResponseEntity.ok(quizService.save(quizRequestDTO));
    }

    @DeleteMapping("/stoody/api/v1/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stoody/api/v1/quizzes/{id}")
    @ResponseBody
    public ResponseEntity<QuizRequestDTO> getQuiz(@PathVariable Long id) {
        QuizRequestDTO quiz = quizService.get(id);
        return ResponseEntity.ok(quiz);
    }

    @GetMapping("/stoody/api/v1/quizzes/all")
    @ResponseBody
    public List<QuizRequestDTO> getAllQuiz() {
        return quizService.getAll(false);
    }

    @GetMapping("/stoody/api/v1/quizzes/all/current/user")
    @ResponseBody
    public List<QuizRequestDTO> getCurrentUserAllQuiz() {
        return quizService.getAll(true);
    }
}
