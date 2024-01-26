import sbt.* 
object Dependencies {
  val circeVersion="0.14.3"
  val catsVersion="2.9.0"
  val fs2Version="3.3.0"
  val monixVersion="3.4.0"
  val scalaTestVersion="3.2.15"

  private val cats_core="org.typelevel"%%"cats-core"%catsVersion
  private val fs2_core="co.fs2"%%"fs2-core"%fs2Version
  private val fs2_io="co.fs2"%%"fs2-io"%fs2Version
  private val circe_core="io.circe"%%"circe-core"%circeVersion
  private val circe_generic="io.circe"%%"circe-generic"%circeVersion
  private val circe_io="io.circe"%%"circe-parser"%circeVersion
  private val monix_eval="io.monix"%%"monix-eval"%monixVersion
  private val monix_reactive="io.monix"%%"monix-reactive"%monixVersion
  private val scalaTest="org.scalatest"%%"scalatest"%scalaTestVersion %"test"
  private val scalaFeatureSpec="org.scalatest"%%"scalatest-featurespec"%scalaTestVersion % "test"

  lazy val commonDeps:Seq[ModuleID] = Seq (cats_core,circe_core,circe_generic,circe_io,scalaTest,scalaFeatureSpec)
  lazy val fsDeps:Seq[ModuleID] = Seq (fs2_core,fs2_io)
  lazy val monixDeps:Seq[ModuleID] = Seq (monix_eval,monix_reactive)
}
