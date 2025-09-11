package iie.rochelle.kentonskoffee

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import iie.rochelle.kentonskoffee.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {

    var order = Order()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_details)
        val binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Retrieve the order from SharedPreferences
        val orderFromPrefs = JsonUtils.getOrderFromPreferences(this)
        orderFromPrefs?.let { order = it }

        //get the name of the ordered product from the intent
        order.productName = intent.getStringExtra("order").toString()

        //set the product name on the text view
        binding.tvPlacedOrder.text = order.productName

        when (order.productName) {
            "Soy Latte" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb1)
            "Chocco Frapp" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb2)
            "Bottled Americano" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb3)
            "Rainbow Frapp" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb4)
            "Caramel Frapp" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb5)
            "Black Forest Frapp" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb6)
        }

        binding.fabOrder.setOnClickListener() {
            shareIntent(applicationContext, order.productName)
        }
    }
}
