package br.ufms.cpcx.api.gamersclub.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_GAME_LOAN")
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GameLoanModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonManagedReference
    @JsonIgnoreProperties("games")
    @ManyToOne
    @JoinColumn(name = "partner_id", nullable = false)
    private PartnerModel partner;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameModel game;

    @CreatedDate
    private LocalDateTime loanDate;

    private LocalDateTime scheduledReturnDate;

    private LocalDateTime returnDate;

//    id, partner, game, loanDate, scheduledReturnDate , returnDate

}
