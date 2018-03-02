package cz4013.shared.currency;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Currency {
  USD("USD"),
  EUR("EUR"),
  SGD("SGD")
  ;

  private final String code;

  Currency(final String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return code;
  }

  public static Stream<Currency> getAllCurrencies() {
    return Arrays.stream(Currency.values());
  }

  public static String getAllCurrenciesString() {
    return String.join(", ", Currency.getAllCurrencies().map(Currency::toString).collect(Collectors.toList()));
  }
}
