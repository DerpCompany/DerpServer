# DefaultApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**accountIdDelete**](DefaultApi.md#accountIdDelete) | **DELETE** /account/{id} |  |
| [**accountIdPut**](DefaultApi.md#accountIdPut) | **PUT** /account/{id} |  |
| [**accountIdRolePut**](DefaultApi.md#accountIdRolePut) | **PUT** /account/{id}/role |  |
| [**accountPost**](DefaultApi.md#accountPost) | **POST** /account |  |
| [**profileGet**](DefaultApi.md#profileGet) | **GET** /profile |  |
| [**profileIdGet**](DefaultApi.md#profileIdGet) | **GET** /profile/{id} |  |
| [**profileRoleRoleIdGet**](DefaultApi.md#profileRoleRoleIdGet) | **GET** /profile/role/{roleId} |  |
| [**profileSubroleSubRoleIdGet**](DefaultApi.md#profileSubroleSubRoleIdGet) | **GET** /profile/subrole/{subRoleId}/ |  |
| [**profileUsernameUsernameGet**](DefaultApi.md#profileUsernameUsernameGet) | **GET** /profile/username/{username} |  |
| [**subroleIdDelete**](DefaultApi.md#subroleIdDelete) | **DELETE** /subrole/{id} |  |
| [**subroleIdProfileProfileIdDelete**](DefaultApi.md#subroleIdProfileProfileIdDelete) | **DELETE** /subrole/{id}/profile/{profileId} |  |
| [**subroleIdProfileProfileIdPut**](DefaultApi.md#subroleIdProfileProfileIdPut) | **PUT** /subrole/{id}/profile/{profileId} |  |
| [**subroleIdPut**](DefaultApi.md#subroleIdPut) | **PUT** /subrole/{id} |  |
| [**subrolePost**](DefaultApi.md#subrolePost) | **POST** /subrole |  |


<a name="accountIdDelete"></a>
# **accountIdDelete**
> Outcome accountIdDelete(id)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |

### Return type

[**Outcome**](../Models/Outcome.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="accountIdPut"></a>
# **accountIdPut**
> Account accountIdPut(id, AccountRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |
| **AccountRequest** | [**AccountRequest**](../Models/AccountRequest.md)|  | |

### Return type

[**Account**](../Models/Account.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="accountIdRolePut"></a>
# **accountIdRolePut**
> Account accountIdRolePut(id, ChangeRoleRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |
| **ChangeRoleRequest** | [**ChangeRoleRequest**](../Models/ChangeRoleRequest.md)|  | |

### Return type

[**Account**](../Models/Account.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="accountPost"></a>
# **accountPost**
> Account accountPost(AccountRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **AccountRequest** | [**AccountRequest**](../Models/AccountRequest.md)|  | |

### Return type

[**Account**](../Models/Account.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="profileGet"></a>
# **profileGet**
> List profileGet()



### Parameters
This endpoint does not need any parameter.

### Return type

[**List**](../Models/Profile.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="profileIdGet"></a>
# **profileIdGet**
> Profile profileIdGet(id)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |

### Return type

[**Profile**](../Models/Profile.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="profileRoleRoleIdGet"></a>
# **profileRoleRoleIdGet**
> List profileRoleRoleIdGet(roleId)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **roleId** | **String**|  | [default to null] |

### Return type

[**List**](../Models/Profile.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="profileSubroleSubRoleIdGet"></a>
# **profileSubroleSubRoleIdGet**
> List profileSubroleSubRoleIdGet(subRoleId)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **subRoleId** | **String**|  | [default to null] |

### Return type

[**List**](../Models/Profile.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="profileUsernameUsernameGet"></a>
# **profileUsernameUsernameGet**
> Profile profileUsernameUsernameGet(username)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **username** | **String**|  | [default to null] |

### Return type

[**Profile**](../Models/Profile.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="subroleIdDelete"></a>
# **subroleIdDelete**
> Outcome subroleIdDelete(id)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |

### Return type

[**Outcome**](../Models/Outcome.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="subroleIdProfileProfileIdDelete"></a>
# **subroleIdProfileProfileIdDelete**
> Outcome subroleIdProfileProfileIdDelete(id, profileId)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |
| **profileId** | **String**|  | [default to null] |

### Return type

[**Outcome**](../Models/Outcome.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="subroleIdProfileProfileIdPut"></a>
# **subroleIdProfileProfileIdPut**
> Outcome subroleIdProfileProfileIdPut(id, profileId)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |
| **profileId** | **String**|  | [default to null] |

### Return type

[**Outcome**](../Models/Outcome.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: Not defined
- **Accept**: application/json

<a name="subroleIdPut"></a>
# **subroleIdPut**
> SubRole subroleIdPut(id, SubRoleRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **String**|  | [default to null] |
| **SubRoleRequest** | [**SubRoleRequest**](../Models/SubRoleRequest.md)|  | |

### Return type

[**SubRole**](../Models/SubRole.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

<a name="subrolePost"></a>
# **subrolePost**
> SubRole subrolePost(SubRoleRequest)



### Parameters

|Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **SubRoleRequest** | [**SubRoleRequest**](../Models/SubRoleRequest.md)|  | |

### Return type

[**SubRole**](../Models/SubRole.md)

### Authorization

No authorization required

### HTTP request headers

- **Content-Type**: application/json
- **Accept**: application/json

