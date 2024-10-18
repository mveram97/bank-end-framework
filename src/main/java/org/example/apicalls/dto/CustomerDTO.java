package org.example.apicalls.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    @JsonProperty("customerId")
    private Integer customerId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("accounts")
    private List<AccountDTO> accounts;
}
