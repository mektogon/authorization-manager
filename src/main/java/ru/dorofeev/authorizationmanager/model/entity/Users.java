package ru.dorofeev.authorizationmanager.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import ru.dorofeev.authorizationmanager.model.enums.ConfirmType;
import ru.dorofeev.authorizationmanager.model.enums.Gender;
import ru.dorofeev.authorizationmanager.model.enums.UserStatusType;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "users")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(targetEntity = Role.class, optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "login", nullable = false, unique = true, length = 250)
    private String login;

    @Column(name = "password", nullable = false, length = 250)
    private String password;

    @Column(name = "phone_number", unique = true, length = 16)
    private String phoneNumber;

    @Column(name = "duration_password", nullable = false)
    private Short durationPassword = 7;

    @Column(name = "nickname", unique = true, length = 250)
    private String nickname;

    @Column(name = "surname", length = 250)
    private String surname;

    @Column(name = "name", length = 250)
    private String name;

    @Column(name = "patronymic", length = 250)
    private String patronymic;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatusType status = UserStatusType.CREATED;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "confirm_type", nullable = false)
    private ConfirmType confirmType = ConfirmType.EMAIL;

    @Builder.Default
    @Column(name = "is_confirm", nullable = false)
    private Boolean isConfirm = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "last_seen", nullable = false)
    private OffsetDateTime lastSeen;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private OffsetDateTime updateDate;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
