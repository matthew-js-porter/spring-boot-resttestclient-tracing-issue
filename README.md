# RestTestClient does not propagate tracing headers

This project reproduces an issue in Spring Boot 4.2.0-M2. With `@AutoConfigureTracing`, an auto-configured
`RestTestClient` does not send a `traceparent` header. An auto-configured `TestRestTemplate` in the same
setup does send it.

## Reproducing

```shell
./mvnw test
```

`HelloController` exposes `GET /hello`. It logs the incoming `traceparent` header and returns it in the
response body. It returns `none` when the header is missing.

Two tests call the controller against a server on a random port. Both use `@AutoConfigureTracing`:

| Test                           | Client             | Result                       |
|--------------------------------|--------------------|------------------------------|
| `TestRestTemplateTracingTests` | `TestRestTemplate` | Passes, `traceparent` is sent |
| `RestTestClientTracingTests`   | `RestTestClient`   | Fails, no `traceparent`       |

The controller logs this output:

```
HelloController : traceparent: 00-8917b61dc85e3414bedb53e71202c039-7022adf9fad11da7-02   <- TestRestTemplate
HelloController : traceparent: null                                                       <- RestTestClient
```

## Expected behavior

`RestTestClient` should behave like `TestRestTemplate`. Its requests should be observed, and the trace context
should be sent in the `traceparent` header.

## Likely cause

- `TestRestTemplateTestAutoConfiguration` builds the `TestRestTemplate` from the context's `RestTemplateBuilder`.
  That builder already has the observation customizer applied, so its requests are observed and the trace
  context is sent.
- `RestTestClientTestAutoConfiguration` only applies `SpringBootRestTestClientBuilderCustomizer`, which sets up
  message converters. Nothing gives the client an `ObservationRegistry`, so there is no client observation and
  no `traceparent` is added.
