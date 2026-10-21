# AppRoomProduto: Cadastro de Produtos com Room SQLite

**Aluno(a):** Gabriel Pilotto, Samuel Mendes
**RA:** 241643, 239890
**Disciplina:** Programação para Dispositivos Móveis
**Professor(a):** Alessandro Ferreira Paz Lima

## Sobre o projeto

Aplicativo Android em Kotlin que cadastra e lista produtos usando persistência local com Room (SQLite).

## Tecnologias
- Kotlin
- Room 2.6.1 (SQLite)
- ViewBinding
- Corrotinas (lifecycleScope)

## Prints do app
![Tela inicial](docs/prints/tela-inicial.png)
![Logcat com a listagem](docs/prints/logcat.png)

## Parte A: Complemento prático

### A1. Limpar campos após inserir

O que foi feito: Implementamos um trecho de código que limpa os campos nome, descrição, e preço após a inserção com sucesso no banco de dados.
Trecho de código:
```kotlin
binding.editProductName.text.clear()
binding.editProductPrice.text.clear()
binding.editProductDescription.text.clear()
binding.editProductName.requestFocus()
```
Print: ![A1](docs/prints/a1.png)

### A2. Validação de Preço

O que foi feito: Ajustamos um trecho de código para evitar a inserção de produtos com preços vazios, negativos ou com valor zero.
Trecho de código:
```kotlin
if (priceText.toDouble() > 0) {
 // ... inserção ao banco de dados
} else {
binding.editProductPrice.error = "Informe um preço válido"
}
```
Print: ![A2](docs/prints/a2.png)

### A3. Exibição na tela

O que foi feito: Ajustamos um trecho de código para exibir uma representação da lista de produtos
Trecho de código:
```kotlin
binding.btnListarProdutos.setOnClickListener {
    // Recupera os produtos de forma assíncrona
    lifecycleScope.launch {
        binding.txvProdutos.text = ""
        val products = productDao.getAll()
        if (products.isNotEmpty()) {
            products.forEach {
                Log.i("TAG", "Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}")
                binding.txvProdutos.text = binding.txvProdutos.text.toString() + "Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}\n"
            }
        } else {
            Log.i("TAG", "Nenhum produto encontrado.")
        }
    }
}
```
Print: ![A3](docs/prints/a3.png)

### A4. Total do estoque

O que foi feito: Ajustamos um trecho de código para adicionar uma exibição total de itens e a soma de seus valores.
Trecho de código:
```kotlin
binding.txvProdutos.text = binding.txvProdutos.text.toString() + "Total de produtos: ${products.size}, Valor total: R$${products.sumOf { it.price }}"
```
Print: ![A4](docs/prints/a4.png)


### A5. Buscar por nome

O que foi feito: Adicionamos a funcionalidade de buscar itens por nome.
Trecho de código:
```kotlin
binding.btnPesquisar.setOnClickListener {
    val search = binding.editSearch.text.toString()
    lifecycleScope.launch {
        binding.txvProdutos.text = ""
        val products = productDao.buscarPorNome("%$search%")
        if (products.isNotEmpty()) {
            products.forEach {
                Log.i("TAG", "Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}")
                binding.txvProdutos.text = binding.txvProdutos.text.toString() + "Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}\n"
            }
        } else {
            Log.i("TAG", "Nenhum produto encontrado.")
        }
    }
}
```
Print: ![A5](docs/prints/a5.png)

(repita para A2 até A8)
## Parte B: Questões dissertativas
### B1. O que é persistência de dados...?
Resposta: ...
(repita para B2 até B8)
## Como executar
1. Clone o repositório
2. Abra no Android Studio
3. Execute em um emulador (API 24+)
