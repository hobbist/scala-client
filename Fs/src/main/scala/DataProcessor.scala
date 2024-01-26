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
import fs2.* 
import java.net.http.HttpResponse.BodyHandlers.ofString
import scala.util.Try
import Constants.given

object DataProcessor {
  def executeTest[F[_]:Async](chunk:Batch[srcItem])(using config:Config,httpClient:HttpClient):F[(Batch[srcItem],Either[Error,Batch[tknItem]])] =
    Async[F].async_{ callback => 
        try {
            // => http request
            //handle if http throws any exception
            val request = HttpRequest.newBuilder(URI(config.uri)).headers(config.headers:_*)
            // => convert to JSON
            .method(config.method,HttpRequest.BodyPublishers.ofString(chunk.asJson.dropNullValues.deepDropNullValues.dropEmptyValues.noSpaces))
            .build()
            val response = httpClient.send(request,ofString())
            val dec=decode[Batch[tknItem]](response.body)
            callback(Right((chunk,dec)))
        } catch{
            case e:Exception => 
                callback(Right((chunk,decode(s"{\"error\":\"Errorinmakinghttprequest ${e.getMessage()} \"}"))))
        }

    }

  def captureResults(chunk:Stream[IO,(Batch[srcItem],Either[Error,Batch[tknItem]])]):Stream[IO,Summary] ={
    chunk.fold(Summary(0.0,0.0,0.0))((summary,rec) => {
        rec._2 match {
            case Left(error) => 
                println(s"parsing Error $error")
                Summary(summary.failed+rec._1.items.length,summary.success,summary.total+rec._1.items.length)
            case Right(Batch(_,_,_,Some(error))) => 
                print(s"processing Error $error")
                Summary(summary.failed+rec._1.items.length,summary.success,summary.total+rec._1.items.length)
            case Right(Batch(_,items,_,None)) =>
                items match {
                    case x if x.isEmpty => Summary(summary.failed+rec._1.items.length,summary.success,summary.total+rec._1.items.length)
                    case _ => 
                        var fail,suc,tot:Double=0.0
                        for(i<-items){
                            i match
                                case tknItem(_, _, _, Some(e)) => fail+=1;tot+=1
                                case tknItem(_, tkn, _, None) if tkn!=null =>
                                    suc+=1;tot+=1
                                case _ => fail+=1;tot+=1;     
                        }
                        Summary(fail,suc,tot)
                }
        }
    })
  }  
}
