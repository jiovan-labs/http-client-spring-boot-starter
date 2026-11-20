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


import java.util.Objects;
import java.util.StringJoiner;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.springframework.util.CollectionUtils;


/**
 * Utilities for {@link org.apache.hc.client5.http.cookie.CookieStore}
 *
 * @author David Hsing
 * @see org.apache.hc.client5.http.cookie.CookieStore
 */
@SuppressWarnings({"unused", "BooleanMethodIsAlwaysInverted", "UnusedReturnValue"})
public abstract class CookieStoreUtils {
    /**
     * Returns the first cookie with the given {@code name} in the given {@link org.apache.hc.client5.http.cookie.CookieStore}
     *
     * @param store the cookie store to search
     * @param name the cookie name to look up
     *
     * @return the matching cookie, or {@code null} if none is found or {@code name} is blank
     */

    @Nullable
    public Cookie getCookie(@Nonnull CookieStore store, @Nonnull String name) {
        if (CollectionUtils.isEmpty(store.getCookies()) || StringUtils.isBlank(name)) {
            return null;
        }
        return store.getCookies().stream().filter(cookie -> StringUtils.equals(name, cookie.getName())).findFirst().orElse(null);
    }

    /**
     * Returns the value of the first cookie with the given {@code name} in the given cookie store
     *
     * @param store the cookie store to search
     * @param name the cookie name to look up
     *
     * @return the cookie value, or {@code null} if no matching cookie is found
     */

    @Nullable
    public String getCookieValue(@Nonnull CookieStore store, @Nonnull String name) {
        Cookie cookie = getCookie(store, name);
        return cookie == null ? null : cookie.getValue();
    }

    /**
     * Returns all cookies in the given cookie store as a string delimited by {@code "="} and {@code ";"}
     *
     * @param store the cookie store whose cookies are formatted
     *
     * @return the formatted cookies, or {@code null} if the store contains no cookies
     */

    @Nullable
    public String getCookiesAsString(@Nonnull CookieStore store) {
        return getCookiesAsString(store, "=", ";");    // $NON-NLS-1$ // $NON-NLS-2$
    }

    /**
     * Returns all cookies in the given cookie store as a string with the given character delimiters
     *
     * @param store the cookie store whose cookies are formatted
     * @param keyValueDelimiter the delimiter between a cookie name and its value
     * @param groupDelimiter the delimiter between cookies
     *
     * @return the formatted cookies, or {@code null} if the store contains no cookies
     */

    @Nullable
    public String getCookiesAsString(@Nonnull CookieStore store, char keyValueDelimiter, char groupDelimiter) {
        return getCookiesAsString(store, CharUtils.toString(keyValueDelimiter), CharUtils.toString(groupDelimiter));
    }

    /**
     * Returns all cookies in the given cookie store as a string with the given delimiters
     *
     * @param store the cookie store whose cookies are formatted
     * @param keyValueDelimiter the delimiter between a cookie name and its value, may be {@code null}
     * @param groupDelimiter the delimiter between cookies, may be {@code null}
     *
     * @return the formatted cookies, or {@code null} if the store contains no cookies
     */

    @Nullable
    public String getCookiesAsString(@Nonnull CookieStore store, @Nullable String keyValueDelimiter, @Nullable String groupDelimiter) {
        if (CollectionUtils.isEmpty(store.getCookies())) {
            return null;
        }
        StringJoiner joiner = new StringJoiner(Objects.toString(groupDelimiter, StringUtils.EMPTY));
        store.getCookies().forEach(cookie -> joiner.add(StringUtils.join(cookie.getName(), Objects.toString(keyValueDelimiter, StringUtils.EMPTY), StringUtils.defaultString(cookie.getValue()))));
        return joiner.toString();
    }
}
