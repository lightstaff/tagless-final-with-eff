package com.example

trait UserRepository[F[_]] {

  def find(id: Int): F[Option[User]]

  def save(user: User): F[Unit]
}
