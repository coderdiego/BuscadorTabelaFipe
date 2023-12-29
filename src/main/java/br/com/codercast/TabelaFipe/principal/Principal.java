package br.com.codercast.TabelaFipe.principal;

import br.com.codercast.TabelaFipe.model.Dados;
import br.com.codercast.TabelaFipe.model.Modelos;
import br.com.codercast.TabelaFipe.model.Veiculo;
import br.com.codercast.TabelaFipe.service.ConsumirApi;
import br.com.codercast.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Principal {

    private final String URL_PRINCIPAL = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumirApi consomeAPI = new ConsumirApi();
    private ConverteDados conversor = new ConverteDados();
    private Scanner lerTexto = new Scanner(System.in);

    public void exibeMenu() {


        var menu = """
                *** OPÇÕES DE PESQUISA ***
                 CARROS
                 MOTOS
                 CAMINHÕES
                 Escolha uma das opções para consultar: 
                 """;
        System.out.println(menu);

        var leOpcaoDigitada = lerTexto.nextLine();

        String endereco;

        if (leOpcaoDigitada.toLowerCase().contains("car")) {
            endereco = URL_PRINCIPAL + "carros/marcas";
        } else if (leOpcaoDigitada.toLowerCase().contains("mo")) {
            endereco = URL_PRINCIPAL + "motos/marcas";
        } else {
            endereco = URL_PRINCIPAL + "caminhoes/marcas";
        }

        var json = consomeAPI.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream().sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Digite o código da Marca/Montadora você quer pesquisar: ");

        var lerMarcaDigitada = lerTexto.nextLine();

        endereco = endereco + "/" + lerMarcaDigitada + "/modelos";
        json = consomeAPI.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);// obterDados ao invez de obterLista porque o tipo da classe modelo é uma lista

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);

        System.out.println("Digite o trecho do nome do carro: ");
        var codigoTipo = lerTexto.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream().filter(m -> m.nome().toLowerCase().contains(codigoTipo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo desejado:");
        var CodigoModeloDesejado = lerTexto.nextLine();
        endereco = endereco + "/" + CodigoModeloDesejado + "/anos";
        json = consomeAPI.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();
        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consomeAPI.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os anos do veículo buscado");
        veiculos.forEach(System.out::println);
    }
}
