package org.launchcode.buildMyAppTriangle_20.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collection;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min=1, max=255)
    private String name;

    @NotNull
    @Size(min=1, max=255)
    private String address;

    @NotNull
    @Size(min=1, max=255)
    private String jobDescription;

    @ManyToMany
    @JoinTable(
            name = "contracts_users",
            joinColumns = @JoinColumn(
                    name = "contract_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"))
    private Collection<User> contractUsers;

    public Contract() {
    }

    public Contract(String name, String address, String jobDescription) {
        this.name = name;
        this.address = address;
        this.jobDescription = jobDescription;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Collection<User> getContractUsers() {
        return contractUsers;
    }

    public void setContractUsers(Collection<User> contractUsers) {
        this.contractUsers = contractUsers;
    }
}
