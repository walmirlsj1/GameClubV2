package br.ufms.cpcx.api.gamersclub.services;

import br.ufms.cpcx.api.gamersclub.models.GameLoanModel;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import br.ufms.cpcx.api.gamersclub.repositories.GameLoanRepository;
import br.ufms.cpcx.api.gamersclub.repositories.GameRepository;
import br.ufms.cpcx.api.gamersclub.repositories.PartnerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GameLoanService {

    private final GameLoanRepository gameLoanRepository;
    private final GameRepository gameRepository;
    private final PartnerRepository partnerRepository;

    private final Long MAX_LOANS_BY_PARTNER = 5L;

    public GameLoanService(GameLoanRepository gameLoanRepository, GameRepository gameRepository, PartnerRepository partnerRepository){
        this.gameLoanRepository = gameLoanRepository;
        this.gameRepository = gameRepository;
        this.partnerRepository = partnerRepository;
    }

    @Transient
    public GameLoanModel loan(GameLoanModel gameLoanModel){

        if (this.checkOverdue(gameLoanModel.getPartner().getId())
                || this.checkMaxLoans(gameLoanModel.getPartner().getId()))
            throw new DataIntegrityViolationException("Delayed returns, or game limit exceeded");

        if(gameLoanRepository.checkGameIsAvailable(gameLoanModel.getGame().getId()) != 0)
            throw new DataIntegrityViolationException("Game has already been borrowed");

        gameLoanModel.setReturnDate(null);

        if(gameLoanModel.getPartner().getGames().stream().filter(p-> p.getId().equals(gameLoanModel.getGame().getId())).count() > 0)
            throw new DataIntegrityViolationException("Partner owns the game");

        return gameLoanRepository.save(gameLoanModel);
    }

    @Transient
    public GameLoanModel refund(GameLoanModel gameLoanModel){
        gameLoanModel.setReturnDate(LocalDateTime.now());
        return gameLoanRepository.save(gameLoanModel);
    }

    public Optional<GameLoanModel> findById(Long gameLoanId){
        return gameLoanRepository.findById(gameLoanId);
    }

    public Page<GameLoanModel> findAll(PartnerModel partnerModel, Pageable pageable){
        return gameLoanRepository.findByPartner(partnerModel, pageable);
    }

    private Boolean checkOverdue(Long partnerId){
        // devoluçao com atrasso
        return gameLoanRepository.checkOverdueLoan(partnerId) != 0L;
    }
    private Boolean checkMaxLoans(Long partnerId){
        return gameLoanRepository.countGameLoansNotRefund(partnerId) >= MAX_LOANS_BY_PARTNER;
    }

    private Boolean checkGameIsAvailable(Long gameId){
        return gameLoanRepository.checkGameIsAvailable(gameId) == 0L;
    }

    public Boolean checkHasLoan(Long partnerId){
        return gameLoanRepository.countGameLoans(partnerId) >= 1L;
    }
}
