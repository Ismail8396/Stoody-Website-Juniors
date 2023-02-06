package com.loam.stoody.controller.product;

import com.loam.stoody.dto.api.request.quiz.QuizRequestDTO;
import com.loam.stoody.model.product.course.quiz.Quiz;
import com.loam.stoody.service.product.quiz.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stoody/api/v1/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<Quiz> saveQuiz(@RequestBody QuizRequestDTO quizRequestDTO) {
        Quiz savedQuiz = quizService.save(quizRequestDTO);
        return ResponseEntity.ok(savedQuiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        quizService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizRequestDTO> getQuiz(@PathVariable Long id) {
        QuizRequestDTO quiz = quizService.get(id);
        return ResponseEntity.ok(quiz);
    }
}
