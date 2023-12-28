package br.com.codercast.TabelaFipe.principal;

import br.com.codercast.TabelaFipe.model.Dados;
import br.com.codercast.TabelaFipe.model.Modelos;
import br.com.codercast.TabelaFipe.service.ConsumirApi;
import br.com.codercast.TabelaFipe.service.ConverteDados;

import java.util.Comparator;
import java.util.Scanner;


public class Principal {

    private final String URL_PRINCIPAL = "https://parallelum.com.br/fipe/api/v1/";
    private ConsumirApi consomeAPI  = new ConsumirApi();
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

        System.out.println("Digite qual Marca/Montadora você quer pesquisar: ");

        var lerMarcaDigitada = lerTexto.nextLine();

        endereco = endereco + "/" + lerMarcaDigitada + "/modelos";
        json = consomeAPI.obterDados(endereco); // obterDados ao invez de obterLista porque o tipo da classe modelo é uma lista
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);
    }
}
