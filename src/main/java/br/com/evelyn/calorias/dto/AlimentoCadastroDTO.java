package br.com.evelyn.calorias.dto;

public record AlimentoCadastroDTO(
        Long alimentoId,
        String nome,
        String porcao,
        Double quantidadeProteina,
        Double quantidadeCarboidrato,
        Double quantidadeGorduras
) {
}
