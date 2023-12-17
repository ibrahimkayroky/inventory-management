import akka.actor.{Actor, ActorSystem, Props}
import scala.io.StdIn
import java.io.{File, PrintWriter}
import scala.io.Source

case class AddProduct(name: String, quantity: Int)

case class DeleteProduct(id: Int)

case class ModifyProductQuantity(id: Int, quantity: Int)

case object ViewProducts

case object SaveProducts

case class Buy(id: Int, quantity: Int)

class Product(val id: Int, var name: String, var quantity: Int)

object Start {
  def main(args: Array[String]): Unit = {
    val password = 1234
    val productsFile = new File("products.txt")
    val system = ActorSystem("StockManagementSystem")
    val stockManager = system.actorOf(Props(new StockManager(productsFile)))
    val customer = system.actorOf(Props(new Customer(productsFile)))
    val fileHandling = new FileHandling(productsFile)

    println("enter 1 for stock manager")
    println("enter 2 for customer")

    val Choice = StdIn.readInt()

    Choice match {
      case 1 =>

        println("enter stockManager pass")
        var pass = StdIn.readInt()
        if (pass == password) {

          var isRunning = true
          while (isRunning) {

            Thread.sleep(100)

            println("-----------------------------------------------------------------------------------")
            println("Stock Management System")
            println("1. View Products")
            println("2. Add Product")
            println("3. Delete Product")
            println("4. Modify Product Quantity")
            println("5. Exit")
            print("Enter your choice: ")
            val choice = StdIn.readInt()

            choice match {

              case 1 =>
                stockManager ! ViewProducts

              case 2 =>
                print("Enter product name: ")
                val name = StdIn.readLine()
                print("Enter product quantity: ")
                val quantity = StdIn.readInt()
                stockManager ! AddProduct(name, quantity)
                stockManager ! SaveProducts

              case 3 =>
                print("Enter the ID of the product to delete: ")
                val id = StdIn.readInt()
                stockManager ! DeleteProduct(id)
                stockManager ! SaveProducts

              case 4 =>
                print("Enter the ID of the product to modify: ")
                val id = StdIn.readInt()
                print("Enter the new quantity: ")
                val quantity = StdIn.readInt()
                stockManager ! ModifyProductQuantity(id, quantity)
                stockManager ! SaveProducts

              case 5 =>
                stockManager ! SaveProducts
                isRunning = false
                system.terminate()

              case _ => println("Invalid choice. Please try again.")
            }
          }
        }

        else {
          println("wrong")
        }

      case 2 =>
        println("-----------------------------------------------------------------------------------")
        println("Customer interface")

        var isRunning = true
        while (isRunning) {
          Thread.sleep(100)
          println("-----------------------------------------------------------------------------------")
          println("1. View Products")
          println("2. Buy Products")
          println("3. Exit")

          val choice = StdIn.readInt()
          choice match {

            case 1 => customer ! ViewProducts

            case 2 =>
              print("Enter the ID of the product to buy: ")
              val id = StdIn.readInt()
              print("Enter product quantity: ")
              val quantity = StdIn.readInt()
              customer ! Buy(id, quantity)
              customer ! SaveProducts

            case 3 =>
              customer ! SaveProducts
              isRunning = false
              system.terminate()

            case _ => println("Invalid choice. Please try again.")
          }
        }
        system.terminate()
    }
  }
}