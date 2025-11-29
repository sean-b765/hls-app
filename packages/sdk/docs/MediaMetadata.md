# MediaMetadata


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** |  | [optional] [default to undefined]
**fileExtension** | **string** |  | [optional] [default to undefined]
**videoCodec** | **string** |  | [optional] [default to undefined]
**audioCodec** | **string** |  | [optional] [default to undefined]
**sizeBytes** | **number** |  | [optional] [default to undefined]
**durationSeconds** | **number** |  | [optional] [default to undefined]
**framerate** | **number** |  | [optional] [default to undefined]
**lastScanDateTime** | **string** |  | [optional] [default to undefined]
**lastModified** | **string** |  | [optional] [default to undefined]
**media** | [**Media**](Media.md) |  | [optional] [default to undefined]

## Example

```typescript
import { MediaMetadata } from './api';

const instance: MediaMetadata = {
    id,
    fileExtension,
    videoCodec,
    audioCodec,
    sizeBytes,
    durationSeconds,
    framerate,
    lastScanDateTime,
    lastModified,
    media,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
