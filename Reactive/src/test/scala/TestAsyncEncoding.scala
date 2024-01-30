import org.scalatest.featurespec.AnyFeatureSpec
import org.scalatest.GivenWhenThen
import Domain.Config
import Reactive.generateTestData
import ObservableProcessor.executeTest
import scala.concurrent.Await
import ObservableProcessor.captureResults
import scala.concurrent.duration.Duration
import Constants.given
import monix.execution.Scheduler.Implicits.global
import org.scalatest.prop.TableDrivenPropertyChecks.*
import Domain.Summary

class TestAsyncEncoding extends AnyFeatureSpec with GivenWhenThen {
  info("Load testing scenario")
  private val data =
    Table(
        ("api","uri","method","custId","valueLength","batchSize","requestCount","balanced","namespaceMap","headers","file"),
        ("encode","http://localhost:8080/encode","POST","12345",12,3,1000,false,Map("STR"-> "PART1","NUM" -> "PART2","ALPHANUM"-> "PART3","BYTE32"-> "PART4").map(kv => (TokenType.valueOf(kv._1),kv._2)),
        Array("Content-Type","application/json","Cache-Control","no-cache","Accept-Encoding","gzip,deflate,br","Accept","*/*"
        ,"Authorization","Bearer <Some VALUE>"),""),
    )

  Feature("Load Test of Encode API"){
    forAll(data){
        (api:String,uri:String,method:String,custId:String,valueLength:Int,batchSize:Int,requestCount:Int,blanaced:Boolean,authorizationMap:Map[TokenType[? >:Int&Char&java.security.SecureRandom<:Int|Char|java.security.SecureRandom],String],headers:Array[String],files:String)=>
        Scenario(s"testing for $api"){
        given cn:Config=Config(uri,method,custId,valueLength,batchSize,requestCount,true,authorizationMap,headers)
        val summary=generateTestData(cn).mapParallelUnordered(10)(s=> executeTest(s,cn,httpClient))
        val value=Await.result(captureResults(summary).runAsyncGetLast,Duration.Inf).getOrElse(Summary(100.0,0.0,100.0))
        assert(value.percentFailure<2)
        }    
    }
  }
}