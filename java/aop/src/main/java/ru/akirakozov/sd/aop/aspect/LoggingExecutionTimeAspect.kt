package ru.akirakozov.sd.aop.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import java.lang.reflect.Method

data class MethodData(
    var callCount: Int = 0,
    var callDelta: Long = 0
)

/*
 * @author akirakozov
 */
@Aspect
class LoggingExecutionTimeAspect {
    val data = HashMap<Method, MethodData>()

    @Around("execution(* ru.akirakozov.sd.aop.*.*.*(..))")
    @Throws(Throwable::class)
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val sig = joinPoint.signature
        val methSig = sig as MethodSignature
        val meth = methSig.method
        val got = data.getOrPut(meth) { MethodData() }
        val startNs = System.nanoTime()
        try {
            return joinPoint.proceed(joinPoint.args)
        } finally {
            got.callDelta += System.nanoTime() - startNs
            got.callCount += 1
        }

    }
}