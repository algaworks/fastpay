# Fastpay Payment API Simulation

## Database
Default database name: fastpay

## Credentials

- public-token: tDBmh0ZiDrmaZ8BVQD7i9UYdcj9KtZUk
- private-token: SSEewtC6Ps5yJLdB6SJmh1bqJXgRbCdf8ocDj2hU

## Payment Methods

- CREDIT
- GATEWAY_BALANCE

## Payment Statuses

- PENDING
- PROCESSING
- FAILED
- PAID
- CANCELED
- REFUNDED

## Valid Credit Cards

| Card             | Brand | First Status | Webhook Status | 
|------------------| ----- | ------------ | -------------- |
| 5120350100064537 | Master | PENDING | PROCESSING |
| 5120350100064545 | Master | PROCESSING | PAID |
| 5120350100064552 | Master | PROCESSING | FAILED |
| 5120350100064560 | Master | PAID | REFUNDED |
| 4622943127011022 | Visa | PAID | PAID |
| 4622943127011030 | Visa | FAILED | FAILED |
| 4622943127011055 | Visa | REFUNDED | REFUNDED |
| 4622943127011060 | Visa | PENDING | PENDING |



