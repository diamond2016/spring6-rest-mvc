package guru.springframework.spring6restmvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;

        
    @NotBlank
    @NotNull
    private String beerName;
    
    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    private String upc;

    @DecimalMin("1")
    @NotNull
    private BigDecimal price;

    @Positive
    @NotNull
    private Integer quantityOnHand;
    
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
