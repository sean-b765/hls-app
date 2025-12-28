# LibraryControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**create4**](#create4) | **POST** /api/library | |
|[**deleteById4**](#deletebyid4) | **DELETE** /api/library/{id} | |
|[**deleteByIds4**](#deletebyids4) | **DELETE** /api/library | |
|[**findAll4**](#findall4) | **GET** /api/library | |
|[**findById4**](#findbyid4) | **GET** /api/library/{id} | |
|[**findByIds4**](#findbyids4) | **POST** /api/library/fetch | |
|[**scan**](#scan) | **POST** /api/library/{id}/scan | |
|[**upsert4**](#upsert4) | **PUT** /api/library | |

# **create4**
> Library create4(library)


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration,
    Library
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let library: Library; //

const { status, data } = await apiInstance.create4(
    library
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **library** | **Library**|  | |


### Return type

**Library**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteById4**
> object deleteById4()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteById4(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**object**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteByIds4**
> object deleteByIds4(requestBody)


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let requestBody: Array<string>; //

const { status, data } = await apiInstance.deleteByIds4(
    requestBody
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **requestBody** | **Array<string>**|  | |


### Return type

**object**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **findAll4**
> Array<Library> findAll4()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

const { status, data } = await apiInstance.findAll4();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<Library>**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **findById4**
> Library findById4()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.findById4(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**Library**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **findByIds4**
> Array<Library> findByIds4(requestBody)


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let requestBody: Array<string>; //

const { status, data } = await apiInstance.findByIds4(
    requestBody
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **requestBody** | **Array<string>**|  | |


### Return type

**Array<Library>**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **scan**
> object scan()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.scan(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**object**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **upsert4**
> Library upsert4(library)


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration,
    Library
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let library: Library; //

const { status, data } = await apiInstance.upsert4(
    library
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **library** | **Library**|  | |


### Return type

**Library**

### Authorization

[Authorization](../README.md#Authorization)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

