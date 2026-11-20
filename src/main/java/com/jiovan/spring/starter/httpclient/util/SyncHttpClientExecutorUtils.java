/*
 * Copyright (c) 2020 Jiovan Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiovan.spring.starter.httpclient.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * Utilities for fetching and handling http response
 *
 * @author David Hsing
 * @see org.apache.hc.client5.http.entity.EntityBuilder
 * @see org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
 * @see org.apache.hc.core5.http.io.support.ClassicRequestBuilder
 * @see "https://hc.apache.org/httpcomponents-client-5.1.x/examples.html"
 */
@Slf4j
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class SyncHttpClientExecutorUtils {
    /**
     * Returns the result of executing the given request with the given client and handling its response
     *
     * @param client the HTTP client used to execute the request
     * @param request the classic HTTP request to execute
     * @param context the HTTP context, may be {@code null}
     * @param handler the response handler applied to the response
     * @param <T> the result type
     *
     * @return the result produced by {@code handler}
     *
     * @throws java.io.IOException if an I/O error occurs while executing the request
     * @throws java.net.URISyntaxException if the request URI is invalid
     */

    public static <T> T executeHandler(@Nonnull HttpClient client, @Nonnull ClassicHttpRequest request, @Nullable HttpContext context, @Nonnull HttpClientResponseHandler<? extends T> handler) throws IOException, URISyntaxException {
        if (log.isDebugEnabled()) {
            log.debug("Preparing to visit: {}", request.getUri());
        }
        if (!request.containsHeader(HttpHeaders.ACCEPT)) {
            request.addHeader(HttpHeaders.ACCEPT, ContentType.WILDCARD.getMimeType());
        }
        return client.execute(request, context, handler);
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the file at {@code pathname} using default timeouts
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param pathname the path of the file to write the response body to
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if the file cannot be opened or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String pathname) throws IOException, URISyntaxException {
        return downloadSimply(client, uri, pathname, RequestConfigUtils.withDefaultTimeouts());
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the file at {@code pathname} using the given request config
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param pathname the path of the file to write the response body to
     * @param config the request config, may be {@code null} to use default timeouts
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if the file cannot be opened or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String pathname, @Nullable RequestConfig config) throws IOException, URISyntaxException {
        return StringUtils.isNoneBlank(uri, pathname) && downloadSimply(client, uri, Files.newOutputStream(Paths.get(pathname)), config);
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the given file using default timeouts
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param output the file to write the response body to
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if the file cannot be opened or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull File output) throws IOException, URISyntaxException {
        return downloadSimply(client, uri, output, RequestConfigUtils.withDefaultTimeouts());
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the given file using the given request config
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param output the file to write the response body to
     * @param config the request config, may be {@code null} to use default timeouts
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if the file cannot be opened or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull File output, @Nullable RequestConfig config) throws IOException, URISyntaxException {
        return downloadSimply(client, uri, output, config, null, null);
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the given file
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param output the file to write the response body to
     * @param config the request config, may be {@code null}
     * @param parameters the request parameters, may be {@code null}
     * @param charset the charset of the request, may be {@code null}
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if the file cannot be opened or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull File output, @Nullable RequestConfig config, @Nullable List<NameValuePair> parameters, @Nullable Charset charset) throws IOException, URISyntaxException {
        return downloadSimply(client, uri, Files.newOutputStream(output.toPath()), config, null, null);
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the given output stream using default timeouts
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param output the output stream to write the response body to
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if an I/O error occurs while writing the response body
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull OutputStream output) throws IOException, URISyntaxException {
        return downloadSimply(client, uri, output, RequestConfigUtils.withDefaultTimeouts());
    }

    /**
     * Returns whether the response body of a GET request to {@code uri} is downloaded to the given output stream using the given request config
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param output the output stream to write the response body to
     * @param config the request config, may be {@code null} to use default timeouts
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if an I/O error occurs while writing the response body
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull OutputStream output, @Nullable RequestConfig config) throws IOException, URISyntaxException {
        return downloadSimply(client, uri, output, config, null, null);
    }

    /**
     * Returns whether the body of the response to a GET request with the given parameters is written to the given output stream
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to download from
     * @param output the output stream to write the response body to
     * @param config the request config, may be {@code null} to use default timeouts
     * @param parameters the request parameters to add to the URI, may be {@code null}
     * @param charset the charset of the request, may be {@code null} to use UTF-8
     *
     * @return whether the download succeeds
     *
     * @throws java.io.IOException if an I/O error occurs while writing the response body
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean downloadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull OutputStream output, @Nullable RequestConfig config, @Nullable List<NameValuePair> parameters, @Nullable Charset charset) throws IOException, URISyntaxException {
        if (StringUtils.isNotBlank(uri)) {
            config = ObjectUtils.getIfNull(config, RequestConfigUtils.withDefaultTimeouts());
            charset = ObjectUtils.getIfNull(charset, StandardCharsets.UTF_8);
            ClassicRequestBuilder builder = ClassicRequestBuilder.get(uri).setCharset(charset);
            if (!CollectionUtils.isEmpty(parameters)) {
                parameters.stream().filter(Objects::nonNull).forEach(builder::addParameter);
            }
            ClassicHttpRequest request = builder.build();
            if (request instanceof HttpUriRequestBase) {
                ((HttpUriRequestBase) request).setConfig(config);
            }
            HttpClientResponseHandler<Boolean> handler = (@Nonnull ClassicHttpResponse response) -> {
                if (response.getCode() == HttpStatus.SC_OK && response.getEntity() != null) {
                    response.getEntity().writeTo(output);
                    output.close();
                    return true;
                }
                return false;
            };
            return BooleanUtils.isTrue(executeHandler(client, request, null, handler));
        }
        return false;
    }

    /**
     * Returns whether the given file is uploaded as the {@code formField} of a multipart POST request to {@code uri} using default timeouts
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to upload to
     * @param formField the form field name for the file
     * @param input the file to upload
     *
     * @return whether the upload succeeds
     *
     * @throws java.lang.IllegalAccessException if the input file cannot be read
     * @throws java.io.IOException if the file cannot be found or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean uploadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String formField, @Nonnull File input) throws IllegalAccessException, IOException, URISyntaxException {
        return uploadSimply(client, uri, formField, input, RequestConfigUtils.withDefaultTimeouts());
    }

    /**
     * Returns whether the given file is uploaded as the {@code formField} of a multipart POST request to {@code uri} using the given request config
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to upload to
     * @param formField the form field name for the file
     * @param input the file to upload
     * @param config the request config, may be {@code null} to use default timeouts
     *
     * @return whether the upload succeeds
     *
     * @throws java.lang.IllegalAccessException if the input file cannot be read
     * @throws java.io.IOException if the file cannot be found or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean uploadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String formField, @Nonnull File input, @Nullable RequestConfig config) throws IllegalAccessException, IOException, URISyntaxException {
        return uploadSimply(client, uri, formField, input, config, null, null);
    }

    /**
     * Returns whether the file at {@code pathname} is uploaded as the {@code formField} of a multipart POST request to {@code uri} using default timeouts
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to upload to
     * @param formField the form field name for the file
     * @param pathname the path of the file to upload
     *
     * @return whether the upload succeeds
     *
     * @throws java.lang.IllegalAccessException if the input file cannot be read
     * @throws java.io.IOException if the file cannot be found or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean uploadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String formField, @Nonnull String pathname) throws IllegalAccessException, IOException, URISyntaxException {
        return uploadSimply(client, uri, formField, pathname, RequestConfigUtils.withDefaultTimeouts());
    }

    /**
     * Returns whether the file at {@code pathname} is uploaded as the {@code formField} of a multipart POST request to {@code uri} using the given request config
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to upload to
     * @param formField the form field name for the file
     * @param pathname the path of the file to upload
     * @param config the request config, may be {@code null} to use default timeouts
     *
     * @return whether the upload succeeds
     *
     * @throws java.lang.IllegalAccessException if the input file cannot be read
     * @throws java.io.IOException if the file cannot be found or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean uploadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String formField, @Nonnull String pathname, @Nullable RequestConfig config) throws IllegalAccessException, IOException, URISyntaxException {
        return uploadSimply(client, uri, formField, pathname, config, null, null);
    }

    /**
     * Returns whether the file at {@code pathname} is uploaded with the given text parameters as a multipart POST request to {@code uri}
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to upload to
     * @param formField the form field name for the file
     * @param pathname the path of the file to upload
     * @param config the request config, may be {@code null} to use default timeouts
     * @param parameters the text parameters to add to the multipart body, may be {@code null}
     * @param charset the charset of the request, may be {@code null} to use UTF-8
     *
     * @return whether the upload succeeds
     *
     * @throws java.lang.IllegalAccessException if the input file cannot be read
     * @throws java.io.IOException if the file cannot be found or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean uploadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String formField, @Nonnull String pathname, @Nullable RequestConfig config, @Nullable List<NameValuePair> parameters, @Nullable Charset charset) throws IllegalAccessException, IOException, URISyntaxException {
        return StringUtils.isNoneBlank(uri, formField, pathname) && uploadSimply(client, uri, formField, new File(pathname), config, parameters, charset);
    }

    /**
     * Returns whether the given file is uploaded with the given text parameters as a multipart POST request to {@code uri}
     *
     * @param client the HTTP client used to execute the request
     * @param uri the URI to upload to
     * @param formField the form field name for the file
     * @param input the file to upload
     * @param config the request config, may be {@code null} to use default timeouts
     * @param parameters the text parameters to add to the multipart body, may be {@code null}
     * @param charset the charset of the request, may be {@code null} to use UTF-8
     *
     * @return whether the upload succeeds
     *
     * @throws java.lang.IllegalAccessException if the input file cannot be read
     * @throws java.io.IOException if the file cannot be found or an I/O error occurs
     * @throws java.net.URISyntaxException if {@code uri} is not a valid URI
     */

    public static boolean uploadSimply(@Nonnull HttpClient client, @Nonnull String uri, @Nonnull String formField, @Nonnull File input, @Nullable RequestConfig config, @Nullable List<NameValuePair> parameters, @Nullable Charset charset) throws IllegalAccessException, IOException, URISyntaxException {
        if (StringUtils.isNoneBlank(uri, formField)) {
            if (!input.exists() || !input.isFile()) {
                throw new FileNotFoundException();
            }
            if (!input.canRead()) {
                throw new IllegalAccessException();
            }
            config = ObjectUtils.getIfNull(config, RequestConfigUtils.withDefaultTimeouts());
            charset = ObjectUtils.getIfNull(charset, StandardCharsets.UTF_8);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create().setCharset(charset);
            builder.addBinaryBody(formField, input, ContentType.MULTIPART_FORM_DATA, input.getName());
            if (!CollectionUtils.isEmpty(parameters)) {
                parameters.stream().filter(Objects::nonNull).forEach(pair -> builder.addTextBody(pair.getName(), pair.getValue()));
            }
            ClassicHttpRequest request = ClassicRequestBuilder.post(uri).setCharset(charset).build();
            if (request instanceof HttpUriRequestBase) {
                ((HttpUriRequestBase) request).setConfig(config);
            }
            HttpClientResponseHandler<Boolean> handler = (@Nonnull ClassicHttpResponse response) -> response.getCode() == HttpStatus.SC_OK;
            return BooleanUtils.isTrue(executeHandler(client, request, null, handler));
        }
        return false;
    }
}
