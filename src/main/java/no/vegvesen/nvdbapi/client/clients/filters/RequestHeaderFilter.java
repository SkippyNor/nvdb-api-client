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

package no.vegvesen.nvdbapi.client.clients.filters;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.util.Arrays;

public class RequestHeaderFilter implements ClientRequestFilter {
    private static final String X_CLIENT = "X-Client";
    private static final String DAKAT_VERSION = "X-Datakatalog-Versjon";

    private final String userAgent;
    private final String xClientName;
    private final String dakatVersion;
    private final boolean enableCompression;
    private final String apiRevision;

    public RequestHeaderFilter(String userAgent,
                               String xClientName,
                               String dakatVersion,
                               boolean enableCompression,
                               String apiRevision) {
        this.userAgent = userAgent;
        this.xClientName = xClientName;
        this.dakatVersion = dakatVersion;
        this.enableCompression = enableCompression;
        this.apiRevision = apiRevision;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.putSingle(HttpHeaders.ACCEPT, apiRevision);
        headers.putSingle(HttpHeaders.USER_AGENT, userAgent);
        if (enableCompression) headers.put(HttpHeaders.ACCEPT_ENCODING, Arrays.asList("gzip", "deflate"));

        headers.putSingle(X_CLIENT, xClientName);
        if (dakatVersion != null) headers.putSingle(DAKAT_VERSION, dakatVersion);
    }
}
