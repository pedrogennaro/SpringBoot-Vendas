package io.github.pedrogennaro.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UsuarioDTO {

    private Integer id;

    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;

    private boolean admin;
}
