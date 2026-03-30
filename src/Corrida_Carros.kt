// Desafio - Simulação de corrida de carros
// A ideia é simular uma corrida com vários pilotos e carros, onde
// a classificação final depende das características de cada piloto e de cada carro.
// O clima também influencia no resultado, o que deixa a corrida mais realista.

// Classe que guarda os dados de cada piloto
class Piloto(
    val nome: String,        // nome completo do piloto
    val idade: Int,          // idade em anos
    val habilidade: Int      // nível de habilidade de 1 a 10 (quanto maior, melhor)
)


// Classe com todas as especificações técnicas do carro
class Carro(
    val modelo: String,       // nome/modelo do carro (ex: "Fusca Turbinado")
    val velocidade: Int,      // velocidade máxima em km/h
    val aceleracao: Double,   // taxa de aceleração em m/s²
    val freio: Double,        // eficiência dos freios de 0 a 1 (quanto maior, melhor)
    val tanque: Double,       // capacidade do tanque em litros
    val consumo: Double,      // litros gastos por volta
    val resistencia: Int      // resistência mecânica do carro de 1 a 10
)


// Classe principal que gerencia a corrida inteira
// Recebe os pilotos e carros na mesma ordem (piloto[0] dirige carro[0], e assim por diante)
class Pista(
    val pilotos: List<Piloto>,  // lista de pilotos participantes
    val carros: List<Carro>,    // lista de carros (deve ter o mesmo tamanho que pilotos)
    val voltas: Int,             // número de voltas da corrida
    val clima: String            // condição climática: "sol" ou "chuva"
) {
    // Aqui vão ficar os pares (piloto, carro) já na ordem de chegada
    // começa vazia e é preenchida pela função definirPosicoes()
    val posicoes: MutableList<Pair<Piloto, Carro>> = mutableListOf()

    // Essa função é o coração da corrida - ela calcula a pontuação de cada dupla
    // e coloca todo mundo em ordem do melhor pro pior resultado
    fun definirPosicoes() {
        // Lista temporária pra guardar a pontuação de cada par piloto+carro
        val pontuacoes = mutableListOf<Triple<Piloto, Carro, Double>>()

        // Percorre todos os participantes usando o índice pra parear piloto com carro
        for (i in carros.indices) {
            val carro  = carros[i]
            val piloto = pilotos[i]

            // Fórmula base: velocidade e aceleração determinam a maior parte do tempo de volta
            // a resistência ajuda o carro a não quebrar durante a prova
            var pontuacao = (carro.velocidade * 0.4) + (carro.aceleracao * 8.0) + (carro.resistencia * 2.5)

            // A habilidade do piloto tem peso grande - um bom piloto tira o máximo do carro
            pontuacao += piloto.habilidade * 4.5

            // Os freios influenciam no tempo de frenagem nas curvas
            // freio ruim faz perder tempo nas curvas, freio bom permite entrada mais rápida
            pontuacao += carro.freio * 10.0

            // Chuva muda tudo: a velocidade importa menos e a habilidade do piloto
            // passa a ser muito mais decisiva pra manter o carro na pista
            if (clima.lowercase() == "chuva") {
                pontuacao -= carro.velocidade * 0.15  // carros rápidos sofrem mais na chuva
                pontuacao += piloto.habilidade * 2.0  // piloto experiente compensa o tempo perdido
            }

            // Verifica se o carro tem combustível suficiente pra completar todas as voltas
            // se não tiver, ele vai parar no meio da corrida e perde muitos pontos
            val voltasQueConsegue = carro.tanque / carro.consumo
            if (voltasQueConsegue < voltas) {
                println("  ⚠️  ${carro.modelo} (${piloto.nome}) não tem combustível suficiente! Vai para os boxes.")
                pontuacao -= 60.0  // penalidade pesada por parada não planejada
            }

            pontuacoes.add(Triple(piloto, carro, pontuacao))
        }

        // Ordena do maior pro menor: quem tem mais pontos chegou na frente
        val classificados = pontuacoes.sortedByDescending { it.third }

        // Preenche a lista de posições com os pares já na ordem certa
        posicoes.clear()
        for (item in classificados) {
            posicoes.add(Pair(item.first, item.second))
        }
    }

    // Essa função imprime o placar final da corrida de forma organizada
    fun exibirPlacar() {
        println("\n")
        println("╔══════════════════════════════════════════════╗")
        println("         🏁  RESULTADO FINAL DA CORRIDA  🏁")
        println("     Pista: $voltas voltas  |  Clima: $clima")
        println("╚══════════════════════════════════════════════╝")

        // Percorre a lista de posições e vai mostrando cada colocado
        for (i in posicoes.indices) {
            val lugar  = i + 1
            val piloto = posicoes[i].first
            val carro  = posicoes[i].second

            // Medalhas pros três primeiros pra deixar mais visual
            val medalha = when (lugar) {
                1 -> "🥇"
                2 -> "🥈"
                3 -> "🥉"
                else -> "   "
            }

            println("$medalha $lugar°  ${piloto.nome.padEnd(20)} → ${carro.modelo}")
            println("       Habilidade: ${piloto.habilidade}/10   " +
                    "Velocidade: ${carro.velocidade} km/h   " +
                    "Resistência: ${carro.resistencia}/10")
        }

        println("══════════════════════════════════════════════")
        println("  Vencedor: ${posicoes.first().first.nome} com o ${posicoes.first().second.modelo}!")
        println("══════════════════════════════════════════════")
    }
}


fun main() {
    println("=== SIMULAÇÃO DE CORRIDA DE CARROS ===\n")

    // Criando os pilotos com nome, idade e nível de habilidade
    val piloto1 = Piloto("Ayrton Silva",    28, 9)
    val piloto2 = Piloto("Carlos Mendes",   35, 7)
    val piloto3 = Piloto("Fernanda Rocha",  24, 8)
    val piloto4 = Piloto("João Pires",      31, 6)
    val piloto5 = Piloto("Renata Souza",    27, 10)

    // Criando os carros com todas as suas especificações técnicas
    // (velocidade, aceleração, freio, tanque, consumo por volta, resistência)
    val carro1 = Carro("Fusca Turbinado",  180, 3.5, 0.7, 40.0, 3.0, 7)
    val carro2 = Carro("Mustang GT",       220, 5.2, 0.9, 60.0, 5.5, 8)
    val carro3 = Carro("Fórmula Caseira",  260, 7.0, 0.6, 30.0, 4.0, 5)
    val carro4 = Carro("Celta do Bairro",  140, 2.1, 0.8, 45.0, 2.0, 9)
    val carro5 = Carro("Civic Preparado",  200, 4.8, 0.9, 50.0, 3.5, 8)

    // Colocando todos os pilotos e carros em listas - a ordem importa aqui!
    // piloto1 dirige carro1, piloto2 dirige carro2, e assim por diante
    val listaDePilotos = listOf(piloto1, piloto2, piloto3, piloto4, piloto5)
    val listaDeCarros  = listOf(carro1, carro2, carro3, carro4, carro5)

    println("Participantes desta corrida:")
    for (i in listaDePilotos.indices) {
        println("  ${listaDePilotos[i].nome} → ${listaDeCarros[i].modelo}")
    }

    println("\nVerificando combustível antes da largada...")

    // Criando a pista com 10 voltas e clima chuvoso pra deixar mais emocionante
    val pista = Pista(
        pilotos = listaDePilotos,
        carros  = listaDeCarros,
        voltas  = 10,
        clima   = "chuva"
    )

    // Calcula quem ficou em cada posição com base nos atributos
    pista.definirPosicoes()

    // Mostra o resultado final
    pista.exibirPlacar()
}
