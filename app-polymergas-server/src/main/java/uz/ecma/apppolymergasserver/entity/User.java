package uz.ecma.apppolymergasserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.ecma.apppolymergasserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Users")
public class User extends AbsEntity implements UserDetails {


    private String state;

    private Long chatId;

    private String language;

    private Float lat, lon;

    private String address;

    private Integer categoryId;

    private UUID productId;

    private UUID productPriceId;

    private UUID orderId;

    private String commentType;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "additional_phone", unique = true, nullable = true)
    private String additionalPhone;

    private String password;

    @Column(nullable = false)
    private String fullName;

    @OneToOne(fetch = FetchType.LAZY)
    private Attachment photo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @ManyToOne
    private Sale sale;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;


    public User(String phoneNumber, String password, String fullName, List<Role> roles) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.fullName = fullName;
        this.roles = roles;
    }

    public User(String phoneNumber, String additionalPhone, String password, String fullName, List<Role> roles) {
        this.phoneNumber = phoneNumber;
        this.additionalPhone = additionalPhone;
        this.password = password;
        this.fullName = fullName;
        this.roles = roles;
    }


    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
