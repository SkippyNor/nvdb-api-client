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

package no.vegvesen.nvdbapi.client.gson;

import com.google.gson.JsonObject;
import no.vegvesen.nvdbapi.client.model.roadnet.roadsysref.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static no.vegvesen.nvdbapi.client.gson.GsonUtil.*;

public final class RoadSysRefParser {
    private RoadSysRefParser() {}

    public static RoadSysRef parse(JsonObject obj) {
        if (isNull(obj)) return null;

        if(!obj.has("vegsystem")) return  null;

        SideArea sideArea = parseSideArea(obj.getAsJsonObject("sideanlegg"));
        Intersection intersection = parseIntersection(obj.getAsJsonObject("kryssystem"));

        JsonObject sectionElement = obj.getAsJsonObject("strekning");
        if (nonNull(sideArea)) {
            sectionElement = obj.getAsJsonObject("sideanlegg");
        } else if (nonNull(intersection)) {
            sectionElement = obj.getAsJsonObject("kryssystem");
        }
        Double startMeter = parseDoubleMember(sectionElement,"fra_meter");
        Double endMeter = parseDoubleMember(sectionElement,"til_meter");
        Double meter = parseDoubleMember(sectionElement,"meter");

        if (isNull(meter) && isNull(startMeter) && isNull(endMeter)) return null;

        Section section = parseSection(obj.getAsJsonObject("strekning"));
        RoadSystem roadSystem = parseRoadSystem(obj.getAsJsonObject("vegsystem"));

        return new RoadSysRef(
                roadSystem,
                section,
                intersection,
                sideArea,
                parseStringMember(obj, "kortform"));
    }

    private static RoadSystem parseRoadSystem(JsonObject obj) {
        return new RoadSystem(
                parseLongMember(obj, "id"),
                parseIntMember(obj, "versjon"),
                parseIntMember(obj,"nummer"),
                parseStringMember(obj, "vegkategori"),
                parseStringMember(obj, "fase"));
    }

    private static Double getFromMeter(JsonObject obj) {
        Double fromMeter = parseDoubleMember(obj, "fra_meter");
        if (isNull(fromMeter) ) {
            return parseDoubleMember(obj, "meter");
        }
        return fromMeter;
    }

    private static Double getToMeter(JsonObject obj) {
        Double toMeter = parseDoubleMember(obj, "til_meter");

        if (isNull(toMeter) && isNull(toMeter)) {
            return parseDoubleMember(obj, "meter");
        }
        return toMeter;
    }

    private static Section parseSection(JsonObject obj) {
        if (isNull(obj)) return null;
        return new Section(
                parseLongMember(obj, "id"),
                parseIntMember(obj, "versjon"),
                parseIntMember(obj, "strekning"),
                parseIntMember(obj, "delstrekning"),
                parseBooleanMember(obj, "arm"),
                parseStringMember(obj, "adskilte_løp"),
                parseStringMember(obj, "trafikantgruppe"),
                getFromMeter(obj),
                getToMeter(obj));
    }

    private static Intersection parseIntersection(JsonObject obj) {
        if (isNull(obj)) return null;

        Integer intersectionNumber = parseIntMember(obj, "kryssystem");
        if (nonNull(intersectionNumber)) {
            return new Intersection(
                    parseLongMember(obj, "id"),
                    parseIntMember(obj, "versjon"),
                    intersectionNumber,
                    parseIntMember(obj, "kryssdel"),
                    getFromMeter(obj),
                    getToMeter(obj));
        }
        return null;
    }

    private static SideArea parseSideArea(JsonObject obj) {
        if (isNull(obj)) return null;

        Integer sideAreaNumber = parseIntMember(obj, "sideanlegg");
        if (nonNull(sideAreaNumber)) {
            return new SideArea(
                    parseLongMember(obj, "id"),
                    parseIntMember(obj, "versjon"),
                    sideAreaNumber,
                    parseIntMember(obj, "sideanleggsdel"),
                    getFromMeter(obj),
                    getToMeter(obj));
        }
        return null;
    }

}