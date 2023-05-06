package libration

import scala.collection.mutable.Queue

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import typings.phaser.mod as Phaser
import typings.phaser.Phaser.Types.Tweens.TweenBuilderConfig
import typings.phaser.Phaser.Types.Core.GameConfig
import typings.phaser.Phaser.Types.Core.ScaleConfig

class Cookie(scene: Phaser.Scene) extends
    Phaser.GameObjects.Sprite(scene: Phaser.Scene, 0, 0, "cookie"):

  var rotationSpeed: Double = .005 * Phaser.Math.Angle.Random()
  var speed = Phaser.Math.Vector2(-.5 + 1.0 * Math.random(), -.5 + 1.0 * Math.random())

  rotation = Phaser.Math.Angle.Random()

  on("pointerdown", () =>
    scene.data.inc("score")
    removeInteractive()
    scene.tweens.add(
      TweenBuilderConfig(this)
        .setDuration(100)
        .set("y",  y + Phaser.Math.Between(-40,100))
        .set("x",  x + Phaser.Math.Between(-60,60))
        .set("alpha", 0.0)
    )
    scene.time.delayedCall(Phaser.Math.Between(900,3000), spawn _: js.Function)
  )

  def fixedUpdate(): Unit =
    if alpha > .8 then
      x += speed.x
      y += speed.y
      rotation += .9 * rotationSpeed
      if !Phaser.Math.Within(x, .5 * scene.cameras.main.width, .6 * scene.cameras.main.width)
        || !Phaser.Math.Within(y, .5 * scene.cameras.main.height, .6 * scene.cameras.main.height) then
        spawn()

  def spawn() =
    setInteractive()
    alpha = .0
    this.scale = .1
    x = Phaser.Math.Between(0, scene.cameras.main.width)
    y = Phaser.Math.Between(0, scene.cameras.main.height)
    scene.tweens.add(
      TweenBuilderConfig(this)
        .setDuration(100)
        .set("alpha", 1.0)
        .set("scale", .7 + .6 * Math.random())
    )


class CookieScene() extends Phaser.Scene:

  val tickMs = 1000.0 / 120.0
  var tickBalance = 0.0

  val cookies = Queue[Cookie]()

  var scoreText: Phaser.GameObjects.Text = null

  def preload() =
    load.image("cookie", "/cookie.png")

  def create() =
    data.set("score", 0)
    scoreText = add.text(.5 * cameras.main.width, 30, "click the cookies!").asInstanceOf[Phaser.GameObjects.Text]
    scoreText.setOrigin(.5, .5)
    scoreText.style
      .setFontSize("4em")
      .setFontStyle("bold")
    data.events.on("changedata-score", ((_: Any, value: Any, _: Any) =>
      scoreText
        .setText(value.toString())
        .setScale(1.0)
      tweens.add(
        TweenBuilderConfig(scoreText)
          .setDuration(100)
          .setYoyo(true)
          .set("scale", 1.5 + .2 * Math.random())
      )
    ) : js.Function)

    for i <- 1 to 23 do
      val c = Cookie(this)
      c.spawn()
      cookies.addOne(c)
      add.existing(c)

  override def update(time: Double, delta: Double): Unit =
    super.update(time, delta)

    tickBalance += delta

    // the movement is calculated as a series of ticks per frame. (120 per second)
    while tickBalance >= tickMs do
      tickBalance -= tickMs
      cookies.foreach(_.fixedUpdate())



val screenScaling = ScaleConfig().setMode(Phaser.Scale.ScaleModes.RESIZE)

val config = GameConfig()
  .setType(Phaser.AUTO)
  .setParent("app")
  .setAutoCenter(Phaser.Scale.Center.CENTER_BOTH)
  .setScale(screenScaling)
  .setWidth(800)
  .setHeight(600)
  .setScene(new CookieScene())

@main
def main(): Unit =
  new Phaser.Game(config)