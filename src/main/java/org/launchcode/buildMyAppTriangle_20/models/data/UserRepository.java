package org.launchcode.buildMyAppTriangle_20.models.data;

import org.launchcode.buildMyAppTriangle_20.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    //Finds User by Username (email)
    public Optional<User> findUserByUsername(String username);


    //Finds all users who have the role name fitting the input parameter
    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.name = :roleName")
    public Iterable<User> findUserByRoleName(@Param("roleName") String roleName);
}