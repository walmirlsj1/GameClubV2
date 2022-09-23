package br.ufms.cpcx.api.gamersclub.dtos;

import javax.validation.constraints.*;

import lombok.*;

import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;


@Getter @Setter
public class GameDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private ConsoleEnum console;

    public GameModel getGameModel(){
        var gameModel = new GameModel();
        gameModel.setName(this.getName());
        gameModel.setConsole(this.getConsole());
        return gameModel;
    }
}
