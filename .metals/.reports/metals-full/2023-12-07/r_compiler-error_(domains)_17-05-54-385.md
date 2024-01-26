file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/Constants.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/HP/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/util/Either.scala

occurred in the presentation compiler.

action parameters:
offset: 1109
uri: file:///C:/Users/HP/Documents/scala-client/Domains/src/main/scala/Constants.scala
text:
```scala
import Domain.{Batch,Metadata,srcItem,tknItem}
import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
import java.util.concurrent.ForkJoinPool
import java.net.http.HttpClient
import java.net.http.HttpClient.Version
import io.circe.{Decoder,HCursor,Encoder,Json}
import io.circe.generic.auto.*
import io.circe.generic.semiauto.*
import io.circe.syntax.*

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
        i <-  c.downField("items").as[Option[Vector[tknItem]]].m@@

    }
}

```



#### Error stacktrace:

```
java.base/sun.nio.fs.WindowsPathParser.normalize(WindowsPathParser.java:182)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:153)
	java.base/sun.nio.fs.WindowsPathParser.parse(WindowsPathParser.java:77)
	java.base/sun.nio.fs.WindowsPath.parse(WindowsPath.java:92)
	java.base/sun.nio.fs.WindowsFileSystem.getPath(WindowsFileSystem.java:229)
	java.base/java.nio.file.Path.of(Path.java:147)
	java.base/java.nio.file.Paths.get(Paths.java:69)
	scala.meta.io.AbsolutePath$.apply(AbsolutePath.scala:60)
	scala.meta.internal.metals.MetalsSymbolSearch.$anonfun$definitionSourceToplevels$2(MetalsSymbolSearch.scala:62)
	scala.Option.map(Option.scala:242)
	scala.meta.internal.metals.MetalsSymbolSearch.definitionSourceToplevels(MetalsSymbolSearch.scala:61)
	scala.meta.internal.pc.completions.CaseKeywordCompletion$.sortSubclasses(MatchCaseCompletions.scala:306)
	scala.meta.internal.pc.completions.CaseKeywordCompletion$.matchContribute(MatchCaseCompletions.scala:254)
	scala.meta.internal.pc.completions.Completions.advancedCompletions(Completions.scala:375)
	scala.meta.internal.pc.completions.Completions.completions(Completions.scala:183)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:86)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:123)
```
#### Short summary: 

java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/HP/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/util/Either.scala