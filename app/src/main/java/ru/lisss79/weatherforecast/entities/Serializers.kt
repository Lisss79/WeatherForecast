package ru.lisss79.weatherforecast.entities

import com.huawei.hms.site.api.model.AddressDetail
import com.huawei.hms.site.api.model.Coordinate
import com.huawei.hms.site.api.model.Site
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object SiteSerializer : KSerializer<Site> {
    override val descriptor = buildClassSerialDescriptor("Site") {
        element<String?>("name")
        element<Double>("latitude")
        element<Double>("longitude")
        element<String?>("country")
        element<String?>("country_code")
        element<String?>("admin_area")
        element<String?>("locality")
        element<String?>("sub_admin_area")
        element<String?>("sub_locality")
        element<String?>("formatted_address")
        element<Int?>("utc_offset")
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: Site) {
        encoder.encodeStructure(descriptor) {
            encodeNullableSerializableElement(descriptor, 0, String.serializer(), value.name)
            encodeDoubleElement(descriptor, 1, value.location.lat)
            encodeDoubleElement(descriptor, 2, value.location.lng)
            encodeNullableSerializableElement(
                descriptor,
                3, String.serializer(), value.address.country
            )
            encodeNullableSerializableElement(
                descriptor,
                4, String.serializer(), value.address.countryCode
            )
            encodeNullableSerializableElement(
                descriptor,
                5, String.serializer(), value.address.adminArea
            )
            encodeNullableSerializableElement(
                descriptor,
                6, String.serializer(), value.address.locality
            )
            encodeNullableSerializableElement(
                descriptor,
                7, String.serializer(), value.address.subAdminArea
            )
            encodeNullableSerializableElement(
                descriptor,
                8, String.serializer(), value.address.subLocality
            )
            encodeNullableSerializableElement(
                descriptor,
                9, String.serializer(), value.formatAddress
            )
            encodeNullableSerializableElement(
                descriptor,
                10, Int.serializer(), value.utcOffset
            )
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): Site {
        val site = Site().apply {
            decoder.decodeStructure(descriptor) {
                var lat = 0.0
                var lng = 0.0
                location = Coordinate(lat, lng)
                val addressDetail = AddressDetail()
                address = addressDetail
                while (true) {
                    when (val index = decodeElementIndex(descriptor)) {
                        0 -> name = decodeNullableSerializableElement(
                            descriptor, 0, String.serializer()
                        )
                        1 -> location.lat = decodeDoubleElement(descriptor, 1)
                        2 -> location.lng = decodeDoubleElement(descriptor, 2)
                        3 -> address.country = decodeNullableSerializableElement(
                            descriptor, 3, String.serializer()
                        )
                        4 -> address.countryCode = decodeNullableSerializableElement(
                            descriptor, 4, String.serializer()
                        )
                        5 -> address.adminArea = decodeNullableSerializableElement(
                            descriptor, 5, String.serializer()
                        )
                        6 -> address.locality = decodeNullableSerializableElement(
                            descriptor, 6, String.serializer()
                        )
                        7 -> address.subAdminArea = decodeNullableSerializableElement(
                            descriptor, 7, String.serializer()
                        )
                        8 -> address.subLocality = decodeNullableSerializableElement(
                            descriptor, 8, String.serializer()
                        )
                        9 -> formatAddress = decodeNullableSerializableElement(
                            descriptor, 9, String.serializer()
                        )
                        10 -> utcOffset = decodeNullableSerializableElement(
                            descriptor, 10, Int.serializer()
                        )
                        DECODE_DONE -> break // Input is over
                        else -> error("Unexpected index: $index")
                    }
                }
            }
        }
        return site
    }
}