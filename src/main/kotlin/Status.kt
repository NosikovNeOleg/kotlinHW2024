package org.education

enum class Status(private val s: String) {


    Open("Открыт"),
    InProgress("В процессе"),
    Ready("Готов"),
    WaitForCourier("В ожидании курьера"),
    InDelivery("В доставке"),
    Done("Завершён");

    fun getName():String {
        return this.s;
    }
}