# TVSeriesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**create1**](#create1) | **POST** /api/series | |
|[**deleteById1**](#deletebyid1) | **DELETE** /api/series/{id} | |
|[**deleteByIds1**](#deletebyids1) | **DELETE** /api/series | |
|[**findAll1**](#findall1) | **GET** /api/series | |
|[**findById1**](#findbyid1) | **GET** /api/series/{id} | |
|[**findByIds1**](#findbyids1) | **POST** /api/series/fetch | |
|[**upsert1**](#upsert1) | **PUT** /api/series | |

# **create1**
> TvSeries create1(tvSeries)


### Example

```typescript
import {
    TVSeriesApi,
    Configuration,
    TvSeries
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

let tvSeries: TvSeries; //

const { status, data } = await apiInstance.create1(
    tvSeries
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **tvSeries** | **TvSeries**|  | |


### Return type

**TvSeries**

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

# **deleteById1**
> object deleteById1()


### Example

```typescript
import {
    TVSeriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.deleteById1(
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

# **deleteByIds1**
> object deleteByIds1(requestBody)


### Example

```typescript
import {
    TVSeriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

let requestBody: Array<string>; //

const { status, data } = await apiInstance.deleteByIds1(
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

# **findAll1**
> Array<TvSeries> findAll1()


### Example

```typescript
import {
    TVSeriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

const { status, data } = await apiInstance.findAll1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<TvSeries>**

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

# **findById1**
> TvSeries findById1()


### Example

```typescript
import {
    TVSeriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

let id: string; // (default to undefined)

const { status, data } = await apiInstance.findById1(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**string**] |  | defaults to undefined|


### Return type

**TvSeries**

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

# **findByIds1**
> Array<TvSeries> findByIds1(requestBody)


### Example

```typescript
import {
    TVSeriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

let requestBody: Array<string>; //

const { status, data } = await apiInstance.findByIds1(
    requestBody
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **requestBody** | **Array<string>**|  | |


### Return type

**Array<TvSeries>**

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

# **upsert1**
> TvSeries upsert1(tvSeries)


### Example

```typescript
import {
    TVSeriesApi,
    Configuration,
    TvSeries
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

let tvSeries: TvSeries; //

const { status, data } = await apiInstance.upsert1(
    tvSeries
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **tvSeries** | **TvSeries**|  | |


### Return type

**TvSeries**

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

