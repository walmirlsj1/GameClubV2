package br.ufms.cpcx.api.gamersclub.services;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufms.cpcx.api.gamersclub.dtos.PartnerDto;
import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import br.ufms.cpcx.api.gamersclub.repositories.PartnerRepository;


@Service
public class PartnerService {
    final PartnerRepository partnerRepository;

    public PartnerService(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Transactional
    public PartnerModel save(PartnerModel partnerModel) {
        return partnerRepository.save(partnerModel);
    }

    @Transactional
    public void delete(PartnerModel partnerModel) {
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

    public Page<PartnerModel> findByPartnerFilter(PartnerDto partnerDto, Pageable pageable) {
        var partnerModel = partnerDto.getPartnerModel();

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith())
                .withMatcher("phoneNumber", match -> match.contains())
                .withIgnoreCase().withIgnoreNullValues();

        var example = Example.of(partnerModel, matcher);

        return partnerRepository.findAll(example, pageable);
    }

}
