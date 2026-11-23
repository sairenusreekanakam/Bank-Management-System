
package com.bank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // 1. Get Address By ID
    Optional<Address> findById(Long id);

    // 2. Get Addresses By City
    List<Address> findByCityIgnoreCase(String city);

    // 3. Get Addresses By Street And City
    List<Address> findByStreetIgnoreCaseAndCityIgnoreCase(String street, String city);

    // 4. Get Addresses By PIN Code
    List<Address> findByPinCode(String pinCode);

    // 5. Get Address By Bank ID
    List<Address> findByBank_Id(Long bankId);
}
