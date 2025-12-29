# LibraryControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**create3**](#create3) | **POST** /api/library | |
|[**deleteById3**](#deletebyid3) | **DELETE** /api/library/{id} | |
|[**deleteByIds3**](#deletebyids3) | **DELETE** /api/library | |
|[**findAll3**](#findall3) | **GET** /api/library | |
|[**findById3**](#findbyid3) | **GET** /api/library/{id} | |
|[**findByIds3**](#findbyids3) | **POST** /api/library/fetch | |
|[**scan**](#scan) | **POST** /api/library/{id}/scan | |
|[**upsert3**](#upsert3) | **PUT** /api/library | |

# **create3**
> Library create3(library)


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

const { status, data } = await apiInstance.create3(
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

# **deleteById3**
> object deleteById3()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteById3(
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

# **deleteByIds3**
> object deleteByIds3(requestBody)


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let requestBody: Array<string>; //

const { status, data } = await apiInstance.deleteByIds3(
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

# **findAll3**
> Array<Library> findAll3()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

const { status, data } = await apiInstance.findAll3();
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

# **findById3**
> Library findById3()


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.findById3(
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

# **findByIds3**
> Array<Library> findByIds3(requestBody)


### Example

```typescript
import {
    LibraryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LibraryControllerApi(configuration);

let requestBody: Array<string>; //

const { status, data } = await apiInstance.findByIds3(
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

# **upsert3**
> Library upsert3(library)


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

const { status, data } = await apiInstance.upsert3(
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

