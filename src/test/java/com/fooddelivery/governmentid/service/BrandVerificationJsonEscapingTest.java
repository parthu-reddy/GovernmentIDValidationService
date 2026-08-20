package com.fooddelivery.governmentid.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pins that stored verification payloads are valid JSON even when the input contains characters
 * that must be escaped.
 *
 * `gstin` is user-supplied and `legalName` comes from the external verification provider, so
 * neither is controlled by this service. They were previously concatenated straight into a JSON
 * string literal, which produced malformed JSON in the stored document and the audit log.
 */
class BrandVerificationJsonEscapingTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private String asJson(String field, String value) throws Exception {
        Method m = BrandVerificationService.class
                .getDeclaredMethod("asJson", String.class, String.class);
        m.setAccessible(true);
        return (String) m.invoke(null, field, value);
    }

    @Test
    void escapesEmbeddedDoubleQuotes() throws Exception {
        String json = asJson("legalName", "Joe \"Best\" Pizza Ltd");
        assertThat(mapper.readTree(json).get("legalName").asText()).isEqualTo("Joe \"Best\" Pizza Ltd");
    }

    @Test
    void escapesBackslashesAndNewlines() throws Exception {
        String json = asJson("legalName", "Line1\\Line2\nLine3");
        assertThat(mapper.readTree(json).get("legalName").asText()).isEqualTo("Line1\\Line2\nLine3");
    }

    @Test
    void handlesUserSuppliedGstinContainingAQuote() throws Exception {
        String json = asJson("gstin", "29ABCDE\"1234F1Z5");
        assertThat(mapper.readTree(json).get("gstin").asText()).isEqualTo("29ABCDE\"1234F1Z5");
    }

    @Test
    void handlesNullWithoutProducingBrokenJson() throws Exception {
        String json = asJson("legalName", null);
        assertThat(mapper.readTree(json).get("legalName").isNull()).isTrue();
    }
}
