# FileSystemApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getFolders**](#getfolders) | **GET** /api/filesystem/folders | |

# **getFolders**
> Array<FolderNode> getFolders()


### Example

```typescript
import {
    FileSystemApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new FileSystemApi(configuration);

let path: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getFolders(
    path
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **path** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<FolderNode>**

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

