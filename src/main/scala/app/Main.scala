package app

import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration

object Main extends JFXApp3 {
  val gridSize = 50
  val cellSize = 10
  val nbTuna = 10
  val nbShark = 3

  val stepDuration: Duration = Duration(100) 

  override def start(): Unit = {
    val canvas = new Canvas(gridSize * cellSize, gridSize * cellSize)
    val gc: GraphicsContext = canvas.graphicsContext2D

    var world = WorldLogic(gridSize).populateWorld(nbTuna, nbShark)

    stage = new JFXApp3.PrimaryStage {
      title = "Wator Simulation"
      scene = new Scene(gridSize * cellSize, gridSize * cellSize) {
        content = List(canvas)
      }
    }

    val simulationTimeline = new Timeline {
      keyFrames = Seq(
        KeyFrame(stepDuration, onFinished = _ => {
          // Nettoyage de l'écran
          gc.fill = Color.White
          gc.fillRect(0, 0, canvas.width.value, canvas.height.value)

          // Mise à jour du monde
          world = world.play()

          // Dessin des entités
          world.drawFishes(cellSize).foreach { circle =>
            gc.fill = circle.fill.value
            gc.fillOval(
              circle.centerX.value - circle.radius.value,
              circle.centerY.value - circle.radius.value,
              circle.radius.value * 2,
              circle.radius.value * 2
            )
          }
        })
      )
      cycleCount = Timeline.Indefinite
    }

    simulationTimeline.play()
  }
}
