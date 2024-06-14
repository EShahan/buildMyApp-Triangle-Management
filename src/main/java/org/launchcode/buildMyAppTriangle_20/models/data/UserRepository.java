package org.launchcode.buildMyAppTriangle_20.models.data;

import org.launchcode.buildMyAppTriangle_20.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    //Finds User by Username (email)
    Optional<User> findUserByUsername(String username);


    //Finds all users who have the role name fitting the input parameter
    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.name = :roleName")
    Iterable<User> findUserByRoleName(@Param("roleName") String roleName);

    //Finds all users who have matching role name associated with contractId
    @Query("SELECT u FROM User u JOIN u.contracts c JOIN u.userRoles r WHERE c.id = :contractId and r.name = :roleName")
    Collection<User> findUserByRoleAndContract(@Param("contractId") Long contractId, @Param("roleName") String roleName);
}