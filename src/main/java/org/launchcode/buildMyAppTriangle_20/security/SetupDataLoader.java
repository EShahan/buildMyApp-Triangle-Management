package org.launchcode.buildMyAppTriangle_20.security;

import jakarta.transaction.Transactional;
import org.launchcode.buildMyAppTriangle_20.models.Privilege;
import org.launchcode.buildMyAppTriangle_20.models.Role;
import org.launchcode.buildMyAppTriangle_20.models.User;
import org.launchcode.buildMyAppTriangle_20.models.data.PrivilegeRepository;
import org.launchcode.buildMyAppTriangle_20.models.data.RoleRepository;
import org.launchcode.buildMyAppTriangle_20.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

//@Component
//public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
//    boolean alreadySetup = false;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PrivilegeRepository privilegeRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if (alreadySetup)
//            return;
//
//        Privilege readPrivilege
//                = createPrivilegeIfNotFound("READ_PRIVILEGE");
//        Privilege writePrivilege
//                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
//
//        createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(readPrivilege, writePrivilege));
//        createRoleIfNotFound("ROLE_EMPLOYEE", Arrays.asList(readPrivilege));
//        createRoleIfNotFound("ROLE_CUSTOMER", Arrays.asList(readPrivilege));
//
//        //Create default Admin
//        Role defaultAdminUser = roleRepository.findByName("ROLE_ADMIN");
//        User adminUser = new User();
//        adminUser.setUsername("Paintgood@gmail.com");
//        adminUser.setFirstName("John");
//        adminUser.setLastName("Paintgood");
//        adminUser.setPassword(passwordEncoder.encode("12345"));
//        adminUser.setUserRoles(Arrays.asList(defaultAdminUser));
//        userRepository.save(adminUser);
//
//        //Create default Employee
//        Role defaultEmployeeUser = roleRepository.findByName("ROLE_EMPLOYEE");
//        User employeeUser = new User();
//        employeeUser.setUsername("Jane@gmail.com");
//        employeeUser.setFirstName("Jane");
//        employeeUser.setLastName("Paintgood");
//        employeeUser.setPassword(passwordEncoder.encode("12345"));
//        employeeUser.setUserRoles(Arrays.asList(defaultEmployeeUser));
//        userRepository.save(employeeUser);
//
//        // Create default Customer
//        Role defaultCustomerUser = roleRepository.findByName("ROLE_CUSTOMER");
//        User customerUser = new User();
//        customerUser.setUsername("Cantpaint@gmail.com");
//        customerUser.setFirstName("Bob");
//        customerUser.setLastName("Cantpaint");
//        customerUser.setPassword(passwordEncoder.encode("12345"));
//        customerUser.setUserRoles(Arrays.asList(defaultCustomerUser));
//        userRepository.save(customerUser);
//    }
//
//    @Transactional
//    Privilege createPrivilegeIfNotFound(String name) {
//
//        Privilege privilege = privilegeRepository.findByName(name);
//        if (privilege == null) {
//            privilege = new Privilege(name);
//            privilegeRepository.save(privilege);
//        }
//        return privilege;
//    }
//
//    @Transactional
//    Role createRoleIfNotFound(
//            String name, Collection<Privilege> privileges) {
//        Role role = roleRepository.findByName(name);
//        if (role == null) {
//            role = new Role(name);
//            role.setPrivileges(privileges);
//            roleRepository.save(role);
//        }
//        return role;
//    }
//}
