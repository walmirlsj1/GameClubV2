package br.ufms.cpcx.api.gamersclub.dtos;

import javax.servlet.http.Part;
import javax.validation.constraints.*;

import lombok.*;

import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;


@Getter @Setter
public class PartnerDto {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 15)
    private String phoneNumber ;


    public PartnerModel getPartnerModel(){
        var partnerModel = new PartnerModel();
        partnerModel.setName(this.getName());
        partnerModel.setPhoneNumber(this.getPhoneNumber());
        return partnerModel;
    }
}
