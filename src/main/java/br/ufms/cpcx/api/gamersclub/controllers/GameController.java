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
import br.ufms.cpcx.api.gamersclub.dtos.GamePartnerDto;

import br.ufms.cpcx.api.gamersclub.services.GameService;
import br.ufms.cpcx.api.gamersclub.services.PartnerService;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/game")
public class GameController {
    final GameService gameService;
    final PartnerService partnerService;

    public GameController(GameService gameService, PartnerService partnerService) {
        this.gameService = gameService;
        this.partnerService = partnerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneGame(@PathVariable(value = "id") Long id) {
        Optional<GameModel> gameModelOptional = gameService.findById(id);
        if (!gameModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(gameModelOptional.get());
    }

    @PostMapping
    public ResponseEntity<Object> saveGame(@RequestBody @Valid GamePartnerDto gamePartnerDto) {
        var gameModel = gamePartnerDto.getGameModel();
        Optional<PartnerModel> partnerModelOptional = partnerService.findByPartner(gameModel.getOwner());

        if (partnerModelOptional.isPresent()) gameModel.setOwner(partnerModelOptional.get());
        else gameModel.setOwner(partnerService.save(gameModel.getOwner()));

        if (gameService.existsGameModelForOwner(gameModel))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: This game already exists for this owner!");

        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.save(gameModel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGame(@PathVariable(value = "id") Long id, @RequestBody @Valid GameDto gameDto) {
        Optional<GameModel> gameModelOptional = gameService.findById(id);
        if (!gameModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found.");
        }
        var gameModel = gameModelOptional.get();
        gameModel.setConsole(gameDto.getConsole());
        gameModel.setName(gameDto.getName());

        return ResponseEntity.status(HttpStatus.OK).body(gameService.save(gameModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGame(@PathVariable(value = "id") Long id) {
        Optional<GameModel> gameModelOptional = gameService.findById(id);
        if (!gameModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found.");
        }

        gameService.delete(gameModelOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Game deleted successfully.");
    }

    @GetMapping
    public ResponseEntity<Page<GameModel>> getAllGames(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findAll(pageable));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<Page<GameModel>> getAllGamesFilter(@RequestParam(required = false) String name, @RequestParam(required = false) ConsoleEnum console, @RequestParam(required = false) String owner, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        var gamePartnerDto = new GamePartnerDto();
        gamePartnerDto.setConsole(console);
        gamePartnerDto.setName(name);
        gamePartnerDto.setOwner(owner);

        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByConsoleAndFilter(gamePartnerDto, pageable));

    }

    @GetMapping(path = "/search/{console}")
    public ResponseEntity<Page<GameModel>> getAllGamesInConsole(@PathVariable ConsoleEnum console, @RequestParam(required = false) String name, @RequestParam(required = false) String owner, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {


        var gamePartnerDto = new GamePartnerDto();
        gamePartnerDto.setConsole(console);
        gamePartnerDto.setName(name);
        gamePartnerDto.setOwner(owner);

        return ResponseEntity.status(HttpStatus.OK).body(gameService.findByConsoleAndFilter(gamePartnerDto, pageable));

    }
}