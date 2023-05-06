# Scala.js / Phaser.js Template

This is a small template project to set up a [Scala.js](https://www.scala-js.org/) project using the [Phaser3](https://phaser.io/phaser3) framework for 2d HTML5 games.

## Set up / building

To set up and build the project

```
npm install
npm run build
```

The first invocation of `npm run build` will take quite some time and memory to build the Scala typing facades for Phaser3.


## Development mode

For development mode with incremental builds and clever reloading, use two terminals, one running vite

```
npm run dev
```

and the other (for sbt):

```
npm run dev-sbt
```