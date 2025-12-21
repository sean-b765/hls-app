# TVSeriesApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**findTvSeries**](#findtvseries) | **GET** /api/series | |

# **findTvSeries**
> Array<TvSeriesCollection> findTvSeries()


### Example

```typescript
import {
    TVSeriesApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new TVSeriesApi(configuration);

const { status, data } = await apiInstance.findTvSeries();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<TvSeriesCollection>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

