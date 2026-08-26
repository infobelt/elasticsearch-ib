/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.core.ssl;

import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.HostnameVerificationPolicy;
import org.apache.hc.core5.http.nio.ssl.TlsStrategy;
import org.apache.hc.core5.reactor.ssl.SSLBufferMode;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

class TlsStrategyBuilder extends AbstractSslBuilder<TlsStrategy> {

    public static final TlsStrategyBuilder INSTANCE = new TlsStrategyBuilder();

    @Override
    TlsStrategy build(SSLContext sslContext, String[] protocols, String[] ciphers, HostnameVerifier verifier) {
        // The verifier is derived from the configured verification_mode (see AbstractSslBuilder), so it - and not the JSSE
        // provider - must be the authority on hostname verification. Pin the policy explicitly rather than letting
        // httpclient5 infer one: as of 5.6.4 a non-null verifier infers BOTH, which additionally enables JSSE endpoint
        // identification and would reject connections that "certificate" and "none" are meant to allow.
        return new DefaultClientTlsStrategy(
            sslContext,
            protocols,
            ciphers,
            SSLBufferMode.DYNAMIC,
            HostnameVerificationPolicy.CLIENT,
            verifier
        );
    }
}
