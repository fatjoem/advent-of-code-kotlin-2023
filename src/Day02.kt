import Color.*

enum class Color { red, green, blue }
class QuantifiedColor(input: String) {
  val quantity = input.split(" ")[0].toInt()
  val color =  input.split(" ")[1].let { Color.valueOf(it) }
}

class Game(input: String) {
  val gameIndex = input.split(":")[0].split(" ")[1].toInt()
  val draws = input.split(":")[1].split(";").map(::Draw)
}

class Draw(input: String) {
  val quantifiedColors = input.split(",").map(String::trim).map(::QuantifiedColor)
}

class GameConfig(vararg config: Pair<Color, Int> = emptyArray()) {
  private val config: MutableMap<Color, Int> = config.takeUnless { it.isEmpty() }?.let { mutableMapOf(*it) } ?: mutableMapOf(
    red to 0,
    green to 0,
    blue to 0,
  )

  fun addMaxColor(quantifiedColor: QuantifiedColor) {
    if (config[quantifiedColor.color]!! < quantifiedColor.quantity) {
      config[quantifiedColor.color] = quantifiedColor.quantity
    }
  }

  fun hasMinQuantity(quantifiedColor: QuantifiedColor): Boolean = config[quantifiedColor.color]!! >= quantifiedColor.quantity

  fun toPower() = config[red]!! * config[green]!! * config[blue]!!
}

fun main() {
  fun part1(input: List<String>) = GameConfig(red to 12, green to 13, blue to 14).let { config ->
    input.map(::Game).filter{ game ->
      game.draws.flatMap { draw -> draw.quantifiedColors.map { config.hasMinQuantity(it) } }.all { it }
    }.sumOf { it.gameIndex }
  }

  fun part2(input: List<String>) = input.map(::Game).sumOf { game ->
    game.draws.flatMap { draw -> draw.quantifiedColors }.fold(GameConfig()) { config, quantifiedColor ->
      config.apply { addMaxColor(quantifiedColor) }
    }.toPower()
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day02_test")
  check(part1(testInput) == 8)

  val input = readInput("Day02")
  part1(input).println()
  part2(input).println()
}
