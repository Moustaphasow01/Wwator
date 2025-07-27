package app

import scalafx.scene.paint.Color

sealed trait Fish {
  def x: Int
  def y: Int
  def breedCounter: Int
  def color: Color
}

case class Tuna(x: Int, y: Int, breedCounter: Int, tBreed: Int, color: Color = Color.Blue) extends Fish

case class Shark(x: Int, y: Int, breedCounter: Int, sBreed: Int, energy: Int, color: Color = Color.Red) extends Fish
