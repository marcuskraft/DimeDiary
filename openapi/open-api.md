# OpenAPI

To be able to set OpenAPI base URL during runtime we use a custom template for `runtime.ts`. Forked from [runtime.mustache on GitHub](https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/resources/typescript-fetch/runtime.mustache).

There we set `BASE_URL` to a `window` variable which value is set during runtime.
