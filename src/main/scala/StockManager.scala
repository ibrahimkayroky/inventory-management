import akka.actor.Actor

import java.io.File

class StockManager(productsFile: File) extends Actor {
  val fileHandling = new FileHandling(productsFile: File)

  def receive: Receive = {

    case ViewProducts =>
      if (fileHandling.products.isEmpty) {
        println("No products available.")
      } else {
        println("ID\tName\tQuantity")
        fileHandling.products.foreach { product =>
          println(s"${product.id}\t${product.name}\t${product.quantity}")
        }
      }


    case AddProduct(name, quantity) =>
      val newProductId = if (fileHandling.products.nonEmpty) fileHandling.products.maxBy(_.id).id + 1 else 1
      val newProduct = new Product(newProductId, name, quantity)
      fileHandling.products :+= newProduct
      println("Product added successfully.")

    case DeleteProduct(id) =>
      val updatedProducts = fileHandling.products.filterNot(_.id == id)
      if (updatedProducts.length < fileHandling.products.length) {
        fileHandling.products = updatedProducts
        println("Product deleted successfully.")
      } else {
        println("Product not found.")
      }

    case ModifyProductQuantity(id, quantity) =>
      fileHandling.products.find(_.id == id) match {
        case Some(product) =>
          product.quantity = quantity
          println("Product quantity modified successfully.")
        case None =>
          println("Product not found.")
      }


    case SaveProducts =>
      fileHandling.saveProductsToFile(fileHandling.products, productsFile)
      println("Products saved to file.")
  }
}