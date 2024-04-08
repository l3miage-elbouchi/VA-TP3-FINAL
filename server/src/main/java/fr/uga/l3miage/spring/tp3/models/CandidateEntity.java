package fr.uga.l3miage.spring.tp3.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateEntity extends UserEntity {
    private Long id;
    private String Email;
    private LocalDate birthDate;
    private boolean hasExtraTime;
    private String firstname;
    private String lastname;

    @OneToMany(mappedBy = "candidateEntity")
    private Set<CandidateEvaluationGridEntity> candidateEvaluationGridEntities;

    @ManyToOne
    private TestCenterEntity testCenterEntity;
    public CandidateEntity firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public CandidateEntity lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }
}
