package com.loam.stoody.service.product.quiz;

import com.loam.stoody.dto.api.request.file.UserFileRequestDTO;
import com.loam.stoody.dto.api.request.quiz.QuizQuestionAnswerRequestDTO;
import com.loam.stoody.dto.api.request.quiz.QuizQuestionRequestDTO;
import com.loam.stoody.dto.api.request.quiz.QuizRequestDTO;
import com.loam.stoody.global.constants.ProjectConfigurationVariables;
import com.loam.stoody.model.communication.file.UserFile;
import com.loam.stoody.model.product.course.quiz.Quiz;
import com.loam.stoody.model.product.course.quiz.QuizQuestion;
import com.loam.stoody.model.product.course.quiz.QuizQuestionAnswer;
import com.loam.stoody.repository.product.quiz.QuizQuestionAnswerRepository;
import com.loam.stoody.repository.product.quiz.QuizQuestionRepository;
import com.loam.stoody.repository.product.quiz.QuizRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizQuestionAnswerRepository quizQuestionAnswerRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public Quiz fromDto(QuizRequestDTO dto){
        Quiz quiz = new Quiz();
        quiz.setId(dto.getId());
        // Set Author
        if(ProjectConfigurationVariables.stoodyEnvironment.equals(ProjectConfigurationVariables.developmentMode)){
            if(customUserDetailsService.getCurrentUser() != null){
                quiz.setAuthor(customUserDetailsService.getCurrentUser());
            }
        }else{
            if(customUserDetailsService.getCurrentUser() != null){
                quiz.setAuthor(customUserDetailsService.getCurrentUser());
            }else{
                throw new RuntimeException();
            }
        }
        return quiz;
    }

    public QuizQuestion fromDto(QuizQuestionRequestDTO dto, Long linkedQuizId){
        QuizQuestion quizQuestion = new QuizQuestion();
        quizQuestion.setId(dto.getId());
        Quiz linkedQuiz = quizRepository.findById(linkedQuizId).orElse(null);
        if(linkedQuiz == null)
            throw new RuntimeException();
        quizQuestion.setQuiz(linkedQuiz);
        quizQuestion.setQuestion(dto.getQuestion());
        quizQuestion.setTimeoutInSeconds(dto.getTimeoutInSeconds());
        return quizQuestion;
    }

    public QuizQuestionAnswer fromDto(QuizQuestionAnswerRequestDTO dto, Long linkedQuestionId){
        QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();
        quizQuestionAnswer.setId(dto.getId());
        quizQuestionAnswer.setIsTrue(dto.getIsTrue());
        quizQuestionAnswer.setAnswer(dto.getAnswer());
        QuizQuestion linkedQuestion = quizQuestionRepository.findById(linkedQuestionId).orElse(null);
        if(linkedQuestion == null)
            throw new RuntimeException();
        quizQuestionAnswer.setQuestion(linkedQuestion);
        return quizQuestionAnswer;
    }

    public QuizRequestDTO fromEntity(Quiz quizEntity) {
        QuizRequestDTO quizRequestDTO = new QuizRequestDTO();
        quizRequestDTO.setId(quizEntity.getId());

        List<QuizQuestionRequestDTO> quizQuestionDTOs = quizQuestionRepository
                .findAll()
                .stream()
                .filter(questionEntity -> questionEntity.getQuiz().getId().equals(quizEntity.getId()))
                .map(questionEntity -> {
                    QuizQuestionRequestDTO quizQuestionDTO = new QuizQuestionRequestDTO();
                    quizQuestionDTO.setId(questionEntity.getId());
                    quizQuestionDTO.setTimeoutInSeconds(questionEntity.getTimeoutInSeconds());
                    quizQuestionDTO.setQuestion(questionEntity.getQuestion());
                    quizQuestionDTO.setQuizId(quizEntity.getId());

                    List<QuizQuestionAnswerRequestDTO> quizQuestionAnswerDTOs = quizQuestionAnswerRepository
                            .findAll()
                            .stream()
                            .filter(answerEntity -> answerEntity.getQuestion().getId().equals(questionEntity.getId()))
                            .map(answerEntity -> {
                                QuizQuestionAnswerRequestDTO quizQuestionAnswerDTO = new QuizQuestionAnswerRequestDTO();
                                quizQuestionAnswerDTO.setId(answerEntity.getId());
                                quizQuestionAnswerDTO.setIsTrue(answerEntity.getIsTrue());
                                quizQuestionAnswerDTO.setAnswer(answerEntity.getAnswer());
                                quizQuestionAnswerDTO.setQuizQuestionId(questionEntity.getId());
                                return quizQuestionAnswerDTO;
                            })
                            .collect(Collectors.toList());

                    quizQuestionDTO.setAnswers(quizQuestionAnswerDTOs);
                    return quizQuestionDTO;
                })
                .collect(Collectors.toList());

        quizRequestDTO.setQuestions(quizQuestionDTOs);
        return quizRequestDTO;
    }

    public QuizRequestDTO get(Long id){
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if(quiz == null)
            throw new RuntimeException();
        return fromEntity(quiz);
    }

    public void delete(Long id){
        quizRepository.deleteById(id);
    }

    public Quiz save(QuizRequestDTO dto){
        boolean validId = dto.getId() != null && dto.getId() > 0;
        Quiz quiz;
        if(validId){
            quiz = quizRepository.findById(dto.getId()).orElse(null);
            if(quiz == null)
                quiz = new Quiz();
            else{
                final Quiz finalQuiz = quiz;
                List<QuizQuestion> questionsToRemove = quizQuestionRepository.findAll().stream().filter(e->e.getQuiz().getId().equals(finalQuiz.getId())).toList();
                List<QuizQuestionAnswer> answersToRemove = new ArrayList<>();
                quizQuestionRepository.findAll().stream().filter(e->e.getQuiz().getId().equals(finalQuiz.getId()))
                        .forEach(e->{
                            quizQuestionAnswerRepository.findAll().stream().filter(a->a.getQuestion().getId().equals(e.getId()))
                                    .forEach(answersToRemove::add);
                        });
                quizQuestionRepository.deleteAll(questionsToRemove);
                quizQuestionAnswerRepository.deleteAll(answersToRemove);
            }
        }else{
            quiz = new Quiz();
        }

        Quiz finalQuiz = quizRepository.save(quiz);

        // Save the questions
        dto.getQuestions().forEach(question->{
                QuizQuestion questionFromDTO = fromDto(question, finalQuiz.getId());
                final QuizQuestion finalQuestion = quizQuestionRepository.save(questionFromDTO);
                List<QuizQuestionAnswer> questionAnswers = new ArrayList<>();
                question.getAnswers().forEach(
                        answer->{
                            questionAnswers.add(fromDto(answer, finalQuestion.getId()));
                        }
                );
                quizQuestionAnswerRepository.saveAll(questionAnswers);
        }
        );

        return quizRepository.save(quiz);
    }

    public QuizRequestDTO mapQuizToRequest(Quiz quiz) {
        QuizRequestDTO quizRequestDTO = new QuizRequestDTO();
        quizRequestDTO.setId(quiz.getId());
        quizRequestDTO.setName(quiz.getName());
        quizRequestDTO.setThumbnailUrl(quiz.getThumbnailUrl());
        return quizRequestDTO;
    }

}
