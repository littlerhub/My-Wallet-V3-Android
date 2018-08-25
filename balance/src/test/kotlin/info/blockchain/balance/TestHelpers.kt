package info.blockchain.balance

import java.math.BigDecimal

fun Number.gbp() = FiatValue.fromMajor("GBP", numberToBigDecimal())

fun Number.usd() = FiatValue.fromMajor("USD", numberToBigDecimal())

fun Number.cad() = FiatValue.fromMajor("CAD", numberToBigDecimal())

fun Number.jpy() = FiatValue.fromMajor("JPY", numberToBigDecimal())

fun Number.eur() = FiatValue.fromMajor("EUR", numberToBigDecimal())

private fun Number.numberToBigDecimal(): BigDecimal =
    when (this) {
        is Double -> toBigDecimal()
        is Int -> toBigDecimal()
        else -> throw NotImplementedError("")
    }
