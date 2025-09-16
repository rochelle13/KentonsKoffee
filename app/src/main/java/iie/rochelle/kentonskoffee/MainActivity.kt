package iie.rochelle.kentonskoffee

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import iie.rochelle.kentonskoffee.databinding.ActivityMainWithNavDrawerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var database: FirebaseDatabase
    var order = Order()
    private lateinit var binding: ActivityMainWithNavDrawerBinding
    private lateinit var orderDAO: OrderDAO //Declare DAO
    private lateinit var db: AppDatabase //Declare Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainWithNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()

        //Initialize the database and DAO
        db = AppDatabase.getDatabase(this) as AppDatabase
        orderDAO = db.orderDAO()

        binding.imgSb1.setOnClickListener(this)
        binding.imgSb2.setOnClickListener(this)
        binding.imgSb3.setOnClickListener(this)
        binding.imgSb4.setOnClickListener(this)
        binding.imgSb5.setOnClickListener(this)
        binding.imgSb6.setOnClickListener(this)

        setSupportActionBar(binding.navToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        var toggleOnOff = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.navToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggleOnOff)
        toggleOnOff.syncState()

        binding.navView.bringToFront()
        binding.navView.setNavigationItemSelectedListener(this)
        insertProducts()
    }

    //Insert products into database
    private fun insertProducts() {
        val products = listOf(
            OrderEntity(
                productName = "Soy Latte",
                customerName = "",
                customerCell = "",
                orderDate = ""
            ),
            OrderEntity(
                productName = "Choco Frappe",
                customerName = "",
                customerCell = "",
                orderDate = ""
            ),
            OrderEntity(
                productName = "Bottled Americano",
                customerName = "",
                customerCell = "",
                orderDate = ""
            ),
            OrderEntity(
                productName = "Rainbow Frappe",
                customerName = "",
                customerCell = "",
                orderDate = ""
            ),
            OrderEntity(
                productName = "Caramel Frappe",
                customerName = "",
                customerCell = "",
                orderDate = ""
            ),
            OrderEntity(
                productName = "Black Forest Frappe",
                customerName = "",
                customerCell = "",
                orderDate = ""
            ),
        )
        //used to insert products in the background
        CoroutineScope(Dispatchers.IO).launch {
            products.forEach {
                orderDAO.insert(it)
                saveOrderToFirebase(it)
            }

        }
    }

    private fun saveOrderToFirebase(order: OrderEntity) {
        //get a reference to the orders node in the database
        val ordersRef = database.getReference("orders")

        //Generate a unique key for the new order
        val orderId = ordersRef.push().key

        //set the data using the key
        orderId?.let {
            ordersRef.child(it).setValue(order).addOnSuccessListener {
                //Successfully saved
                Toast.makeText(this, "Order saved to Firebase", Toast.LENGTH_SHORT).show()

            }
                .addOnFailureListener { e ->
                    //Successfully saved
                    Toast.makeText(this, "Failed to save order: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }

        }
    }

    //Query from Room and display a Toast
    private fun getProductFromDB(productName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val product = orderDAO.getAllOrders().firstOrNull { it.productName == productName }
            product?.let {
                //saves to Json
                JsonUtils.saveOrderPrefences(this@MainActivity, it)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "S{it.productName}", Toast.LENGTH_SHORT)
                        .show()
                    openIntent(
                        applicationContext,
                        it.productName,
                        OrderDetailsActivity::class.java
                    )
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_sb1 -> getProductFromDB("Soy Latte")
            R.id.img_sb2 -> getProductFromDB("Choco Frappe")
            R.id.img_sb3 -> getProductFromDB("Bottled Americano")
            R.id.img_sb4 -> getProductFromDB("Rainbow Frappe")
            R.id.img_sb5 -> getProductFromDB("Caramel Frappe")
            R.id.img_sb6 -> getProductFromDB("Black Forest Frappe")
        }
    }

    override fun onBackPressed() {
        //closes drawer if open
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            //lets super class handle it
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_photo -> openIntent(this, "", CoffeeSnapsActivity::class.java)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}