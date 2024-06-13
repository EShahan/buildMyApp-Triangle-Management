package org.launchcode.buildMyAppTriangle_20.models.data;

import org.launchcode.buildMyAppTriangle_20.models.Contract;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
    Optional<Contract> findContractByName(String name);
}