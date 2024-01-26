import Domain.*
import monix.reactive.Observable
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import cats.effect.{Async,IO}
import Constants.*
import java.net.URI
import java.net.http.{HttpClient,HttpRequest}
import cats.effect.{Async,IO}
import java.net.URI
import java.net.http.{HttpClient,HttpRequest}
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.* 
import io.circe.syntax.* 
import Domain.* 
import io.circe.Encoder.AsObject.importedAsObjectEncoder
import io.circe.Encoder.AsRoot.importedAsRootEncoder
import io.circe.Encoder.importedEncoder
import java.net.http.HttpResponse.BodyHandlers.ofString
import scala.util.Try
import Constants.given
import Domain.given
import monix.reactive.OverflowStrategy
import ObservableProcessor.*
import scala.concurrent.Await
import scala.concurrent.duration.Duration
object Reactive extends CoreDataGenerator {

  implicit val config:Config=Config("http://localhost:8080/encode","POST","12345",12,3,1000,false,Map("STR"-> "PART1","NUM" -> "PART2","ALPHANUM"-> "PART3","BYTE32"-> "PART4").map(kv => (TokenType.valueOf(kv._1),kv._2)),
        Array("Content-Type","application/json","Cache-Control","no-cache","Accept-Encoding","gzip,deflate,br","Accept","*/*"
        ,"Authorization","Bearer <Some VALUE>"))

  private def generateBalancedBatch(c2:Seq[Long])(config:Config)={
    c2.map(
        i => srcItem(i.toInt,getRandomValues[Char](config.valueLength,TokenType.STR).toString,
                    Option(config.authorizationMap.getOrElse(TokenType.STR,config.authorizationMap(TokenType.STR)))
                    )
    )
  }

  def generateTestData(config:Config):Observable[Batch[srcItem]] = {
    Observable.range(0,config.requestCount*config.batchSize,1)
    .bufferTumbling(config.batchSize)
    .map(s=> Batch[srcItem](Option(Metadata(config.custId)),generateBalancedBatch(s)(config).toVector))    
  }  

  @main
  def main()={
   val f=generateTestData(config)
   .mapParallelUnordered(10)(s=> executeTest(s,config,httpClient))
   
   val value=Await.result(captureResults(f).runAsyncGetLast,Duration.Inf)
   println(value)   
  }
}
