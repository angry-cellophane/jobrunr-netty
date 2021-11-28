# jobrunr-spring-webflux
[jobrunr](https://www.jobrunr.io) is not fully integrated with spring web.

jobruns creates a new webserver on a different port and does have integration with default spring actuator endpoints like
`health`, `metrics`, `info`. 

Also spring-security configuration is not applied to the jobrunr webserver.

This project rewrites the defautl jobrunr webserver to reuse spring webflux to add jobrunr endpoints.
