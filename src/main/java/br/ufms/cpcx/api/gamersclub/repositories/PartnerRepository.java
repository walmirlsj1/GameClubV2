package br.ufms.cpcx.api.gamersclub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.ufms.cpcx.api.gamersclub.models.PartnerModel;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface PartnerRepository extends JpaRepository<PartnerModel, Long> {
    Optional<PartnerModel> findByNameAndPhoneNumber(String name, String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "delete from tb_partner p where p.id = :partnerId", nativeQuery = true)
    public void customDeleteByPartnerId(Long partnerId);

}
