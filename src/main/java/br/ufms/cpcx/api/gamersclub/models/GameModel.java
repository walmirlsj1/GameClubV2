package br.ufms.cpcx.api.gamersclub.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Entity
@Table(name = "TB_GAME")
@Data
@NoArgsConstructor
public class GameModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ConsoleEnum console;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private PartnerModel owner;

    @JsonBackReference
    @OneToMany(mappedBy = "game", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @Column(nullable = false)
    private List<GameLoanModel> games = new ArrayList<>();
}