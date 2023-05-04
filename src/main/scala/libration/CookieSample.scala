package libration

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import typings.phaser.mod as Phaser
import typings.phaser.Phaser.Types.Tweens.TweenBuilderConfig
import typings.phaser.Phaser.Types.Tweens.TweenOnCompleteCallback
import typings.phaser.Phaser.Types.Core.GameConfig
import typings.std.stdStrings.add


class Cookie(scene: Phaser.Scene, startX: Double, startY: Double) extends
    Phaser.GameObjects.Sprite(scene: Phaser.Scene, startX: Double, startY: Double, "cookie"):
  
  setInteractive()
  on("pointerdown", () => {
    println(s"click! $x")

    setInteractive(false)
    scene.tweens.add(
      TweenBuilderConfig(this)
        .setDuration(100)
        .set("y",  y + Phaser.Math.Between(40,100))
        .set("x",  x + Phaser.Math.Between(-40,40))
        .set("alpha", 0.0)
        .set("onComplete", (() => this.destroy()): js.Function)
    )
  })

class CookieScene() extends Phaser.Scene:

  def preload() =
    load.image("cookie", "/cookie.png")

  def create() =
    add.existing(new Cookie(this, 20, 20))
    add.existing(new Cookie(this, 20, 500))
    add.existing(new Cookie(this, 420, 400))
    add.existing(new Cookie(this, 220, 400))
    add.existing(new Cookie(this, 120, 300))

  override def update(time: Double, delta: Double): Unit =
    super.update(time, delta)

val config = GameConfig()
  .setType(Phaser.AUTO: Double)
  .setParent("app")
  .setWidth(800)
  .setHeight(600)
  .setScene(new CookieScene())

@main
def main(): Unit =
  new Phaser.Game(config)