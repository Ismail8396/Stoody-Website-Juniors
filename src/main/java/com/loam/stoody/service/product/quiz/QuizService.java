package com.loam.stoody.service.product.quiz;

import com.loam.stoody.dto.api.request.QuizQuestionAnswerRequestDTO;
import com.loam.stoody.dto.api.request.QuizRequestDTO;
import com.loam.stoody.dto.api.response.CourseLectureResponseDTO;
import com.loam.stoody.dto.api.response.QuizQuestionAnswerResponseDTO;
import com.loam.stoody.dto.api.response.QuizQuestionResponseDTO;
import com.loam.stoody.dto.api.response.QuizResponseDTO;
import com.loam.stoody.model.product.course.CourseLecture;
import com.loam.stoody.model.product.course.quiz.Quiz;
import com.loam.stoody.model.product.course.quiz.QuizQuestion;
import com.loam.stoody.model.product.course.quiz.QuizQuestionAnswer;
import com.loam.stoody.model.user.User;
import com.loam.stoody.repository.product.quiz.QuizQuestionAnswerRepository;
import com.loam.stoody.repository.product.quiz.QuizQuestionRepository;
import com.loam.stoody.repository.product.quiz.QuizRepository;
import com.loam.stoody.service.user.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizQuestionAnswerRepository quizQuestionAnswerRepository;
    private final CustomUserDetailsService customUserDetailsService;

    public void findAllQuizDetails(Long lectureId, CourseLectureResponseDTO courseLectureResponseDTO) {
        Quiz quiz = quizRepository.findByCourseLecture_Id(lectureId);
        if (Objects.isNull(quiz)) return;

        QuizResponseDTO quizResponseDTO = new QuizResponseDTO();
        quizResponseDTO.setId(quiz.getId());

        List<QuizQuestion> quizQuestions = quizQuestionRepository.findAllByQuiz_Id(quiz.getId());
        if (CollectionUtils.isEmpty(quizQuestions)) return;

        List<QuizQuestionResponseDTO> questionResponseDTOS = new ArrayList<>();
        quizQuestions.forEach(question -> {
            QuizQuestionResponseDTO quizQuestionResponseDTO = new QuizQuestionResponseDTO();
            quizQuestionResponseDTO.setQuestion(question.getQuestion());
            findAllQuestionAnswers(question.getId(), quizQuestionResponseDTO);
            questionResponseDTOS.add(quizQuestionResponseDTO);
        });
        quizResponseDTO.setQuizQuestions(questionResponseDTOS);
        courseLectureResponseDTO.setQuiz(quizResponseDTO);
    }

    private void findAllQuestionAnswers(Long questionId, QuizQuestionResponseDTO quizQuestionResponseDTO) {
        List<QuizQuestionAnswer> answers = quizQuestionAnswerRepository.findAllByQuestion_Id(questionId);
        if (CollectionUtils.isEmpty(answers)) return;

        List<QuizQuestionAnswerResponseDTO> quizQuestionAnswersDtos = new ArrayList<>();
        answers.forEach(answer -> {
            QuizQuestionAnswerResponseDTO responseDTO = new QuizQuestionAnswerResponseDTO();
            responseDTO.setId(answer.getId());
            responseDTO.setAnswer(answer.getAnswer());
            responseDTO.setIsTrue(answer.getIsTrue());
            quizQuestionAnswersDtos.add(responseDTO);
        });
        quizQuestionResponseDTO.setQuizQuestionAnswers(quizQuestionAnswersDtos);
    }

    public void save(QuizRequestDTO quizDTO, CourseLecture courseLecture) {
        if (Objects.isNull(quizDTO)) return;

        Quiz quiz = new Quiz();
        //getting current login user
        User user = customUserDetailsService.getCurrentUser();
        if (null == user) user = customUserDetailsService.getUserByUsername("Stoody");
        if (null != user) quiz.setAuthor(user);
        quiz.setId(quizDTO.getId());
        quiz.setCourseLecture(courseLecture);
        quizRepository.save(quiz);
        //save quiz questions
        if (!CollectionUtils.isEmpty(quizDTO.getQuizQuestions())) {
            quizDTO.getQuizQuestions().forEach(quizQuestionDTO -> {
                QuizQuestion question = new QuizQuestion();
                question.setId(quizQuestionDTO.getId());
                question.setQuestion(quizQuestionDTO.getQuestion());
                question.setQuiz(quiz);
                quizQuestionRepository.save(question);
                //save question answers
                saveQuizAnswers(question, quizQuestionDTO.getQuizQuestionAnswers());
            });
        }

    }

    private void saveQuizAnswers(QuizQuestion question, List<QuizQuestionAnswerRequestDTO> quizQuestionAnswerDTOS) {
        if (CollectionUtils.isEmpty(quizQuestionAnswerDTOS)) return;

        quizQuestionAnswerDTOS.forEach(answersDTO -> {
            QuizQuestionAnswer quizQuestionAnswer = new QuizQuestionAnswer();
            quizQuestionAnswer.setId(answersDTO.getId());
            quizQuestionAnswer.setQuestion(question);
            quizQuestionAnswer.setAnswer(answersDTO.getAnswer());
            quizQuestionAnswer.setIsTrue(answersDTO.getIsTrue());
            quizQuestionAnswerRepository.save(quizQuestionAnswer);
        });
    }

    public void deleteAllQuiz(List<CourseLecture> courseLectureList) {
        if (CollectionUtils.isEmpty(courseLectureList)) return;
        courseLectureList.forEach(courseLecture -> {
            Quiz quizzes = quizRepository.findByCourseLecture_Id(courseLecture.getId());
            if (CollectionUtils.isEmpty(courseLectureList) || null == quizzes) return;
            //delete all quiz questions and questionAnswers
            deleteQuizQuestionsAndAnswers(quizzes);
            quizRepository.deleteAllByCourseLecture_Id(courseLecture.getId());
        });
    }

    private void deleteQuizQuestionsAndAnswers(Quiz quiz) {
        quizQuestionAnswerRepository.deleteAllByQuizId(quiz.getId());
        quizQuestionRepository.deleteAllByQuiz_Id(quiz.getId());
    }
}
