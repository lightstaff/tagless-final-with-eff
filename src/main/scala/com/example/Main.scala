package com.example

import scala.concurrent.ExecutionContext

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import org.atnos.eff._
import org.atnos.eff.all._
import org.atnos.eff.syntax.addon.cats.effect._
import org.atnos.eff.syntax.addon.doobie._
import org.atnos.eff.syntax.all._

object Main extends App {

  import Syntax._

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val tx = Transactor.fromDriverManager[IO](
    "org.h2.Driver",
    "jdbc:h2:mem:test;DATABASE_TO_UPPER=false;MODE=MYSQL;DB_CLOSE_DELAY=-1",
    "sa",
    ""
  )

  val create =
    sql"""|CREATE TABLE users
          |(
          |  id INT NOT NULL PRIMARY KEY
          |  , name VARCHAR(50)
          |)
          |""".stripMargin.update.run

  val insert =
    sql"""INSERT INTO users
         |(
         |  id
         |  , name
         |)
         |VALUES
         |(
         |  1,
         |  ''
         |)
         |""".stripMargin.update.run

  (create, insert).mapN(_ + _).transact(tx).unsafeRunSync()

  def update[R: _throwableEither](id: Int, name: String)(
      implicit userRepository: UserRepository[Eff[R, *]]
  ): Eff[R, User] =
    for {
      optionalUser <- userRepository.find(id)
      user <- fromEither(
               optionalUser.toRight[Throwable](new Exception("Not Found"))
             )
      updated = user.copy(name = name)
      _ <- userRepository.save(updated)
    } yield updated

  type Stack = Fx.fx3[ThrowableEither, ConnectionIO, IO]

  val result = update[Stack](1, "test")
    .runEither[Throwable]
    .runConnectionIO(tx)
    .unsafeRunSync

  println(result)
}
