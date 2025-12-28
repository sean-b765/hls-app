# HlsPlaylistControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**generateMultiVariantPlaylist**](#generatemultivariantplaylist) | **GET** /api/playlist/{mediaId} | |
|[**generateVodPlaylist**](#generatevodplaylist) | **GET** /api/playlist/{mediaId}/{qualityProfile} | |

# **generateMultiVariantPlaylist**
> object generateMultiVariantPlaylist()


### Example

```typescript
import {
    HlsPlaylistControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new HlsPlaylistControllerApi(configuration);

let mediaId: string; // (default to undefined)

const { status, data } = await apiInstance.generateMultiVariantPlaylist(
    mediaId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mediaId** | [**string**] |  | defaults to undefined|


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

# **generateVodPlaylist**
> object generateVodPlaylist()


### Example

```typescript
import {
    HlsPlaylistControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new HlsPlaylistControllerApi(configuration);

let mediaId: string; // (default to undefined)
let qualityProfile: string; // (default to undefined)

const { status, data } = await apiInstance.generateVodPlaylist(
    mediaId,
    qualityProfile
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mediaId** | [**string**] |  | defaults to undefined|
| **qualityProfile** | [**string**] |  | defaults to undefined|


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

