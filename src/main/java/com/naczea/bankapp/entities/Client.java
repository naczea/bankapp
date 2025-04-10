package com.naczea.bankapp.entities;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Client extends Person {

    @NotNull(message ="The field [password-Client] is required")
    private String password;

    @NotNull(message ="The field [state-Client] is required")
    private Boolean state;
}
