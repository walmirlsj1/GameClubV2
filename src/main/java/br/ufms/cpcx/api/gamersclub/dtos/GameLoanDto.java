package br.ufms.cpcx.api.gamersclub.dtos;


import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
public class GameLoanDto {

    @NotNull
    private Date scheduledReturnDate ;

}
