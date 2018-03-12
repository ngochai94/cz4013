package cz4013.shared.currency;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This enum contains the available currencies
 */
public enum Currency {
  USD("USD"),
  EUR("EUR"),
  SGD("SGD");

  private final String code;

  Currency(final String code) {
    this.code = code;
  }

  /**
   * @return stream of available currencies
   */
  public static Stream<Currency> getAllCurrencies() {
    return Arrays.stream(Currency.values());
  }

  /**
   * @return list of available currencies as a string
   */
  public static String getAllCurrenciesString() {
    return String.join(", ", Currency.getAllCurrencies().map(Currency::toString).collect(Collectors.toList()));
  }

  @Override
  public String toString() {
    return code;
  }
}
