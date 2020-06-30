package com.youloft.coolktx

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.*

/**
 * IO线程调用block
 * @receiver LifecycleCoroutineScope
 * @param onError Function1<[@kotlin.ParameterName] Exception, Unit>
 * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, Unit>
 * @return Job
 */
fun CoroutineScope.launchIO(
    onError: (e: java.lang.Exception) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job {
    return this.launch {
        try {
            withContext(Dispatchers.IO, block)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onError.invoke(e)
            }
            this.cancel(CancellationException(e.message))
        }
    }
}


