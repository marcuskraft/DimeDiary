const {
  Locale,
} = require("@js-joda/locale_de-de")

export default class AmountHelper {

  public static onlyTwoPrecision(value: number): boolean {
    return !(value != null && value.toLocaleString().indexOf(".") > -1 &&
        (value.toLocaleString().split('.')[1].length > 2));
  }

  public static onlyIntegers(value: number): boolean {
    return !(value != null && value.toLocaleString().indexOf(".") > -1 &&
        (value.toLocaleString().split('.').length > 0));
  }

  public static euroCentToStringWithEuroSign(amountEuroCent: number) {
    return (amountEuroCent / 100).toLocaleString(Locale.GERMANY) + " €";
  }

}