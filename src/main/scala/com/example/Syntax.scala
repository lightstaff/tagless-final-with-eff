package com.example

import doobie._
import org.atnos.eff._
import org.atnos.eff.addon.doobie.DoobieConnectionIOEffect._

trait Syntax {

  implicit final def toEffConnectionIOInterpreter[R: _connectionIO]
      : UserRepository[Eff[R, *]] =
    UserRepositoryEffInterpreter.effInterpreter[R, ConnectionIO](
      UserRepositoryConnectionIOInterpreter.connectionIOInterpreter
    )
}

object Syntax extends Syntax
