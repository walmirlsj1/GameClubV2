package br.ufms.cpcx.api.gamersclub.dtos;

import javax.validation.constraints.*;

import br.ufms.cpcx.api.gamersclub.models.GameModel;
import lombok.*;

import br.ufms.cpcx.api.gamersclub.models.PartnerModel;

import java.util.List;
import java.util.stream.Collectors;


@Getter @Setter
public class PartnerDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String phoneNumber ;

    @NotNull
    private List<GameDto> games;

    public PartnerModel getPartnerModel(){
        var partnerModel = new PartnerModel();
        partnerModel.setName(this.getName());
        partnerModel.setPhoneNumber(getPhoneNumber());

        var listGames = games.stream().map(g -> {
            GameModel gameNew = g.getGameModel();
            gameNew.setOwner(partnerModel);
            return gameNew;
        }).collect(Collectors.toList());

        partnerModel.setGames(listGames);

        return partnerModel;
    }
}
