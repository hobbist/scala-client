import cats.effect.IO
import StreamDataGenerator._
import Domain._
import DataProcessor._
import DataProcessor.given
import cats.effect.unsafe.implicits.global
import Constants.given

object LoadExecutor {
  def sendRequests(config:Config)=
    given cn:Config=config
    val summary=generateTestData(config).covary[IO]
    .parEvalMapUnordered(10)(executeTest[IO])
    .through(captureResults)
    .compile.toVector
    println(summary.unsafeRunSync().head.percentFailure)

  @main
  def main():Unit=
    val map:Map[TokenType[? >:Int&Char&java.security.SecureRandom<:Int|Char|java.security.SecureRandom],String] =
        Map("STR"-> "PART1","NUM" -> "PART2","ALPHANUM"-> "PART3","BYTE32"-> "PART4").map(kv => (TokenType.valueOf(kv._1),kv._2))
    val headers:Array[String]=Array()
    val config:Config=Config("http:localhost:8080/encode","POST","12345",12,3,2,true,map,headers)
    this.sendRequests(config)    
}
