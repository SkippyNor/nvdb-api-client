/*
 * Copyright (c) 2015-2017, Statens vegvesen
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.vegvesen.nvdbapi.client.clients;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.*;
import java.util.stream.Collectors;

class RoadObjectRequestBuilder {
    private RoadObjectRequestBuilder() {
    }

    static MultivaluedMap<String, String> convert(RoadObjectRequest request) {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();

        // Single parameters
        request.getSegmented().ifPresent(v -> map.putSingle("segmentering", Boolean.toString(v)));
        request.getProjection().ifPresent(v -> map.putSingle("srid", Integer.toString(v.getSrid())));
        request.getDistanceTolerance().ifPresent(v -> map.putSingle("geometritoleranse", Integer.toString(v)));
        request.getDepth().ifPresent(v -> map.putSingle("dybde", v));
        getIncludeArgument(request.getIncludes()).ifPresent(v -> map.putSingle("inkluder", v));
        getIncludeGeometriesArgument(request.getIncludeGeometries()).ifPresent(v -> map.putSingle("inkludergeometri", v));
        request.getAttributeFilter().ifPresent(v -> map.putSingle("egenskap", v));
        request.getBbox().ifPresent(v -> map.putSingle("kartutsnitt", v));
        request.getRoadRefFilter().ifPresent(v -> map.putSingle("vegreferanse", v));
        request.getRefLinkFilter().ifPresent(v -> map.putSingle("veglenke", v));
        flatten(request.getMunicipalities()).ifPresent(v -> map.putSingle("kommune", v));
        flatten(request.getCounties()).ifPresent(v -> map.putSingle("fylke", v));
        flatten(request.getRegions()).ifPresent(v -> map.putSingle("region", v));
        flatten(request.getRoadDepartments()).ifPresent(v -> map.putSingle("vegavdeling", v));
        flattenString(request.getContractAreas()).ifPresent(v -> map.putSingle("kontraktsomrade", v));
        flattenString(request.getNationalRoutes()).ifPresent(v -> map.putSingle("riksvegrute", v));

        // Multiple parameters
        request.getOverlapFilters().forEach(f -> map.add("overlapp", f.toString()));

        return map;
    }

    private static Optional<String> getIncludeArgument(Set<RoadObjectClient.Include> values) {
        // Defaults
        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }

        // "All" trumps any other values
        if (values.contains(RoadObjectClient.Include.ALL)) {
            return Optional.of(RoadObjectClient.Include.ALL.stringValue());
        }

        // "minimum" is redundant except when alone
        if (values.size() == 1 && values.contains(RoadObjectClient.Include.MINIMUM)) {
            return Optional.of(RoadObjectClient.Include.MINIMUM.stringValue());
        }

        String val = values.stream()
                           .filter(i -> i != RoadObjectClient.Include.MINIMUM)
                           .map(RoadObjectClient.Include::stringValue)
                           .collect(Collectors.joining(","));
        return Optional.of(val);
    }

    private static Optional<String> getIncludeGeometriesArgument(Set<RoadObjectClient.IncludeGeometry> values) {
        // Defaults
        if (values == null
                || values.isEmpty()
                || values.equals(RoadObjectClient.IncludeGeometry.all())) {
            return Optional.empty();
        }

        if (values.contains(RoadObjectClient.IncludeGeometry.NONE)) {
            return Optional.ofNullable(RoadObjectClient.IncludeGeometry.NONE.stringValue());
        }

        return Optional.of(values.stream()
                .map(RoadObjectClient.IncludeGeometry::stringValue)
                .collect(Collectors.joining(",")));
    }

    private static Optional<String> flatten(List<Integer> set) {
        if (set.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(set.stream()
                              .map(Object::toString)
                              .collect(Collectors.joining(",")));
    }

    private static Optional<String> flattenString(List<String> set) {
        if (set.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(set.stream()
                              .collect(Collectors.joining(",")));
    }
}
