const {
  Locale,
} = require("@js-joda/locale_de-de")

export default class AmountHelper {

  public static onlyTwoPrecision(value: number): boolean {
    if (value != null && value.toLocaleString().indexOf(".") > -1 &&
        (value.toLocaleString().split('.')[1].length > 2)) {
      return false;
    }
    return true;
  }

  public static euroCentToStringWithEuroSign(amountEuroCent: number) {
    return (amountEuroCent / 100).toLocaleString(Locale.GERMANY) + " €";
  }

}