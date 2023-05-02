package me.a0g.hyk.utils

import java.lang.reflect.Field
import java.lang.reflect.Method

object ReflectionHelper {

    val classes = hashMapOf<String, Class<*>>()
    val fields = hashMapOf<String, Field>()
    val methods = hashMapOf<String, Method>()

    fun Class<*>.getFieldHelper(fieldName: String) = runCatching {
        ReflectionHelper.fields.getOrPut("$name $fieldName") {
            getDeclaredField(fieldName).apply {
                isAccessible = true
            }
        }
    }.getOrNull()

    fun Class<*>.getMethodHelper(methodName: String) = runCatching {
        ReflectionHelper.methods.getOrPut("$name $methodName") {
            getDeclaredMethod(methodName).apply {
                isAccessible = true
            }
        }
    }.getOrNull()

    fun getClassHelper(className: String) = runCatching {
        classes.getOrPut(className) {
            Class.forName(className)
        }
    }.getOrNull()

    fun getFieldFor(className: String, fieldName: String) = getClassHelper(className)?.getFieldHelper(fieldName)
    fun getMethodFor(className: String, methodName: String) = getClassHelper(className)?.getMethodHelper(methodName)

}