package fr.uga.l3miage.spring.tp3.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateEvaluationGridEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long sheetNumber;

    @Column(scale = 2)
    private double grade;

    private LocalDateTime submissionDate;

    @ManyToOne
    private CandidateEntity candidateEntity;

    @ManyToOne
    private ExaminerEntity examinerEntity;

    @ManyToOne
    private ExamEntity examEntity;

    @ManyToMany
    private Set<EvaluationCriteriaEntity> evaluationCriteriaEntities;


}
