import Domain.{Batch,Metadata,srcItem,tknItem}
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.ForkJoinPool
import java.net.http.HttpClient
import java.net.http.HttpClient.Version
import io.circe.{Decoder,HCursor,Encoder,Json}
import io.circe.generic.auto.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import java.security.SecureRandom
import java.util.Base64


/**
  * send to token - { metadata:{custId:""},
  *   items:[{id:"",source:"",namespace:""},...}
  * }
  * response - { metadata:{custId:""},
  *   items:[{id:"",token:"",<error:"">},...}
  * }
  *  
  * get original - { metadata:{custId:""},
  *   items:[{id:"",token:"",namespace:""},...}
  * }
  * response - { metadata:{custId:""},
  *   items:[{id:"",soruce:"",<error:"">},...}
  * }
  */

object Constants {
  given HTTP_TIMOUT:Long=20L
  given httpClientpoolFactory:ForkJoinWorkerThreadFactory=(pool:ForkJoinPool)=>{
    val worker= ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool)
    worker.setName("http-client-calls-pool"+worker.getPoolIndex())
    worker
  }

  given httpClient:HttpClient=HttpClient.newBuilder
  .version(Version.HTTP_1_1)
  .executor(new ForkJoinPool(Runtime.getRuntime.availableProcessors,httpClientpoolFactory,null,true))
  .connectTimeout(java.time.Duration.ofSeconds(HTTP_TIMOUT)).build

  given responseDecoder : Decoder[Batch[tknItem]]= (c:HCursor) =>
    for{
        m <-  c.downField("metadata").as[Option[Metadata]]
        i <-  c.downField("items").as[Option[Vector[tknItem]]].map(_.getOrElse(Vector.empty[tknItem]))
        s <-  c.downField("status").as[Option[Int]].map(_.map(_.toString))
        e <-  c.downField("error").as[Option[String]]
    } yield Batch(m,i,s,e)
}

  given metadataEncoder:Encoder[Option[Metadata]]={
    case Some(x) => Json.obj(("custId",x.custId.asJson))
    case None => Json.Null
  }

  given batchRequestEncoder:Encoder[Batch[srcItem]]=(obj:Batch[srcItem])=>{
    val errorJson=if obj.error.isEmpty then Json.Null else obj.error.get.asJson
    val status=if obj.statusCode.isEmpty then Json.Null else obj.statusCode.get.asJson
    Json.obj(("metadata",obj.metadata.asJson),("items",obj.items.asJson),("error",errorJson),("status",status))
  }

  given sec: SecureRandom=SecureRandom()
  given base:Base64.Encoder=Base64.getEncoder.withoutPadding