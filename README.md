# IOS Banking Backend
This is the sample SpringBoot application showcasing stripe and firebase integration. This is backend application build for [IOS project](https://github.com/santoshkc05/iOSBanking).

## Installation

1. Install the [docker](https://docs.docker.com/get-docker/) in your machine.

2. Build and push the image to docker registry

```bash 
 ./mvnw compile jib:build -Djib.to.auth.username=<your_dockerhub_username> -Djib.to.auth.password=<your_dockerhub_password>
```

3. Run the image

```bash 
docker run -e STRIPE_API_KEY=<your_stripe_api_key> -p 8080:8080 <your_dockerhub_username>/stripe-rest
```

## Features

- Create Payment Intent
- Create OffSession Payment Intent
- List Payment Methods
- Token authentication

## TODO

- Write Unit Tests
- Feature enhancements
