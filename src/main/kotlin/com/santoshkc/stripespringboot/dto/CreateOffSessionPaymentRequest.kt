package com.santoshkc.stripespringboot.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null
import javax.validation.constraints.Size

data class CreateOffSessionPaymentRequest(
    @NotNull
    @Size(min = 4, max = 10)
    @JsonProperty("product")
    var product: String,

    @NotNull
    @Size(min = 4, max = 50)
    @JsonProperty("payment_method_id")
    var paymentMethodId:String
)