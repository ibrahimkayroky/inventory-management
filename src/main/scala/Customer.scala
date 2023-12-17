import akka.actor.Actor

import java.io.File

class Customer(productsFile: File) extends Actor {
  val fileHandling = new FileHandling(productsFile: File)

  def receive: Receive = {
    case Buy(id, quantity) =>
      fileHandling.products.find(_.id == id) match {
        case Some(product) =>
          if (product.quantity < quantity)
            println("not enough quantity available")
          else {
            product.quantity -= quantity
            println("Product quantity modified successfully.")
          }
        case None =>
          println("Product not found.")
      }

    case ViewProducts =>
      if (fileHandling.products.isEmpty) {
        println("No products available.")
      } else {
        println("ID\tName\tQuantity")
        fileHandling.products.foreach { product =>
          println(s"${product.id}\t${product.name}\t${product.quantity}")
        }
      }
    case SaveProducts =>
      fileHandling.saveProductsToFile(fileHandling.products, productsFile)
      println("changes handled.")
  }
}