package br.unisanta.approomproduto.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import br.unisanta.approomproduto.dao.ProductDao
import br.unisanta.approomproduto.databinding.ActivityMainBinding
import br.unisanta.approomproduto.db.AppDatabase
import br.unisanta.approomproduto.model.Product

import br.unisanta.approomproduto.R

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase
    private lateinit var productDao: ProductDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializa o ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Inicializa o banco de dados Room de forma recomendada (sem usar         allowMainThreadQueries)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        )
            .fallbackToDestructiveMigration() // Usado para recriar o DB se o esquema         mudar
            .build()
        productDao = db.productDao()
        // Exemplo de inserção de produto
        binding.btnInserirProduto.setOnClickListener {
            val name = binding.editProductName.text.toString()
            val priceText = binding.editProductPrice.text.toString()
            val description = binding.editProductDescription.text.toString()
            val quantityText = binding.editProductQuantity.text.toString()

            // Verifica se os campos estão preenchidos
            if (name.isNotEmpty() && priceText.isNotEmpty() && quantityText.isNotEmpty()) {
                if (priceText.toDouble() > 0) {
                    if (quantityText.toInt() > 0) {
                        val price = priceText.toDoubleOrNull() ?: 0.0
                        val quantity = quantityText.toIntOrNull() ?: 0
                        val product = Product(name = name, price = price, description =
                        description, quantity = quantity)
                        // Insere o produto no banco de dados de forma assíncrona (usando                 corrotina)
                        lifecycleScope.launch {
                            productDao.insertAll(product)
                            Log.i("TAG", "Produto inserido: $name - R$$price")
                            Toast.makeText(this@MainActivity, "Produto inserido!",
                                Toast.LENGTH_SHORT).show()
                            binding.editProductName.text.clear()
                            binding.editProductPrice.text.clear()
                            binding.editProductDescription.text.clear()
                            binding.editProductQuantity.text.clear()
                            binding.editProductName.requestFocus()
                        }
                    } else {
                        binding.editProductQuantity.error = "Informe um valor acima de zero."
                    }

                } else {
                    binding.editProductPrice.error = "Informe um preço válido"
                }
            } else {
                Toast.makeText(this, "Preencha os campos obrigatórios!",
                    Toast.LENGTH_SHORT).show()
            }
        }
        // Exemplo de recuperação de produtos
        binding.btnListarProdutos.setOnClickListener {
            // Recupera os produtos de forma assíncrona
            lifecycleScope.launch {
                binding.txvProdutos.text = ""
                val products = productDao.getAll()
                if (products.isNotEmpty()) {
                    products.forEach {
                        Log.i("TAG", "ID = ${it.id}, Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}, Quantidade = ${it.quantity}")
                        binding.txvProdutos.text = binding.txvProdutos.text.toString() + "ID = ${it.id}, Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}, Quantidade = ${it.quantity}\n"
                    }
                    binding.txvProdutos.text = binding.txvProdutos.text.toString() + "Total de produtos: ${products.size}, Valor total: R$${products.sumOf { it.price }}"
                } else {
                    Log.i("TAG", "Nenhum produto encontrado.")
                }
            }
        }

        binding.btnPesquisar.setOnClickListener {
            val search = binding.editSearch.text.toString()
            lifecycleScope.launch {
                binding.txvProdutos.text = ""
                val products = productDao.buscarPorNome("%$search%")
                if (products.isNotEmpty()) {
                    products.forEach {
                        Log.i("TAG", "ID = ${it.id}, Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}, Quantidade = ${it.quantity}")
                        binding.txvProdutos.text = binding.txvProdutos.text.toString() + "ID = ${it.id}, Produto = ${it.name}, Preço = R$${it.price}, Descrição = ${it.description}, Quantidade = ${it.quantity}\n"
                    }
                } else {
                    Log.i("TAG", "Nenhum produto encontrado.")
                }
            }
        }

        binding.btnExcluir.setOnClickListener {
            lifecycleScope.launch {
                productDao.deleteAll()
                Log.i("TAG", "Todos os produtos foram excluídos.")
                Toast.makeText(this@MainActivity, "Produtos excluídos com sucesso!",
                    Toast.LENGTH_SHORT).show()
            }
        }

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
    }
}