/**
 * EnumRetrofitConverterFactory.java - Traackr, Inc.
 *
 * This document set is the property of Traackr, Inc., a Massachusetts
 * Corporation, and contains confidential and trade secret information. It
 * cannot be transferred from the custody or control of Traackr except as
 * authorized in writing by an officer of Traackr. Neither this item nor the
 * information it contains can be used, transferred, reproduced, published,
 * or disclosed, in whole or in part, directly or indirectly, except as
 * expressly authorized by an officer of Traackr, pursuant to written
 * agreement.
 *
 * Copyright 2012-2015 Traackr, Inc. All Rights Reserved.
 */

package com.willwinder.robinhood4j.retrofit;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.annotation.Nullable;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Leverage the SerializedName annotation to serialize enum objects in retrofit parameters.
 *
 * from https://stackoverflow.com/a/35801262/204023
 */
public class EnumRetrofitConverterFactory extends Converter.Factory {
  @Override
  public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
    Converter<?, String> converter = null;
    if (type instanceof Class && ((Class<?>)type).isEnum()) {
      converter = value -> getSerializedNameValue((Enum) value);
    }
    return converter;
  }

  @Nullable
  static public <E extends Enum<E>> String getSerializedNameValue(E e) {
    try {
      return e.getClass().getField(e.name()).getAnnotation(SerializedName.class).value();
    } catch (Exception exception) {
      return e.toString();
    }
  }
}

