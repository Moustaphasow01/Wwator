package app

import scalafx.Includes.jfxPaint2sfx
import scala.util.Random
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

case class WorldLogic(
    size: Int,
    fishes: List[Fish] = List.empty
) {
  private val random = new Random()

  private val mooreDirs = Direction.values.map(d => (d.dx, d.dy)).toList

  def populateWorld(nbTuna: Int, nbShark: Int): WorldLogic = {
    val allPositions = (0 until size).flatMap(x => (0 until size).map(y => (x, y))).toList
    val shuffled = random.shuffle(allPositions)

    val tunaList = shuffled.take(nbTuna).map { case (x, y) => Tuna(x, y, 0, 3) }
    val sharkList = shuffled.drop(nbTuna).take(nbShark).map { case (x, y) => Shark(x, y, 0, 5, 10) }

    this.copy(fishes = tunaList ++ sharkList)
  }

  def moveFishes(): WorldLogic = {
    val fishMap = fishes.map(f => (f.x, f.y) -> f).toMap

    val updatedFishes = fishes.foldLeft(List.empty[Fish]) { (acc, fish) =>
      val occupied = acc.map(f => (f.x, f.y)).toSet

      fish match {
        case t: Tuna =>
          val emptyAdj = mooreDirs
            .map { case (dx, dy) => ((t.x + dx + size) % size, (t.y + dy + size) % size) }
            .filterNot(occupied.contains)

          val (nx, ny) = emptyAdj.headOption.getOrElse((t.x, t.y))
          acc :+ t.copy(x = nx, y = ny, breedCounter = t.breedCounter + 1)

        case s: Shark =>
          val nearbyTuna = mooreDirs
            .map { case (dx, dy) => ((s.x + dx + size) % size, (s.y + dy + size) % size) }
            .filter(p => fishMap.get(p).exists(_.isInstanceOf[Tuna]))

          val moveTo = nearbyTuna.headOption.orElse(
            mooreDirs
              .map { case (dx, dy) => ((s.x + dx + size) % size, (s.y + dy + size) % size) }
              .filterNot(occupied.contains)
              .headOption
          )

          moveTo match {
            case Some((nx, ny)) =>
              val hasEaten = fishMap.get((nx, ny)).exists(_.isInstanceOf[Tuna])
              val newEnergy = if (hasEaten) s.energy + 1 else s.energy - 1
              if (newEnergy > 0) acc :+ s.copy(x = nx, y = ny, breedCounter = s.breedCounter + 1, energy = newEnergy)
              else acc
            case None =>
              val newEnergy = s.energy - 1
              if (newEnergy > 0) acc :+ s.copy(breedCounter = s.breedCounter + 1, energy = newEnergy)
              else acc
          }
      }
    }

    this.copy(fishes = updatedFishes)
  }

  def reproduceFishes(): WorldLogic = {
    val occupied = fishes.map(f => (f.x, f.y)).toSet

    val newBorns = fishes.flatMap {
      case t: Tuna if t.breedCounter >= t.tBreed =>
        mooreDirs
          .map { case (dx, dy) => ((t.x + dx + size) % size, (t.y + dy + size) % size) }
          .filterNot(occupied.contains)
          .headOption
          .map { case (nx, ny) => Tuna(nx, ny, 0, t.tBreed) }

      case s: Shark if s.breedCounter >= s.sBreed =>
        mooreDirs
          .map { case (dx, dy) => ((s.x + dx + size) % size, (s.y + dy + size) % size) }
          .filterNot(occupied.contains)
          .headOption
          .map { case (nx, ny) => Shark(nx, ny, 0, s.sBreed, 5) }

      case _ => None
    }

    val resetFishes = fishes.map {
      case t: Tuna if t.breedCounter >= t.tBreed => t.copy(breedCounter = 0)
      case s: Shark if s.breedCounter >= s.sBreed => s.copy(breedCounter = 0)
      case f => f
    }

    this.copy(fishes = resetFishes ++ newBorns)
  }

  def drawFishes(cellSize: Int): List[Circle] = {
    fishes.map { f =>
      new Circle {
        centerX = f.x * cellSize + cellSize / 2
        centerY = f.y * cellSize + cellSize / 2
        radius = cellSize / 5
        fill = f.color
      }
    }
  }

  def play(): WorldLogic = this.moveFishes().reproduceFishes()
}
