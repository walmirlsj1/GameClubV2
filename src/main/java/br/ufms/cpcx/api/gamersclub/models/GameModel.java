package br.ufms.cpcx.api.gamersclub.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @NotNull
    private PartnerModel owner;
}