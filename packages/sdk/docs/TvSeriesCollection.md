# TvSeriesCollection


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **string** |  | [default to undefined]
**createdAt** | **string** |  | [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]
**library** | [**Library**](Library.md) |  | [optional] [default to undefined]
**externalId** | **string** |  | [optional] [default to undefined]
**name** | **string** |  | [default to undefined]
**description** | **string** |  | [optional] [default to undefined]
**releaseDate** | **string** |  | [optional] [default to undefined]
**thumbnail** | **string** |  | [optional] [default to undefined]
**banner** | **string** |  | [optional] [default to undefined]
**tvSeasons** | [**Array&lt;TvSeasonCollection&gt;**](TvSeasonCollection.md) |  | [optional] [default to undefined]

## Example

```typescript
import { TvSeriesCollection } from './api';

const instance: TvSeriesCollection = {
    id,
    createdAt,
    updatedAt,
    library,
    externalId,
    name,
    description,
    releaseDate,
    thumbnail,
    banner,
    tvSeasons,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
