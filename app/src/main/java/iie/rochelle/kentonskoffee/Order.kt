package iie.rochelle.kentonskoffee

class Order() {
    lateinit var productName : String
    lateinit var  customerName : String
    lateinit var  customerCell : String
    lateinit var  orderDate : String

    //secondary constructor
    constructor(pName : String) : this(){
        productName = pName
    }

    //secondary constructor
    constructor(pName : String, cName : String, cCell : String, oDate : String) : this(pName){
        customerName = cName
        customerCell = cCell
        orderDate = oDate
    }

}