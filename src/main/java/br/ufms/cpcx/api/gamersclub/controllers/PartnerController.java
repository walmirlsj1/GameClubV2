package br.ufms.cpcx.api.gamersclub.controllers;

import br.ufms.cpcx.api.gamersclub.dtos.PartnerDto;
import br.ufms.cpcx.api.gamersclub.services.GameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import br.ufms.cpcx.api.gamersclub.models.PartnerModel;

import br.ufms.cpcx.api.gamersclub.dtos.PartnerGameDto;

import br.ufms.cpcx.api.gamersclub.services.PartnerService;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/partner")
public class PartnerController {
    final PartnerService partnerService;
    final GameService gameService;

    public PartnerController(PartnerService partnerService, GameService gameService) {
        this.partnerService = partnerService;
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<Page<PartnerModel>> getAllPartners(
            @PageableDefault(page = 0, size = 10, sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(partnerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPartner(@PathVariable(value = "id") Long id) {
        Optional<PartnerModel> partnerModelOptional = partnerService.findById(id);
        if (!partnerModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(partnerModelOptional.get());
    }

    @PostMapping()
    public ResponseEntity<Object> createPartner(@RequestBody @Valid PartnerGameDto partnerGameDto) {
        var partnerModel = partnerGameDto.getPartnerModel();

        var partnerModelOptional = partnerService.findByPartner(partnerModel);

        if (partnerModelOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner already registered.");

        if (partnerGameDto.getGames().isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("List of game is empty.");

        return ResponseEntity.status(HttpStatus.CREATED).body(partnerService.save(partnerModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePartner(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid PartnerDto partnerDto) {
        Optional<PartnerModel> partnerModelOptional = partnerService.findById(id);
        if (!partnerModelOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");

        var partnerModel = partnerDto.getPartnerModel();
        var partnerModelDb = partnerModelOptional.get();

        partnerModel.setId(partnerModelDb.getId());
        partnerModel.setGames(partnerModelDb.getGames());
        partnerModelDb = partnerService.save(partnerModel);


        return ResponseEntity.status(HttpStatus.OK).body(partnerModelDb);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePartner(@PathVariable(value = "id") Long id) {
        Optional<PartnerModel> partnerModelOptional = partnerService.findById(id);
        if (!partnerModelOptional.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");

        var partner = partnerModelOptional.get();

        partnerService.delete(partner);

        return ResponseEntity.status(HttpStatus.OK).body("Partner deleted successfully.");
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Page<PartnerModel>> getAllPartnersFilter(@RequestParam(required = false) String name, @RequestParam(required = false) String phoneNumber, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        var partnerDto = new PartnerGameDto();
        partnerDto.setName(name);
        partnerDto.setPhoneNumber(phoneNumber);

        return ResponseEntity.status(HttpStatus.OK).body(partnerService.findByPartnerFilter(partnerDto, pageable));

    }
}