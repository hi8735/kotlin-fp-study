package chpater4

import chapter3.Cons
import chapter3.Nil
import chapter3.foldRight
import chapter3.List

//4.1
fun <A, B> Option<A>.map(f: (A) -> B): Option<B> = when (this) {
    is None -> None
    is Some -> Some(f(this.get))
}

fun <A, B> Option<A>.flatMap(f: (A) -> Option<B>): Option<B> = when (this) {
    is None -> None
    is Some -> f(this.get)
}

fun <A> Option<A>.getOrElse(default: () -> A): A = when (this) {
    is None -> default()
    is Some -> this.get
}

fun <A> Option<A>.orElse(ob: () -> Option<A>): Option<A> = when (this) {
    is None -> ob()
    is Some -> this
}

fun <A> Option<A>.filter(f: (A) -> Boolean): Option<A> = when (this) {
    is None -> None
    is Some -> if (f(this.get)) this else None
}

//4.2
fun variance(xs: List<Double>): Option<Double> =
    mean(xs).flatMap{ m -> mean(xs.map { x -> Math.pow(x - m, 2.0) }) }

//4.3
fun <A, B, C> map2(
    oa: Option<A>,
    ob: Option<B>,
    f: (A, B) -> C
): Option<C> = oa.flatMap { a -> ob.map { b -> f(a, b) } }

//4.4
fun <A> sequence(
    xa: List<Option<A>>
): Option<List<A>> = xa.foldRight

//4.5
fun <A, B> traverse(
    xa: List<A>,
    f: (A) -> Option<B>
): Option<List<B>> =
    if(xa.contains(None)) None
    else Some(xa.map { x -> f(x).getOrElse { throw IllegalStateException() } })

//4.6
fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> = when (this) {
    is Left -> this
    is Right -> Right(f(this.value))
}


sealed class Option<out A>

data class Some<out A>(val get:A) : Option<A>()

object None : Option<Nothing>()

fun mean(xs: List<Double>): Option<Double> =
    if(xs.isEmpty()) None
    else Some(xs.sum() / xs.size)

sealed class Either<out E, out A>
data class Left<out E>(val value: E) : Either<E, Nothing>()
data class Right<out A>(val value: A) : Either<Nothing, A>()
inline fun <A, B> List<A>.map(crossinline f: (A) -> B): List<B> = foldRight(this, List.of()) { a, ls -> Cons(f(a), ls)}
fun <A, B> List<A>.foldRight(z: B, f: (A, B) -> B): B = when (this) {
    is Nil -> z
    is Cons -> f(this.head, foldRight(this.tail, z, f))
}

fun <A> List<A>.isEmpty(): Boolean = when (this) {
    is Nil -> true
    is Cons -> false
}
