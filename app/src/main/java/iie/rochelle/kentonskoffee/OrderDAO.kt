package iie.rochelle.kentonskoffee

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDAO {
    @Insert
    fun insert(orderEEntity: OrderEntity): Long

    @Query("SELECT * FROM orders")
    fun getAllOrders(): List<OrderEntity>
}