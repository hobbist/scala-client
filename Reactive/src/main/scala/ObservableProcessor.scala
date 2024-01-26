import Domain.*
import java.net.http.HttpClient
import monix.eval.Task
import java.net.http.HttpRequest
import java.net.URI
import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.* 
import io.circe.syntax.* 
import java.net.http.HttpResponse.BodyHandlers.ofString
import monix.reactive.Observable
object ObservableProcessor {

    def executeTest(chunk:Batch[srcItem],config:Config,httpClient:HttpClient):Task[(Batch[srcItem],Either[Error,Batch[tknItem]])] = {
   Task { 
      val request =HttpRequest.newBuilder(URI(config.uri))
      .headers(config.headers:_*)
      .method(config.method,HttpRequest.BodyPublishers.ofString(chunk.asJson.dropNullValues.deepDropNullValues.dropEmptyValues.noSpaces))
      .build()
      val response = httpClient.send(request,ofString())
      val dec:Either[io.circe.Error, Batch[tknItem]]=decode[Batch[tknItem]](response.body)
      
      (chunk,dec)
    }.onErrorRecover(t=> {
        println(t)
        (chunk,decode("{\"error\":\"Errorinmakinghttprequest\"}"))
    })
  }
  
    def captureResults(chunk:Observable[(Batch[srcItem],Either[Error,Batch[tknItem]])]):Observable[Summary]= {
        chunk.foldLeft(Summary(0.0,0.0,0.0))((summary,rec) => {
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
