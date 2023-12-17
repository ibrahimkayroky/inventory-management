import java.io.{File, PrintWriter}
import scala.io.Source

class FileHandling(productsFile: File) {
  var products: List[Product] = loadProductsFromFile(productsFile)

  private def loadProductsFromFile(file: File): List[Product] = {
    if (file.exists()) {
      val source = Source.fromFile(file)
      val products = source.getLines().map(parseProduct).toList
      source.close()
      products
    } else {
      List.empty[Product]
    }
  }

  def saveProductsToFile(products: List[Product], file: File): Unit = {
    val writer = new PrintWriter(file)
    products.foreach { product =>
      writer.println(s"${product.id},${product.name},${product.quantity}")
    }
    writer.close()
  }

  private def parseProduct(line: String): Product = {
    val fields = line.split(",")
    val id = fields(0).toInt
    val name = fields(1)
    val quantity = fields(2).toInt
    new Product(id, name, quantity)
  }

}