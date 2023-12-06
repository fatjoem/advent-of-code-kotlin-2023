import Color.*

enum class Color { red, green, blue }

class GameConfig {
  private val config  = mutableMapOf(
    red to 0,
    green to 0,
    blue to 0,
  )

  fun addMaxColorQuantity(color: Color, quantity: Int) {
    if (config[color]!! < quantity) {
      config[color] = quantity
    }
  }

  fun addMaxColorQuantity(quantifiedColor: QuantifiedColor) = addMaxColorQuantity(quantifiedColor.color, quantifiedColor.quantity)

  fun hasMinQuantity(quantifiedColor: QuantifiedColor): Boolean = config[quantifiedColor.color]!! >= quantifiedColor.quantity

  fun toPower() = config[red]!! * config[green]!! * config[blue]!!
}

data class QuantifiedColor(val quantity: Int, val color: Color) {
  companion object {}
}

fun QuantifiedColor.Companion.fromInput(input: String): QuantifiedColor {
  val (quantity, color) = input.trim().split(" ")
  return QuantifiedColor(quantity.toInt(), Color.valueOf(color))
}

fun main() {
  fun part1(input: List<String>): Int {
    val part1configuration = GameConfig().apply {
      addMaxColorQuantity(red, 12)
      addMaxColorQuantity(green, 13)
      addMaxColorQuantity(blue, 14)
    }

    return input.sumOf { game ->
      val (gameIndex, gameValue) = game.split(":")

      gameIndex.split(" ")[1].toInt().takeIf {
        gameValue.split(';').flatMap { draw -> draw.split(",").map { part1configuration.hasMinQuantity(QuantifiedColor.fromInput(it)) } }.all { it }
      } ?: 0
    }
  }

  fun part2(input: List<String>) = input.sumOf { game ->
    val gameConfig = GameConfig()

    game.split(":")[1].split(';').flatMap { draw -> draw.split(",") }.forEach {
      gameConfig.addMaxColorQuantity(QuantifiedColor.fromInput(it))
    }

    gameConfig.toPower()
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day02_test")
  check(part1(testInput) == 8)

  val input = readInput("Day02")
  part1(input).println()
  part2(input).println()
}
