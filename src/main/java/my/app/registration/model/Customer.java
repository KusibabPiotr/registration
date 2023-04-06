package my.app.registration.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Setter
@Table(name = "CUSTOMERS")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "customer")
    private AppUser appUser;
    private String firstName;
    private String lastName;
}
