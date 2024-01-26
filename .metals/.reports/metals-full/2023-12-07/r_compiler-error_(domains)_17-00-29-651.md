file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/Constants.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 809
uri: file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/Constants.scala
text:
```scala
import Domain.{Batch,Metadata,srcItem,tknItem}
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.ForkJoinPool
import java.net.http.HttpClient
import java.net.http.HttpClient.Version


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

  given responseDecoder : Decoder[]@@
}

```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2576)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:94)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner