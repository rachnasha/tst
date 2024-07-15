package com.tst

import scala.math.Ordered.orderingToOrdered

// The types that are string should be NonEmptyString -
opaque type NonEmptyString = String

object NonEmptyString:

  def apply(s: String): Option[NonEmptyString] =
    if s.isEmpty then None else Some(s)

  def unsafeFrom(s: String): NonEmptyString =
    NonEmptyString(s).getOrElse(
      throw new RuntimeException(s"Empty Strings input [${s}] are not supported. Please provide a string is not empty")
    )

  given Conversion[NonEmptyString, String] with
    def apply(nes: NonEmptyString): String = nes

  given Ordering[NonEmptyString] with
    def compare(x: NonEmptyString, y: NonEmptyString): Int = x.compare(y)

// some types to abstract String  and make code more readable ...
opaque type CabinCode = NonEmptyString
object CabinCode:
  def apply(s: String): CabinCode =
    Option(s).flatMap(NonEmptyString(_)) match {
      case None      => throw new RuntimeException(s"Invalid input for CabinCode:[${s}]. Please provide a not null and non empty value.")
      case Some(nes) => nes
    }

opaque type RateGroup = NonEmptyString
object RateGroup:
  def apply(s: String): RateGroup =
    Option(s).flatMap(NonEmptyString(_)) match {
      case None      => throw new RuntimeException(s"Invalid input for RateGroup:[${s}]. Please provide a not null and non empty value.")
      case Some(nes) => nes
    }

opaque type RateCode = NonEmptyString
object RateCode:
  def apply(s: String): RateCode =
    Option(s).flatMap(NonEmptyString(_)) match {
      case None      => throw new RuntimeException(s"Invalid input for RateCode:[${s}]. Please provide a not null and non empty value.")
      case Some(nes) => nes
    }

// type alias
type CabinCodeRateGroup = (CabinCode, RateGroup)

case class Rate(rateCode: RateCode, rateGroup: RateGroup)
case class CabinPrice(cabinCode: CabinCode, rateCode: RateCode, price: BigDecimal)

case class BestGroupPrice(cabinCode: CabinCode, rateCode: RateCode, price: BigDecimal, rateGroup: RateGroup)

case class CabinPriceWithRateGroup(cabinPrice: CabinPrice, rateGroup: RateGroup)

object PricingEngine:

  def getBestGroupPrices(rates: Seq[Rate], prices: Seq[CabinPrice]): Seq[BestGroupPrice] =
    val ratesCodesToGroupMap: Map[RateCode, RateGroup] = rates.map(r => (r.rateCode, r.rateGroup)).toMap

    val pricesWithRateGroup = prices.collect {
      case cp if ratesCodesToGroupMap.get(cp.rateCode).nonEmpty =>
        CabinPriceWithRateGroup(cp, ratesCodesToGroupMap(cp.rateCode))
    }
    val groupedByRateGroup = pricesWithRateGroup.groupBy(prg =>
      (prg.cabinPrice.cabinCode, prg.rateGroup)
    )

    groupedByRateGroup.flatMap {
      case (cabinCodeRateGroup, allCabinPricesForGroup) =>
        allCabinPricesForGroup.sortBy(_.cabinPrice.price).headOption.map { bestPrice =>
          BestGroupPrice(cabinCodeRateGroup._1, bestPrice.cabinPrice.rateCode, bestPrice.cabinPrice.price, cabinCodeRateGroup._2)
        }
    }.toSeq
