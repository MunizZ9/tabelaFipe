package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() {

        var menu = """
                *** OPÇÕES ***
                Carro
                Moto
                Caminhão
                
                Digite uma das opções para consultar:
                """;
        System.out.println(menu);
        var opcao = leitura.nextLine();
        String endereco;

        if (opcao.toLowerCase().contains("car")){
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else {
            endereco = URL_BASE + "caminhao/marcas";
        }

        var json = consumo.obterDados(endereco);
        var marcas = conversor.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(d -> System.out.println("Cód: " + d.codigo() + " Descrição: " + d.nome()));

        System.out.println("Informe o código da marca para consulta.");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";

        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("Modelos dessa marca:");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::nome))
                .forEach(d -> System.out.println("Cód: " + d.codigo() + " Descrição: " + d.nome()));

        System.out.println("Digite um trecho do nome do veículo para consulta: ");

        var trechoNome = leitura.nextLine();

        List<Dados> veiculosConsulta = modeloLista.modelos().stream()
                .filter(d -> d.nome().toUpperCase().contains(trechoNome.toUpperCase()))
                .collect(Collectors.toList());

        veiculosConsulta.stream()
                .forEach(d -> System.out.println("Cód: " + d.codigo() + " Descrição: " + d.nome()));

        System.out.println("Digite o código do modelo para consultar valores: ");

        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculosAnos = new ArrayList<>();

       for (int i = 0; i < anos.size(); i++) {
           var enderecoAnos = endereco + "/" + anos.get(i).codigo();
           json = consumo.obterDados(enderecoAnos);
           Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
           veiculosAnos.add(veiculo);
       }

        System.out.println("Todos os veículos filtrados com avaliações por ano: ");
       veiculosAnos.forEach(System.out::println);

    }

}
