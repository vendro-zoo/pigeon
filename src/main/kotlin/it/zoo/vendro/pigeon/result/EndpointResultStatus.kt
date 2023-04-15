package it.zoo.vendro.pigeon.result

/**
 * Indicates whether a result is valid or not.
 */
enum class EndpointResultStatus {
    /**
     * An OK result is returned when an [Endpoint][it.zoo.vendro.pigeon.endpoint.Endpoint] generates a valid response.
     */
    OK,
    /**
     * An ERROR result is returned when an [Endpoint][it.zoo.vendro.pigeon.endpoint.Endpoint] generates an invalid response.
     */
    ERROR
}