package com.swoqe.newsstand.model.entities;

import com.swoqe.newsstand.security.entity.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max=255)
    @Pattern(regexp="^[a-zA-Z]+$", message = "Only letters are allowed")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 1, max=255)
    @Pattern(regexp="^[a-zA-Z]+$", message = "Only letters are allowed")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Size(min = 1, max=255)
    @Email
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 1, max=255)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.COMMON_USER;

    private boolean locked = false;
    private boolean enable = true;

    private BigDecimal account = BigDecimal.ZERO;

    @OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;

    public User(String firstName, String lastName, String password, UserRole userRole, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.userRole = userRole;
        this.email = email;
    }

    public void addSubscription(Subscription subscription){
        this.subscriptions.add(subscription);
    }

    public void deleteSubscription(Subscription subscription){
        this.subscriptions.remove(subscription);
    }

    public void accountMinus(BigDecimal number){
        this.account = this.account.subtract(number);
    }

}
