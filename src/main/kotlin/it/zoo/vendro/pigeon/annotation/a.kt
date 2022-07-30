package it.zoo.vendro.pigeon.annotation

@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class RequiredGroup(vararg val groups: String)