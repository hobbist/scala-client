import java.security.SecureRandom

enum TokenType[T](baseValue:Vector[T]){
    case NUM extends TokenType(Vector(0,1,2,3,4,5,6,7,8,9))
    case STR extends TokenType(Vector('A','a','B','b','C','c','D','d','E','e','F','f','G','g','H','h','I','i','J','j','K','k','L','l','M','m','N','n','O','o','P','p','Q','q','R','r','S','s','T','t','U','u','V','v','W','w','X','x','Y','y','Z','z'))
    case ALPHANUM extends TokenType(Vector('A','a','B','b','C','c','D','d','E','e','F','f','G','g','H','h','I','i','J','j','K','k','L','l','M','m','N','n','O','o','P','p','Q','q','R','r','S','s','T','t','U','u','V','v','W','w','X','x','Y','y','Z','z','0','1','2','3','4','5','6','7','8','9'))
    case BYTE32 extends TokenType(Vector(SecureRandom()))

    def value=baseValue
}

object Domain {
    case class Metadata(custId:String)
    case class Config(uri:String,method:String,custId:String,valueLength:Int,batchSize:Int,requestCount:Int,balanceValues:Boolean=true,
    authorizationMap:Map[TokenType[? >:Int&Char&java.security.SecureRandom<:Int|Char|java.security.SecureRandom],String],headers:Array[String]
    )

    sealed trait Item(id:Int,namespace:Option[String],error:Option[String])
    case class srcItem(id:Int,source:String, namespace:Option[String],error:Option[String]=None) extends Item(id,namespace,error)
    case class tknItem(id:Int,tkn:Option[String],namespace:Option[String]=None,error:Option[String]=None) extends Item(id,namespace,error)

    case class Batch[+A<:Item](metadata:Option[Metadata]=None,items:Vector[A]=Vector.empty,statusCode:Option[String]=None,error:Option[String]=None)

    case class Summary(failed:Double,success:Double,total:Double):
        lazy val percentFailure:Double=(failed/total)*100  
}
