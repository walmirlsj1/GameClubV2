package br.ufms.cpcx.api.gamersclub.dtos;

import javax.validation.constraints.*;

import lombok.*;

import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;


@Getter @Setter
public class GamePartnerDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    private ConsoleEnum console;

    @NotBlank
    @Size(max = 100)
    private String owner;

    @NotBlank
    @Size(max = 15)
    private String ownerPhoneNumber ;


    public GameModel getGameModel(){
        var partnerModel = new PartnerModel();
        partnerModel.setName(this.getOwner());
        partnerModel.setPhoneNumber(this.getOwnerPhoneNumber());

        var gameModel = new GameModel();
        gameModel.setName(this.getName());
        gameModel.setConsole(this.getConsole());
        gameModel.setOwner(partnerModel);
        return gameModel;
    }
}
