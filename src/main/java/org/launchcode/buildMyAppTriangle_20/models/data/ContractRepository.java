package org.launchcode.buildMyAppTriangle_20.models.data;

import org.launchcode.buildMyAppTriangle_20.models.Contract;
import org.launchcode.buildMyAppTriangle_20.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
    Optional<Contract> findContractByName(String name);


}