package br.unisanta.approomproduto.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.unisanta.approomproduto.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    suspend fun getAll(): List<Product>

    @Insert
    suspend fun insertAll(vararg products: Product)

}