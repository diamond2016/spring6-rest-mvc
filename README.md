### Spring Framework 6
**Servizi RESTful in Spring Boot**

- Utilizza il progetto Lombok
- Creato con Spring initializer java >=17 maven e dependencies Spring Boot Web
- Necessario plugin Lombok in IDE

Da verificare enable annotation processors e obtain processors from project claspath per abilitare Lombok (da vedere cosa ha senso su VsCode)

1. Generato il progetto da zero con initializer spring
2. Pubblicato versione iniziale

Facciamo riferimento all'api di test https://api.springframework.guru/api/v1/beer, verificata con Postman
Da notare uso di UUID per id bean unico (sarà usato in chiave primaria in persistenza), di LocalDateTime per le date,
di BigDecimal per i valori valuta (float).
 

