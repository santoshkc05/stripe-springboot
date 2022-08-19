package com.santoshkc.stripespringboot.services

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.santoshkc.stripespringboot.FirebaseInitializer
import com.santoshkc.stripespringboot.dto.*
import com.stripe.Stripe
import com.stripe.exception.CardException
import com.stripe.model.Customer
import com.stripe.model.EphemeralKey
import com.stripe.model.PaymentIntent
import com.stripe.model.PaymentMethod
import com.stripe.net.RequestOptions
import com.stripe.param.CustomerCreateParams
import com.stripe.param.EphemeralKeyCreateParams
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.PaymentMethodListParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class StripeServices(private val db: Firestore) {

    @Value("\${stripe.api.key}")
    private var secretKey: String? = null
    private val logger = LoggerFactory.getLogger(FirebaseInitializer::class.java)

    @PostConstruct
    fun init() {
        Stripe.apiKey = secretKey
    }

    fun createIntent( userId: String, request: CreatePaymentRequest): IntentResponse {
        val customer = getOrCreateCustomer(userId)

        val params = PaymentIntentCreateParams.builder()
            .setAmount(calculatePrice(request.product))
            .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.OFF_SESSION)
            .setCustomer(customer.stripeCustomerId)
            .setCurrency("usd")
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods
                    .builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        val intent = PaymentIntent.create(params)
        val ephemeralKey = EphemeralKey.create(
            EphemeralKeyCreateParams.builder()
                .setCustomer(customer.stripeCustomerId)
                .build(),
            RequestOptions.builder().setStripeVersionOverride(Stripe.API_VERSION).build()
        )
        return IntentResponse(
            intent.clientSecret,
            ephemeralKey = ephemeralKey.secret,
            customer = customer.stripeCustomerId!!
        )
    }

    fun createOffSessionPaymentIntent(userId: String, request: CreateOffSessionPaymentRequest): OffSessionIntentResponse {
        val customer = getOrCreateCustomer(userId)

        val params = PaymentIntentCreateParams.builder()
            .setCurrency("usd")
            .setAmount(calculatePrice(request.product))
            .setPaymentMethod(request.paymentMethodId)
            .setCustomer(customer.stripeCustomerId)
            .setConfirm(true)
            .setOffSession(true)
            .build()
        try {
             PaymentIntent.create(params)
            return OffSessionIntentResponse("success")
        } catch (err: CardException) {
            // Error code will be authentication_required if authentication is needed
            logger.error("Error code is : " + err.code)
            val paymentIntentId = err.stripeError.paymentIntent.id
            logger.error("paymentIntentId is : $paymentIntentId")
            return OffSessionIntentResponse(err.code)
        }
    }

    fun listPaymentMethods(userId: String): MutableList<PaymentMethod>? {
        val customer = getOrCreateCustomer(userId)

        val params = PaymentMethodListParams
            .builder()
            .setCustomer(customer.stripeCustomerId)
            .setType(PaymentMethodListParams.Type.CARD)
            .build()

        return PaymentMethod.list(params).data
    }

    private fun getOrCreateCustomer(userId: String): User {
        val document = db.collection("users").document(userId).get().get()
        val email = document.get("email") as String
        //if stripeCustomerId exists return the user
        (document.get("stripeCustomerId") as? String)?.let {
            return User(userId, it, email)
        }
        // Otherwise, create stripe customer
        val customer = Customer.create(
            CustomerCreateParams.builder()
                .setEmail(email)
                .setMetadata(mapOf("firebaseUID" to userId))
                .build()
        )
        db.collection("users").document(userId).set(mapOf("stripeCustomerId" to customer.id), SetOptions.merge())
        return User(userId, customer.id, email)
    }

    //TODO: Integrate with database
    private fun calculatePrice(type: String): Long {
        return when(type) {
            "netflix" -> 7 * 100L
            "spotify" -> 9 * 100L
            else -> 10 * 100L
        }
    }
}