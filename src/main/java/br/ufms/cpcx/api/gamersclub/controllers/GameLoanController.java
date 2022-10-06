package br.ufms.cpcx.api.gamersclub.controllers;

import br.ufms.cpcx.api.gamersclub.dtos.GameLoanDto;
import br.ufms.cpcx.api.gamersclub.dtos.PartnerGameDto;
import br.ufms.cpcx.api.gamersclub.models.GameLoanModel;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import br.ufms.cpcx.api.gamersclub.services.GameLoanService;
import br.ufms.cpcx.api.gamersclub.services.GameService;
import br.ufms.cpcx.api.gamersclub.services.PartnerService;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/v2/partner/{partner_id}")
public class GameLoanController {
    /**
     * Loan
     * Refund
     * Find About "Loan and Refund"
     * <p>
     * /partner/{partner_id}/loan/{game_id}
     * /partner/{partner_id}/refund/{game_id}
     * /partner/{partner_id}/listLoanAndRefund
     */
    private final GameLoanService gameLoanService;
    private final PartnerService partnerService;
    private final GameService gameService;

    public GameLoanController(GameLoanService gameLoanService, PartnerService partnerService, GameService gameService) {
        this.gameLoanService = gameLoanService;
        this.partnerService = partnerService;
        this.gameService = gameService;
    }


    @GetMapping("listLoanAndRefund")
    public ResponseEntity<Object> listAllLoanAndRefundFilter(@PathVariable(value = "partner_id") Long partnerId,
                                                        @PageableDefault(page = 0, size = 10, sort = "id",
                                                                direction = Sort.Direction.ASC) Pageable pageable) {
        /**
         * List all Game loan for partner id
         */

        Optional<PartnerModel> partnerModelOptional = partnerService.findById(partnerId);
        if (!partnerModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(gameLoanService.findAll(partnerModelOptional.get(), pageable));
    }

    @PostMapping("loan/{game_id}")
    public ResponseEntity<Object> gameLoan(@PathVariable(value = "partner_id") Long partnerId,
                                      @PathVariable(value = "game_id") Long gameId, @RequestBody @Valid GameLoanDto gameLoanDto) {
        /**
         * List all Game loan for partner id
         */
        Optional<PartnerModel> partnerModelOptional = partnerService.findById(partnerId);
        if (!partnerModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner not found.");
        }
        Optional<GameModel> gameModelOptional = gameService.findById(gameId);
        if (!gameModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found.");
        }

        GameLoanModel gameLoanModel = new GameLoanModel();
        gameLoanModel.setPartner(partnerModelOptional.get());
        gameLoanModel.setGame(gameModelOptional.get());
        gameLoanModel.setScheduledReturnDate(gameLoanDto.getScheduledReturnDate());
        // checkar se esta disponivel.. se nao esta emprestado pra alguem

        return ResponseEntity.status(HttpStatus.OK)
                .body(gameLoanService.loan(gameLoanModel));
    }

    @DeleteMapping("refund/{game_loan_id}")
    public ResponseEntity<Object> gameRefund(@PathVariable(value = "partner_id") Long partnerId,
                                        @PathVariable(value = "game_loan_id") Long gameLoanId) {
        Optional<GameLoanModel> gameLoanModelOptional = gameLoanService.findById(gameLoanId);
        if (!gameLoanModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found.");
        }

        // checkar se esta disponivel.. se nao esta emprestado pra alguem

        return ResponseEntity.status(HttpStatus.OK)
                .body(gameLoanService.refund(gameLoanModelOptional.get()));
    }

}
