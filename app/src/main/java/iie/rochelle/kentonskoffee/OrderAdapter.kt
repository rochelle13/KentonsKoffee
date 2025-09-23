package iie.rochelle.kentonskoffee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private var orders: List<OrderEntity>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productNameTextView: TextView = view.findViewById(R.id.tv_product_name)
        val customerNameTextView: TextView = view.findViewById(R.id.tv_customert_name)
        val orderDateTextView: TextView = view.findViewById(R.id.tv_order_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun getItemCount(): Int {
       return orders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.productNameTextView.text = order.productName
        holder.customerNameTextView.text = order.customerName
        holder.orderDateTextView.text = order.orderDate
    }

    fun updateOrders(newOrders: List<OrderEntity>){
        orders = newOrders
        notifyDataSetChanged()
    }
}