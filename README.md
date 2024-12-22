# Smart Lodge

## OpenAPI / Swagger

### Generate the OpenAPI spec:
![Generate_OpenAPI_Spec_INTELLIJ.png](.assets/Generate_OpenAPI_Spec_INTELLIJ.png)
![OpenAPI_documentation.yaml_INTELLIJ.png](.assets/OpenAPI_documentation.yaml_INTELLIJ.png)

## Usage with Docker

### Build

```shell
docker build -t smart-loge:dev-latest -f ./Dockerfile .
```

### RUN

```shell
docker run -p 8080:8080 smart-loge:dev-latest
```
