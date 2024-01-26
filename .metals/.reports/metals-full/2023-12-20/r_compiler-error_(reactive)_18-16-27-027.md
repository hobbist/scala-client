file:///C:/Users/HP/Documents/scala-client/Reactive/src/main/scala/Reactive.worksheet.sc
### java.lang.StringIndexOutOfBoundsException: String index out of range: 0

occurred in the presentation compiler.

action parameters:
offset: 38
uri: file:///C:/Users/HP/Documents/scala-client/Reactive/src/main/scala/Reactive.worksheet.sc
text:
```scala
object worksheet{
  
  import _root_.@@Reactive.*
  
  Reactive.generateTestData
}
```



#### Error stacktrace:

```
java.base/java.lang.StringLatin1.charAt(StringLatin1.java:47)
	java.base/java.lang.String.charAt(String.java:693)
	scala.collection.StringOps$.apply$extension(StringOps.scala:188)
	dotty.tools.dotc.interactive.Completion$.needsBacktick(Completion.scala:187)
	dotty.tools.dotc.interactive.Completion$.backtickCompletions(Completion.scala:167)
	dotty.tools.dotc.interactive.Completion$.$anonfun$1(Completion.scala:154)
	scala.collection.immutable.List.map(List.scala:250)
	dotty.tools.dotc.interactive.Completion$.computeCompletions(Completion.scala:154)
	dotty.tools.dotc.interactive.Completion$.completions(Completion.scala:50)
	scala.meta.internal.pc.completions.Completions.completions(Completions.scala:196)
	scala.meta.internal.pc.completions.CompletionProvider.completions(CompletionProvider.scala:86)
	scala.meta.internal.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:136)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: String index out of range: 0