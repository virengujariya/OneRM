package me.fitbod.repetition.mappers

interface Mapper<I, O> {
    fun map(input: I): O
}