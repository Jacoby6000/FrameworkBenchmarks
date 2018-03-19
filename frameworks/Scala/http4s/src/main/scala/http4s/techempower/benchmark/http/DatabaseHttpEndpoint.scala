package http4s.techempower.benchmark.http

import cats.Monad
import cats.effect.Effect
import cats.implicits._
import http4s.techempower.benchmark.implicits._
import http4s.techempower.benchmark.service.DatabaseService
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Content-Type`
import org.http4s.{HttpService, MediaType}


final class DatabaseHttpEndpoint[F[_]: Effect](
    databaseService: DatabaseService[F]) {

  def service(implicit F: Monad[F]): HttpService[F] = {
    val dsl: Http4sDsl[F] = new Http4sDsl[F] {}
    import databaseService._
    import dsl._
    HttpService[F] {
      case GET -> Root / "db" =>
        for {
          w <- random >>= databaseService.selectRandomWorldId
          r <- Ok(w, `Content-Type`.apply(MediaType.`application/json`))
        } yield r
    }
  }
}
