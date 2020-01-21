package com.drunkServer.database.utils;

import com.drunkServer.database.utils.customTypes.LongArrayType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.MappedSuperclass;

@TypeDefs({
        @TypeDef(
                name = "long-array",
                typeClass = LongArrayType.class
        )
})
@MappedSuperclass
public class DaoConfig {
}