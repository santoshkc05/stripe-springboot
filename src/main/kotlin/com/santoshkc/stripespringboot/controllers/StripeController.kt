package com.santoshkc.stripespringboot.controllers

import com.santoshkc.stripespringboot.dto.CreateOffSessionPaymentRequest
import com.santoshkc.stripespringboot.dto.CreatePaymentRequest
import com.santoshkc.stripespringboot.dto.IntentResponse
import com.santoshkc.stripespringboot.dto.OffSessionIntentResponse
import com.santoshkc.stripespringboot.services.StripeServices
import com.stripe.model.PaymentMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
class StripeController(private val services: StripeServices) {

    @PostMapping("/create-payment-intent")
    fun createPaymentIntent(@RequestBody createPayment: @Valid CreatePaymentRequest): IntentResponse {
        val authentication = SecurityContextHolder.getContext().authentication
        return services.createIntent(authentication.name, createPayment)
    }

    @GetMapping("/get-cards")
    fun getCards(): ResponseEntity<MutableList<PaymentMethod>?> {
        val authentication = SecurityContextHolder.getContext().authentication
        return ResponseEntity(services.listPaymentMethods(authentication.name), HttpStatus.OK)
    }

    @PostMapping("/off-session-payment-intent")
    fun setupIntent(@RequestBody createPayment: @Valid CreateOffSessionPaymentRequest): ResponseEntity<OffSessionIntentResponse> {
        val authentication = SecurityContextHolder.getContext().authentication
        return ResponseEntity(services.createOffSessionPaymentIntent(authentication.name, createPayment), HttpStatus.OK)
    }
}