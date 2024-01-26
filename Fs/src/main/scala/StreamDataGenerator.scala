import fs2.{Chunk,Pipe, Pull,Pure,Stream}
import fs2.io.file.{Files,Path}
import cats.effect.{Async,Deferred,IO}
import cats.effect.unsafe.implicits.global
import Domain.*
import Constants.given

object StreamDataGenerator extends CoreDataGenerator {
  private def generateBalancedBatch(c2:Chunk[Int])(config:Config)={
    c2.map(
        i => srcItem(i,getRandomValues[Char](config.valueLength,TokenType.STR).toString,
                    Option(config.authorizationMap.getOrElse(TokenType.STR,config.authorizationMap(TokenType.STR)))
                    )
    )
  }

  // config => batch of types => Stream of Batches
  def generateTestData(config:Config):Stream[Pure,Batch[srcItem]]={
    Stream.range(0,config.requestCount*config.batchSize,1)
    .chunkN(config.batchSize,false)
    .mapChunks(c =>
      c.map(c2 =>
        Batch[srcItem](Option(Metadata(config.custId)),generateBalancedBatch(c2)(config).toVector)
        )    
    )
  }
}
