package br.com.evelyn.calorias.dto;

public record UsuarioCadastroDTO(
        Long usuarioId,
        String nome,
        String email,
        String senha
) {
}
