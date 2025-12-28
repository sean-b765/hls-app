# HlsVideoControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getVideoSegment**](#getvideosegment) | **GET** /api/video/{mediaId}/{qualityProfile}/{segmentName} | |

# **getVideoSegment**
> string getVideoSegment()


### Example

```typescript
import {
    HlsVideoControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new HlsVideoControllerApi(configuration);

let mediaId: string; // (default to undefined)
let qualityProfile: string; // (default to undefined)
let segmentName: string; // (default to undefined)

const { status, data } = await apiInstance.getVideoSegment(
    mediaId,
    qualityProfile,
    segmentName
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mediaId** | [**string**] |  | defaults to undefined|
| **qualityProfile** | [**string**] |  | defaults to undefined|
| **segmentName** | [**string**] |  | defaults to undefined|


### Return type

**string**

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

