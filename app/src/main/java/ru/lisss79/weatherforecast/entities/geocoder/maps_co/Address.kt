package ru.lisss79.weatherforecast.entities.geocoder.maps_co

import com.google.gson.annotations.SerializedName


data class Address (

  @SerializedName("leisure"        ) var leisure        : String? = null,
  @SerializedName("road"           ) var road           : String? = null,
  @SerializedName("village"        ) var village        : String? = null,
  @SerializedName("county"         ) var county         : String? = null,
  @SerializedName("neighbourhood"  ) var neighbourhood  : String? = null,
  @SerializedName("region"         ) var region         : String? = null,
  @SerializedName("suburb"         ) var suburb         : String? = null,
  @SerializedName("town"           ) var town           : String? = null,
  @SerializedName("city"           ) var city           : String? = null,
  @SerializedName("state"          ) var state          : String? = null,
  @SerializedName("ISO3166-2-lvl4" ) var iso3166        : String? = null,
  @SerializedName("country"        ) var country        : String? = null,
  @SerializedName("country_code"   ) var countryCode    : String? = null

)