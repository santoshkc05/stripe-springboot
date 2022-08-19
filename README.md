# Sample Stripe Application
This is the sample SpringBoot REST API application for stripe integrations.

### Install using docker
1. Build and push the image to docker registry

   `./mvnw compile jib:build -Djib.to.auth.username=<your_dockerhub_username> -Djib.to.auth.password=<your_dockerhub_password>
   `


2. Run the image

   `docker run -e STRIPE_API_KEY=<your_stripe_api_key> -p 8080:8080 santoshkc/stripe-rest`
