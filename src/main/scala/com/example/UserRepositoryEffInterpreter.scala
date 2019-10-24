package com.example

import org.atnos.eff._

trait UserRepositoryEffInterpreter {

  protected def effInterpreter[R, F[_]](
      interpreter: UserRepository[F]
  )(implicit evidence: F |= R): UserRepository[Eff[R, *]] =
    new UserRepository[Eff[R, *]] {

      override def find(id: Int): Eff[R, Option[User]] =
        Eff.send(interpreter.find(id))

      override def save(user: User): Eff[R, Unit] =
        Eff.send(interpreter.save(user))
    }
}
