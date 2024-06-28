package org.education

enum class Status(private val s: String, private val i: Int) {


    Open("Открыт",1),
    InProgress("В процессе", 2),
    Ready("Готов", 3),
    WaitForCourier("В ожидании курьера", 4),
    InDelivery("В доставке", 5),
    Done("Завершён", 6);

    fun getName():String {
        return this.s;
    }

    fun getNum():Int {
        return this.i;
    }

}