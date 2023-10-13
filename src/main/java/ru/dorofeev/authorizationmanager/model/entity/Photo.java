package ru.dorofeev.authorizationmanager.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "photo")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(targetEntity = Users.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "url_preview", nullable = false)
    private String urlPreview;

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
