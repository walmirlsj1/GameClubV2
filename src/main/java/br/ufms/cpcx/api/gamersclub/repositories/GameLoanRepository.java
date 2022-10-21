package br.ufms.cpcx.api.gamersclub.repositories;

import br.ufms.cpcx.api.gamersclub.models.GameLoanModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface GameLoanRepository extends JpaRepository<GameLoanModel, Long> {
    public Page<GameLoanModel> findByPartner(PartnerModel partnerModel, Pageable pageable);

    @Query(value = "SELECT count(gl) FROM GameLoanModel gl WHERE gl.partner.id = :partnerId and gl.returnDate is NULL and gl.scheduledReturnDate < current_timestamp ")
    public Long checkOverdueLoan(Long partnerId);

    @Query(value = "SELECT count(gl) FROM GameLoanModel gl WHERE gl.partner.id = :partnerId and gl.returnDate is NULL")
    public Long countGameLoansNotRefund(Long partnerId);

    @Query(value = "SELECT EXISTS (SELECT gl FROM tb_game_loan gl WHERE gl.partner_id = :partnerId LIMIT 1)", nativeQuery = true)
    public Boolean existActiveLoans(Long partnerId);

    @Query(value = "SELECT EXISTS (SELECT gl FROM tb_game_loan gl, tb_game gg WHERE gl.game_id = gg.id AND gg.owner_id = :ownerId LIMIT 1)", nativeQuery = true)
    public Boolean existBurrowedGames(Long ownerId);

    @Query(value = "SELECT NOT EXISTS (SELECT gl FROM tb_game_loan gl WHERE gl.game_id = :gameId AND gl.return_date IS NULL )", nativeQuery = true)
    public Boolean checkGameIsAvailable(Long gameId);



}
