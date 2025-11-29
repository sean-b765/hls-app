# MediaMetadataControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getAll1**](#getall1) | **GET** /api/metadata | |

# **getAll1**
> Array<MediaMetadata> getAll1()


### Example

```typescript
import {
    MediaMetadataControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new MediaMetadataControllerApi(configuration);

const { status, data } = await apiInstance.getAll1();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<MediaMetadata>**

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

