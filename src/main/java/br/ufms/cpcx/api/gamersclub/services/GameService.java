package br.ufms.cpcx.api.gamersclub.services;

import java.util.Optional;

import br.ufms.cpcx.api.gamersclub.models.GameLoanModel;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufms.cpcx.api.gamersclub.models.GameModel;
import br.ufms.cpcx.api.gamersclub.repositories.GameRepository;
import br.ufms.cpcx.api.gamersclub.repositories.PartnerRepository;


@Service
public class GameService {
    final GameRepository gameRepository;
    final PartnerRepository partnerRepository;

    public GameService(GameRepository gameRepository, PartnerRepository partnerRepository) {
        this.gameRepository = gameRepository;
        this.partnerRepository = partnerRepository;
    }

    @Transactional
    public GameModel save(GameModel gameModel) {
        return gameRepository.save(gameModel);
    }

    @Transactional
    public void delete(GameModel gameModel) {

        if (gameModel.getGames().stream().filter(o -> o.getReturnDate() == null).count() != 0)
            throw new RuntimeException("There are game relationships");
        else if(gameModel.getOwner().getGames().size() == 1 )
            throw new RuntimeException("It is not allowed to delete a game when it is the only game owned by the partner");
        gameRepository.delete(gameModel);
    }

    @Transactional
    public void deleteAllByOwnerId(Long partnerId) {
        gameRepository.customDeleteAllOwnerId(partnerId);
    }

    public Page<GameModel> findAll(Pageable pageable) {
        return gameRepository.findAll(pageable);
    }

    public Optional<GameModel> findById(Long id) {
        return gameRepository.findById(id);
    }

    public boolean existsGameModelForOwner(GameModel gameModel) {
        return gameRepository.existsByNameAndConsoleAndOwner(gameModel.getName(), gameModel.getConsole(), gameModel.getOwner());
    }

    public Page<GameModel> findAllGameModelByOwnerId(Long id, Pageable pageable) {
        return gameRepository.findAllGameModelByOwnerId(id, pageable);
    }

    public Page<GameModel> findByConsoleAndFilter(GameModel gameModel, Pageable pageable) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("console", match -> match.exact())
                .withMatcher("name", match -> match.startsWith())
                .withMatcher("owner", match -> match.contains())
                .withIgnoreCase().withIgnoreNullValues();

        var example = Example.of(gameModel, matcher);

        return gameRepository.findAll(example, pageable);
    }

    public Page<GameModel> findAllGameModelByOwnerAndFilter(GameModel gameModel, Pageable pageable) {

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("console", match -> match.exact())
                .withMatcher("name", match -> match.startsWith())
                .withMatcher("owner.id", match -> match.exact())
                .withIgnoreCase().withIgnoreNullValues();

        var example = Example.of(gameModel, matcher);

        return gameRepository.findAll(example, pageable);
    }


}
