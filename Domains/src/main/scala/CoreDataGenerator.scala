import TokenType.*
import java.security.SecureRandom
import java.util.Base64

trait CoreDataGenerator {
  trait Algo[T] {
    def generate(length:Int,t:TokenType[T])(using sec:SecureRandom,base:Base64.Encoder):String
  }

  given CommonAlgo[T]:Algo[T] with
    override def generate(length:Int,t:TokenType[T])(using sec:SecureRandom,base:Base64.Encoder):String ={
        t match
            case x:(NUM.type | STR.type | ALPHANUM.type) =>
                (for (i<- 0 until length) yield x.value(sec.nextInt(x.value.length))).foldLeft(StringBuffer())((sb,e)=>sb.append(e)).toString()
            case x:BYTE32.type =>
                val buffer : Array[Byte]=new Array[Byte](24)
                x.value(0).nextBytes(buffer)
                base.encodeToString(buffer)    
    }

  def getRandomValues[T](length:Int,t:TokenType[T])(using algo:Algo[T]):T|String =algo.generate(length,t) 
}
