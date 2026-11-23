
package com.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {

    // 5. Get Bank By IFSC
    Optional<Bank> findByIfscCode(String ifscCode);

    // 6. Get Bank By Address ID
    List<Bank> findByAddress_Id(Long addressId);

    // 7. Get Bank By City
    List<Bank> findByAddress_CityIgnoreCase(String city);

    // 8. Get Bank By Contact
    Optional<Bank> findByContactNumber(String contactNumber);

    // 9. Get Bank By Pagination And Sorting
    Page<Bank> findAll(Pageable pageable);
}
