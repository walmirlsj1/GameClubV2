package br.ufms.cpcx.api.gamersclub.dtos;

import org.springframework.beans.BeanUtils;

import lombok.*;

import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;
import br.ufms.cpcx.api.gamersclub.models.GameModel;


@Getter
@Setter
public class GameDtoOutput {
    private Long id;
    private String name;
    private ConsoleEnum console;

    public static GameDtoOutput getNewGameDtoOutput(GameModel gameModel){
        var gameDtoOutput = new GameDtoOutput();
        BeanUtils.copyProperties(gameModel, gameDtoOutput);
        return gameDtoOutput;
    }
}
