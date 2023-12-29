package br.com.codercast.TabelaFipe.service;

import java.util.List;

public interface ItfcConverteDados {
    <T> T obterDados(String json, Class<T> classe);

    <T> List<T> obterLista(String json, Class<T> classe);
}
