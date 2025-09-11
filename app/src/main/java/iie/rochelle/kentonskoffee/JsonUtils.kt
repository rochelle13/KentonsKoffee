package iie.rochelle.kentonskoffee

import android.content.Context
import com.google.gson.Gson
import org.json.JSONObject

object JsonUtils {
    private val gson = Gson();

    fun orderToJson(order: OrderEntity): String {
        return gson.toJson(order)
    }

    fun jsonToOrder(json: String): OrderEntity {
        return gson.fromJson(json, OrderEntity::class.java)
    }

    fun saveOrderPrefences(context: Context, order: OrderEntity) {
        val jsonString = orderToJson(order)
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("order_key", jsonString)
            apply()
        }
    }

    fun getOrderFromPreferences(context: Context): Order? {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString("order_key", null)
        val jsonObject = JSONObject(jsonString)
        val returnOrder  = Order(jsonObject.getString("productName"),jsonObject.getString("customerName"), jsonObject.getString("customerCell"),jsonObject.getString("orderDate"))
        return returnOrder
    }
}