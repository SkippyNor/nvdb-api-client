/*
 * Copyright (c) 2015-2017, Statens vegvesen
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.vegvesen.nvdbapi.client.model.datakatalog;

import java.util.Set;

public class DoubleAttributeType extends AbstractEnumAttributeType {
    private final Double defaultvalue;
    private final Double minValue;
    private final Double maxValue;
    private final Double absMinValue;
    private final Double absMaxValue;
    private final Integer fieldLength;
    private final Integer decimalCount;
    private final Unit unit;

    public DoubleAttributeType(AttributeCommonProperties props,
                               AttributeTypeParameters parameters, Double defaultvalue, Double minValue, Double maxValue,
                               Double absMinValue, Double absMaxValue, Integer fieldLength, Integer decimalCount, Unit unit,
                               Set<EnumValue> values) {
        super(props, parameters, values);
        this.defaultvalue = defaultvalue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.absMinValue = absMinValue;
        this.absMaxValue = absMaxValue;
        this.fieldLength = fieldLength;
        this.decimalCount = decimalCount;
        this.unit = unit;
    }

    public Double getDefaultvalue() {
        return defaultvalue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public Double getAbsMinValue() {
        return absMinValue;
    }

    public Double getAbsMaxValue() {
        return absMaxValue;
    }

    public Integer getFieldLength() {
        return fieldLength;
    }

    public Integer getDecimalCount() {
        return decimalCount;
    }

    public Unit getUnit() {
        return unit;
    }
}
