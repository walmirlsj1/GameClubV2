package br.ufms.cpcx.api.gamersclub.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;

import br.ufms.cpcx.api.gamersclub.dtos.GameDto;
import br.ufms.cpcx.api.gamersclub.dtos.GameDtoOutput;

import br.ufms.cpcx.api.gamersclub.services.GameService;
import br.ufms.cpcx.api.gamersclub.services.PartnerService;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/partner/{partner_id}/game")
public class PartnerGameController {
    final GameService gameService;
    final PartnerService partnerService;

    public PartnerGameController(GameService gameService, PartnerService partnerService) {
        this.gameService = gameService;
        this.partnerService = partnerService;
    }

    @GetMapping()
    public ResponseEntity<Object> getAllGameByPartner(@PathVariable(value = "partner_id") Long partnerId, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<GameDtoOutput> pageGameModel = gameService.findAllGameModelByOwnerId(partnerId, pageable)
                .map(GameDtoOutput::getNewGameDtoOutput);

        if (pageGameModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pageGameModel);
    }

    @PostMapping
    public ResponseEntity<Object> saveGameByPartner(@PathVariable(value = "partner_id") Long partnerId, @RequestBody @Valid GameDto gameDto) {

        Optional<PartnerModel> partnerModelOptional = partnerService.findById(partnerId);

        if (!partnerModelOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");

        var partnerModel = partnerModelOptional.get();

        var gameModel = gameDto.getGameModel();
        gameModel.setOwner(partnerModel);

        if (gameService.existsGameModelForOwner(gameModel))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: This game already exists for this owner!");

        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.save(gameModel));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Object> getAllGamesFilter(@PathVariable(value = "partner_id") Long partnerId, @RequestParam(required = false) String name, @RequestParam(required = false)  ConsoleEnum console, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Optional<PartnerModel> partnerModelOptional = partnerService.findById(partnerId);

        if (!partnerModelOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");

        var partnerModel = partnerModelOptional.get();
        var gameModel = new GameModel();
        gameModel.setConsole(console);
        gameModel.setName(name);
        gameModel.setOwner(partnerModel);
        Page<GameDtoOutput> pageGameDtoOutput = gameService.findAllGameModelByOwnerAndFilter(gameModel, pageable)
                .map(GameDtoOutput::getNewGameDtoOutput);

        return ResponseEntity.status(HttpStatus.OK).body(pageGameDtoOutput);

    }
}
