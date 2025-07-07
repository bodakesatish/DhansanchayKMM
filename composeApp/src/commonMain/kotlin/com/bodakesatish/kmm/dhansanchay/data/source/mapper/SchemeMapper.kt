package com.bodakesatish.kmm.dhansanchay.data.source.mapper

import com.bodakesatish.kmm.dhansanchay.SchemeEntity
import com.bodakesatish.kmm.dhansanchay.data.source.remote.model.SchemeNetworkModel
import com.bodakesatish.kmm.dhansanchay.domain.model.SchemeModel

object SchemeMapper : Mapper<SchemeNetworkModel, SchemeEntity, SchemeModel> {
    override fun SchemeNetworkModel.mapToEntity(): SchemeEntity {
        return SchemeEntity(
            schemeCode = schemeCode.toLong(),
            schemeName = schemeName,
            lastFetched = 0L
        )
    }

    override fun SchemeEntity.mapToDomain(): SchemeModel {
        return SchemeModel(
            schemeCode = schemeCode,
            schemeName = schemeName
        )
    }

    fun List<SchemeEntity>.toDomainModelList(): List<SchemeModel> {
        return this.map { it.mapToDomain() } // Calls the extension within the object's scope
    }

    fun List<SchemeNetworkModel>.toEntityModelList(): List<SchemeEntity> {
        return this.map { it.mapToEntity() } // Calls the extension within the object's scope
    }

}