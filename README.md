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

### A6. Excluir produtos

O que foi feito: Implementamos a funcionalidade de excluir todos os itens.
Trecho de código:
```kotlin
binding.btnExcluir.setOnClickListener { 
            lifecycleScope.launch { 
                productDao.deleteAll()
                Log.i("TAG", "Todos os produtos foram excluídos.")
                Toast.makeText(this@MainActivity, "Produtos excluídos com sucesso!",
                    Toast.LENGTH_SHORT).show()
            }
        }
```
Print: ![A6](docs/prints/a6.png)

### A7. Atualização de Preço

O que foi feito: Implementamos a funcionalidade de alterar o preço de um item informando o seu ID.
Trecho de código:
```kotlin
binding.btnAtualizar.setOnClickListener {
    val productId = binding.editAtualizarId.text.toString()
    val priceText = binding.editAtualizarPreco.text.toString()

    if (productId.isNotEmpty() && priceText.isNotEmpty()) {
        if (priceText.toDouble() > 0) {
            lifecycleScope.launch {
                productDao.alterarProduto(price = priceText.toDouble(), id = productId.toInt())
                Log.i("TAG", "Produto alterado: ID = $productId, Novo preço: $priceText.")
                Toast.makeText(this@MainActivity, "Produto $productId alterado!",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.editAtualizarPreco.error = "Informe um preço válido"
        }
    } else {
        Toast.makeText(this, "Preencha os campos obrigatórios!",
            Toast.LENGTH_SHORT).show()
    }
}
```
Print: ![A7](docs/prints/a7.png)

### A8. Novo campo na tabela

O que foi feito: Implementamos o campo quantidade no banco de dados.
Trecho de código:
```kotlin
binding.btnAtualizar.setOnClickListener {
    val productId = binding.editAtualizarId.text.toString()
    val priceText = binding.editAtualizarPreco.text.toString()

    if (productId.isNotEmpty() && priceText.isNotEmpty()) {
        if (priceText.toDouble() > 0) {
            lifecycleScope.launch {
                productDao.alterarProduto(price = priceText.toDouble(), id = productId.toInt())
                Log.i("TAG", "Produto alterado: ID = $productId, Novo preço: $priceText.")
                Toast.makeText(this@MainActivity, "Produto $productId alterado!",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            binding.editAtualizarPreco.error = "Informe um preço válido"
        }
    } else {
        Toast.makeText(this, "Preencha os campos obrigatórios!",
            Toast.LENGTH_SHORT).show()
    }
}
```
Print: ![A8](docs/prints/a8.png)
O que aconteceu com os itens: Após a alteração da versão do banco de dados, os dados anteriores foram excluídos.

## Parte B: Questões dissertativas

### B1. O que é persistência de dados...?
Resposta: Persistência de dados é a capacidade de um aplicativo de armazenar dados em um formato durável, de forma que ele permaneça cadastrado ao longo de múltiplas reinicializações. Caso os produtos fossem guardados apenas em uma mutablelist, seriam excluídos assim que o aplicativo encerrasse.

### B2. Qual a diferença de usar SQLite diretamente...?
Resposta: Diferente de utilizar uma biblioteca de conexão direta com o SQLite, utilizar o Room não exige, diretamente, a escrita de códigos SQL, pois envelopa as requisições em uma camada ORM, permitindo que a manipulação dos registros do banco seja realizada através de objetos. Entre as vantagens, pode-se citar:
1. Facilidade de integração com outras bibliotecas ORM, pois utiliza sintaxes semelhantes a outros sistemas compatíveis
2. Redução de código, pois reduz a necessidade de escrever e gerenciar queries SQL manualmente.

### B3

## Como executar
1. Clone o repositório
2. Abra no Android Studio
3. Execute em um emulador (API 24+)
