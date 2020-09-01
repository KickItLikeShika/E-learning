package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String firstname;

    @NotNull
    @Column(nullable = false)
    private String lastname;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    private String phone;

    @NotNull
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user")
    private Review review;

    @OneToMany(mappedBy = "user")
    private List<EnrollmentRequest> enrollmentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserAnswer> userAnswers = new ArrayList<>();

    public User() {}

    public User(String firstname, String lastname,
                String email, String username,
                String password, String phone,
                boolean enabled, Set<Role> roles) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.enabled = enabled;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void addRoles(Role role) {
        this.roles.add(role);
    }

    public void removeRoles(Role role) {
        this.roles.remove(role);
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public List<EnrollmentRequest> getEnrollmentRequests() {
        return enrollmentRequests;
    }

    public void addEnrollmentRequest(EnrollmentRequest enrollmentRequest) {
        this.enrollmentRequests.add(enrollmentRequest);
    }

    public void removeEnrollmentRequest(EnrollmentRequest enrollmentRequest) {
        this.enrollmentRequests.remove(enrollmentRequest);
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void addUserAnswers(UserAnswer userAnswer) {
        this.userAnswers.add(userAnswer);
    }

    public void removeUserAnswers(UserAnswer userAnswer) {
        this.userAnswers.remove(userAnswer);
    }
}
