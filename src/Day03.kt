import java.util.*
import kotlin.math.max
import kotlin.math.min

sealed class Token
data object Blank : Token()
data class Number(val value: Int) : Token()
open class Symbol(val value: Char) : Token()
data object Gear : Symbol('*')

typealias Row = List<Token>

data class ThreeRows(var previous: Row, var current: Row, var next: Row) {
  fun addNextRow(row: Row) {
    previous = current
    current = next
    next = row
  }

  fun hasAdjacentSymbolAt(index: Int): Boolean {
    val prevIndex = max(index - 1, 0)
    val nextIndex = min(index + 1, current.size - 1)

    return listOf(previous, current, next).flatMap { row ->
      listOf(row[prevIndex], row[index], row[nextIndex])
    }.any { it is Symbol }
  }

  fun getAdjacentNumbers(index: Int): List<Int> {
    val prevIndex = max(index - 1, 0)
    val nextIndex = min(index + 1, current.size - 1)

    return listOf(previous, current, next).flatMap { row ->
      listOf(row[prevIndex], row[index], row[nextIndex])
    }.filterIsInstance<Number>().toIdentitySet().map { it.value }
  }

  fun getCurrentPartNumbers(): List<Int> = current.mapIndexedNotNull { index, token ->
    if (token !is Number) null
    else if (!hasAdjacentSymbolAt(index)) null
    else token
  }.toIdentitySet().map { it.value }

  fun getCurrentGearRatios(): List<Int> = current.mapIndexedNotNull { index, token ->
    if (token !is Gear) null
    else getAdjacentNumbers(index).takeIf { it.size == 2 }?.fold(1) { acc, number ->
      acc * number
    }
  }
}

fun List<Char>.toInt() = String(this.toCharArray()).toInt()

fun List<Number>.toIdentitySet() = map { it to it }.let{ IdentityHashMap<Number, Number>().apply { putAll(it) } }.keys

fun String.toTokenRow(): Row = mutableListOf<Token>().apply {
  val runningNumber = mutableListOf<Char>()

  fun addNumber() = runningNumber.takeUnless { it.isEmpty() }?.let { n ->
    val number = Number(n.toInt())
    repeat(n.size) { add(number) }
    runningNumber.clear()
  }

  toCharArray().forEach { c ->
    when (c) {
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
        runningNumber += c
      }
      '.' -> {
        addNumber()
        add(Blank)
      }
      '*' -> {
        addNumber()
        add(Gear)
      }
      else -> {
        addNumber()
        add(Symbol(c))
      }
    }
  }

  addNumber()
}

fun blankRow(n: Int) = listOf(".".repeat(n))

fun main() {
  fun part1(input: List<String>): Int = input.let {
    blankRow(input[0].length) + input + blankRow(input[0].length)
  }.let { input ->
    val rows = ThreeRows(input[0].toTokenRow(), input[0].toTokenRow(), input[0].toTokenRow())

    input.sumOf { line ->
      rows.addNextRow(line.toTokenRow())
      rows.getCurrentPartNumbers().sum()
    }
  }

  fun part2(input: List<String>): Int = input.let {
    blankRow(input[0].length) + input + blankRow(input[0].length)
  }.let { input ->
    val rows = ThreeRows(input[0].toTokenRow(), input[0].toTokenRow(), input[0].toTokenRow())

    input.sumOf { line ->
      rows.addNextRow(line.toTokenRow())
      rows.getCurrentGearRatios().sum()
    }
  }

  // test if implementation meets criteria from the description, like:
  val testInput = readInput("Day03_test")
  check(part1(testInput) == 4361)

  val input = readInput("Day03")
  part1(input).println()
  part2(input).println()
}
