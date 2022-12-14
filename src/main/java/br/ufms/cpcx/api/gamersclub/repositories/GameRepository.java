package br.ufms.cpcx.api.gamersclub.repositories;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface GameRepository extends JpaRepository<GameModel, Long> {
    boolean existsByNameAndConsoleAndOwner(String name, ConsoleEnum consoleEnum, PartnerModel owner);

    Page<GameModel> findAllGameModelByOwnerId(Long ownerId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "delete from tb_game g where g.owner_id = :ownerId", nativeQuery = true)
    void customDeleteAllOwnerId(Long ownerId);
}
