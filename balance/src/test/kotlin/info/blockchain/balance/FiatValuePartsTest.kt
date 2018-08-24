package info.blockchain.balance

import org.amshove.kluent.`should equal`
import org.junit.Test
import java.util.Locale

class FiatValuePartsTest {

    @Test
    fun `extract GBP parts in UK`() {
        FiatValue("GBP", 1.2.toBigDecimal())
            .toParts(Locale.UK).apply {
                symbol `should equal` "£"
                major `should equal` "1"
                minor `should equal` "20"
            }
    }

    @Test
    fun `extract USD parts in US`() {
        FiatValue("USD", 9.89.toBigDecimal())
            .toParts(Locale.US).apply {
                symbol `should equal` "$"
                major `should equal` "9"
                minor `should equal` "89"
            }
    }

    @Test
    fun `extract USD parts in UK`() {
        FiatValue("USD", 5.86.toBigDecimal())
            .toParts(Locale.UK).apply {
                symbol `should equal` "USD"
                major `should equal` "5"
                minor `should equal` "86"
            }
    }

    @Test
    fun `extract JPY parts in Japan`() {
        FiatValue("JPY", 512.0.toBigDecimal())
            .toParts(Locale.JAPAN).apply {
                symbol `should equal` "￥"
                major `should equal` "512"
                minor `should equal` ""
            }
    }

    @Test
    fun `extract USD parts in France`() {
        FiatValue("USD", 1512.99.toBigDecimal())
            .toParts(Locale.FRANCE).apply {
                symbol `should equal` "USD"
                major `should equal` "1 512"
                minor `should equal` "99"
            }
    }

    @Test
    fun `extract Euro parts in Italy`() {
        FiatValue("EUR", 2356.32.toBigDecimal())
            .toParts(Locale.FRANCE).apply {
                symbol `should equal` "€"
                major `should equal` "2 356"
                minor `should equal` "32"
            }
    }

    @Test
    fun `extract Euro parts in Germany`() {
        FiatValue("EUR", 4567.98.toBigDecimal())
            .toParts(Locale.GERMANY).apply {
                symbol `should equal` "€"
                major `should equal` "4.567"
                minor `should equal` "98"
            }
    }
}
