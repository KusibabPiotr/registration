package my.app.registration.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime created;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "APP_USER_ID"
    )
    private AppUser appUser;
}