package br.com.alura.TabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculo(String Valor,
                      String Marca,
                      String Modelo,
                      Integer AnoModelo,
                      String Combustível) {
}
