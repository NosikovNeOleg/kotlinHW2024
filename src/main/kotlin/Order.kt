package org.education

import kotlinx.coroutines.runBlocking
import org.education.Destination.Delivery
import org.education.Destination.DeliveryByExternalService
import org.education.Status.*
import java.util.*

class Order(
    private var dishes: MutableList<Dish>,
    private var destination: Destination,
    private var destinationAddress: String?,
) {
    private var status: Status = Open;
    private var cost: Long = dishes.sumOf { dish -> dish.getPrice() };
    private val id: UUID = UUID.randomUUID();


    fun getDishes(): List<Dish> {
        return dishes; }

    fun getAddress(): String? {
        return destinationAddress; }

    fun getStatus(): Status {
        return status; }

    fun getDestination() : Destination {
        return destination;
    }

    fun getCost(): Long {
        return cost; }

    fun getId(): String {
        return this.id.toString(); }

    fun addDish(dish: Dish) = runBlocking {
        dishes.add(dish);
        cost += dish.getPrice();
        if (status == InProgress || status == Ready) {
            dish.cook();
        }
    }

    fun nextStatus(): Status {
        if (status == Done) {
            log();
            return Done;
        }
        this.status = when (status) {
            Open -> {
                runBlocking {
                    for (dish in dishes.filter { dish -> dish.getStatus() == Dish.DishStatus.Open }) {
                        dish.cook();
                    }
                }
                InProgress
            };
            InProgress -> {
                print(StringBuilder().append("Заказ ").append(this.id).append(" в процессе!").appendLine())
                return InProgress;
            };
            Ready -> {
                when (destination) {
                    DeliveryByExternalService -> WaitForCourier;
                    Delivery -> WaitForCourier;
                    else -> Done
                }
            }

            WaitForCourier -> InDelivery;
            InDelivery -> Done;
            Done -> Done;
        }
        return status;
    }


    fun log() {
        print(this.toString());
    }

    fun logAsTable() {
        print(StringBuilder()
            .append(this.id)
            .append(getSpaces(this.dishes.size.toString().length)).append(this.dishes.size)
            .append(getSpaces(this.cost.toString().length)).append(this.cost)
            .append(getSpaces(this.status.toString().length)).append(this.status)
            .appendLine());
    }

    override fun toString(): String {
        return StringBuilder()
            .append("Заказ № ").append(this.id).appendLine()
            .append("Кол-во блюд - ").append(this.dishes.size).appendLine()
            .append("На сумму - ").append(this.cost).appendLine()
            .append("В статусе: ").append(this.status.getName())
            .append(
                if (destinationAddress == null) {
                    "\n"
                } else {
                    "\nДоставка по адресу: "
                }
            ).append(this.destinationAddress).appendLine()
            .toString()
    }

}