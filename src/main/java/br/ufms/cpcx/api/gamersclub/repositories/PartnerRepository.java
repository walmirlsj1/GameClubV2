package br.ufms.cpcx.api.gamersclub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufms.cpcx.api.gamersclub.models.PartnerModel;


@Repository
public interface PartnerRepository extends JpaRepository<PartnerModel, Long> {
    Optional<PartnerModel> findByNameAndPhoneNumber(String name, String phoneNumber);

}
