const MATRIZ = [
    ["A", "B", "0", "0", "C", "D", "0", "E"],
    ["0", "0", "0", "0", "0", "0", "0", "0"],
    ["F", "G", "0", "0", "H", "0", "0", "I"],
    ["0", "J", "0", "K", "L", "M", "0", "N"],
    ["0", "0", "0", "0", "O", "0", "0", "P"],
    ["Q", "R", "0", "S", "T", "0", "0", "U"],
];

const DIRECOES = {
    A: ["B", "F"],
    B: ["G", "A", "C"],
    C: ["B", "D"],
    D: ["C", "E", "M"],
    E: ["D", "I"],
    F: ["A", "G", "Q"],
    G: ["F", "H", "J"],
    H: ["G", "C"],
    I: ["N"],
    J: ["R"],
    K: ["J", "S"],
    L: ["H", "K"],
    M: ["D", "L"],
    N: ["P", "M"],
    O: ["L"],
    P: ["U", "O"],
    Q: ["R", "F"],
    R: ["S"],
    S: ["K", "T"],
    T: ["O", "U"],
    U: ["T"],
};

const ESPACAMENTO = 100;
const MARGEM = 40;
const RAIO_NO = 18;

let origem = null;
let destino = null;

// chave "A-B" (ordenada) -> elemento <line> correspondente no SVG
const mapaArestasEl = {};

const mapaEl = document.getElementById("mapa");
const origemEl = document.getElementById("origem-selecionada");
const destinoEl = document.getElementById("destino-selecionado");
const resultadoEl = document.getElementById("resultado");
const heuristicaEl = document.getElementById("heuristica");

function extraiPosicoes() {
    const posicoes = {};
    MATRIZ.forEach((linha, lin) => {
        linha.forEach((valor, col) => {
            if (valor !== "0") {
                posicoes[valor] = { x: col, y: lin };
            }
        });
    });
    return posicoes;
}

function montaMapa() {
    const posicoes = extraiPosicoes();
    const larguraCols = MATRIZ[0].length - 1;
    const alturaLinhas = MATRIZ.length - 1;
    const largura = larguraCols * ESPACAMENTO + MARGEM * 2;
    const altura = alturaLinhas * ESPACAMENTO + MARGEM * 2;

    const svg = criarSvg("svg", {
        viewBox: `0 0 ${largura} ${altura}`,
        width: "100%",
        height: "100%",
    });

    svg.appendChild(criarDefsSetas());
    svg.appendChild(desenhaGrade(largura, altura));

    const grupoArestas = criarSvg("g", { id: "arestas" });
    const grupoNos = criarSvg("g", { id: "nos" });

    desenhaArestas(grupoArestas, posicoes);
    desenhaNos(grupoNos, posicoes);

    svg.appendChild(grupoArestas);
    svg.appendChild(grupoNos);

    mapaEl.innerHTML = "";
    mapaEl.appendChild(svg);
}

function criarSvg(tag, atributos = {}) {
    const el = document.createElementNS("http://www.w3.org/2000/svg", tag);
    Object.entries(atributos).forEach(([chave, valor]) => el.setAttribute(chave, valor));
    return el;
}

function px(coordUnidade) {
    return coordUnidade * ESPACAMENTO + MARGEM;
}

function criarDefsSetas() {
    const defs = criarSvg("defs");
    defs.innerHTML = `
        <marker id="seta" viewBox="0 0 10 10" refX="8" refY="5"
                markerWidth="8" markerHeight="8" orient="auto-start-reverse">
            <path d="M0,0 L10,5 L0,10 Z" fill="#444"></path>
        </marker>
    `;
    return defs;
}

function desenhaGrade(largura, altura) {
    const grupo = criarSvg("g", { id: "grade" });
    for (let x = MARGEM; x <= largura - MARGEM; x += ESPACAMENTO) {
        grupo.appendChild(criarSvg("line", {
            x1: x, y1: MARGEM, x2: x, y2: altura - MARGEM,
            stroke: "#e0e0e0", "stroke-width": 1,
        }));
    }
    for (let y = MARGEM; y <= altura - MARGEM; y += ESPACAMENTO) {
        grupo.appendChild(criarSvg("line", {
            x1: MARGEM, y1: y, x2: largura - MARGEM, y2: y,
            stroke: "#e0e0e0", "stroke-width": 1,
        }));
    }
    return grupo;
}

function desenhaArestas(grupo, posicoes) {
    const desenhadas = new Set();
    const folga = RAIO_NO + 4;

    Object.entries(DIRECOES).forEach(([origemLetra, vizinhos]) => {
        vizinhos.forEach((destinoLetra) => {
            const chave = [origemLetra, destinoLetra].sort().join("-");
            if (desenhadas.has(chave)) return;
            desenhadas.add(chave);

            const vaiEExiste = DIRECOES[origemLetra]?.includes(destinoLetra);
            const voltaExiste = DIRECOES[destinoLetra]?.includes(origemLetra);

            const p1 = posicoes[origemLetra];
            const p2 = posicoes[destinoLetra];
            if (!p1 || !p2) return;

            const x1 = px(p1.x), y1 = px(p1.y);
            const x2 = px(p2.x), y2 = px(p2.y);

            const dx = x2 - x1;
            const dy = y2 - y1;
            const distancia = Math.hypot(dx, dy) || 1;
            const ux = dx / distancia;
            const uy = dy / distancia;

            const linha = criarSvg("line", {
                x1: x1 + ux * folga, y1: y1 + uy * folga,
                x2: x2 - ux * folga, y2: y2 - uy * folga,
                stroke: "#555", "stroke-width": 2.5,
                "data-aresta": chave,
            });

            if (voltaExiste) linha.setAttribute("marker-start", "url(#seta)");
            if (vaiEExiste) linha.setAttribute("marker-end", "url(#seta)");

            grupo.appendChild(linha);
            mapaArestasEl[chave] = linha;
        });
    });
}

function desenhaNos(grupo, posicoes) {
    Object.entries(posicoes).forEach(([letra, pos]) => {
        const cx = px(pos.x);
        const cy = px(pos.y);

        const circulo = criarSvg("circle", {
            cx, cy, r: RAIO_NO,
            class: "no",
            "data-ponto": letra,
        });
        circulo.addEventListener("click", () => selecionarPonto(letra));

        const texto = criarSvg("text", {
            x: cx, y: cy,
            class: "rotulo-no",
            "text-anchor": "middle",
            "dominant-baseline": "central",
        });
        texto.textContent = letra;
        texto.style.pointerEvents = "none";

        grupo.appendChild(circulo);
        grupo.appendChild(texto);
    });
}

function selecionarPonto(ponto) {
    if (origem === null) {
        origem = ponto;
    } else if (destino === null && ponto !== origem) {
        destino = ponto;
    } else {
        origem = ponto;
        destino = null;
    }

    atualizarSelecaoVisual();
}

function atualizarSelecaoVisual() {
    document.querySelectorAll("circle.no").forEach((el) => {
        el.classList.remove("origem", "destino");
        if (el.dataset.ponto === origem) el.classList.add("origem");
        if (el.dataset.ponto === destino) el.classList.add("destino");
    });

    origemEl.textContent = origem ?? "-";
    destinoEl.textContent = destino ?? "-";
}

function limparSelecao() {
    origem = null;
    destino = null;
    atualizarSelecaoVisual();
    resetaCoresArestas();
    resultadoEl.textContent = "Selecione a origem e o destino no mapa.";
    resultadoEl.classList.remove("erro");
}

function resetaCoresArestas() {
    Object.values(mapaArestasEl).forEach((linha) => {
        linha.setAttribute("stroke", "#555");
        linha.setAttribute("stroke-width", 2.5);
    });
}

// Pinta o resultado no mapa: azul para o caminho final,
// amarelo para as arestas testadas e não selecionadas.
function destacaResultado(caminho, testadas) {
    resetaCoresArestas();

    const chavesCaminho = new Set();
    for (let i = 0; i < caminho.length - 1; i++) {
        const chave = [caminho[i].ponto, caminho[i + 1].ponto].sort().join("-");
        chavesCaminho.add(chave);
    }

    (testadas || []).forEach(({ de, para }) => {
        const chave = [de, para].sort().join("-");
        if (chavesCaminho.has(chave)) return;
        const linha = mapaArestasEl[chave];
        if (linha) {
            linha.setAttribute("stroke", "#f9c74f");
            linha.setAttribute("stroke-width", 3.5);
        }
    });

    chavesCaminho.forEach((chave) => {
        const linha = mapaArestasEl[chave];
        if (linha) {
            linha.setAttribute("stroke", "#1976d2");
            linha.setAttribute("stroke-width", 4.5);
        }
    });
}

async function calcularCaminho() {
    if (!origem || !destino) {
        resultadoEl.textContent = "Selecione origem e destino antes de calcular.";
        resultadoEl.classList.add("erro");
        return;
    }

    resultadoEl.textContent = "Calculando...";
    resultadoEl.classList.remove("erro");

    try {
        const heuristica = heuristicaEl.value;
        const url = `/api/caminho?inicio=${origem}&fim=${destino}&heuristica=${heuristica}`;
        const resposta = await fetch(url);
        const dados = await resposta.json();

        if (dados.erro) {
            resultadoEl.textContent = dados.erro;
            resultadoEl.classList.add("erro");
            return;
        }

        resultadoEl.textContent = formataTrajeto(dados.caminho);
        window.ultimoCaminho = dados.caminho;
        destacaResultado(dados.caminho, dados.testadas);

    } catch (erro) {
        resultadoEl.textContent = "Erro ao conectar com o servidor.";
        resultadoEl.classList.add("erro");
        console.error(erro);
    }
}

function formataTrajeto(caminho) {
    return caminho
        .map((etapa, i) => (i === 0 ? etapa.ponto : `${etapa.ponto}(${etapa.acumulado})`))
        .join(" → ");
}

document.getElementById("btn-calcular").addEventListener("click", calcularCaminho);
document.getElementById("btn-limpar").addEventListener("click", limparSelecao);

montaMapa();