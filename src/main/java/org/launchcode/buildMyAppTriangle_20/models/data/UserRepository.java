package org.launchcode.buildMyAppTriangle_20.models.data;

import org.launchcode.buildMyAppTriangle_20.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryRewriter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long>, QueryRewriter {
    //Finds User by Username (email)
    Optional<User> findOptionalUserByUsername(String username);

    User findUserByUsername(String username);


    //Finds all users who have the role name fitting the input parameter
    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.name = :roleName")
    Iterable<User> findUserByRoleName(@Param("roleName") String roleName);

    @Query(value= "SELECT *\n" +
            "FROM user u\n" +
            "WHERE EXISTS (\n" +
            "\tSELECT 1\n" +
            "    FROM users_roles ur\n" +
            "    WHERE ur.user_id = u.id AND ur.role_id = 1\n" +
            ")\n" +
            "AND NOT EXISTS (\n" +
            "\tSELECT 1\n" +
            "    FROM users_roles ur\n" +
            "    WHERE ur.user_id = u.id AND ur.role_id <> 1\n" +
            ")",
    nativeQuery = true)
    Collection<User> findExclusiveAdmin();

//    TODO: FIX THIS!!!
//    @Query("SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.name = :wantedRoleName and not r.name <> :excludedRoleName")
//    Collection<User> findUserByRoleNameAndExcludedRoleName(@Param("wantedRoleName") String wantedRoleName, @Param("excludedRoleName") String excludedRoleName);

    //Finds all users who have matching role name associated with contractId
    @Query("SELECT u FROM User u JOIN u.contracts c JOIN u.userRoles r WHERE c.id = :contractId and r.name = :roleName")
    Collection<User> findUserByRoleAndContract(@Param("contractId") Long contractId, @Param("roleName") String roleName);

    @Query("SELECT u FROM User u JOIN u.contracts c JOIN u.userRoles r WHERE c.id IN :contractIds and r.name = :roleName")
    Collection<User> findUsersByMatchingContracts(@Param("contractIds") List<Long> contractIds, @Param("roleName") String roleName);

    @Query("SELECT c.id FROM User u JOIN u.contracts c WHERE u.id = :userId")
    List<Long> findAllUserContractIds(@Param("userId") Long userId);

    @Query(value = "SELECT u FROM User u",
    queryRewriter = UserRepository.class)
    List<User> customSearch();

    @Override
    default String rewrite(String query, Sort sort) {
        return "SELECT u FROM User u INNER JOIN u.userRoles r WHERE r.name = 'ROLE_ADMIN'";
    }
}