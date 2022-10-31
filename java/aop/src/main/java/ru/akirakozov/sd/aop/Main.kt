package ru.akirakozov.sd.aop

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import ru.akirakozov.sd.aop.aspect.LoggingExecutionTimeAspect
import ru.akirakozov.sd.aop.aspect.MethodData
import ru.akirakozov.sd.aop.dao.EntityNotFoundException
import ru.akirakozov.sd.aop.dao.RandomActions
import ru.akirakozov.sd.aop.domain.Customer
import ru.akirakozov.sd.aop.domain.CustomerManager
import java.lang.reflect.Method

fun getBuckets(a: LoggingExecutionTimeAspect): Map<Package, Map<Class<*>, Map<Method, MethodData>>> {
    val sorted = HashMap<Package, HashMap<Class<*>, HashMap<Method, MethodData>>>()
    a.data.forEach { it ->
        val clz = it.key.declaringClass
        val pack = clz.`package`

        sorted
                .getOrPut(pack) { HashMap() }
                .getOrPut(clz) { HashMap() }
                .put(it.key, it.value)
    }
    return sorted
}

fun dump(sorted: Map<Package, Map<Class<*>, Map<Method, MethodData>>>) {
    var indent = 0
    sorted.forEach {p ->
        print("  ".repeat(indent))
        println(p.key.name)
        indent++
        p.value.forEach { c ->
            print("  ".repeat(indent))
            println(c.key.simpleName)
            indent++
            c.value.forEach { m ->
                print("  ".repeat(indent))
                print(m.key.name)
                println("\t" + m.value.callCount + "\t" + m.value.callDelta / m.value.callCount + "\t" + m.value.callDelta)
            }
            indent--
        }
        indent--
    }
}

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val ctx: ApplicationContext = AnnotationConfigApplicationContext(ContextConfiguration::class.java)
        val customerManager = ctx.getBean(CustomerManager::class.java)
        val id = customerManager.addCustomer(Customer("Petr"))
        val tryGet = { id: Int ->
            try {
                val customer = customerManager.findCustomer(id)
                println("Found customer name: " + customer.name)
            } catch (_: EntityNotFoundException) {
                println("Not found customer name: " + id)
            }
        }
        tryGet(id)
        tryGet(id + 30)

        val rnd = ctx.getBean(RandomActions::class.java)
        rnd.sleepyMethod()
        repeat(10) {
            rnd.someMethod()
        }


        val a = ctx.getBean(LoggingExecutionTimeAspect::class.java)
        val sorted = getBuckets(a)

        dump(sorted)
    }
}