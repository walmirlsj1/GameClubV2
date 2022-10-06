package br.ufms.cpcx.api.gamersclub.repositories;

import br.ufms.cpcx.api.gamersclub.models.GameLoanModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameLoanRepository extends JpaRepository<GameLoanModel, Long> {
    public Page<GameLoanModel> findByPartner(PartnerModel partnerModel, Pageable pageable);

    @Query(value = "SELECT count(gl) FROM GameLoanModel gl WHERE gl.partner.id = :partnerId and gl.returnDate is NULL and gl.scheduledReturnDate > CURRENT_DATE")
    public Long checkOverdueLoan(Long partnerId);

    @Query(value = "SELECT count(gl) FROM GameLoanModel gl WHERE gl.partner.id = :partnerId and gl.returnDate is NULL")
    public Long countGameLoansNotRefund(Long partnerId);

    @Query(value = "select count(gl)\n" +
            "      from GameLoanModel gl\n" +
            "      LEFT JOIN PartnerModel p1 ON p1.id = gl.partner.id\n" +
            "      LEFT JOIN GameModel g1 ON g1.id = gl.game.id\n" +
            "      where g1.owner.id = :partnerId")
    public Long countGameLoans(Long partnerId);

    @Query(value = "SELECT count(gl) FROM GameLoanModel gl WHERE gl.game.id = :gameId and gl.returnDate is null")
    public Long checkGameIsAvailable(Long gameId);

    /**
     *
     verificar vinculos sobre registro
     select *
      from tb_game_loan gl
      LEFT JOIN tb_partner p1 ON p1.id = gl.partner_id
      LEFT JOIN tb_game g1 ON g1.id = gl.game_id
      where g1.owner_id = 4


     */
}
