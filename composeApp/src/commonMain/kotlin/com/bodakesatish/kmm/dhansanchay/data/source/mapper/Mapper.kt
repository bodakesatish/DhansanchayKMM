package com.bodakesatish.kmm.dhansanchay.data.source.mapper

interface Mapper<Network, Entity, Domain> {
    fun Network.mapToEntity(): Entity
    fun Entity.mapToDomain(): Domain
}