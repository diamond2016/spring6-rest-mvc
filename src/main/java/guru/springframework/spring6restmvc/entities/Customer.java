package guru.springframework.spring6restmvc.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter                     // @Data not recommended for entities
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private UUID id;
    
    @Version
    private Integer version;  //0,1...

    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
