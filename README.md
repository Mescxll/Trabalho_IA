# Trabalho de Inteligência Artificial — Algoritmo A*

Implementação do algoritmo de busca A* em Java para encontrar rotas em um mapa de uma cidade virtual.

## Funcionalidades

* Seleção de origem e destino no mapa.
* Cálculo de rotas utilizando o algoritmo A*.
* Utilização das heurísticas Manhattan, Euclidiana e Chebyshev.
* Apresentação textual e gráfica dos resultados.

## Tecnologias

* Java
* HTML, CSS e JavaScript (frontend)

## Como rodar

### Pré-requisitos

* JDK 21 ou superior instalado.

### 1. Compilar

Na raiz do projeto:

```bash
javac -d bin src/implementacao/*.java src/servidor/*.java
```

### 2. Iniciar o servidor

```bash
java -cp bin servidor.ServidorHttp
```

Se aparecer `Servidor rodando em http://localhost:8000`, deu certo.


### 3. Abrir a interface

Acesse **http://localhost:8000** no navegador.

* Clique em dois pontos do mapa para escolher **origem** e **destino**.
* Selecione a heurística desejada (Manhattan, Euclidiana ou Chebyshev).
* Clique em **Calcular caminho**.

O resultado aparece na tela e também é impresso no terminal onde o servidor está rodando.

## Integrantes

1.  [Maria Eduarda Campos](https://github.com/Mescxll)
2. [Helen Santos](https://github.com/sunHelen12)
3. [Gabryelle Duarte](https://github.com/GabryelleDart)
4. [Vinícius Nunes](https://github.com/Venan42)
5. [Kaique Silva](https://github.com/KAHISS)