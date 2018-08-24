package info.blockchain.balance

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

private object LocaleDecimalFormat {

    private val cache: MutableMap<Locale, DecimalFormat> = ConcurrentHashMap()

    operator fun get(locale: Locale) = cache.getOrPut(locale) {
        NumberFormat.getCurrencyInstance(locale) as DecimalFormat
    }
}

private data class Key(val locale: Locale, val currencyCode: String, val includeSymbol: Boolean)

private object FiatFormat {

    private val cache: MutableMap<Key, NumberFormat> = ConcurrentHashMap()

    operator fun get(key: Key) = cache.getOrPut(key) {
        val currencyInstance = Currency.getInstance(key.currencyCode)
        (NumberFormat.getCurrencyInstance(key.locale) as DecimalFormat)
            .apply {
                decimalFormatSymbols =
                    decimalFormatSymbols.apply {
                        currency = currencyInstance
                        if (!key.includeSymbol) {
                            currencySymbol = ""
                        }
                    }
                minimumFractionDigits = currencyInstance.defaultFractionDigits
                maximumFractionDigits = currencyInstance.defaultFractionDigits
            }
    }
}

data class FiatValue(
    val currencyCode: String,
    val value: BigDecimal
) {
    val isZero: Boolean = value.signum() == 0

    fun toStringWithSymbol(locale: Locale): String =
        FiatFormat[Key(locale, currencyCode, includeSymbol = true)]
            .format(value)

    fun toStringWithoutSymbol(locale: Locale): String =
        FiatFormat[Key(locale, currencyCode, includeSymbol = false)]
            .format(value)
            .trim()

    operator fun plus(other: FiatValue): FiatValue {
        if (currencyCode != other.currencyCode)
            throw MismatchedCurrencyCodeException("Mismatched currency codes during add")
        return FiatValue(currencyCode, value + other.value)
    }

    fun toParts(locale: Locale) = toStringWithoutSymbol(locale)
        .let {
            val index = it.lastIndexOf(LocaleDecimalFormat[locale].decimalFormatSymbols.decimalSeparator)
            if (index != -1) {
                Parts(
                    symbol(locale),
                    it.substring(0, index),
                    it.substring(index + 1)
                )
            } else {
                Parts(
                    symbol(locale),
                    it,
                    ""
                )
            }
        }

    private fun symbol(locale: Locale) = Currency.getInstance(currencyCode).getSymbol(locale)

    companion object {

        fun fromMinor(currencyCode: String, minor: Long) =
            FiatValue(
                currencyCode,
                BigDecimal.valueOf(minor).movePointLeft(Currency.getInstance(currencyCode).defaultFractionDigits)
            )
    }
}

class Parts(
    val symbol: String,
    val major: String,
    val minor: String
)

class MismatchedCurrencyCodeException(message: String) : Exception(message)
