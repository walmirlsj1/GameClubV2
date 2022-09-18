package br.ufms.cpcx.api.gamersclub.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufms.cpcx.api.gamersclub.models.ConsoleEnum;
import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;


@Repository
public interface GameRepository extends JpaRepository<GameModel, Long> {
    boolean existsByNameAndConsoleAndOwner(String name, ConsoleEnum consoleEnum, PartnerModel owner);

    Long countByOwnerId(Long ownerId);

    Page<GameModel> findAllGameModelByOwnerId(Long ownerId, Pageable pageable);

    void deleteAllByOwnerId(Long ownerId);
}
