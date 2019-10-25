package com.example

import doobie._
import doobie.implicits._

trait UserRepositoryConnectionIOInterpreter {

  val connectionIOInterpreter: UserRepository[ConnectionIO] =
    new UserRepository[ConnectionIO] {

      override def find(id: Int): ConnectionIO[Option[User]] =
        sql"""|SELECT
              |  id
              |  , name
              |FROM
              |  users
              |WHERE
              |  id = $id
              |""".stripMargin.query[User].option

      override def save(user: User): ConnectionIO[Unit] =
        sql"""|INSERT INTO
              |  users
              |  (
              |    id
              |    , name
              |  )
              |VALUES
              |  (
              |    ${user.id}
              |    , ${user.name}
              |  )
              |ON DUPLICATE KEY UPDATE
              |  id = ${user.id}
              |  , name = ${user.name}
              |""".stripMargin.update.run
          .map(_ => ())
    }
}

object UserRepositoryConnectionIOInterpreter
    extends UserRepositoryConnectionIOInterpreter
