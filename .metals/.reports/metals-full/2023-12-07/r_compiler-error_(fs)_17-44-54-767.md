file:///C:/Users/HP/Documents/scala-client/Fs/src/main/scala/DataProcessor.scala
### java.nio.file.InvalidPathException: Illegal char <:> at index 3: jar:file:///C:/Users/HP/AppData/Local/Coursier/cache/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.10/scala-library-2.13.10-sources.jar!/scala/util/Either.scala

occurred in the presentation compiler.

action parameters:
offset: 1339
uri: file:///C:/Users/HP/Documents/scala-client/Fs/src/main/scala/DataProcessor.scala
text:
```scala
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
        // => http request
        val request =HttpRequest.newBuilder(URI(config.uri))
        .headers(config.headers:_*)
        // => convert to JSON
        .method(config.method,HttpRequest.BodyPublishers.ofString(chunk.asJson.dropNullValues.deepDropNullValues.dropEmptyValues.noSpaces))
        .build()
        
        val response = httpClient.send(request,ofString())
        val dec=decode[Batch[tknItem]](response.body)
        callback(Right((chunk,dec)))
    }

  def captureResults(chunk:Stream[IO,(Batch[srcItem],Either[Error,Batch[tknItem]])]):Stream[IO,Summary] ={
    chunk.fold(Summary(0.0,0.0,0.0))((summary,rec) => {
        rec._2 m@@
    }
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