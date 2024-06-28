package org.education


fun main() {

    init();

    while (getAction(
            { value: String -> globalAction = value },
            globalActions,
            "Необходимо ввести корректную операцию!",
            ::globalMenu
        ) != "q"
    ) {
        when (globalAction) {
            "a" -> {
                var addAction = "";
                while (getAction(
                        { value: String -> addAction = value },
                        addActions,
                        "Необходимо ввести корректную операцию!",
                        ::addMenu
                    ) != "q"
                ) {
                    when (addAction) {
                        "o" -> {
                            addOrder()
                        }

                        "d" -> {
                            addDishToOrder(getOrder("Нет такого заказа!", "Введите номер заказа: "))
                        }
                    }
                }
            }

            "f" -> {
                filterOrders();
            }

            "s" -> {
                sortOrders();
            }

            "r" -> {
                removeOrder()
            }

            "pending" -> continue;
        }
    }
    println("Closing restaurant..")
}

var globalAction: String = "";
var orders: MutableList<Order> = ArrayList();
var dishes: MutableList<Dish> = ArrayList();

fun init() {
    dishes.add(Dish("Хлеб", 10, 1000))
    dishes.add(Dish("Суп", 90, 25000))
    dishes.add(Dish("Второе", 150, 15000))
    dishes.add(Dish("Чай", 5, 1000))

}

fun removeOrder() {
    try {
        println("Введите номер заказа: ")
        var input = readln()
        while (input.lowercase() != "q" && input.lowercase() != "quit") {
            val deleted = orders.filter { order -> order.getId() == input }.size;
            if (deleted > 0) {
                orders = orders.filter { order -> order.getId() != input }.toMutableList()
                print("Удалены заказы с id $input! Кол-во - $deleted\n")
            } else {
                print("Нет заказов с таким id!\n")
            }
            print("Введите новый id или команду quit(q) для выхода\n")
            input = readln();
        }
    } catch (_: Exception) {
    }
}

fun filterOrders() {
    print("Выберите фильтр:")
    var filterAction = "";
    while (getAction({ value: String -> filterAction = value }, filterActions, "Введите корректный пункт!" ,::filterMenu) != "q"){
        when (filterAction) {
            "p" -> {
                var lowNum: Int = -1;
                var highNum: Int = -1;
                while (lowNum < 0) {
                    try {
                        println("Введите минимальную сумму:")
                        lowNum = readln().toInt()
                    } catch (e : Exception) {
                        println("Некорректное число!")
                    }
                }
                while (highNum < 0) {
                    try {
                        println("Введите максимальную сумму:")
                        highNum = readln().toInt()
                    } catch (e : Exception) {
                        println("Некорректное число!")
                    }
                }
                printTableHeader()
                orders.filter { order ->
                    var sum = order.getCost();
                    sum in (lowNum).. highNum;
                } . forEach{ it.logAsTable(); }
            }
            "d" -> {
                lateinit var destination : Destination;
                when (getAction(destinationActions, "Необходимо ввести корректный пункт!", ::destinationMenu)) {
                    "i" -> {
                        destination = Destination.InRestaurant;
                    }
                    "s" -> {
                        destination = Destination.SelfPickUp;
                    }
                    "d" -> {
                        destination = Destination.Delivery
                    }
                    "e" -> {
                        destination = Destination.DeliveryByExternalService;
                    }
                }
                printTableHeader()
                orders.filter { it.getDestination() == destination }
                    .forEach { it.logAsTable() }

            }
            "с" -> {
                var num: Int;
                while (true) {
                    try {
                        println("Введите количество блюд:")
                        num = readln().toInt()
                        if (num < 0) continue;
                        printTableHeader()
                        orders.filter { order -> order.getDishes().size == num }
                            .forEach { it.logAsTable() }
                    } catch (e : Exception) {
                        println("Некорректное число!")
                    }
                }
            }
        }
    }
}


private fun sortOrders(){
    var sortAction = "";
    while (getAction({ value: String -> sortAction = value }, sortActions, "Введите корректный пункт!" ,::sortMenu) != "q"){
        when (sortAction) {
            "p" -> {
                val asc = getAsc();
                printTableHeader()
                orders.sortedBy { order ->
                    order.getCost() * asc;
                } . forEach{ it.logAsTable(); }
            }
            "s" -> {
                val asc = getAsc();
                printTableHeader()
                orders.sortedBy { it.getStatus().getNum() * asc }.forEach{ it.logAsTable(); }
            }
            "с" -> {
                val asc = getAsc();
                printTableHeader()
                orders.sortedBy { order ->
                    order.getDishes().size * asc;
                } . forEach{ it.logAsTable(); }
            }
        }
    }
}

private fun getAsc() : Int {
    var asc = 0;
    while (asc != -1 && asc != 1){
        try {
            println("По возрастанию(1) или по убыванию(-1)?")
            asc = readln().toInt();
            if (asc == -1 || asc == 1) break;
        } catch (_: Exception){ }
    }
    return asc;
}

fun addOrder() {
    lateinit var destination: Destination;
    lateinit var destinationAddress: String;
    when (getAction(destinationActions, "Необходимо ввести корректный пункт!", ::destinationMenu)) {
        "i" -> {
            destination = Destination.InRestaurant;
            destinationAddress = "none";
        }

        "s" -> {
            destination = Destination.SelfPickUp;
            destinationAddress = "none";
        }

        "d" -> {
            destination = Destination.Delivery
            print("Введите адрес доставки: ")
            try {
                destinationAddress = readln();
            } catch (e: Exception) {
                print("Возникла неожиданная ошибка!")
                destinationAddress = "error"
            }
        }

        "e" -> {
            destination = Destination.DeliveryByExternalService;
            destinationAddress = "none"
        }
    }
    val order = Order(
        ArrayList(),
        destination,
        destinationAddress
    )
    orders.add(
        order
    )
    println(StringBuilder().append("Номер заказа: ").append(order.getId()).toString());
}

fun addDishToOrder(order: Order) {
    println("Выберите блюдо")
    var i = 1;
    dishes.forEach {
        print(
            StringBuilder()
                .append(i++.toString())
                .append(" - ")
                .append(it.getName())
                .appendLine()
        )
    }
    order.addDish(getDish())
}

private fun printTableHeader(){
    print(StringBuilder()
        .append(getSpaces(2)).append("Id")
        .append(getSpaces(11)).append("Кол-во блюд")
        .append(getSpaces(12)).append("Сумма заказа")
        .append(getSpaces(6)).append("Статус")
        .append(getSymbols("-",52)).appendLine()
    )
}


private fun getDish(): Dish {
    var num: Int;
    while (true) {
        print("Введите число: ")
        try {
            num = readln().toInt()
            if (num in (1..dishes.size)) {
                return dishes[num - 1];
            }
            print("Введите корректное число: ")
        } catch (e: Exception) {
            print("Введите корректное число: ")
        }
    }

}

private fun getAction(
    actionVar: (value: String) -> Unit,
    actions: List<String>,
    err: String,
    menu: () -> Unit
): String {
    var action: String;
    actionVar("pending")
    while (true) {
        menu()
        try {
            action = readln()
            if (action.lowercase() in actions) {
                actionVar(action.lowercase().substring(0, 1));
                return action.lowercase().substring(0, 1);
            }
            println(err)
        } catch (e: Exception) {
            println(err)
        }
    }

}

private fun getAction(actions: List<String>, err: String, menu: () -> Unit): String {
    var action: String;
    while (true) {
        menu()
        try {
            action = readln()
            if (action.lowercase() in actions) {
                return action.lowercase().substring(0, 1);
            }
            println(err)
        } catch (e: Exception) {
            println(err)
        }
    }
}

private fun getOrder(err: String, msg: String): Order {
    var orderId: String;
    while (true) {
        print(msg)
        try {
            orderId = readln()
            orders.filter { order -> order.getId() == orderId }
                .forEach { return it }
            throw Exception()
        } catch (e: Exception) {
            println(err)
        }
    }
}

val globalActions = listOf(
    "q", "quit",
    "a", "add",
    "f", "filter",
    "r", "remove",
    "s", "sort",
)

val filterActions = listOf(
    "p", "price",
    "d", "dishes",
    "c", "count dishes",
    "q", "quit",
)

val sortActions = listOf(
    "p", "price",
    "s", "status",
    "c", "count dishes",
    "q", "quit",
)

val addActions = listOf(
    "o", "order",
    "d", "dish",
    "q", "quit",
)

val destinationActions = listOf(
    "i", "inRestaurant",
    "s", "selfPickup",
    "d", "delivery",
    "e", "externalService"
)


private fun globalMenu() {
    println("Введите действие:")
    println("Добавить заказ/блюдо к заказу - add(a)")
    println("Удалить заказ - remove(r)")
    println("Фильтр заказов - filter(f)")
    println("Сортировка заказов - sort(s)")
    println("Выйти - quit(q)")
}

private fun addMenu() {
    println("Введите действие:")
    println("Добавить заказ - order(o)")
    println("Добавить блюдо к заказу - dish(d)")
    println("Выйти - quit(q)")
}

private fun destinationMenu() {
    println("Введите назначение заказа:")
    println("В ресторане - inRestaurant(i)")
    println("Самовывоз - selfPickup(s)")
    println("Доставка нашими силами - delivery(d)")
    println("Доставка внешними сервисами - externalService(e)")
}

private fun filterMenu() {
    println("Введите фильтр:")
    println("По сумме заказа - price(p)")
    println("По назначению - destination(d)")
    println("Количеству блюд - count dishes(c)")
    println("Выйти - quit(q)")
}

private fun sortMenu() {
    println("Введите пункт сортировки:")
    println("По сумме заказа - price(p)")
    println("По статусу - status(s)")
    println("Количеству блюд - count dishes(c)")
    println("Выйти - quit(q)")
}
