package org.education

enum class Destination(s: String) {
    DeliveryByExternalService("Доставка внешними сервисами"),
    Delivery("Доставка нашими силами"),
    SelfPickUp("Самовывоз"),
    InRestaurant("В ресторане");
}