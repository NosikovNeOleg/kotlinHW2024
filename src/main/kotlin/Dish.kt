package org.education

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.education.Status.Ready
import kotlin.math.log

class Dish(
    private val name: String,
    private val price: Long,
    private val cookingTime: Int,

) {
    private var status : DishStatus = DishStatus.Open;

    init {
        if (cookingTime < 1) {
            // LOG
        }
        if (name.isEmpty() || name.isBlank()) {
            // LOG
        }
    }

    fun getName(): String {
        return this.name;
    }

    fun getPrice(): Long {
        return this.price;
    }

    fun getTime(): Long {
        return (this.cookingTime * 1000).toLong();
    }
    fun getStatus(): DishStatus {
        return this.status;
    }

    suspend fun cook() {
        this.status = DishStatus.InProgress;
        val scope = CoroutineScope(newSingleThreadContext("kitchen")).launch {
            delay(getTime())
            print(
                StringBuilder().append("Блюдо ")
                    .append(getName()).append(" приготовлено!")
                    .append(" Затраченное время - ")
                    .append(getTime() / 1000)
                    .append(" секунд").appendLine().toString()
            )
        }
        scope.invokeOnCompletion { this.status = DishStatus.Ready }
    }

    enum class DishStatus(private val s: String) {
        Open("Открыт"),
        InProgress("В процессе"),
        Ready("Готов");
    }
}