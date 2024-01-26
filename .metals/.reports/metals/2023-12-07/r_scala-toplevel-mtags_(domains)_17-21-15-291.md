id: file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/CoreDataGenerator.scala:[273..274) in Input.VirtualFile("file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/CoreDataGenerator.scala", "import TokenType.*
import java.security.SecureRandom
import java.util.Base64

trait CoreDataGenerator {
  trait Algo[T] {
    def generate(length:Int,t:TokenType[T])(using sec:SecureRandom,base:Base64.Encoder):String
  }

  given CommonAlgo[T]:Algo[T] with
  override def 
}
")
file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/CoreDataGenerator.scala
file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/CoreDataGenerator.scala:12: error: expected identifier; obtained rbrace
}
^
#### Short summary: 

expected identifier; obtained rbrace