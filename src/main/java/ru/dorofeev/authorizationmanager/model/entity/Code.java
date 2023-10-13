package ru.dorofeev.authorizationmanager.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "code")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {

    @Id
    @UuidGenerator
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(targetEntity = Users.class, cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "name", nullable = false)
    private String name;

    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    private OffsetDateTime createDate;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
