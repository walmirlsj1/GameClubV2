package br.ufms.cpcx.api.gamersclub.models;

import javax.persistence.*;
import java.io.Serializable;

import lombok.*;


@Entity
@Table(name="TB_PARTNER")
@Data @NoArgsConstructor
public class PartnerModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

}
