package br.ufms.cpcx.api.gamersclub.services;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufms.cpcx.api.gamersclub.dtos.PartnerGameDto;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import br.ufms.cpcx.api.gamersclub.repositories.PartnerRepository;


@Service
public class PartnerService {
    final PartnerRepository partnerRepository;
    final GameLoanService gameLoanService;

    public PartnerService(PartnerRepository partnerRepository, GameLoanService gameLoanService) {
        this.partnerRepository = partnerRepository;
        this.gameLoanService = gameLoanService;
    }

    @Transactional
    public PartnerModel save(PartnerModel partnerModel) {
        return partnerRepository.save(partnerModel);
    }

    @Transactional
    public void delete(PartnerModel partnerModel) {
        if(gameLoanService.checkHasLoan(partnerModel.getId())) throw new DataIntegrityViolationException("There are partner relationships");

        partnerRepository.delete(partnerModel);
    }

    public Page<PartnerModel> findAll(Pageable pageable) {
        return partnerRepository.findAll(pageable);
    }

    public Optional<PartnerModel> findById(Long id) {
        return partnerRepository.findById(id);
    }

    public Optional<PartnerModel> findByPartner(PartnerModel partnerModel) {
        return partnerRepository.findByNameAndPhoneNumber(partnerModel.getName(), partnerModel.getPhoneNumber());
    }

    public Page<PartnerModel> findByPartnerFilter(PartnerGameDto partnerGameDto, Pageable pageable) {
        var partnerModel = partnerGameDto.getPartnerModel();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith())
                .withMatcher("phoneNumber", match -> match.contains())
                .withIgnoreCase().withIgnoreNullValues();

        var example = Example.of(partnerModel, matcher);

        return partnerRepository.findAll(example, pageable);
    }
}
