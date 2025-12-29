# Media


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** |  | [default to undefined]
**createdAt** | **string** |  | [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]
**library** | [**Library**](Library.md) |  | [optional] [default to undefined]
**path** | **string** |  | [optional] [default to undefined]
**metadata** | [**MediaMetadata**](MediaMetadata.md) |  | [optional] [default to undefined]
**info** | [**MediaInfo**](MediaInfo.md) |  | [optional] [default to undefined]
**tvSeason** | [**TvSeason**](TvSeason.md) |  | [optional] [default to undefined]

## Example

```typescript
import { Media } from './api';

const instance: Media = {
    id,
    createdAt,
    updatedAt,
    library,
    path,
    metadata,
    info,
    tvSeason,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
