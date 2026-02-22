# 用户中心文档

**简介**:用户中心文档

**HOST**:

**联系人**:yunjin

**Version**:1.0.0

**接口路径**:/org/

# ai服务

## 元景AI

**接口地址**:`/bbt-api/org/ai/yj/text`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| text | text | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 区域编码Feign接口

## addRegion

**接口地址**:`/bbt-api/api/sys/region`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "boundBottom": 0,
  "boundLeft": 0,
  "boundRight": 0,
  "boundTop": 0,
  "children": [
    {
      "boundBottom": 0,
      "boundLeft": 0,
      "boundRight": 0,
      "boundTop": 0,
      "children": [],
      "createBy": "",
      "createTime": "",
      "extend": 0,
      "gisOid": 0,
      "id": "",
      "latitude": 0,
      "longitude": 0,
      "parentCode": "",
      "parents": "",
      "regionArea": 0,
      "regionCode": "",
      "regionLeader": "",
      "regionLevel": 0,
      "regionName": "",
      "regionPeopleNumber": 0,
      "regionType": "",
      "remark": "",
      "subNum": 0,
      "updateBy": "",
      "updateTime": ""
    }
  ],
  "createBy": "",
  "createTime": "",
  "extend": 0,
  "gisOid": 0,
  "id": "",
  "latitude": 0,
  "longitude": 0,
  "parentCode": "",
  "parents": "",
  "regionArea": 0,
  "regionCode": "",
  "regionLeader": "",
  "regionLevel": 0,
  "regionName": "",
  "regionPeopleNumber": 0,
  "regionType": "",
  "remark": "",
  "subNum": 0,
  "updateBy": "",
  "updateTime": ""
}
```

**请求参数**:

| 参数名称                           | 参数说明                 | 请求类型 | 是否必须  | 数据类型                 | schema               |
|--------------------------------|----------------------|------|-------|----------------------|----------------------|
| apiSysRegionAddParam           | ApiSysRegionAddParam | body | true  | ApiSysRegionAddParam | ApiSysRegionAddParam |
| &emsp;&emsp;boundBottom        |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;boundLeft          |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;boundRight         |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;boundTop           |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;children           |                      |      | false | array                | ApiSysRegionAddParam |
| &emsp;&emsp;createBy           |                      |      | false | string               |                      |
| &emsp;&emsp;createTime         |                      |      | false | string(date-time)    |                      |
| &emsp;&emsp;extend             |                      |      | false | integer(int64)       |                      |
| &emsp;&emsp;gisOid             |                      |      | false | integer(int64)       |                      |
| &emsp;&emsp;id                 |                      |      | false | string               |                      |
| &emsp;&emsp;latitude           |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;longitude          |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;parentCode         |                      |      | false | string               |                      |
| &emsp;&emsp;parents            |                      |      | false | string               |                      |
| &emsp;&emsp;regionArea         |                      |      | false | number(bigdecimal)   |                      |
| &emsp;&emsp;regionCode         |                      |      | false | string               |                      |
| &emsp;&emsp;regionLeader       |                      |      | false | string               |                      |
| &emsp;&emsp;regionLevel        |                      |      | false | integer(int32)       |                      |
| &emsp;&emsp;regionName         |                      |      | false | string               |                      |
| &emsp;&emsp;regionPeopleNumber |                      |      | false | integer(int64)       |                      |
| &emsp;&emsp;regionType         |                      |      | false | string               |                      |
| &emsp;&emsp;remark             |                      |      | false | string               |                      |
| &emsp;&emsp;subNum             |                      |      | false | integer(int64)       |                      |
| &emsp;&emsp;updateBy           |                      |      | false | string               |                      |
| &emsp;&emsp;updateTime         |                      |      | false | string(date-time)    |                      |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«SysRegionAO» |

**响应参数**:

| 参数名称                           | 参数说明 | 类型                 | schema         |
|--------------------------------|------|--------------------|----------------| 
| code                           |      | integer(int32)     | integer(int32) |
| data                           |      | SysRegionAO        | SysRegionAO    |
| &emsp;&emsp;boundBottom        |      | number(bigdecimal) |                |
| &emsp;&emsp;boundLeft          |      | number(bigdecimal) |                |
| &emsp;&emsp;boundRight         |      | number(bigdecimal) |                |
| &emsp;&emsp;boundTop           |      | number(bigdecimal) |                |
| &emsp;&emsp;children           |      | array              | SysRegionAO    |
| &emsp;&emsp;createBy           |      | string             |                |
| &emsp;&emsp;createTime         |      | string(date-time)  |                |
| &emsp;&emsp;extend             |      | integer(int64)     |                |
| &emsp;&emsp;gisOid             |      | integer(int64)     |                |
| &emsp;&emsp;id                 |      | string             |                |
| &emsp;&emsp;latitude           |      | number(bigdecimal) |                |
| &emsp;&emsp;longitude          |      | number(bigdecimal) |                |
| &emsp;&emsp;parentCode         |      | string             |                |
| &emsp;&emsp;parents            |      | string             |                |
| &emsp;&emsp;regionArea         |      | number(bigdecimal) |                |
| &emsp;&emsp;regionCode         |      | string             |                |
| &emsp;&emsp;regionLeader       |      | string             |                |
| &emsp;&emsp;regionLevel        |      | integer(int32)     |                |
| &emsp;&emsp;regionName         |      | string             |                |
| &emsp;&emsp;regionPeopleNumber |      | integer(int64)     |                |
| &emsp;&emsp;regionType         |      | string             |                |
| &emsp;&emsp;remark             |      | string             |                |
| &emsp;&emsp;subNum             |      | integer(int64)     |                |
| &emsp;&emsp;updateBy           |      | string             |                |
| &emsp;&emsp;updateTime         |      | string(date-time)  |                |
| message                        |      | string             |                |
| stack                          |      | string             |                |
| success                        |      | boolean            |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"boundBottom": 0,
		"boundLeft": 0,
		"boundRight": 0,
		"boundTop": 0,
		"children": [
			{
				"boundBottom": 0,
				"boundLeft": 0,
				"boundRight": 0,
				"boundTop": 0,
				"children": [
					{}
				],
				"createBy": "",
				"createTime": "",
				"extend": 0,
				"gisOid": 0,
				"id": "",
				"latitude": 0,
				"longitude": 0,
				"parentCode": "",
				"parents": "",
				"regionArea": 0,
				"regionCode": "",
				"regionLeader": "",
				"regionLevel": 0,
				"regionName": "",
				"regionPeopleNumber": 0,
				"regionType": "",
				"remark": "",
				"subNum": 0,
				"updateBy": "",
				"updateTime": ""
			}
		],
		"createBy": "",
		"createTime": "",
		"extend": 0,
		"gisOid": 0,
		"id": "",
		"latitude": 0,
		"longitude": 0,
		"parentCode": "",
		"parents": "",
		"regionArea": 0,
		"regionCode": "",
		"regionLeader": "",
		"regionLevel": 0,
		"regionName": "",
		"regionPeopleNumber": 0,
		"regionType": "",
		"remark": "",
		"subNum": 0,
		"updateBy": "",
		"updateTime": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## all

**接口地址**:`/bbt-api/api/sys/region/all`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«List«SysRegionAO»» |

**响应参数**:

| 参数名称                           | 参数说明 | 类型                 | schema         |
|--------------------------------|------|--------------------|----------------| 
| code                           |      | integer(int32)     | integer(int32) |
| data                           |      | array              | SysRegionAO    |
| &emsp;&emsp;boundBottom        |      | number(bigdecimal) |                |
| &emsp;&emsp;boundLeft          |      | number(bigdecimal) |                |
| &emsp;&emsp;boundRight         |      | number(bigdecimal) |                |
| &emsp;&emsp;boundTop           |      | number(bigdecimal) |                |
| &emsp;&emsp;children           |      | array              | SysRegionAO    |
| &emsp;&emsp;createBy           |      | string             |                |
| &emsp;&emsp;createTime         |      | string(date-time)  |                |
| &emsp;&emsp;extend             |      | integer(int64)     |                |
| &emsp;&emsp;gisOid             |      | integer(int64)     |                |
| &emsp;&emsp;id                 |      | string             |                |
| &emsp;&emsp;latitude           |      | number(bigdecimal) |                |
| &emsp;&emsp;longitude          |      | number(bigdecimal) |                |
| &emsp;&emsp;parentCode         |      | string             |                |
| &emsp;&emsp;parents            |      | string             |                |
| &emsp;&emsp;regionArea         |      | number(bigdecimal) |                |
| &emsp;&emsp;regionCode         |      | string             |                |
| &emsp;&emsp;regionLeader       |      | string             |                |
| &emsp;&emsp;regionLevel        |      | integer(int32)     |                |
| &emsp;&emsp;regionName         |      | string             |                |
| &emsp;&emsp;regionPeopleNumber |      | integer(int64)     |                |
| &emsp;&emsp;regionType         |      | string             |                |
| &emsp;&emsp;remark             |      | string             |                |
| &emsp;&emsp;subNum             |      | integer(int64)     |                |
| &emsp;&emsp;updateBy           |      | string             |                |
| &emsp;&emsp;updateTime         |      | string(date-time)  |                |
| message                        |      | string             |                |
| stack                          |      | string             |                |
| success                        |      | boolean            |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"boundBottom": 0,
			"boundLeft": 0,
			"boundRight": 0,
			"boundTop": 0,
			"children": [
				{
					"boundBottom": 0,
					"boundLeft": 0,
					"boundRight": 0,
					"boundTop": 0,
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"extend": 0,
					"gisOid": 0,
					"id": "",
					"latitude": 0,
					"longitude": 0,
					"parentCode": "",
					"parents": "",
					"regionArea": 0,
					"regionCode": "",
					"regionLeader": "",
					"regionLevel": 0,
					"regionName": "",
					"regionPeopleNumber": 0,
					"regionType": "",
					"remark": "",
					"subNum": 0,
					"updateBy": "",
					"updateTime": ""
				}
			],
			"createBy": "",
			"createTime": "",
			"extend": 0,
			"gisOid": 0,
			"id": "",
			"latitude": 0,
			"longitude": 0,
			"parentCode": "",
			"parents": "",
			"regionArea": 0,
			"regionCode": "",
			"regionLeader": "",
			"regionLevel": 0,
			"regionName": "",
			"regionPeopleNumber": 0,
			"regionType": "",
			"remark": "",
			"subNum": 0,
			"updateBy": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 区域预览指定父级

**接口地址**:`/bbt-api/api/sys/region/name`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明       | 请求类型  | 是否必须 | 数据类型           | schema |
|------------|------------|-------|------|----------------|--------|
| regionCode | regionCode | query | true | string         |        |
| limit      | limit      | query | true | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«List«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取区域信息

**接口地址**:`/bbt-api/api/sys/region/info`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明       | 请求类型  | 是否必须 | 数据类型           | schema |
|------------|------------|-------|------|----------------|--------|
| regionCode | regionCode | query | true | string         |        |
| limit      | limit      | query | true | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | Result«RegionInfo» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | RegionInfo     | RegionInfo     |
| &emsp;&emsp;code     |      | string         |                |
| &emsp;&emsp;fullName |      | array          | string         |
| &emsp;&emsp;id       |      | Serializable   | Serializable   |
| &emsp;&emsp;name     |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"code": "",
		"fullName": [],
		"id": {},
		"name": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## getRegionTree

**接口地址**:`/bbt-api/api/sys/region/getRegionTree`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«List«SysRegionAO»» |

**响应参数**:

| 参数名称                           | 参数说明 | 类型                 | schema         |
|--------------------------------|------|--------------------|----------------| 
| code                           |      | integer(int32)     | integer(int32) |
| data                           |      | array              | SysRegionAO    |
| &emsp;&emsp;boundBottom        |      | number(bigdecimal) |                |
| &emsp;&emsp;boundLeft          |      | number(bigdecimal) |                |
| &emsp;&emsp;boundRight         |      | number(bigdecimal) |                |
| &emsp;&emsp;boundTop           |      | number(bigdecimal) |                |
| &emsp;&emsp;children           |      | array              | SysRegionAO    |
| &emsp;&emsp;createBy           |      | string             |                |
| &emsp;&emsp;createTime         |      | string(date-time)  |                |
| &emsp;&emsp;extend             |      | integer(int64)     |                |
| &emsp;&emsp;gisOid             |      | integer(int64)     |                |
| &emsp;&emsp;id                 |      | string             |                |
| &emsp;&emsp;latitude           |      | number(bigdecimal) |                |
| &emsp;&emsp;longitude          |      | number(bigdecimal) |                |
| &emsp;&emsp;parentCode         |      | string             |                |
| &emsp;&emsp;parents            |      | string             |                |
| &emsp;&emsp;regionArea         |      | number(bigdecimal) |                |
| &emsp;&emsp;regionCode         |      | string             |                |
| &emsp;&emsp;regionLeader       |      | string             |                |
| &emsp;&emsp;regionLevel        |      | integer(int32)     |                |
| &emsp;&emsp;regionName         |      | string             |                |
| &emsp;&emsp;regionPeopleNumber |      | integer(int64)     |                |
| &emsp;&emsp;regionType         |      | string             |                |
| &emsp;&emsp;remark             |      | string             |                |
| &emsp;&emsp;subNum             |      | integer(int64)     |                |
| &emsp;&emsp;updateBy           |      | string             |                |
| &emsp;&emsp;updateTime         |      | string(date-time)  |                |
| message                        |      | string             |                |
| stack                          |      | string             |                |
| success                        |      | boolean            |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"boundBottom": 0,
			"boundLeft": 0,
			"boundRight": 0,
			"boundTop": 0,
			"children": [
				{
					"boundBottom": 0,
					"boundLeft": 0,
					"boundRight": 0,
					"boundTop": 0,
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"extend": 0,
					"gisOid": 0,
					"id": "",
					"latitude": 0,
					"longitude": 0,
					"parentCode": "",
					"parents": "",
					"regionArea": 0,
					"regionCode": "",
					"regionLeader": "",
					"regionLevel": 0,
					"regionName": "",
					"regionPeopleNumber": 0,
					"regionType": "",
					"remark": "",
					"subNum": 0,
					"updateBy": "",
					"updateTime": ""
				}
			],
			"createBy": "",
			"createTime": "",
			"extend": 0,
			"gisOid": 0,
			"id": "",
			"latitude": 0,
			"longitude": 0,
			"parentCode": "",
			"parents": "",
			"regionArea": 0,
			"regionCode": "",
			"regionLeader": "",
			"regionLevel": 0,
			"regionName": "",
			"regionPeopleNumber": 0,
			"regionType": "",
			"remark": "",
			"subNum": 0,
			"updateBy": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前区域及子级区域编码

**接口地址**:`/bbt-api/api/sys/region/subCodes`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明       | 请求类型  | 是否必须 | 数据类型   | schema |
|------------|------------|-------|------|--------|--------|
| regionCode | regionCode | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«Set«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

# 投诉反馈

## 处理

**接口地址**:`/bbt-api/org/complaint/deal`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "complaintForm": "",
  "complaintSource": 0,
  "complaintTask": "",
  "complaintType": 0,
  "createBy": "",
  "createTime": "",
  "formDept": "",
  "id": "",
  "isDeleted": 0,
  "reason": "",
  "reply": "",
  "status": 0,
  "taskSource": 0,
  "taskSys": "",
  "updateBy": "",
  "updateTime": ""
}
```

**请求参数**:

| 参数名称                        | 参数说明               | 请求类型 | 是否必须  | 数据类型               | schema             |
|-----------------------------|--------------------|------|-------|--------------------|--------------------|
| orgComplaintReport          | OrgComplaintReport | body | true  | OrgComplaintReport | OrgComplaintReport |
| &emsp;&emsp;complaintForm   |                    |      | false | string             |                    |
| &emsp;&emsp;complaintSource |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;complaintTask   |                    |      | false | string             |                    |
| &emsp;&emsp;complaintType   |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;createBy        |                    |      | false | string             |                    |
| &emsp;&emsp;createTime      |                    |      | false | string(date-time)  |                    |
| &emsp;&emsp;formDept        |                    |      | false | string             |                    |
| &emsp;&emsp;id              |                    |      | false | string             |                    |
| &emsp;&emsp;isDeleted       |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;reason          |                    |      | false | string             |                    |
| &emsp;&emsp;reply           |                    |      | false | string             |                    |
| &emsp;&emsp;status          |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;taskSource      |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;taskSys         |                    |      | false | string             |                    |
| &emsp;&emsp;updateBy        |                    |      | false | string             |                    |
| &emsp;&emsp;updateTime      |                    |      | false | string(date-time)  |                    |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 投诉反馈分页列表

**接口地址**:`/bbt-api/org/complaint/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称            | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|-----------------|-----------|-------|-------|----------------|--------|
| complaintSource | 投诉来源      | query | false | integer(int32) |        |
| current         | 页码,默认为1   | query | false | integer(int32) |        |
| size            | 页大小,默认为10 | query | false | integer(int32) |        |
| status          | 投诉状态      | query | false | integer(int32) |        |
| userId          | 用户id      | query | false | string         |        |

**响应状态**:

| 状态码 | 说明 | schema                      |
|-----|----|-----------------------------| 
| 200 | OK | IPage«OrgComplaintReportVO» |

**响应参数**:

| 参数名称                            | 参数说明   | 类型                | schema               |
|---------------------------------|--------|-------------------|----------------------| 
| current                         |        | integer(int64)    | integer(int64)       |
| pages                           |        | integer(int64)    | integer(int64)       |
| records                         |        | array             | OrgComplaintReportVO |
| &emsp;&emsp;complaintForm       | 投诉表单   | string            |                      |
| &emsp;&emsp;complaintFormName   | 投诉表单中文 | string            |                      |
| &emsp;&emsp;complaintSource     | 投诉来源   | integer(int32)    |                      |
| &emsp;&emsp;complaintSourceName | 投诉来源中文 | string            |                      |
| &emsp;&emsp;complaintTask       | 投诉任务   | string            |                      |
| &emsp;&emsp;complaintType       | 投诉类型   | integer(int32)    |                      |
| &emsp;&emsp;complaintTypeName   | 投诉类型中文 | string            |                      |
| &emsp;&emsp;createTime          | 创建时间   | string(date-time) |                      |
| &emsp;&emsp;formDept            | 制表部门   | string            |                      |
| &emsp;&emsp;id                  | 主键     | string            |                      |
| &emsp;&emsp;parFormDept         | 制表父部门  | string            |                      |
| &emsp;&emsp;reason              | 投诉原因   | string            |                      |
| &emsp;&emsp;reply               | 投诉反馈   | string            |                      |
| &emsp;&emsp;status              | 投诉状态   | integer(int32)    |                      |
| &emsp;&emsp;statusName          | 投诉状态中文 | string            |                      |
| &emsp;&emsp;taskSource          | 任务来源   | integer(int32)    |                      |
| &emsp;&emsp;taskSourceName      | 任务来源中文 | string            |                      |
| &emsp;&emsp;taskSys             | 任务来源系统 | string            |                      |
| size                            |        | integer(int64)    | integer(int64)       |
| total                           |        | integer(int64)    | integer(int64)       |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"complaintForm": "",
			"complaintFormName": "",
			"complaintSource": 0,
			"complaintSourceName": "",
			"complaintTask": "",
			"complaintType": 0,
			"complaintTypeName": "",
			"createTime": "",
			"formDept": "",
			"id": "",
			"parFormDept": "",
			"reason": "",
			"reply": "",
			"status": 0,
			"statusName": "",
			"taskSource": 0,
			"taskSourceName": "",
			"taskSys": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 新增投诉反馈

**接口地址**:`/bbt-api/org/complaint`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "complaintForm": "",
  "complaintSource": 0,
  "complaintTask": "",
  "complaintType": 0,
  "createBy": "",
  "createTime": "",
  "formDept": "",
  "id": "",
  "isDeleted": 0,
  "reason": "",
  "reply": "",
  "status": 0,
  "taskSource": 0,
  "taskSys": "",
  "updateBy": "",
  "updateTime": ""
}
```

**请求参数**:

| 参数名称                        | 参数说明               | 请求类型 | 是否必须  | 数据类型               | schema             |
|-----------------------------|--------------------|------|-------|--------------------|--------------------|
| orgComplaintReport          | OrgComplaintReport | body | true  | OrgComplaintReport | OrgComplaintReport |
| &emsp;&emsp;complaintForm   |                    |      | false | string             |                    |
| &emsp;&emsp;complaintSource |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;complaintTask   |                    |      | false | string             |                    |
| &emsp;&emsp;complaintType   |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;createBy        |                    |      | false | string             |                    |
| &emsp;&emsp;createTime      |                    |      | false | string(date-time)  |                    |
| &emsp;&emsp;formDept        |                    |      | false | string             |                    |
| &emsp;&emsp;id              |                    |      | false | string             |                    |
| &emsp;&emsp;isDeleted       |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;reason          |                    |      | false | string             |                    |
| &emsp;&emsp;reply           |                    |      | false | string             |                    |
| &emsp;&emsp;status          |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;taskSource      |                    |      | false | integer(int32)     |                    |
| &emsp;&emsp;taskSys         |                    |      | false | string             |                    |
| &emsp;&emsp;updateBy        |                    |      | false | string             |                    |
| &emsp;&emsp;updateTime      |                    |      | false | string(date-time)  |                    |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 签收

**接口地址**:`/bbt-api/org/complaint/sign/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 导出

**接口地址**:`/bbt-api/org/complaint/export`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "complaintSource": 0,
  "current": 1,
  "size": 10,
  "status": 0,
  "userId": ""
}
```

**请求参数**:

| 参数名称                        | 参数说明      | 请求类型 | 是否必须  | 数据类型           | schema   |
|-----------------------------|-----------|------|-------|----------------|----------|
| 投诉反馈查询参数                    | 投诉反馈查询参数  | body | true  | 投诉反馈查询参数       | 投诉反馈查询参数 |
| &emsp;&emsp;complaintSource | 投诉来源      |      | false | integer(int32) |          |
| &emsp;&emsp;current         | 页码,默认为1   |      | false | integer(int32) |          |
| &emsp;&emsp;size            | 页大小,默认为10 |      | false | integer(int32) |          |
| &emsp;&emsp;status          | 投诉状态      |      | false | integer(int32) |          |
| &emsp;&emsp;userId          | 用户id      |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 文件bucket操作

## 设置Policy

**接口地址**:`/bbt-api/sys/file/bucket/policy`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "buckets": [
    {
      "name": "",
      "policy": ""
    }
  ]
}
```

**请求参数**:

| 参数名称                           | 参数说明               | 请求类型 | 是否必须  | 数据类型        | schema      |
|--------------------------------|--------------------|------|-------|-------------|-------------|
| bucketArray                    | BucketArray        | body | true  | BucketArray | BucketArray |
| &emsp;&emsp;buckets            |                    |      | false | array       | Bucket      |
| &emsp;&emsp;&emsp;&emsp;name   |                    |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;policy | 可用值:PRIVATE,PUBLIC |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 创建bucket

**接口地址**:`/bbt-api/sys/file/bucket/create`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "buckets": [
    {
      "name": "",
      "policy": ""
    }
  ]
}
```

**请求参数**:

| 参数名称                           | 参数说明               | 请求类型 | 是否必须  | 数据类型        | schema      |
|--------------------------------|--------------------|------|-------|-------------|-------------|
| bucketArray                    | BucketArray        | body | true  | BucketArray | BucketArray |
| &emsp;&emsp;buckets            |                    |      | false | array       | Bucket      |
| &emsp;&emsp;&emsp;&emsp;name   |                    |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;policy | 可用值:PRIVATE,PUBLIC |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 文件服务

## 获取二进制文件流

**接口地址**:`/bbt-api/sys/file/load`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>下载文件或者播放文件</p>

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| id   | id   | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 查询文件详情信息

**接口地址**:`/bbt-api/sys/file/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>查询文件信息获取地址</p>

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | FileInfoVO |

**响应参数**:

| 参数名称    | 参数说明  | 类型     | schema |
|---------|-------|--------|--------| 
| groupId | 文件组id | string |        |
| id      | 文件id  | string |        |
| name    | 文件名   | string |        |
| url     | 文件地址  | string |        |

**响应示例**:

```javascript
{
	"groupId": "",
	"id": "",
	"name": "",
	"url": ""
}
```

## 上传文件

**接口地址**:`/bbt-api/sys/file/upload`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,multipart/form-data`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称      | 参数说明      | 请求类型  | 是否必须 | 数据类型   | schema |
|-----------|-----------|-------|------|--------|--------|
| namespace | namespace | query | true | string |        |
| files     | files     | query | true | array  | string |

**响应状态**:

| 状态码 | 说明 | schema                 |
|-----|----|------------------------| 
| 200 | OK | Result«UploadResultVO» |

**响应参数**:

| 参数名称                            | 参数说明  | 类型             | schema         |
|---------------------------------|-------|----------------|----------------| 
| code                            |       | integer(int32) | integer(int32) |
| data                            |       | UploadResultVO | UploadResultVO |
| &emsp;&emsp;files               |       | array          | FileInfoVO     |
| &emsp;&emsp;&emsp;&emsp;groupId | 文件组id | string         |                |
| &emsp;&emsp;&emsp;&emsp;id      | 文件id  | string         |                |
| &emsp;&emsp;&emsp;&emsp;name    | 文件名   | string         |                |
| &emsp;&emsp;&emsp;&emsp;url     | 文件地址  | string         |                |
| &emsp;&emsp;groupId             |       | string         |                |
| message                         |       | string         |                |
| stack                           |       | string         |                |
| success                         |       | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"files": [
			{
				"groupId": "",
				"id": "",
				"name": "",
				"url": ""
			}
		],
		"groupId": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 单文件上传

**接口地址**:`/bbt-api/sys/file/singleUpload`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称      | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|-----------|------|-------|-------|--------|--------|
| file      | 文件   | query | true  | file   |        |
| namespace | 文件空间 | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | Result«FileInfoVO» |

**响应参数**:

| 参数名称                | 参数说明  | 类型             | schema         |
|---------------------|-------|----------------|----------------| 
| code                |       | integer(int32) | integer(int32) |
| data                |       | FileInfoVO     | FileInfoVO     |
| &emsp;&emsp;groupId | 文件组id | string         |                |
| &emsp;&emsp;id      | 文件id  | string         |                |
| &emsp;&emsp;name    | 文件名   | string         |                |
| &emsp;&emsp;url     | 文件地址  | string         |                |
| message             |       | string         |                |
| stack               |       | string         |                |
| success             |       | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"groupId": "",
		"id": "",
		"name": "",
		"url": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

# 标签

## 更新标签

**接口地址**:`/bbt-api/org/label/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "code": "",
  "hidden": false,
  "id": "",
  "name": "",
  "sort": 0,
  "typeId": ""
}
```

**请求参数**:

| 参数名称               | 参数说明   | 请求类型 | 是否必须  | 数据类型           | schema |
|--------------------|--------|------|-------|----------------|--------|
| 标签修改参数             | 标签修改参数 | body | true  | 标签修改参数         | 标签修改参数 |
| &emsp;&emsp;code   | 唯一编码   |      | false | string         |        |
| &emsp;&emsp;hidden | 是否隐藏   |      | false | boolean        |        |
| &emsp;&emsp;id     | 主键ID   |      | false | string         |        |
| &emsp;&emsp;name   | 标签名称   |      | false | string         |        |
| &emsp;&emsp;sort   | 排序     |      | false | integer(int32) |        |
| &emsp;&emsp;typeId | 类型ID   |      | false | string         |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除标签

**接口地址**:`/bbt-api/org/label/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 标签分页列表

**接口地址**:`/bbt-api/org/label/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型              | schema |
|---------|-----------|-------|-------|-------------------|--------|
| code    | 唯一编码      | query | false | string            |        |
| current | 页码,默认为1   | query | false | integer(int32)    |        |
| end     | 结束时间      | query | false | string(date-time) |        |
| hidden  | 是否隐藏      | query | false | boolean           |        |
| name    | 标签名称      | query | false | string            |        |
| size    | 页大小,默认为10 | query | false | integer(int32)    |        |
| sort    | 排序        | query | false | integer(int32)    |        |
| start   | 开始时间      | query | false | string(date-time) |        |
| typeId  | 类型ID      | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema          |
|-----|----|-----------------| 
| 200 | OK | 分页结果对象«LabelVO» |

**响应参数**:

| 参数名称                   | 参数说明  | 类型                | schema         |
|------------------------|-------|-------------------|----------------| 
| current                | 当前页码  | integer(int32)    | integer(int32) |
| pages                  | 总页数   | integer(int32)    | integer(int32) |
| records                | 数据列表  | array             | LabelVO        |
| &emsp;&emsp;code       | 唯一编码  | string            |                |
| &emsp;&emsp;createBy   | 创建者编号 | string            |                |
| &emsp;&emsp;createTime | 创建时间  | string(date-time) |                |
| &emsp;&emsp;hidden     | 是否隐藏  | boolean           |                |
| &emsp;&emsp;id         | 主键ID  | string            |                |
| &emsp;&emsp;name       | 标签名称  | string            |                |
| &emsp;&emsp;sort       | 排序    | integer(int32)    |                |
| &emsp;&emsp;typeId     | 类型ID  | string            |                |
| &emsp;&emsp;typeName   | 类型名称  | string            |                |
| &emsp;&emsp;updateBy   | 更新者编号 | string            |                |
| &emsp;&emsp;updateTime | 更新时间  | string(date-time) |                |
| size                   | 页大小   | integer(int32)    | integer(int32) |
| total                  | 总行数   | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"code": "",
			"createBy": "",
			"createTime": "",
			"hidden": false,
			"id": "",
			"name": "",
			"sort": 0,
			"typeId": "",
			"typeName": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 标签详情

**接口地址**:`/bbt-api/org/label/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema  |
|-----|----|---------| 
| 200 | OK | LabelVO |

**响应参数**:

| 参数名称       | 参数说明  | 类型                | schema            |
|------------|-------|-------------------|-------------------| 
| code       | 唯一编码  | string            |                   |
| createBy   | 创建者编号 | string            |                   |
| createTime | 创建时间  | string(date-time) | string(date-time) |
| hidden     | 是否隐藏  | boolean           |                   |
| id         | 主键ID  | string            |                   |
| name       | 标签名称  | string            |                   |
| sort       | 排序    | integer(int32)    | integer(int32)    |
| typeId     | 类型ID  | string            |                   |
| typeName   | 类型名称  | string            |                   |
| updateBy   | 更新者编号 | string            |                   |
| updateTime | 更新时间  | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"code": "",
	"createBy": "",
	"createTime": "",
	"hidden": false,
	"id": "",
	"name": "",
	"sort": 0,
	"typeId": "",
	"typeName": "",
	"updateBy": "",
	"updateTime": ""
}
```

## 新增标签

**接口地址**:`/bbt-api/org/label/add`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "code": "",
  "hidden": false,
  "name": "",
  "sort": 0,
  "typeId": ""
}
```

**请求参数**:

| 参数名称               | 参数说明   | 请求类型 | 是否必须  | 数据类型           | schema |
|--------------------|--------|------|-------|----------------|--------|
| 标签新增参数             | 标签新增参数 | body | true  | 标签新增参数         | 标签新增参数 |
| &emsp;&emsp;code   | 唯一编码   |      | false | string         |        |
| &emsp;&emsp;hidden | 是否隐藏   |      | false | boolean        |        |
| &emsp;&emsp;name   | 标签名称   |      | false | string         |        |
| &emsp;&emsp;sort   | 排序     |      | false | integer(int32) |        |
| &emsp;&emsp;typeId | 类型ID   |      | false | string         |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 标签Feign接口

## getByIds

**接口地址**:`/bbt-api/api/org/label/ids/{ids}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| ids  | ids  | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                |
|-----|----|-----------------------| 
| 200 | OK | Result«List«LabelAO»» |

**响应参数**:

| 参数名称                   | 参数说明 | 类型                | schema         |
|------------------------|------|-------------------|----------------| 
| code                   |      | integer(int32)    | integer(int32) |
| data                   |      | array             | LabelAO        |
| &emsp;&emsp;code       |      | string            |                |
| &emsp;&emsp;createBy   |      | string            |                |
| &emsp;&emsp;createTime |      | string(date-time) |                |
| &emsp;&emsp;hidden     |      | boolean           |                |
| &emsp;&emsp;id         |      | string            |                |
| &emsp;&emsp;name       |      | string            |                |
| &emsp;&emsp;sort       |      | integer(int32)    |                |
| &emsp;&emsp;typeId     |      | string            |                |
| &emsp;&emsp;updateBy   |      | string            |                |
| &emsp;&emsp;updateTime |      | string(date-time) |                |
| message                |      | string            |                |
| stack                  |      | string            |                |
| success                |      | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"code": "",
			"createBy": "",
			"createTime": "",
			"hidden": true,
			"id": "",
			"name": "",
			"sort": 0,
			"typeId": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 标签详情

**接口地址**:`/bbt-api/api/org/label/id/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema          |
|-----|----|-----------------| 
| 200 | OK | Result«LabelAO» |

**响应参数**:

| 参数名称                   | 参数说明 | 类型                | schema         |
|------------------------|------|-------------------|----------------| 
| code                   |      | integer(int32)    | integer(int32) |
| data                   |      | LabelAO           | LabelAO        |
| &emsp;&emsp;code       |      | string            |                |
| &emsp;&emsp;createBy   |      | string            |                |
| &emsp;&emsp;createTime |      | string(date-time) |                |
| &emsp;&emsp;hidden     |      | boolean           |                |
| &emsp;&emsp;id         |      | string            |                |
| &emsp;&emsp;name       |      | string            |                |
| &emsp;&emsp;sort       |      | integer(int32)    |                |
| &emsp;&emsp;typeId     |      | string            |                |
| &emsp;&emsp;updateBy   |      | string            |                |
| &emsp;&emsp;updateTime |      | string(date-time) |                |
| message                |      | string            |                |
| stack                  |      | string            |                |
| success                |      | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"code": "",
		"createBy": "",
		"createTime": "",
		"hidden": true,
		"id": "",
		"name": "",
		"sort": 0,
		"typeId": "",
		"updateBy": "",
		"updateTime": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

# 标签分类

## 删除标签分类

**接口地址**:`/bbt-api/org/label/type/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 新增标签分类

**接口地址**:`/bbt-api/org/label/type/add`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "hidden": false,
  "name": "",
  "parentId": "",
  "sort": 0
}
```

**请求参数**:

| 参数名称                 | 参数说明     | 请求类型 | 是否必须  | 数据类型           | schema   |
|----------------------|----------|------|-------|----------------|----------|
| 标签分类新增参数             | 标签分类新增参数 | body | true  | 标签分类新增参数       | 标签分类新增参数 |
| &emsp;&emsp;hidden   | 是否隐藏     |      | false | boolean        |          |
| &emsp;&emsp;name     | 类型名称     |      | false | string         |          |
| &emsp;&emsp;parentId | 父级id     |      | false | string         |          |
| &emsp;&emsp;sort     | 排序       |      | false | integer(int32) |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 获取本级分类已存在的标签分类的个数

**接口地址**:`/bbt-api/org/label/type/{parentId}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明     | 请求类型 | 是否必须 | 数据类型   | schema |
|----------|----------|------|------|--------|--------|
| parentId | parentId | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 标签分类列表

**接口地址**:`/bbt-api/org/label/type/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| name | name | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 标签分类分页列表

**接口地址**:`/bbt-api/org/label/type/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称      | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|-----------|-----------|-------|-------|----------------|--------|
| current   | 页码,默认为1   | query | false | integer(int32) |        |
| labelName | 标签名称      | query | false | string         |        |
| size      | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | 分页结果对象«LabelTypeVO» |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema         |
|------------------------------------|-------|-------------------|----------------| 
| current                            | 当前页码  | integer(int32)    | integer(int32) |
| pages                              | 总页数   | integer(int32)    | integer(int32) |
| records                            | 数据列表  | array             | LabelTypeVO    |
| &emsp;&emsp;createBy               | 创建者编号 | string            |                |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                |
| &emsp;&emsp;hidden                 | 是否隐藏  | boolean           |                |
| &emsp;&emsp;id                     | 主键ID  | string            |                |
| &emsp;&emsp;labels                 | 标签列表  | array             | LabelVO        |
| &emsp;&emsp;&emsp;&emsp;code       | 唯一编码  | string            |                |
| &emsp;&emsp;&emsp;&emsp;createBy   | 创建者编号 | string            |                |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                |
| &emsp;&emsp;&emsp;&emsp;hidden     | 是否隐藏  | boolean           |                |
| &emsp;&emsp;&emsp;&emsp;id         | 主键ID  | string            |                |
| &emsp;&emsp;&emsp;&emsp;name       | 标签名称  | string            |                |
| &emsp;&emsp;&emsp;&emsp;sort       | 排序    | integer           |                |
| &emsp;&emsp;&emsp;&emsp;typeId     | 类型ID  | string            |                |
| &emsp;&emsp;&emsp;&emsp;typeName   | 类型名称  | string            |                |
| &emsp;&emsp;&emsp;&emsp;updateBy   | 更新者编号 | string            |                |
| &emsp;&emsp;&emsp;&emsp;updateTime | 更新时间  | string            |                |
| &emsp;&emsp;name                   | 类型名称  | string            |                |
| &emsp;&emsp;parentId               | 父级id  | string            |                |
| &emsp;&emsp;sort                   | 排序    | integer(int32)    |                |
| &emsp;&emsp;subTypes               | 子类型列表 | array             | LabelTypeVO    |
| &emsp;&emsp;updateBy               | 更新者编号 | string            |                |
| &emsp;&emsp;updateTime             | 更新时间  | string(date-time) |                |
| size                               | 页大小   | integer(int32)    | integer(int32) |
| total                              | 总行数   | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"createBy": "",
			"createTime": "",
			"hidden": false,
			"id": "",
			"labels": [
				{
					"code": "",
					"createBy": "",
					"createTime": "",
					"hidden": false,
					"id": "",
					"name": "",
					"sort": 0,
					"typeId": "",
					"typeName": "",
					"updateBy": "",
					"updateTime": ""
				}
			],
			"name": "",
			"parentId": "",
			"sort": 0,
			"subTypes": [
				{}
			],
			"updateBy": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 更新标签分类

**接口地址**:`/bbt-api/org/label/type/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "hidden": false,
  "id": "",
  "name": "",
  "parentId": "",
  "sort": 0
}
```

**请求参数**:

| 参数名称                 | 参数说明     | 请求类型 | 是否必须  | 数据类型           | schema   |
|----------------------|----------|------|-------|----------------|----------|
| 标签分类修改参数             | 标签分类修改参数 | body | true  | 标签分类修改参数       | 标签分类修改参数 |
| &emsp;&emsp;hidden   | 是否隐藏     |      | false | boolean        |          |
| &emsp;&emsp;id       | 主键ID     |      | false | string         |          |
| &emsp;&emsp;name     | 类型名称     |      | false | string         |          |
| &emsp;&emsp;parentId | 父级id     |      | false | string         |          |
| &emsp;&emsp;sort     | 排序       |      | false | integer(int32) |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 标签分类详情

**接口地址**:`/bbt-api/org/label/type/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | LabelTypeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| createBy                           | 创建者编号 | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| hidden                             | 是否隐藏  | boolean           |                   |
| id                                 | 主键ID  | string            |                   |
| labels                             | 标签列表  | array             | LabelVO           |
| &emsp;&emsp;code                   | 唯一编码  | string            |                   |
| &emsp;&emsp;createBy               | 创建者编号 | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;hidden                 | 是否隐藏  | boolean           |                   |
| &emsp;&emsp;id                     | 主键ID  | string            |                   |
| &emsp;&emsp;name                   | 标签名称  | string            |                   |
| &emsp;&emsp;sort                   | 排序    | integer(int32)    |                   |
| &emsp;&emsp;typeId                 | 类型ID  | string            |                   |
| &emsp;&emsp;typeName               | 类型名称  | string            |                   |
| &emsp;&emsp;updateBy               | 更新者编号 | string            |                   |
| &emsp;&emsp;updateTime             | 更新时间  | string(date-time) |                   |
| name                               | 类型名称  | string            |                   |
| parentId                           | 父级id  | string            |                   |
| sort                               | 排序    | integer(int32)    | integer(int32)    |
| subTypes                           | 子类型列表 | array             | LabelTypeVO       |
| &emsp;&emsp;createBy               | 创建者编号 | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;hidden                 | 是否隐藏  | boolean           |                   |
| &emsp;&emsp;id                     | 主键ID  | string            |                   |
| &emsp;&emsp;labels                 | 标签列表  | array             | LabelVO           |
| &emsp;&emsp;&emsp;&emsp;code       | 唯一编码  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createBy   | 创建者编号 | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;hidden     | 是否隐藏  | boolean           |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键ID  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 标签名称  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 排序    | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;typeId     | 类型ID  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;typeName   | 类型名称  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;updateBy   | 更新者编号 | string            |                   |
| &emsp;&emsp;&emsp;&emsp;updateTime | 更新时间  | string            |                   |
| &emsp;&emsp;name                   | 类型名称  | string            |                   |
| &emsp;&emsp;parentId               | 父级id  | string            |                   |
| &emsp;&emsp;sort                   | 排序    | integer(int32)    |                   |
| &emsp;&emsp;subTypes               | 子类型列表 | array             | LabelTypeVO       |
| &emsp;&emsp;updateBy               | 更新者编号 | string            |                   |
| &emsp;&emsp;updateTime             | 更新时间  | string(date-time) |                   |
| updateBy                           | 更新者编号 | string            |                   |
| updateTime                         | 更新时间  | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"createBy": "",
	"createTime": "",
	"hidden": false,
	"id": "",
	"labels": [
		{
			"code": "",
			"createBy": "",
			"createTime": "",
			"hidden": false,
			"id": "",
			"name": "",
			"sort": 0,
			"typeId": "",
			"typeName": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"name": "",
	"parentId": "",
	"sort": 0,
	"subTypes": [
		{
			"createBy": "",
			"createTime": "",
			"hidden": false,
			"id": "",
			"labels": [
				{
					"code": "",
					"createBy": "",
					"createTime": "",
					"hidden": false,
					"id": "",
					"name": "",
					"sort": 0,
					"typeId": "",
					"typeName": "",
					"updateBy": "",
					"updateTime": ""
				}
			],
			"name": "",
			"parentId": "",
			"sort": 0,
			"subTypes": [
				{}
			],
			"updateBy": "",
			"updateTime": ""
		}
	],
	"updateBy": "",
	"updateTime": ""
}
```

## 标签分类树形

**接口地址**:`/bbt-api/org/label/type/tree`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明        | 请求类型  | 是否必须  | 数据类型   | schema |
|-------------|-------------|-------|-------|--------|--------|
| labelName   | labelName   | query | false | string |        |
| typeHidden  | typeHidden  | query | false | string |        |
| labelHidden | labelHidden | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 标签分类树形

**接口地址**:`/bbt-api/org/label/type/treeByType`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明        | 请求类型  | 是否必须  | 数据类型   | schema |
|-------------|-------------|-------|-------|--------|--------|
| typeName    | typeName    | query | true  | string |        |
| typeHidden  | typeHidden  | query | false | string |        |
| labelHidden | labelHidden | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 浏览记录

## 未读统计-浏览记录

**接口地址**:`/bbt-api/sys/view/record/getCount/{type}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型           | schema |
|------|------|------|------|----------------|--------|
| type | type | path | true | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema       |
|-----|----|--------------| 
| 200 | OK | Result«long» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | integer(int64) | integer(int64) |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": 0,
	"message": "",
	"stack": "",
	"success": true
}
```

## 新增浏览记录

**接口地址**:`/bbt-api/sys/view/record`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "referenceId": "",
  "type": 0,
  "userId": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明                | 请求类型 | 是否必须  | 数据类型           | schema   |
|-------------------------|---------------------|------|-------|----------------|----------|
| 浏览记录新增参数                | 浏览记录新增参数            | body | true  | 浏览记录新增参数       | 浏览记录新增参数 |
| &emsp;&emsp;referenceId | 关联id                |      | false | string         |          |
| &emsp;&emsp;type        | 记录类型(0 通知公告,1 消息提醒) |      | false | integer(int32) |          |
| &emsp;&emsp;userId      | 用户id                |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除浏览记录

**接口地址**:`/bbt-api/sys/view/record/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更新浏览记录

**接口地址**:`/bbt-api/sys/view/record/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "referenceId": "",
  "type": 0,
  "userId": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明                | 请求类型 | 是否必须  | 数据类型           | schema   |
|-------------------------|---------------------|------|-------|----------------|----------|
| 浏览记录修改参数                | 浏览记录修改参数            | body | true  | 浏览记录修改参数       | 浏览记录修改参数 |
| &emsp;&emsp;id          |                     |      | false | string         |          |
| &emsp;&emsp;referenceId | 关联id                |      | false | string         |          |
| &emsp;&emsp;type        | 记录类型(0 通知公告,1 消息提醒) |      | false | integer(int32) |          |
| &emsp;&emsp;userId      | 用户id                |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 浏览记录分页列表

**接口地址**:`/bbt-api/sys/view/record/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明                | 请求类型  | 是否必须  | 数据类型              | schema |
|-------------|---------------------|-------|-------|-------------------|--------|
| current     | 页码,默认为1             | query | false | integer(int32)    |        |
| end         | 结束时间                | query | false | string(date-time) |        |
| referenceId | 关联id                | query | false | string            |        |
| size        | 页大小,默认为10           | query | false | integer(int32)    |        |
| start       | 开始时间                | query | false | string(date-time) |        |
| type        | 记录类型(0 通知公告,1 消息提醒) | query | false | integer(int32)    |        |
| userId      | 用户id                | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema                  |
|-----|----|-------------------------| 
| 200 | OK | 分页结果对象«SysViewRecordVO» |

**响应参数**:

| 参数名称                    | 参数说明                | 类型                | schema          |
|-------------------------|---------------------|-------------------|-----------------| 
| current                 | 当前页码                | integer(int32)    | integer(int32)  |
| pages                   | 总页数                 | integer(int32)    | integer(int32)  |
| records                 | 数据列表                | array             | SysViewRecordVO |
| &emsp;&emsp;createTime  | 创建时间                | string(date-time) |                 |
| &emsp;&emsp;id          |                     | string            |                 |
| &emsp;&emsp;referenceId | 关联id                | string            |                 |
| &emsp;&emsp;type        | 记录类型(0 通知公告,1 消息提醒) | integer(int32)    |                 |
| &emsp;&emsp;userId      | 用户id                | string            |                 |
| size                    | 页大小                 | integer(int32)    | integer(int32)  |
| total                   | 总行数                 | integer(int32)    | integer(int32)  |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"createTime": "",
			"id": "",
			"referenceId": "",
			"type": 0,
			"userId": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 浏览记录详情

**接口地址**:`/bbt-api/sys/view/record/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema          |
|-----|----|-----------------| 
| 200 | OK | SysViewRecordVO |

**响应参数**:

| 参数名称        | 参数说明                | 类型                | schema            |
|-------------|---------------------|-------------------|-------------------| 
| createTime  | 创建时间                | string(date-time) | string(date-time) |
| id          |                     | string            |                   |
| referenceId | 关联id                | string            |                   |
| type        | 记录类型(0 通知公告,1 消息提醒) | integer(int32)    | integer(int32)    |
| userId      | 用户id                | string            |                   |

**响应示例**:

```javascript
{
	"createTime": "",
	"id": "",
	"referenceId": "",
	"type": 0,
	"userId": ""
}
```

# 用户-登录

## 登录

**接口地址**:`/bbt-api/org/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>系统用户登录</p>

**请求示例**:

```javascript
{
  "deptCodes": [],
  "roleIds": [
    {}
  ],
  "roleNames": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明 | 请求类型 | 是否必须  | 数据类型  | schema       |
|-----------------------|------|------|-------|-------|--------------|
| 登录参数                  | 登录参数 | body | true  | 登录参数  | 登录参数         |
| &emsp;&emsp;deptCodes |      |      | false | array | string       |
| &emsp;&emsp;roleIds   |      |      | false | array | Serializable |
| &emsp;&emsp;roleNames |      |      | false | array | Serializable |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 大联动登录

**接口地址**:`/bbt-api/org/linkage/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>大联动用户登录</p>

**请求示例**:

```javascript
{
  "userid": ""
}
```

**请求参数**:

| 参数名称                 | 参数说明                 | 请求类型 | 是否必须  | 数据类型                 | schema               |
|----------------------|----------------------|------|-------|----------------------|----------------------|
| bigLinkAgeLoginParam | BigLinkAgeLoginParam | body | true  | BigLinkAgeLoginParam | BigLinkAgeLoginParam |
| &emsp;&emsp;userid   | userid               |      | false | string               |                      |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 获取短信验证码

**接口地址**:`/bbt-api/org/getSmsCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明                            | 请求类型  | 是否必须 | 数据类型   | schema |
|-------------|---------------------------------|-------|------|--------|--------|
| phone       | phone                           | query | true | string |        |
| verifyToken | verifyToken                     | query | true | string |        |
| code        | code                            | query | true | string |        |
| verifyType  | verifyType,可用值:LOGIN,UPDATE_PWD | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 钉钉登录

**接口地址**:`/bbt-api/org/dingtalk/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>钉钉登录</p>

**请求示例**:

```javascript
{
  "code": ""
}
```

**请求参数**:

| 参数名称               | 参数说明               | 请求类型 | 是否必须  | 数据类型               | schema             |
|--------------------|--------------------|------|-------|--------------------|--------------------|
| dingtalkLoginParam | DingtalkLoginParam | body | true  | DingtalkLoginParam | DingtalkLoginParam |
| &emsp;&emsp;code   | code               |      | false | string             |                    |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 获取验证码

**接口地址**:`/bbt-api/org/verify`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称      | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|-----------|-----------|-------|-------|----------------|--------|
| width     | width     | query | false | integer(int32) |        |
| height    | height    | query | false | integer(int32) |        |
| codeCount | codeCount | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema        |
|-----|----|---------------| 
| 200 | OK | LoginVerifyVO |

**响应参数**:

| 参数名称        | 参数说明 | 类型     | schema |
|-------------|------|--------|--------| 
| image       | 图片   | string |        |
| verifyToken | 图片令牌 | string |        |

**响应示例**:

```javascript
{
	"image": "",
	"verifyToken": ""
}
```

## 第三方登录统一接口

**接口地址**:`/bbt-api/org/third/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>第三方登录统一接口</p>

**请求示例**:

```javascript
{
  "loginType": "",
  "param": {}
}
```

**请求参数**:

| 参数名称                  | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------------|-----------------|------|-------|-----------------|-----------------|
| thirdLoginParam       | ThirdLoginParam | body | true  | ThirdLoginParam | ThirdLoginParam |
| &emsp;&emsp;loginType | loginType,登录方式  |      | false | string          |                 |
| &emsp;&emsp;param     | param第三方登录参数    |      | false | object          |                 |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 登出

**接口地址**:`/bbt-api/org/logout`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 检查验证码

**接口地址**:`/bbt-api/org/checkToken`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称  | 参数说明  | 请求类型  | 是否必须  | 数据类型   | schema |
|-------|-------|-------|-------|--------|--------|
| token | token | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 获取签名

**接口地址**:`/bbt-api/org/sign`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 事件中枢登录

**接口地址**:`/bbt-api/org/event/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>事件中枢登录</p>

**请求示例**:

```javascript
{
  "token": ""
}
```

**请求参数**:

| 参数名称              | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-------------------|-----------------|------|-------|-----------------|-----------------|
| eventLoginParam   | EventLoginParam | body | true  | EventLoginParam | EventLoginParam |
| &emsp;&emsp;token | token           |      | false | string          |                 |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 天府Oauth登录

**接口地址**:`/bbt-api/org/tfoauth/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>天府Oauth登录</p>

**请求示例**:

```javascript
{
  "code": ""
}
```

**请求参数**:

| 参数名称              | 参数说明              | 请求类型 | 是否必须  | 数据类型              | schema            |
|-------------------|-------------------|------|-------|-------------------|-------------------|
| tfLoginLoginParam | TfLoginLoginParam | body | true  | TfLoginLoginParam | TfLoginLoginParam |
| &emsp;&emsp;code  | code              |      | false | string            |                   |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 选择部门

**接口地址**:`/bbt-api/org/choiceDept`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>选择部门</p>

**请求示例**:

```javascript
{
  "departmentId": {}
}
```

**请求参数**:

| 参数名称                     | 参数说明                  | 请求类型 | 是否必须 | 数据类型                  | schema                |
|--------------------------|-----------------------|------|------|-----------------------|-----------------------|
| choiceDepartmentParam    | ChoiceDepartmentParam | body | true | ChoiceDepartmentParam | ChoiceDepartmentParam |
| &emsp;&emsp;departmentId | 部门id                  |      | true | Serializable          | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 用户-系统用户

## writeoff系统用户

**接口地址**:`/bbt-api/org/user/writeoff`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 重置密码

**接口地址**:`/bbt-api/org/user/resetPassword`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "pwd": ""
}
```

**请求参数**:

| 参数名称            | 参数说明       | 请求类型 | 是否必须  | 数据类型       | schema     |
|-----------------|------------|------|-------|------------|------------|
| 系统用户重置密码参数      | 系统用户重置密码参数 | body | true  | 系统用户重置密码参数 | 系统用户重置密码参数 |
| &emsp;&emsp;id  | 主键         |      | false | string     |            |
| &emsp;&emsp;pwd | 密码         |      | false | string     |            |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更新个人资料

**接口地址**:`/bbt-api/org/user/updateUserInfo`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "account": "",
  "description": "",
  "email": "",
  "gender": "",
  "head": {
    "groupId": "",
    "id": "",
    "name": "",
    "url": ""
  },
  "phone": "",
  "realname": ""
}
```

**请求参数**:

| 参数名称                            | 参数说明                       | 请求类型 | 是否必须  | 数据类型        | schema      |
|---------------------------------|----------------------------|------|-------|-------------|-------------|
| 个人中心用户修改参数                      | 个人中心用户修改参数                 | body | true  | 个人中心用户修改参数  | 个人中心用户修改参数  |
| &emsp;&emsp;account             | 账号                         |      | false | string      |             |
| &emsp;&emsp;description         | 介绍                         |      | false | string      |             |
| &emsp;&emsp;email               | 邮箱                         |      | false | string      |             |
| &emsp;&emsp;gender              | 性别,可用值:FEMALE,MALE,UNKNOWN |      | false | string      |             |
| &emsp;&emsp;head                | 头像ID（需要更新头像时，传空）           |      | false | HeaderImage | HeaderImage |
| &emsp;&emsp;&emsp;&emsp;groupId |                            |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;id      |                            |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;name    |                            |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;url     |                            |      | false | string      |             |
| &emsp;&emsp;phone               | 手机号码                       |      | false | string      |             |
| &emsp;&emsp;realname            | 真实姓名                       |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 忘记密码

**接口地址**:`/bbt-api/org/user/forgetPassword`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "code": "",
  "phone": "",
  "pwd": "",
  "pwd2": ""
}
```

**请求参数**:

| 参数名称               | 参数说明               | 请求类型 | 是否必须  | 数据类型               | schema             |
|--------------------|--------------------|------|-------|--------------------|--------------------|
| userForgetPwdParam | UserForgetPwdParam | body | true  | UserForgetPwdParam | UserForgetPwdParam |
| &emsp;&emsp;code   | 验证码                |      | false | string             |                    |
| &emsp;&emsp;phone  | 手机号码               |      | false | string             |                    |
| &emsp;&emsp;pwd    | 密码                 |      | false | string             |                    |
| &emsp;&emsp;pwd2   | 再次输入密码             |      | false | string             |                    |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 通过账号获取用户

**接口地址**:`/bbt-api/org/user/account/{account}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型 | 是否必须 | 数据类型   | schema |
|---------|---------|------|------|--------|--------|
| account | account | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | UserBaseInfoVO |

**响应参数**:

| 参数名称      | 参数说明                                             | 类型                | schema            |
|-----------|--------------------------------------------------|-------------------|-------------------| 
| account   | 用户名                                              | string            |                   |
| admin     | 管理员                                              | boolean           |                   |
| email     | 邮箱                                               | string            |                   |
| gender    | 性别,可用值:FEMALE,MALE,UNKNOWN                       | string            |                   |
| head      | 头像                                               | string            |                   |
| id        | 主键                                               | string            |                   |
| lastLogin | 最后登录时间                                           | string(date-time) | string(date-time) |
| phone     | 手机号码                                             | string            |                   |
| realname  | 真实姓名                                             | string            |                   |
| roleName  | 角色名称                                             | string            |                   |
| state     | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |

**响应示例**:

```javascript
{
	"account": "",
	"admin": false,
	"email": "",
	"gender": "",
	"head": "",
	"id": "",
	"lastLogin": "",
	"phone": "",
	"realname": "",
	"roleName": "",
	"state": ""
}
```

## 用户分页列表

**接口地址**:`/bbt-api/org/user/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|---------|-----------|-------|-------|----------------|--------|
| current | 页码,默认为1   | query | false | integer(int32) |        |
| keyword | 搜索关键字     | query | false | string         |        |
| size    | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«分页结果对象«UserPageVO»» |

**响应参数**:

| 参数名称                               | 参数说明                                             | 类型                 | schema             |
|------------------------------------|--------------------------------------------------|--------------------|--------------------| 
| code                               |                                                  | integer(int32)     | integer(int32)     |
| data                               |                                                  | 分页结果对象«UserPageVO» | 分页结果对象«UserPageVO» |
| &emsp;&emsp;current                | 当前页码                                             | integer(int32)     |                    |
| &emsp;&emsp;pages                  | 总页数                                              | integer(int32)     |                    |
| &emsp;&emsp;records                | 数据列表                                             | array              | UserPageVO         |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名                                              | string             |                    |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间                                             | string             |                    |
| &emsp;&emsp;&emsp;&emsp;department | 部门                                               | string             |                    |
| &emsp;&emsp;&emsp;&emsp;id         | 主键                                               | string             |                    |
| &emsp;&emsp;&emsp;&emsp;identify   | 身份证号                                             | string             |                    |
| &emsp;&emsp;&emsp;&emsp;phone      | 联系人                                              | string             |                    |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名                                             | string             |                    |
| &emsp;&emsp;&emsp;&emsp;region     | 区域                                               | string             |                    |
| &emsp;&emsp;&emsp;&emsp;role       | 角色                                               | string             |                    |
| &emsp;&emsp;&emsp;&emsp;sort       | 排序                                               | integer            |                    |
| &emsp;&emsp;&emsp;&emsp;state      | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string             |                    |
| &emsp;&emsp;size                   | 页大小                                              | integer(int32)     |                    |
| &emsp;&emsp;total                  | 总行数                                              | integer(int32)     |                    |
| message                            |                                                  | string             |                    |
| stack                              |                                                  | string             |                    |
| success                            |                                                  | boolean            |                    |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"account": "",
				"createTime": "",
				"department": "",
				"id": "",
				"identify": "",
				"phone": "",
				"realname": "",
				"region": "",
				"role": "",
				"sort": 0,
				"state": ""
			}
		],
		"size": 0,
		"total": 0
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 用户列表(模糊匹配)

**接口地址**:`/bbt-api/org/user/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须  | 数据类型   | schema |
|---------|---------|-------|-------|--------|--------|
| keyword | keyword | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | UserListVO |

**响应参数**:

| 参数名称           | 参数说明                                | 类型                | schema            |
|----------------|-------------------------------------|-------------------|-------------------| 
| account        | 用户名                                 | string            |                   |
| createBy       | 创建者                                 | string            |                   |
| createTime     | 创建时间                                | string(date-time) | string(date-time) |
| departmentName | 部门名称                                | string            |                   |
| id             | 主键                                  | string            |                   |
| phone          | 手机号码                                | string            |                   |
| realname       | 真实姓名                                | string            |                   |
| sort           | 用户排序                                | integer(int32)    | integer(int32)    |
| state          | 用户状态,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |

**响应示例**:

```javascript
[
	{
		"account": "",
		"createBy": "",
		"createTime": "",
		"departmentName": "",
		"id": "",
		"phone": "",
		"realname": "",
		"sort": 0,
		"state": ""
	}
]
```

## 获取重置密码默认密码

**接口地址**:`/bbt-api/org/user/getDefaultPwd`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| id   | id   | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 第一次登录强制修改密码

**接口地址**:`/bbt-api/org/user/force/changePwd`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "account": "",
  "pwdNew": "",
  "pwdOld": ""
}
```

**请求参数**:

| 参数名称                | 参数说明         | 请求类型 | 是否必须  | 数据类型         | schema       |
|---------------------|--------------|------|-------|--------------|--------------|
| 个人中心用户密码修改参数        | 个人中心用户密码修改参数 | body | true  | 个人中心用户密码修改参数 | 个人中心用户密码修改参数 |
| &emsp;&emsp;account | 用户账号         |      | false | string       |              |
| &emsp;&emsp;pwdNew  | 新密码          |      | false | string       |              |
| &emsp;&emsp;pwdOld  | 旧密码          |      | false | string       |              |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 批量修改用户区域

**接口地址**:`/bbt-api/org/user/regionUpdate`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明       | 请求类型  | 是否必须  | 数据类型   | schema |
|------------|------------|-------|-------|--------|--------|
| regionName | regionName | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更新系统用户

**接口地址**:`/bbt-api/org/user/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "account": "",
  "confirmPwd": "",
  "departmentIds": [],
  "id": "",
  "identify": "",
  "phone": "",
  "pwd": "",
  "realname": "",
  "regionCode": "",
  "roleIds": [],
  "salt": "",
  "sort": 0
}
```

**请求参数**:

| 参数名称                      | 参数说明     | 请求类型 | 是否必须  | 数据类型           | schema   |
|---------------------------|----------|------|-------|----------------|----------|
| 系统用户修改参数                  | 系统用户修改参数 | body | true  | 系统用户修改参数       | 系统用户修改参数 |
| &emsp;&emsp;account       | 用户名      |      | false | string         |          |
| &emsp;&emsp;confirmPwd    | 确认密码     |      | false | string         |          |
| &emsp;&emsp;departmentIds | 部门id     |      | false | array          | string   |
| &emsp;&emsp;id            | 主键       |      | false | string         |          |
| &emsp;&emsp;identify      | 证件号码     |      | false | string         |          |
| &emsp;&emsp;phone         | 手机号码     |      | false | string         |          |
| &emsp;&emsp;pwd           | 密码       |      | false | string         |          |
| &emsp;&emsp;realname      | 真实姓名     |      | false | string         |          |
| &emsp;&emsp;regionCode    | 行政区域编码   |      | false | string         |          |
| &emsp;&emsp;roleIds       | 角色id     |      | false | array          | string   |
| &emsp;&emsp;salt          | 盐值       |      | false | string         |          |
| &emsp;&emsp;sort          | 用户排序     |      | false | integer(int32) |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 系统用户详情

**接口地址**:`/bbt-api/org/user/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema       |
|-----|----|--------------| 
| 200 | OK | UserDetailVO |

**响应参数**:

| 参数名称          | 参数说明   | 类型                | schema            |
|---------------|--------|-------------------|-------------------| 
| account       | 用户账号   | string            |                   |
| address       | 详细地址   | string            |                   |
| createTime    | 创建时间   | string(date-time) | string(date-time) |
| departmentIds | 部门id列表 | array             |                   |
| id            | 主键     | string            |                   |
| identify      | 身份证号   | string            |                   |
| phone         | 手机号码   | string            |                   |
| realname      | 真实姓名   | string            |                   |
| regionCode    | 行政区域   | string            |                   |
| roleIds       | 角色id列表 | array             |                   |
| sort          | 排序     | integer(int32)    | integer(int32)    |

**响应示例**:

```javascript
{
	"account": "",
	"address": "",
	"createTime": "",
	"departmentIds": [],
	"id": "",
	"identify": "",
	"phone": "",
	"realname": "",
	"regionCode": "",
	"roleIds": [],
	"sort": 0
}
```

## 获取当前登录用户信息

**接口地址**:`/bbt-api/org/user`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>用户信息包括用户菜单, 基本信息</p>

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema        |
|-----|----|---------------| 
| 200 | OK | UserSessionVO |

**响应参数**:

| 参数名称                   | 参数说明                                             | 类型                | schema            |
|------------------------|--------------------------------------------------|-------------------|-------------------| 
| account                | 用户名                                              | string            |                   |
| address                | 详细地址                                             | string            |                   |
| admin                  | 是否是机构管理员                                         | boolean           |                   |
| createBy               | 创建者                                              | string            |                   |
| createTime             | 创建时间                                             | string(date-time) | string(date-time) |
| department             | 部门名称                                             | string            |                   |
| departmentInfo         | 部门id                                             | DepartmentBaseVO  | DepartmentBaseVO  |
| &emsp;&emsp;address    | 详细地址                                             | string            |                   |
| &emsp;&emsp;createTime | 创建时间                                             | string(date-time) |                   |
| &emsp;&emsp;deptCode   | 部门编号                                             | string            |                   |
| &emsp;&emsp;deptName   | 部门名称                                             | string            |                   |
| &emsp;&emsp;id         | 主键                                               | string            |                   |
| &emsp;&emsp;level      | 部门层级                                             | integer(int32)    |                   |
| &emsp;&emsp;sort       | 部门排序                                             | integer(int32)    |                   |
| departments            | 所属部门信息                                           | array             | DepartmentVO      |
| &emsp;&emsp;address    | 详细地址                                             | string            |                   |
| &emsp;&emsp;adminId    | 管理员                                              | string            |                   |
| &emsp;&emsp;createBy   | 创建者                                              | string            |                   |
| &emsp;&emsp;createTime | 创建时间                                             | string(date-time) |                   |
| &emsp;&emsp;deptCode   | 部门编号                                             | string            |                   |
| &emsp;&emsp;deptName   | 部门名称                                             | string            |                   |
| &emsp;&emsp;id         | 主键                                               | string            |                   |
| &emsp;&emsp;level      | 部门层级                                             | integer(int32)    |                   |
| &emsp;&emsp;sort       | 部门排序                                             | integer(int32)    |                   |
| &emsp;&emsp;tel        | 联系方式                                             | string            |                   |
| &emsp;&emsp;updateBy   | 修改者                                              | string            |                   |
| &emsp;&emsp;updateTime | 修改时间                                             | string(date-time) |                   |
| description            | 介绍                                               | string            |                   |
| email                  | 邮箱                                               | string            |                   |
| gender                 | 性别，0：保密, 1：男，2：女，默认0,可用值:FEMALE,MALE,UNKNOWN     | string            |                   |
| head                   | 头像ID                                             | string            |                   |
| id                     | 主键                                               | string            |                   |
| lastLogin              | 最后登录时间                                           | string(date-time) | string(date-time) |
| loginCount             | 登录次数                                             | integer(int64)    | integer(int64)    |
| menus                  | 用户菜单                                             | array             | MenuVO            |
| &emsp;&emsp;children   | 子集                                               | array             | MenuVO            |
| &emsp;&emsp;component  | 目标组件                                             | string            |                   |
| &emsp;&emsp;hidden     | 显示和隐藏，0：显示，1：隐藏                                  | boolean           |                   |
| &emsp;&emsp;icon       | 前端图标                                             | string            |                   |
| &emsp;&emsp;id         | ID                                               | string            |                   |
| &emsp;&emsp;level      | 菜单级数                                             | integer(int32)    |                   |
| &emsp;&emsp;meta       | 菜单配置                                             | object            |                   |
| &emsp;&emsp;name       | 前端名称                                             | string            |                   |
| &emsp;&emsp;parentId   | 父级ID                                             | integer(int64)    |                   |
| &emsp;&emsp;path       | 前端路由                                             | string            |                   |
| &emsp;&emsp;roles      |                                                  | array             | string            |
| &emsp;&emsp;state      | 菜单状态，0：禁用，1：启用                                   | boolean           |                   |
| &emsp;&emsp;title      | controller名称                                     | string            |                   |
| phone                  | 手机号码                                             | string            |                   |
| realname               | 真实姓名                                             | string            |                   |
| roleIds                | 角色id列表                                           | array             |                   |
| roleName               | 角色名称                                             | string            |                   |
| state                  | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |
| updateBy               | 修改者                                              | string            |                   |
| updateTime             | 修改时间                                             | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"account": "",
	"address": "",
	"admin": false,
	"createBy": "",
	"createTime": "",
	"department": "",
	"departmentInfo": {
		"address": "",
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"level": 0,
		"sort": 0
	},
	"departments": [
		{
			"address": "",
			"adminId": "",
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"id": "",
			"level": 0,
			"sort": 0,
			"tel": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"description": "",
	"email": "",
	"gender": "",
	"head": "",
	"id": "",
	"lastLogin": "",
	"loginCount": 0,
	"menus": [
		{
			"children": [
				{
					"children": [
						{}
					],
					"component": "",
					"hidden": false,
					"icon": "",
					"id": "",
					"level": 0,
					"meta": {},
					"name": "",
					"parentId": 0,
					"path": "",
					"roles": [],
					"state": false,
					"title": ""
				}
			],
			"component": "",
			"hidden": false,
			"icon": "",
			"id": "",
			"level": 0,
			"meta": {},
			"name": "",
			"parentId": 0,
			"path": "",
			"roles": [],
			"state": false,
			"title": ""
		}
	],
	"phone": "",
	"realname": "",
	"roleIds": [],
	"roleName": "",
	"state": "",
	"updateBy": "",
	"updateTime": ""
}
```

## 新增用户

**接口地址**:`/bbt-api/org/user`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "account": "",
  "admin": false,
  "departmentIds": [],
  "identify": "",
  "phone": "",
  "pwd": "",
  "realname": "",
  "regionCode": "",
  "roleIds": [],
  "sort": 0
}
```

**请求参数**:

| 参数名称                      | 参数说明     | 请求类型 | 是否必须  | 数据类型           | schema   |
|---------------------------|----------|------|-------|----------------|----------|
| 系统用户新增参数                  | 系统用户新增参数 | body | true  | 系统用户新增参数       | 系统用户新增参数 |
| &emsp;&emsp;account       | 用户名      |      | false | string         |          |
| &emsp;&emsp;admin         | 管理员      |      | false | boolean        |          |
| &emsp;&emsp;departmentIds | 部门id列表   |      | false | array          | string   |
| &emsp;&emsp;identify      | 证件号码     |      | false | string         |          |
| &emsp;&emsp;phone         | 手机号码     |      | false | string         |          |
| &emsp;&emsp;pwd           | 密码       |      | false | string         |          |
| &emsp;&emsp;realname      | 真实姓名     |      | false | string         |          |
| &emsp;&emsp;regionCode    | 行政区域     |      | false | string         |          |
| &emsp;&emsp;roleIds       | 角色id列表   |      | false | array          | string   |
| &emsp;&emsp;sort          | 用户排序     |      | false | integer(int32) |          |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | UserBaseInfoVO |

**响应参数**:

| 参数名称      | 参数说明                                             | 类型                | schema            |
|-----------|--------------------------------------------------|-------------------|-------------------| 
| account   | 用户名                                              | string            |                   |
| admin     | 管理员                                              | boolean           |                   |
| email     | 邮箱                                               | string            |                   |
| gender    | 性别,可用值:FEMALE,MALE,UNKNOWN                       | string            |                   |
| head      | 头像                                               | string            |                   |
| id        | 主键                                               | string            |                   |
| lastLogin | 最后登录时间                                           | string(date-time) | string(date-time) |
| phone     | 手机号码                                             | string            |                   |
| realname  | 真实姓名                                             | string            |                   |
| roleName  | 角色名称                                             | string            |                   |
| state     | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |

**响应示例**:

```javascript
{
	"account": "",
	"admin": false,
	"email": "",
	"gender": "",
	"head": "",
	"id": "",
	"lastLogin": "",
	"phone": "",
	"realname": "",
	"roleName": "",
	"state": ""
}
```

## 获取非某个部门下的用户

**接口地址**:`/bbt-api/org/user/excludeByDept`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|---------|-----------|-------|-------|----------------|--------|
| current | 页码,默认为1   | query | false | integer(int32) |        |
| id      | 部门id列表    | query | false | string         |        |
| keyword | 搜索关键字     | query | false | string         |        |
| size    | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | 分页结果对象«UserBaseVO» |

**响应参数**:

| 参数名称                   | 参数说明 | 类型                | schema         |
|------------------------|------|-------------------|----------------| 
| current                | 当前页码 | integer(int32)    | integer(int32) |
| pages                  | 总页数  | integer(int32)    | integer(int32) |
| records                | 数据列表 | array             | UserBaseVO     |
| &emsp;&emsp;account    | 用户名  | string            |                |
| &emsp;&emsp;createTime | 创建时间 | string(date-time) |                |
| &emsp;&emsp;id         | 用户id | string            |                |
| &emsp;&emsp;phone      | 手机号码 | string            |                |
| &emsp;&emsp;realname   | 真实姓名 | string            |                |
| &emsp;&emsp;sort       | 排序   | integer(int32)    |                |
| size                   | 页大小  | integer(int32)    | integer(int32) |
| total                  | 总行数  | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"account": "",
			"createTime": "",
			"id": "",
			"phone": "",
			"realname": "",
			"sort": 0
		}
	],
	"size": 0,
	"total": 0
}
```

## 删除系统用户

**接口地址**:`/bbt-api/org/user/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更改状态

**接口地址**:`/bbt-api/org/user/state`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "state": ""
}
```

**请求参数**:

| 参数名称              | 参数说明                              | 请求类型 | 是否必须  | 数据类型     | schema   |
|-------------------|-----------------------------------|------|-------|----------|----------|
| 系统用户状态参数          | 系统用户状态参数                          | body | true  | 系统用户状态参数 | 系统用户状态参数 |
| &emsp;&emsp;id    | 主键                                |      | false | string   |          |
| &emsp;&emsp;state | 状态,可用值:DELETE,DISABLE,ENABLE,LOCK |      | false | string   |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 用户列表(完全匹配)

**接口地址**:`/bbt-api/org/user/complete/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须  | 数据类型   | schema |
|---------|---------|-------|-------|--------|--------|
| keyword | keyword | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | UserListVO |

**响应参数**:

| 参数名称           | 参数说明                                | 类型                | schema            |
|----------------|-------------------------------------|-------------------|-------------------| 
| account        | 用户名                                 | string            |                   |
| createBy       | 创建者                                 | string            |                   |
| createTime     | 创建时间                                | string(date-time) | string(date-time) |
| departmentName | 部门名称                                | string            |                   |
| id             | 主键                                  | string            |                   |
| phone          | 手机号码                                | string            |                   |
| realname       | 真实姓名                                | string            |                   |
| sort           | 用户排序                                | integer(int32)    | integer(int32)    |
| state          | 用户状态,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |

**响应示例**:

```javascript
[
	{
		"account": "",
		"createBy": "",
		"createTime": "",
		"departmentName": "",
		"id": "",
		"phone": "",
		"realname": "",
		"sort": 0,
		"state": ""
	}
]
```

## 修改密码

**接口地址**:`/bbt-api/org/user/changePwd`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "account": "",
  "pwdNew": "",
  "pwdOld": ""
}
```

**请求参数**:

| 参数名称                | 参数说明         | 请求类型 | 是否必须  | 数据类型         | schema       |
|---------------------|--------------|------|-------|--------------|--------------|
| 个人中心用户密码修改参数        | 个人中心用户密码修改参数 | body | true  | 个人中心用户密码修改参数 | 个人中心用户密码修改参数 |
| &emsp;&emsp;account | 用户账号         |      | false | string       |              |
| &emsp;&emsp;pwdNew  | 新密码          |      | false | string       |              |
| &emsp;&emsp;pwdOld  | 旧密码          |      | false | string       |              |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 通过id获取用户基本信息

**接口地址**:`/bbt-api/org/user/baseinfo/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | UserBaseInfoVO |

**响应参数**:

| 参数名称      | 参数说明                                             | 类型                | schema            |
|-----------|--------------------------------------------------|-------------------|-------------------| 
| account   | 用户名                                              | string            |                   |
| admin     | 管理员                                              | boolean           |                   |
| email     | 邮箱                                               | string            |                   |
| gender    | 性别,可用值:FEMALE,MALE,UNKNOWN                       | string            |                   |
| head      | 头像                                               | string            |                   |
| id        | 主键                                               | string            |                   |
| lastLogin | 最后登录时间                                           | string(date-time) | string(date-time) |
| phone     | 手机号码                                             | string            |                   |
| realname  | 真实姓名                                             | string            |                   |
| roleName  | 角色名称                                             | string            |                   |
| state     | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |

**响应示例**:

```javascript
{
	"account": "",
	"admin": false,
	"email": "",
	"gender": "",
	"head": "",
	"id": "",
	"lastLogin": "",
	"phone": "",
	"realname": "",
	"roleName": "",
	"state": ""
}
```

# 用户-角色

## 角色详情

**接口地址**:`/bbt-api/org/role/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:YangLe

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema       |
|-----|----|--------------| 
| 200 | OK | RoleDetailVO |

**响应参数**:

| 参数名称                                               | 参数说明                           | 类型                          | schema                      |
|----------------------------------------------------|--------------------------------|-----------------------------|-----------------------------| 
| departmentList                                     | 组织机构列表                         | array                       | DepartmentDTO               |
| &emsp;&emsp;address                                |                                | string                      |                             |
| &emsp;&emsp;adminId                                |                                | string                      |                             |
| &emsp;&emsp;children                               |                                | array                       | DepartmentDTO               |
| &emsp;&emsp;createBy                               |                                | string                      |                             |
| &emsp;&emsp;createTime                             |                                | string(date-time)           |                             |
| &emsp;&emsp;deptCode                               |                                | string                      |                             |
| &emsp;&emsp;deptName                               |                                | string                      |                             |
| &emsp;&emsp;description                            |                                | string                      |                             |
| &emsp;&emsp;id                                     |                                | string                      |                             |
| &emsp;&emsp;level                                  |                                | integer(int32)              |                             |
| &emsp;&emsp;parentDeptName                         |                                | string                      |                             |
| &emsp;&emsp;parentId                               |                                | string                      |                             |
| &emsp;&emsp;regionCode                             |                                | string                      |                             |
| &emsp;&emsp;sort                                   |                                | integer(int32)              |                             |
| &emsp;&emsp;state                                  |                                | string                      |                             |
| &emsp;&emsp;tel                                    |                                | string                      |                             |
| &emsp;&emsp;updateBy                               |                                | string                      |                             |
| &emsp;&emsp;updateTime                             |                                | string(date-time)           |                             |
| &emsp;&emsp;userPages                              |                                | PageInfo«UserDepartmentDTO» | PageInfo«UserDepartmentDTO» |
| &emsp;&emsp;&emsp;&emsp;countId                    |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;current                    |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;maxLimit                   |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;optimizeCountSql           |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;orderMapping               |                                | OrderMapping                | OrderMapping                |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;map            |                                | object                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;underLineMode  |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;orders                     |                                | array                       | OrderItem                   |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;asc            |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;column         |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;pages                      |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;paramPage                  |                                | 分页参数                        | 分页参数                        |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;current        | 页码,默认为1                        | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;size           | 页大小,默认为10                      | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;records                    |                                | array                       | UserDepartmentDTO           |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;account        |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;admin          |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;createTime     |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;departmentCode |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;departmentId   |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;departmentName |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id             |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;phone          |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;realname       |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sort           |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;state          | 可用值:DELETE,DISABLE,ENABLE,LOCK | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId         |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;searchCount                |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;size                       |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;total                      |                                | integer                     |                             |
| &emsp;&emsp;users                                  |                                | array                       | UserDepartmentDTO           |
| &emsp;&emsp;&emsp;&emsp;account                    |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;admin                      |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;createTime                 |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;departmentCode             |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;departmentId               |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;departmentName             |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;id                         |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;phone                      |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;realname                   |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;sort                       |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;state                      | 可用值:DELETE,DISABLE,ENABLE,LOCK | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;userId                     |                                | string                      |                             |
| &emsp;&emsp;version                                |                                | integer(int64)              |                             |
| description                                        | 备注                             | string                      |                             |
| id                                                 | 主键                             | string                      |                             |
| name                                               | 角色名称                           | string                      |                             |
| permissionList                                     | 表单权限列表                         | array                       | FormPermission              |
| &emsp;&emsp;operates                               | 操作集合                           | array                       | string                      |
| &emsp;&emsp;table                                  | 表单列表                           | FormTable                   | FormTable                   |
| &emsp;&emsp;&emsp;&emsp;id                         | 主键ID                           | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;tableLabel                 | 数据表中文名                         | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;tableName                  | 数据表名                           | string                      |                             |
| userIds                                            | 用户id                           | array                       |                             |
| userList                                           | 用户列表                           | array                       | UserInfo                    |
| &emsp;&emsp;id                                     |                                | Serializable                | Serializable                |
| &emsp;&emsp;name                                   |                                | string                      |                             |
| &emsp;&emsp;nickName                               |                                | string                      |                             |
| &emsp;&emsp;realName                               |                                | string                      |                             |

**响应示例**:

```javascript
{
	"departmentList": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"userPages": {
						"countId": "",
						"current": 0,
						"maxLimit": 0,
						"optimizeCountSql": true,
						"orderMapping": {
							"map": {},
							"underLineMode": true
						},
						"orders": [
							{
								"asc": true,
								"column": ""
							}
						],
						"pages": 0,
						"paramPage": {
							"current": 1,
							"size": 10
						},
						"records": [
							{
								"account": "",
								"admin": true,
								"createTime": "",
								"departmentCode": "",
								"departmentId": "",
								"departmentName": "",
								"id": "",
								"phone": "",
								"realname": "",
								"sort": 0,
								"state": "",
								"userId": ""
							}
						],
						"searchCount": true,
						"size": 0,
						"total": 0
					},
					"users": [
						{
							"account": "",
							"admin": true,
							"createTime": "",
							"departmentCode": "",
							"departmentId": "",
							"departmentName": "",
							"id": "",
							"phone": "",
							"realname": "",
							"sort": 0,
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"userPages": {},
			"users": [
				{
					"account": "",
					"admin": true,
					"createTime": "",
					"departmentCode": "",
					"departmentId": "",
					"departmentName": "",
					"id": "",
					"phone": "",
					"realname": "",
					"sort": 0,
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"description": "",
	"id": "",
	"name": "",
	"permissionList": [
		{
			"operates": [],
			"table": {
				"id": "",
				"tableLabel": "",
				"tableName": ""
			}
		}
	],
	"userIds": [],
	"userList": [
		{
			"id": {},
			"name": "",
			"nickName": "",
			"realName": ""
		}
	]
}
```

## 指定菜单角色初始化

**接口地址**:`/bbt-api/org/role/init/specifiedMenuForAllRole`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
[]
```

**请求参数**:

| 参数名称    | 参数说明   | 请求类型 | 是否必须 | 数据类型  | schema |
|---------|--------|------|------|-------|--------|
| strings | string | body | true | array |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更新角色

**接口地址**:`/bbt-api/org/role/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "departmentIds": [],
  "id": "",
  "menuIds": [],
  "mobileMenuIds": [],
  "pcMenuIds": [],
  "permissions": [
    {
      "operateIds": [],
      "tableId": ""
    }
  ],
  "userIds": []
}
```

**请求参数**:

| 参数名称                               | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema          |
|------------------------------------|----------|------|-------|----------|-----------------|
| 系统角色修改参数                           | 系统角色修改参数 | body | true  | 系统角色修改参数 | 系统角色修改参数        |
| &emsp;&emsp;departmentIds          | 组织机构id   |      | false | array    | string          |
| &emsp;&emsp;id                     | 主键       |      | false | string   |                 |
| &emsp;&emsp;menuIds                | 菜单id     |      | false | array    | string          |
| &emsp;&emsp;mobileMenuIds          | 移动端菜单id  |      | false | array    | string          |
| &emsp;&emsp;pcMenuIds              | pc菜单id   |      | false | array    | string          |
| &emsp;&emsp;permissions            | 表单权限     |      | false | array    | PermissionParam |
| &emsp;&emsp;&emsp;&emsp;operateIds | 操作类型ids  |      | false | array    | string          |
| &emsp;&emsp;&emsp;&emsp;tableId    | 表id      |      | false | string   |                 |
| &emsp;&emsp;userIds                | 用户id     |      | false | array    | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 初始化角色菜单

**接口地址**:`/bbt-api/org/role/init/roleMenu`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 角色列表

**接口地址**:`/bbt-api/org/role/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须  | 数据类型   | schema |
|---------|---------|-------|-------|--------|--------|
| keyword | keyword | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | RoleBaseVO |

**响应参数**:

| 参数名称        | 参数说明   | 类型     | schema |
|-------------|--------|--------|--------| 
| code        | 角色唯一编码 | string |        |
| description | 备注     | string |        |
| id          | 主键     | string |        |
| name        | 角色名称   | string |        |

**响应示例**:

```javascript
[
	{
		"code": "",
		"description": "",
		"id": "",
		"name": ""
	}
]
```

## 角色分页列表

**接口地址**:`/bbt-api/org/role/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|---------|-----------|-------|-------|----------------|--------|
| current | 页码,默认为1   | query | false | integer(int32) |        |
| keyword | 关键字       | query | false | string         |        |
| size    | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | 分页结果对象«RolePageVO» |

**响应参数**:

| 参数名称                       | 参数说明     | 类型                | schema         |
|----------------------------|----------|-------------------|----------------| 
| current                    | 当前页码     | integer(int32)    | integer(int32) |
| pages                      | 总页数      | integer(int32)    | integer(int32) |
| records                    | 数据列表     | array             | RolePageVO     |
| &emsp;&emsp;code           | 角色唯一编码   | string            |                |
| &emsp;&emsp;createBy       | 创建者      | string            |                |
| &emsp;&emsp;createTime     | 创建时间     | string(date-time) |                |
| &emsp;&emsp;departmentIds  | 归属机构ID集合 | array             | string         |
| &emsp;&emsp;departmentName | 归属机构     | string            |                |
| &emsp;&emsp;id             | 主键       | string            |                |
| &emsp;&emsp;name           | 角色名称     | string            |                |
| &emsp;&emsp;updateBy       | 修改者      | string            |                |
| &emsp;&emsp;updateTime     | 修改时间     | string(date-time) |                |
| size                       | 页大小      | integer(int32)    | integer(int32) |
| total                      | 总行数      | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"code": "",
			"createBy": "",
			"createTime": "",
			"departmentIds": [],
			"departmentName": "",
			"id": "",
			"name": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 初始化移动端角色菜单

**接口地址**:`/bbt-api/org/role/init/mobileRoleMenu`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除角色

**接口地址**:`/bbt-api/org/role/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 获取角色菜单

**接口地址**:`/bbt-api/org/role/menu`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>获取所选菜单的id用户回显</p>

**请求参数**:

| 参数名称   | 参数说明   | 请求类型  | 是否必须  | 数据类型   | schema |
|--------|--------|-------|-------|--------|--------|
| roleId | roleId | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema   |
|-----|----|----------| 
| 200 | OK | MenuIdVO |

**响应参数**:

| 参数名称          | 参数说明    | 类型    | schema |
|---------------|---------|-------|--------| 
| mobileMenuIds | 移动端菜单id | array |        |
| pcMenuIds     | pc菜单id  | array |        |

**响应示例**:

```javascript
{
	"mobileMenuIds": [],
	"pcMenuIds": []
}
```

## 角色分配菜单

**接口地址**:`/bbt-api/org/role/menu`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [],
  "menuIds": []
}
```

**请求参数**:

| 参数名称                | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema   |
|---------------------|----------|------|-------|----------|----------|
| 角色菜单新增参数            | 角色菜单新增参数 | body | true  | 角色菜单新增参数 | 角色菜单新增参数 |
| &emsp;&emsp;ids     | 角色id     |      | false | array    | string   |
| &emsp;&emsp;menuIds | 菜单id     |      | false | array    | string   |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 获取包含用户id列表的角色列表

**接口地址**:`/bbt-api/org/role/getRoleListIncludeUserIdList`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema       |
|-----|----|--------------| 
| 200 | OK | RoleDetailVO |

**响应参数**:

| 参数名称                                               | 参数说明                           | 类型                          | schema                      |
|----------------------------------------------------|--------------------------------|-----------------------------|-----------------------------| 
| departmentList                                     | 组织机构列表                         | array                       | DepartmentDTO               |
| &emsp;&emsp;address                                |                                | string                      |                             |
| &emsp;&emsp;adminId                                |                                | string                      |                             |
| &emsp;&emsp;children                               |                                | array                       | DepartmentDTO               |
| &emsp;&emsp;createBy                               |                                | string                      |                             |
| &emsp;&emsp;createTime                             |                                | string(date-time)           |                             |
| &emsp;&emsp;deptCode                               |                                | string                      |                             |
| &emsp;&emsp;deptName                               |                                | string                      |                             |
| &emsp;&emsp;description                            |                                | string                      |                             |
| &emsp;&emsp;id                                     |                                | string                      |                             |
| &emsp;&emsp;level                                  |                                | integer(int32)              |                             |
| &emsp;&emsp;parentDeptName                         |                                | string                      |                             |
| &emsp;&emsp;parentId                               |                                | string                      |                             |
| &emsp;&emsp;regionCode                             |                                | string                      |                             |
| &emsp;&emsp;sort                                   |                                | integer(int32)              |                             |
| &emsp;&emsp;state                                  |                                | string                      |                             |
| &emsp;&emsp;tel                                    |                                | string                      |                             |
| &emsp;&emsp;updateBy                               |                                | string                      |                             |
| &emsp;&emsp;updateTime                             |                                | string(date-time)           |                             |
| &emsp;&emsp;userPages                              |                                | PageInfo«UserDepartmentDTO» | PageInfo«UserDepartmentDTO» |
| &emsp;&emsp;&emsp;&emsp;countId                    |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;current                    |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;maxLimit                   |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;optimizeCountSql           |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;orderMapping               |                                | OrderMapping                | OrderMapping                |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;map            |                                | object                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;underLineMode  |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;orders                     |                                | array                       | OrderItem                   |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;asc            |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;column         |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;pages                      |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;paramPage                  |                                | 分页参数                        | 分页参数                        |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;current        | 页码,默认为1                        | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;size           | 页大小,默认为10                      | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;records                    |                                | array                       | UserDepartmentDTO           |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;account        |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;admin          |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;createTime     |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;departmentCode |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;departmentId   |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;departmentName |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id             |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;phone          |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;realname       |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sort           |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;state          | 可用值:DELETE,DISABLE,ENABLE,LOCK | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId         |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;searchCount                |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;size                       |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;total                      |                                | integer                     |                             |
| &emsp;&emsp;users                                  |                                | array                       | UserDepartmentDTO           |
| &emsp;&emsp;&emsp;&emsp;account                    |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;admin                      |                                | boolean                     |                             |
| &emsp;&emsp;&emsp;&emsp;createTime                 |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;departmentCode             |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;departmentId               |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;departmentName             |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;id                         |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;phone                      |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;realname                   |                                | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;sort                       |                                | integer                     |                             |
| &emsp;&emsp;&emsp;&emsp;state                      | 可用值:DELETE,DISABLE,ENABLE,LOCK | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;userId                     |                                | string                      |                             |
| &emsp;&emsp;version                                |                                | integer(int64)              |                             |
| description                                        | 备注                             | string                      |                             |
| id                                                 | 主键                             | string                      |                             |
| name                                               | 角色名称                           | string                      |                             |
| permissionList                                     | 表单权限列表                         | array                       | FormPermission              |
| &emsp;&emsp;operates                               | 操作集合                           | array                       | string                      |
| &emsp;&emsp;table                                  | 表单列表                           | FormTable                   | FormTable                   |
| &emsp;&emsp;&emsp;&emsp;id                         | 主键ID                           | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;tableLabel                 | 数据表中文名                         | string                      |                             |
| &emsp;&emsp;&emsp;&emsp;tableName                  | 数据表名                           | string                      |                             |
| userIds                                            | 用户id                           | array                       |                             |
| userList                                           | 用户列表                           | array                       | UserInfo                    |
| &emsp;&emsp;id                                     |                                | Serializable                | Serializable                |
| &emsp;&emsp;name                                   |                                | string                      |                             |
| &emsp;&emsp;nickName                               |                                | string                      |                             |
| &emsp;&emsp;realName                               |                                | string                      |                             |

**响应示例**:

```javascript
[
	{
		"departmentList": [
			{
				"address": "",
				"adminId": "",
				"children": [
					{
						"address": "",
						"adminId": "",
						"children": [
							{}
						],
						"createBy": "",
						"createTime": "",
						"deptCode": "",
						"deptName": "",
						"description": "",
						"id": "",
						"level": 0,
						"parentDeptName": "",
						"parentId": "",
						"regionCode": "",
						"sort": 0,
						"state": "",
						"tel": "",
						"updateBy": "",
						"updateTime": "",
						"userPages": {
							"countId": "",
							"current": 0,
							"maxLimit": 0,
							"optimizeCountSql": true,
							"orderMapping": {
								"map": {},
								"underLineMode": true
							},
							"orders": [
								{
									"asc": true,
									"column": ""
								}
							],
							"pages": 0,
							"paramPage": {
								"current": 1,
								"size": 10
							},
							"records": [
								{
									"account": "",
									"admin": true,
									"createTime": "",
									"departmentCode": "",
									"departmentId": "",
									"departmentName": "",
									"id": "",
									"phone": "",
									"realname": "",
									"sort": 0,
									"state": "",
									"userId": ""
								}
							],
							"searchCount": true,
							"size": 0,
							"total": 0
						},
						"users": [
							{
								"account": "",
								"admin": true,
								"createTime": "",
								"departmentCode": "",
								"departmentId": "",
								"departmentName": "",
								"id": "",
								"phone": "",
								"realname": "",
								"sort": 0,
								"state": "",
								"userId": ""
							}
						],
						"version": 0
					}
				],
				"createBy": "",
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"description": "",
				"id": "",
				"level": 0,
				"parentDeptName": "",
				"parentId": "",
				"regionCode": "",
				"sort": 0,
				"state": "",
				"tel": "",
				"updateBy": "",
				"updateTime": "",
				"userPages": {},
				"users": [
					{
						"account": "",
						"admin": true,
						"createTime": "",
						"departmentCode": "",
						"departmentId": "",
						"departmentName": "",
						"id": "",
						"phone": "",
						"realname": "",
						"sort": 0,
						"state": "",
						"userId": ""
					}
				],
				"version": 0
			}
		],
		"description": "",
		"id": "",
		"name": "",
		"permissionList": [
			{
				"operates": [],
				"table": {
					"id": "",
					"tableLabel": "",
					"tableName": ""
				}
			}
		],
		"userIds": [],
		"userList": [
			{
				"id": {},
				"name": "",
				"nickName": "",
				"realName": ""
			}
		]
	}
]
```

## 新增角色

**接口地址**:`/bbt-api/org/role`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:YangLe

**接口描述**:

**请求示例**:

```javascript
{
  "departmentIds": [],
  "menuIds": [],
  "mobileMenuIds": [],
  "name": "",
  "pcMenuIds": [],
  "permissions": [
    {
      "operateIds": [],
      "tableId": ""
    }
  ],
  "userIds": []
}
```

**请求参数**:

| 参数名称                               | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema          |
|------------------------------------|----------|------|-------|----------|-----------------|
| 系统角色新增参数                           | 系统角色新增参数 | body | true  | 系统角色新增参数 | 系统角色新增参数        |
| &emsp;&emsp;departmentIds          | 组织机构id   |      | false | array    | string          |
| &emsp;&emsp;menuIds                | 菜单id     |      | false | array    | string          |
| &emsp;&emsp;mobileMenuIds          | 移动端菜单id  |      | false | array    | string          |
| &emsp;&emsp;name                   | 角色名称     |      | false | string   |                 |
| &emsp;&emsp;pcMenuIds              | pc菜单id   |      | false | array    | string          |
| &emsp;&emsp;permissions            | 表单权限     |      | false | array    | PermissionParam |
| &emsp;&emsp;&emsp;&emsp;operateIds | 操作类型ids  |      | false | array    | string          |
| &emsp;&emsp;&emsp;&emsp;tableId    | 表id      |      | false | string   |                 |
| &emsp;&emsp;userIds                | 用户id     |      | false | array    | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 用户-部门

## 新增或修改部门

**接口地址**:`/bbt-api/org/department`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "address": "",
  "deptName": "",
  "id": "",
  "parentId": "",
  "sort": 0
}
```

**请求参数**:

| 参数名称                 | 参数说明   | 请求类型 | 是否必须  | 数据类型           | schema |
|----------------------|--------|------|-------|----------------|--------|
| 部门新增参数               | 部门新增参数 | body | true  | 部门新增参数         | 部门新增参数 |
| &emsp;&emsp;address  | 机构地址   |      | false | string         |        |
| &emsp;&emsp;deptName | 部门名称   |      | false | string         |        |
| &emsp;&emsp;id       | 主键     |      | false | string         |        |
| &emsp;&emsp;parentId | 上级机构   |      | false | string         |        |
| &emsp;&emsp;sort     | 机构排序   |      | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentBaseVO |

**响应参数**:

| 参数名称       | 参数说明 | 类型                | schema            |
|------------|------|-------------------|-------------------| 
| address    | 详细地址 | string            |                   |
| createTime | 创建时间 | string(date-time) | string(date-time) |
| deptCode   | 部门编号 | string            |                   |
| deptName   | 部门名称 | string            |                   |
| id         | 主键   | string            |                   |
| level      | 部门层级 | integer(int32)    | integer(int32)    |
| sort       | 部门排序 | integer(int32)    | integer(int32)    |

**响应示例**:

```javascript
{
	"address": "",
	"createTime": "",
	"deptCode": "",
	"deptName": "",
	"id": "",
	"level": 0,
	"sort": 0
}
```

## 删除部门

**接口地址**:`/bbt-api/org/department/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 部门树形列表

**接口地址**:`/bbt-api/org/department/tree`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>所有部门</p>

**请求参数**:

| 参数名称         | 参数说明         | 请求类型  | 是否必须  | 数据类型    | schema |
|--------------|--------------|-------|-------|---------|--------|
| departmentId | departmentId | query | false | string  |        |
| userFlag     | userFlag     | query | false | boolean |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentTreeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| children                           | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;children               | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;deptCode               | 部门编号  | string            |                   |
| &emsp;&emsp;deptName               | 部门名称  | string            |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;parentId               | 上级id  | string            |                   |
| &emsp;&emsp;sort                   | 部门排序  | integer(int32)    |                   |
| &emsp;&emsp;users                  | 用户信息  | array             | User              |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名   | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 姓名    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 用户排序  | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;userId     | 用户id  | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| deptCode                           | 部门编号  | string            |                   |
| deptName                           | 部门名称  | string            |                   |
| id                                 | 主键    | string            |                   |
| parentId                           | 上级id  | string            |                   |
| sort                               | 部门排序  | integer(int32)    | integer(int32)    |
| users                              | 用户信息  | array             | User              |
| &emsp;&emsp;account                | 用户名   | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;name                   | 姓名    | string            |                   |
| &emsp;&emsp;realname               | 真实姓名  | string            |                   |
| &emsp;&emsp;sort                   | 用户排序  | integer(int32)    |                   |
| &emsp;&emsp;userId                 | 用户id  | string            |                   |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"id": "",
				"parentId": "",
				"sort": 0,
				"users": []
			}
		],
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"parentId": "",
		"sort": 0,
		"users": [
			{
				"account": "",
				"createTime": "",
				"id": "",
				"name": "",
				"realname": "",
				"sort": 0,
				"userId": ""
			}
		]
	}
]
```

## 用户同级部门及下级部门

**接口地址**:`/bbt-api/org/department/sameAndSubLeve`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentTreeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| children                           | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;children               | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;deptCode               | 部门编号  | string            |                   |
| &emsp;&emsp;deptName               | 部门名称  | string            |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;parentId               | 上级id  | string            |                   |
| &emsp;&emsp;sort                   | 部门排序  | integer(int32)    |                   |
| &emsp;&emsp;users                  | 用户信息  | array             | User              |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名   | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 姓名    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 用户排序  | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;userId     | 用户id  | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| deptCode                           | 部门编号  | string            |                   |
| deptName                           | 部门名称  | string            |                   |
| id                                 | 主键    | string            |                   |
| parentId                           | 上级id  | string            |                   |
| sort                               | 部门排序  | integer(int32)    | integer(int32)    |
| users                              | 用户信息  | array             | User              |
| &emsp;&emsp;account                | 用户名   | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;name                   | 姓名    | string            |                   |
| &emsp;&emsp;realname               | 真实姓名  | string            |                   |
| &emsp;&emsp;sort                   | 用户排序  | integer(int32)    |                   |
| &emsp;&emsp;userId                 | 用户id  | string            |                   |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"id": "",
				"parentId": "",
				"sort": 0,
				"users": []
			}
		],
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"parentId": "",
		"sort": 0,
		"users": [
			{
				"account": "",
				"createTime": "",
				"id": "",
				"name": "",
				"realname": "",
				"sort": 0,
				"userId": ""
			}
		]
	}
]
```

## 部门所有用户

**接口地址**:`/bbt-api/org/department/user/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentUserVO |

**响应参数**:

| 参数名称     | 参数说明                                             | 类型             | schema         |
|----------|--------------------------------------------------|----------------|----------------| 
| account  | 用户名                                              | string         |                |
| admin    | 管理员                                              | boolean        |                |
| id       | 主键                                               | string         |                |
| phone    | 手机号码                                             | string         |                |
| realname | 真实姓名                                             | string         |                |
| sort     | 部门用户排序                                           | integer(int32) | integer(int32) |
| state    | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                |
| userId   | 用户id                                             | string         |                |

**响应示例**:

```javascript
[
	{
		"account": "",
		"admin": false,
		"id": "",
		"phone": "",
		"realname": "",
		"sort": 0,
		"state": "",
		"userId": ""
	}
]
```

## 初始化部门角色

**接口地址**:`/bbt-api/org/department/initRoles`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称   | 参数说明   | 请求类型  | 是否必须  | 数据类型   | schema |
|--------|--------|-------|-------|--------|--------|
| deptId | deptId | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 非部门下的用户

**接口地址**:`/bbt-api/org/department/user/notIn`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|---------|-----------|-------|-------|----------------|--------|
| current | 页码,默认为1   | query | false | integer(int32) |        |
| id      | 部门id列表    | query | false | string         |        |
| keyword | 搜索关键字     | query | false | string         |        |
| size    | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema                        |
|-----|----|-------------------------------| 
| 200 | OK | 分页结果对象«DepartmentUserNotInVO» |

**响应参数**:

| 参数名称                   | 参数说明 | 类型                | schema                |
|------------------------|------|-------------------|-----------------------| 
| current                | 当前页码 | integer(int32)    | integer(int32)        |
| pages                  | 总页数  | integer(int32)    | integer(int32)        |
| records                | 数据列表 | array             | DepartmentUserNotInVO |
| &emsp;&emsp;account    | 用户名  | string            |                       |
| &emsp;&emsp;admin      | 管理员  | boolean           |                       |
| &emsp;&emsp;createBy   | 创建者  | string            |                       |
| &emsp;&emsp;createTime | 创建时间 | string(date-time) |                       |
| &emsp;&emsp;department | 归属机构 | string            |                       |
| &emsp;&emsp;id         | 用户id | string            |                       |
| &emsp;&emsp;phone      | 手机号码 | string            |                       |
| &emsp;&emsp;realname   | 真实姓名 | string            |                       |
| &emsp;&emsp;sort       | 用户排序 | integer(int32)    |                       |
| size                   | 页大小  | integer(int32)    | integer(int32)        |
| total                  | 总行数  | integer(int32)    | integer(int32)        |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"account": "",
			"admin": false,
			"createBy": "",
			"createTime": "",
			"department": "",
			"id": "",
			"phone": "",
			"realname": "",
			"sort": 0
		}
	],
	"size": 0,
	"total": 0
}
```

## 部门新增用户

**接口地址**:`/bbt-api/org/department/user/add`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "users": [
    {
      "account": "",
      "admin": false,
      "id": "",
      "phone": "",
      "realname": ""
    }
  ]
}
```

**请求参数**:

| 参数名称                             | 参数说明                    | 请求类型 | 是否必须  | 数据类型                    | schema                  |
|----------------------------------|-------------------------|------|-------|-------------------------|-------------------------|
| departmentUserListParam          | DepartmentUserListParam | body | true  | DepartmentUserListParam | DepartmentUserListParam |
| &emsp;&emsp;id                   | 部门id                    |      | false | string                  |                         |
| &emsp;&emsp;users                | 人员信息                    |      | false | array                   | DepartmentUserModify    |
| &emsp;&emsp;&emsp;&emsp;account  | 用户名                     |      | false | string                  |                         |
| &emsp;&emsp;&emsp;&emsp;admin    | 管理员                     |      | false | boolean                 |                         |
| &emsp;&emsp;&emsp;&emsp;id       | 主键                      |      | false | string                  |                         |
| &emsp;&emsp;&emsp;&emsp;phone    | 手机号码                    |      | false | string                  |                         |
| &emsp;&emsp;&emsp;&emsp;realname | 真实姓名                    |      | false | string                  |                         |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 设置部门管理员权限

**接口地址**:`/bbt-api/org/department/setAdmin`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "adminFlag": false,
  "deptId": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称                  | 参数说明                        | 请求类型 | 是否必须 | 数据类型      | schema    |
|-----------------------|-----------------------------|------|------|-----------|-----------|
| 部门管理员设置参数             | 部门管理员设置参数                   | body | true | 部门管理员设置参数 | 部门管理员设置参数 |
| &emsp;&emsp;adminFlag | 是否为管理标识 true:设为管理员;false:取消 |      | true | boolean   |           |
| &emsp;&emsp;deptId    | 部门id                        |      | true | string    |           |
| &emsp;&emsp;userId    | 用户id                        |      | true | string    |           |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 用户当前部门及下级部门

**接口地址**:`/bbt-api/org/department/currentAndSubLeve`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentTreeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| children                           | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;children               | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;deptCode               | 部门编号  | string            |                   |
| &emsp;&emsp;deptName               | 部门名称  | string            |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;parentId               | 上级id  | string            |                   |
| &emsp;&emsp;sort                   | 部门排序  | integer(int32)    |                   |
| &emsp;&emsp;users                  | 用户信息  | array             | User              |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名   | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 姓名    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 用户排序  | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;userId     | 用户id  | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| deptCode                           | 部门编号  | string            |                   |
| deptName                           | 部门名称  | string            |                   |
| id                                 | 主键    | string            |                   |
| parentId                           | 上级id  | string            |                   |
| sort                               | 部门排序  | integer(int32)    | integer(int32)    |
| users                              | 用户信息  | array             | User              |
| &emsp;&emsp;account                | 用户名   | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;name                   | 姓名    | string            |                   |
| &emsp;&emsp;realname               | 真实姓名  | string            |                   |
| &emsp;&emsp;sort                   | 用户排序  | integer(int32)    |                   |
| &emsp;&emsp;userId                 | 用户id  | string            |                   |

**响应示例**:

```javascript
{
	"children": [
		{
			"children": [],
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"id": "",
			"parentId": "",
			"sort": 0,
			"users": []
		}
	],
	"createTime": "",
	"deptCode": "",
	"deptName": "",
	"id": "",
	"parentId": "",
	"sort": 0,
	"users": [
		{
			"account": "",
			"createTime": "",
			"id": "",
			"name": "",
			"realname": "",
			"sort": 0,
			"userId": ""
		}
	]
}
```

## 初始化缓存

**接口地址**:`/bbt-api/org/department/initCache`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 获取当前登录用户下级部门列表

**接口地址**:`/bbt-api/org/department/childrenList`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentBaseVO |

**响应参数**:

| 参数名称       | 参数说明 | 类型                | schema            |
|------------|------|-------------------|-------------------| 
| address    | 详细地址 | string            |                   |
| createTime | 创建时间 | string(date-time) | string(date-time) |
| deptCode   | 部门编号 | string            |                   |
| deptName   | 部门名称 | string            |                   |
| id         | 主键   | string            |                   |
| level      | 部门层级 | integer(int32)    | integer(int32)    |
| sort       | 部门排序 | integer(int32)    | integer(int32)    |

**响应示例**:

```javascript
[
	{
		"address": "",
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"level": 0,
		"sort": 0
	}
]
```

## 当前用户部门同级部门

**接口地址**:`/bbt-api/org/department/current/sameLeve`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentTreeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| children                           | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;children               | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;deptCode               | 部门编号  | string            |                   |
| &emsp;&emsp;deptName               | 部门名称  | string            |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;parentId               | 上级id  | string            |                   |
| &emsp;&emsp;sort                   | 部门排序  | integer(int32)    |                   |
| &emsp;&emsp;users                  | 用户信息  | array             | User              |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名   | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 姓名    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 用户排序  | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;userId     | 用户id  | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| deptCode                           | 部门编号  | string            |                   |
| deptName                           | 部门名称  | string            |                   |
| id                                 | 主键    | string            |                   |
| parentId                           | 上级id  | string            |                   |
| sort                               | 部门排序  | integer(int32)    | integer(int32)    |
| users                              | 用户信息  | array             | User              |
| &emsp;&emsp;account                | 用户名   | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;name                   | 姓名    | string            |                   |
| &emsp;&emsp;realname               | 真实姓名  | string            |                   |
| &emsp;&emsp;sort                   | 用户排序  | integer(int32)    |                   |
| &emsp;&emsp;userId                 | 用户id  | string            |                   |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"id": "",
				"parentId": "",
				"sort": 0,
				"users": []
			}
		],
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"parentId": "",
		"sort": 0,
		"users": [
			{
				"account": "",
				"createTime": "",
				"id": "",
				"name": "",
				"realname": "",
				"sort": 0,
				"userId": ""
			}
		]
	}
]
```

## 部门编辑详情

**接口地址**:`/bbt-api/org/department/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|---------|-----------|-------|-------|----------------|--------|
| id      | id        | path  | true  | string         |        |
| current | 页码,默认为1   | query | false | integer(int32) |        |
| size    | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | DepartmentDetailVO |

**响应参数**:

| 参数名称                                  | 参数说明                                             | 类型                         | schema                     |
|---------------------------------------|--------------------------------------------------|----------------------------|----------------------------| 
| address                               | 详细地址                                             | string                     |                            |
| createTime                            | 创建时间                                             | string(date-time)          | string(date-time)          |
| deptCode                              | 部门编号                                             | string                     |                            |
| deptName                              | 部门名称                                             | string                     |                            |
| id                                    | 主键                                               | string                     |                            |
| parentDeptName                        | 上级名称                                             | string                     |                            |
| parentId                              | 上级Id                                             | string                     |                            |
| sort                                  | 部门排序                                             | integer(int32)             | integer(int32)             |
| userPages                             | 人员信息                                             | PageInfo«UserDepartmentVO» | PageInfo«UserDepartmentVO» |
| &emsp;&emsp;countId                   |                                                  | string                     |                            |
| &emsp;&emsp;current                   |                                                  | integer(int64)             |                            |
| &emsp;&emsp;maxLimit                  |                                                  | integer(int64)             |                            |
| &emsp;&emsp;optimizeCountSql          |                                                  | boolean                    |                            |
| &emsp;&emsp;orderMapping              |                                                  | OrderMapping               | OrderMapping               |
| &emsp;&emsp;&emsp;&emsp;map           |                                                  | object                     |                            |
| &emsp;&emsp;&emsp;&emsp;underLineMode |                                                  | boolean                    |                            |
| &emsp;&emsp;orders                    |                                                  | array                      | OrderItem                  |
| &emsp;&emsp;&emsp;&emsp;asc           |                                                  | boolean                    |                            |
| &emsp;&emsp;&emsp;&emsp;column        |                                                  | string                     |                            |
| &emsp;&emsp;pages                     |                                                  | integer(int64)             |                            |
| &emsp;&emsp;paramPage                 |                                                  | 分页参数                       | 分页参数                       |
| &emsp;&emsp;&emsp;&emsp;current       | 页码,默认为1                                          | integer                    |                            |
| &emsp;&emsp;&emsp;&emsp;size          | 页大小,默认为10                                        | integer                    |                            |
| &emsp;&emsp;records                   |                                                  | array                      | UserDepartmentVO           |
| &emsp;&emsp;&emsp;&emsp;account       | 用户名                                              | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;admin         | 是否是管理员                                           | boolean                    |                            |
| &emsp;&emsp;&emsp;&emsp;createTime    | 用户创建时间                                           | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;departmentId  | 部门id                                             | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;id            | 主键                                               | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;phone         | 手机号码                                             | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;realname      | 真实姓名                                             | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;sort          | 排序                                               | integer                    |                            |
| &emsp;&emsp;&emsp;&emsp;state         | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;tenantId      | 租户id                                             | string                     |                            |
| &emsp;&emsp;&emsp;&emsp;userId        | 用户id                                             | string                     |                            |
| &emsp;&emsp;searchCount               |                                                  | boolean                    |                            |
| &emsp;&emsp;size                      |                                                  | integer(int64)             |                            |
| &emsp;&emsp;total                     |                                                  | integer(int64)             |                            |

**响应示例**:

```javascript
{
	"address": "",
	"createTime": "",
	"deptCode": "",
	"deptName": "",
	"id": "",
	"parentDeptName": "",
	"parentId": "",
	"sort": 0,
	"userPages": {
		"countId": "",
		"current": 0,
		"maxLimit": 0,
		"optimizeCountSql": true,
		"orderMapping": {
			"map": {},
			"underLineMode": true
		},
		"orders": [
			{
				"asc": true,
				"column": ""
			}
		],
		"pages": 0,
		"paramPage": {
			"current": 1,
			"size": 10
		},
		"records": [
			{
				"account": "",
				"admin": false,
				"createTime": "",
				"departmentId": "",
				"id": "",
				"phone": "",
				"realname": "",
				"sort": 0,
				"state": "",
				"tenantId": "",
				"userId": ""
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	}
}
```

## 部门移除用户

**接口地址**:`/bbt-api/org/department/user/remove`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "users": [
    {
      "account": "",
      "admin": false,
      "id": "",
      "phone": "",
      "realname": ""
    }
  ]
}
```

**请求参数**:

| 参数名称                             | 参数说明                    | 请求类型 | 是否必须  | 数据类型                    | schema                  |
|----------------------------------|-------------------------|------|-------|-------------------------|-------------------------|
| departmentUserListParam          | DepartmentUserListParam | body | true  | DepartmentUserListParam | DepartmentUserListParam |
| &emsp;&emsp;id                   | 部门id                    |      | false | string                  |                         |
| &emsp;&emsp;users                | 人员信息                    |      | false | array                   | DepartmentUserModify    |
| &emsp;&emsp;&emsp;&emsp;account  | 用户名                     |      | false | string                  |                         |
| &emsp;&emsp;&emsp;&emsp;admin    | 管理员                     |      | false | boolean                 |                         |
| &emsp;&emsp;&emsp;&emsp;id       | 主键                      |      | false | string                  |                         |
| &emsp;&emsp;&emsp;&emsp;phone    | 手机号码                    |      | false | string                  |                         |
| &emsp;&emsp;&emsp;&emsp;realname | 真实姓名                    |      | false | string                  |                         |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 子级部门树形列表

**接口地址**:`/bbt-api/org/department/subtree`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>当前用户下级部门树（不包括当前部门）</p>

**请求参数**:

| 参数名称     | 参数说明     | 请求类型  | 是否必须  | 数据类型    | schema |
|----------|----------|-------|-------|---------|--------|
| userFlag | userFlag | query | false | boolean |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentTreeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| children                           | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;children               | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;deptCode               | 部门编号  | string            |                   |
| &emsp;&emsp;deptName               | 部门名称  | string            |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;parentId               | 上级id  | string            |                   |
| &emsp;&emsp;sort                   | 部门排序  | integer(int32)    |                   |
| &emsp;&emsp;users                  | 用户信息  | array             | User              |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名   | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 姓名    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 用户排序  | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;userId     | 用户id  | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| deptCode                           | 部门编号  | string            |                   |
| deptName                           | 部门名称  | string            |                   |
| id                                 | 主键    | string            |                   |
| parentId                           | 上级id  | string            |                   |
| sort                               | 部门排序  | integer(int32)    | integer(int32)    |
| users                              | 用户信息  | array             | User              |
| &emsp;&emsp;account                | 用户名   | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;name                   | 姓名    | string            |                   |
| &emsp;&emsp;realname               | 真实姓名  | string            |                   |
| &emsp;&emsp;sort                   | 用户排序  | integer(int32)    |                   |
| &emsp;&emsp;userId                 | 用户id  | string            |                   |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"id": "",
				"parentId": "",
				"sort": 0,
				"users": []
			}
		],
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"parentId": "",
		"sort": 0,
		"users": [
			{
				"account": "",
				"createTime": "",
				"id": "",
				"name": "",
				"realname": "",
				"sort": 0,
				"userId": ""
			}
		]
	}
]
```

## 部门分页列表

**接口地址**:`/bbt-api/org/department/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|----------|-----------|-------|-------|----------------|--------|
| current  | 页码,默认为1   | query | false | integer(int32) |        |
| deptCode | 部门编号      | query | false | string         |        |
| deptName | 部门名称      | query | false | string         |        |
| size     | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | 分页结果对象«DepartmentVO» |

**响应参数**:

| 参数名称                   | 参数说明 | 类型                | schema         |
|------------------------|------|-------------------|----------------| 
| current                | 当前页码 | integer(int32)    | integer(int32) |
| pages                  | 总页数  | integer(int32)    | integer(int32) |
| records                | 数据列表 | array             | DepartmentVO   |
| &emsp;&emsp;address    | 详细地址 | string            |                |
| &emsp;&emsp;adminId    | 管理员  | string            |                |
| &emsp;&emsp;createBy   | 创建者  | string            |                |
| &emsp;&emsp;createTime | 创建时间 | string(date-time) |                |
| &emsp;&emsp;deptCode   | 部门编号 | string            |                |
| &emsp;&emsp;deptName   | 部门名称 | string            |                |
| &emsp;&emsp;id         | 主键   | string            |                |
| &emsp;&emsp;level      | 部门层级 | integer(int32)    |                |
| &emsp;&emsp;sort       | 部门排序 | integer(int32)    |                |
| &emsp;&emsp;tel        | 联系方式 | string            |                |
| &emsp;&emsp;updateBy   | 修改者  | string            |                |
| &emsp;&emsp;updateTime | 修改时间 | string(date-time) |                |
| size                   | 页大小  | integer(int32)    | integer(int32) |
| total                  | 总行数  | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"address": "",
			"adminId": "",
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"id": "",
			"level": 0,
			"sort": 0,
			"tel": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 当前用户的下级网格以及微网格

**接口地址**:`/bbt-api/org/department/current/grid`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentTreeVO |

**响应参数**:

| 参数名称                               | 参数说明  | 类型                | schema            |
|------------------------------------|-------|-------------------|-------------------| 
| children                           | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;children               | 子部门信息 | array             | DepartmentTreeVO  |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;deptCode               | 部门编号  | string            |                   |
| &emsp;&emsp;deptName               | 部门名称  | string            |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;parentId               | 上级id  | string            |                   |
| &emsp;&emsp;sort                   | 部门排序  | integer(int32)    |                   |
| &emsp;&emsp;users                  | 用户信息  | array             | User              |
| &emsp;&emsp;&emsp;&emsp;account    | 用户名   | string            |                   |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;id         | 主键    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;name       | 姓名    | string            |                   |
| &emsp;&emsp;&emsp;&emsp;realname   | 真实姓名  | string            |                   |
| &emsp;&emsp;&emsp;&emsp;sort       | 用户排序  | integer           |                   |
| &emsp;&emsp;&emsp;&emsp;userId     | 用户id  | string            |                   |
| createTime                         | 创建时间  | string(date-time) | string(date-time) |
| deptCode                           | 部门编号  | string            |                   |
| deptName                           | 部门名称  | string            |                   |
| id                                 | 主键    | string            |                   |
| parentId                           | 上级id  | string            |                   |
| sort                               | 部门排序  | integer(int32)    | integer(int32)    |
| users                              | 用户信息  | array             | User              |
| &emsp;&emsp;account                | 用户名   | string            |                   |
| &emsp;&emsp;createTime             | 创建时间  | string(date-time) |                   |
| &emsp;&emsp;id                     | 主键    | string            |                   |
| &emsp;&emsp;name                   | 姓名    | string            |                   |
| &emsp;&emsp;realname               | 真实姓名  | string            |                   |
| &emsp;&emsp;sort                   | 用户排序  | integer(int32)    |                   |
| &emsp;&emsp;userId                 | 用户id  | string            |                   |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"id": "",
				"parentId": "",
				"sort": 0,
				"users": []
			}
		],
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"parentId": "",
		"sort": 0,
		"users": [
			{
				"account": "",
				"createTime": "",
				"id": "",
				"name": "",
				"realname": "",
				"sort": 0,
				"userId": ""
			}
		]
	}
]
```

## 批量修改用户区域

**接口地址**:`/bbt-api/org/department/regionUpdate`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明       | 请求类型  | 是否必须  | 数据类型   | schema |
|------------|------------|-------|-------|--------|--------|
| regionName | regionName | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 当前登录部门所有用户

**接口地址**:`/bbt-api/org/department/current/dept/user`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | DepartmentUserVO |

**响应参数**:

| 参数名称     | 参数说明                                             | 类型             | schema         |
|----------|--------------------------------------------------|----------------|----------------| 
| account  | 用户名                                              | string         |                |
| admin    | 管理员                                              | boolean        |                |
| id       | 主键                                               | string         |                |
| phone    | 手机号码                                             | string         |                |
| realname | 真实姓名                                             | string         |                |
| sort     | 部门用户排序                                           | integer(int32) | integer(int32) |
| state    | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                |
| userId   | 用户id                                             | string         |                |

**响应示例**:

```javascript
[
	{
		"account": "",
		"admin": false,
		"id": "",
		"phone": "",
		"realname": "",
		"sort": 0,
		"state": "",
		"userId": ""
	}
]
```

# 用户初始化

## 初始化管理员账号

**接口地址**:`/bbt-api/org/init/user/initAdmin`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 天府用户信息手动同步

**接口地址**:`/bbt-api/org/init/user/tfsync`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 初始化用户信息

**接口地址**:`/bbt-api/org/init/user/init`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 合并用户信息

**接口地址**:`/bbt-api/org/init/user/merge`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 导出当前系统用户信息

**接口地址**:`/bbt-api/org/init/user/export`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 用户消息表

## 用户消息表详情

**接口地址**:`/bbt-api/org/user/message/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | OrgUserMessageVO |

**响应参数**:

| 参数名称        | 参数说明                                               | 类型                | schema            |
|-------------|----------------------------------------------------|-------------------|-------------------| 
| content     | 内容                                                 | string            |                   |
| creator     | 创建人                                                | string            |                   |
| creatorName |                                                    | string            |                   |
| deptCode    | 部门Code                                             | string            |                   |
| id          | 主键                                                 | string            |                   |
| messageId   | 消息id                                               | string            |                   |
| sendDate    | 发布日期                                               | string(date-time) | string(date-time) |
| status      | 读取状态,可用值:READ,UNREAD                               | string            |                   |
| title       | 标题                                                 | string            |                   |
| type        | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE | string            |                   |
| userId      | 用户id                                               | string            |                   |

**响应示例**:

```javascript
{
	"content": "",
	"creator": "",
	"creatorName": "",
	"deptCode": "",
	"id": "",
	"messageId": "",
	"sendDate": "",
	"status": "",
	"title": "",
	"type": "",
	"userId": ""
}
```

## 更新用户消息表

**接口地址**:`/bbt-api/org/user/message/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "content": "",
  "creator": "",
  "id": "",
  "messageId": "",
  "sendDate": "",
  "status": "",
  "title": "",
  "type": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称                  | 参数说明                                               | 请求类型 | 是否必须  | 数据类型              | schema    |
|-----------------------|----------------------------------------------------|------|-------|-------------------|-----------|
| 用户消息表修改参数             | 用户消息表修改参数                                          | body | true  | 用户消息表修改参数         | 用户消息表修改参数 |
| &emsp;&emsp;content   | 内容                                                 |      | false | string            |           |
| &emsp;&emsp;creator   | 创建人                                                |      | false | string            |           |
| &emsp;&emsp;id        | 主键                                                 |      | false | string            |           |
| &emsp;&emsp;messageId | 消息id                                               |      | false | string            |           |
| &emsp;&emsp;sendDate  | 发布日期                                               |      | false | string(date-time) |           |
| &emsp;&emsp;status    | 读取状态,可用值:READ,UNREAD                               |      | false | string            |           |
| &emsp;&emsp;title     | 标题                                                 |      | false | string            |           |
| &emsp;&emsp;type      | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE |      | false | string            |           |
| &emsp;&emsp;userId    | 用户id                                               |      | false | string            |           |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 新增用户消息表

**接口地址**:`/bbt-api/org/user/message/add`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "content": "",
  "creator": "",
  "messageId": "",
  "sendDate": "",
  "status": "",
  "title": "",
  "type": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称                  | 参数说明                                               | 请求类型 | 是否必须  | 数据类型              | schema    |
|-----------------------|----------------------------------------------------|------|-------|-------------------|-----------|
| 用户消息表新增参数             | 用户消息表新增参数                                          | body | true  | 用户消息表新增参数         | 用户消息表新增参数 |
| &emsp;&emsp;content   | 内容                                                 |      | false | string            |           |
| &emsp;&emsp;creator   | 创建人                                                |      | false | string            |           |
| &emsp;&emsp;messageId | 消息id                                               |      | false | string            |           |
| &emsp;&emsp;sendDate  | 发布日期                                               |      | false | string(date-time) |           |
| &emsp;&emsp;status    | 读取状态,可用值:READ,UNREAD                               |      | false | string            |           |
| &emsp;&emsp;title     | 标题                                                 |      | false | string            |           |
| &emsp;&emsp;type      | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE |      | false | string            |           |
| &emsp;&emsp;userId    | 用户id                                               |      | false | string            |           |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除用户消息表

**接口地址**:`/bbt-api/org/user/message/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 用户消息表Feign接口

## getMessageList

**接口地址**:`/bbt-api/api/org/user/message/getMessageList`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "current": 1,
  "deptCode": "",
  "end": "",
  "size": 10,
  "start": "",
  "status": "",
  "title": "",
  "type": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称                 | 参数说明                                               | 请求类型 | 是否必须  | 数据类型           | schema      |
|----------------------|----------------------------------------------------|------|-------|----------------|-------------|
| 用户消息表列表查询参数          | 用户消息表列表查询参数                                        | body | true  | 用户消息表列表查询参数    | 用户消息表列表查询参数 |
| &emsp;&emsp;current  | 页码,默认为1                                            |      | false | integer(int32) |             |
| &emsp;&emsp;deptCode | 用户deptCode                                         |      | false | string         |             |
| &emsp;&emsp;end      | 结束时间                                               |      | false | string         |             |
| &emsp;&emsp;size     | 页大小,默认为10                                          |      | false | integer(int32) |             |
| &emsp;&emsp;start    | 开始时间                                               |      | false | string         |             |
| &emsp;&emsp;status   | 读取状态,可用值:READ,UNREAD                               |      | false | string         |             |
| &emsp;&emsp;title    | 标题                                                 |      | false | string         |             |
| &emsp;&emsp;type     | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE |      | false | string         |             |
| &emsp;&emsp;userId   | 用户id                                               |      | false | string         |             |

**响应状态**:

| 状态码 | 说明 | schema                           |
|-----|----|----------------------------------| 
| 200 | OK | Result«分页结果对象«OrgUserMessageAO»» |

**响应参数**:

| 参数名称                                | 参数说明                                          | 类型                       | schema                   |
|-------------------------------------|-----------------------------------------------|--------------------------|--------------------------| 
| code                                |                                               | integer(int32)           | integer(int32)           |
| data                                |                                               | 分页结果对象«OrgUserMessageAO» | 分页结果对象«OrgUserMessageAO» |
| &emsp;&emsp;current                 | 当前页码                                          | integer(int32)           |                          |
| &emsp;&emsp;pages                   | 总页数                                           | integer(int32)           |                          |
| &emsp;&emsp;records                 | 数据列表                                          | array                    | OrgUserMessageAO         |
| &emsp;&emsp;&emsp;&emsp;content     |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;creator     |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;creatorName |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;deptCode    |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;id          |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;messageId   |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;sendDate    |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;status      | 可用值:READ,UNREAD                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;title       |                                               | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;type        | 可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE | string                   |                          |
| &emsp;&emsp;&emsp;&emsp;userId      |                                               | string                   |                          |
| &emsp;&emsp;size                    | 页大小                                           | integer(int32)           |                          |
| &emsp;&emsp;total                   | 总行数                                           | integer(int32)           |                          |
| message                             |                                               | string                   |                          |
| stack                               |                                               | string                   |                          |
| success                             |                                               | boolean                  |                          |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"content": "",
				"creator": "",
				"creatorName": "",
				"deptCode": "",
				"id": "",
				"messageId": "",
				"sendDate": "",
				"status": "",
				"title": "",
				"type": "",
				"userId": ""
			}
		],
		"size": 0,
		"total": 0
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## smsMessage

**接口地址**:`/bbt-api/api/org/user/message/smsMessage`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| id   | id   | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## messagesRead

**接口地址**:`/bbt-api/api/org/user/message/messagesRead`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## addMessage

**接口地址**:`/bbt-api/api/org/user/message/addMessage`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "content": "",
  "creator": "",
  "messageId": "",
  "sendDate": "",
  "status": "",
  "title": "",
  "type": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称                  | 参数说明                                               | 请求类型 | 是否必须  | 数据类型              | schema    |
|-----------------------|----------------------------------------------------|------|-------|-------------------|-----------|
| 用户消息表新增参数             | 用户消息表新增参数                                          | body | true  | 用户消息表新增参数         | 用户消息表新增参数 |
| &emsp;&emsp;content   | 内容                                                 |      | false | string            |           |
| &emsp;&emsp;creator   | 创建人                                                |      | false | string            |           |
| &emsp;&emsp;messageId | 消息id                                               |      | false | string            |           |
| &emsp;&emsp;sendDate  | 发布日期                                               |      | false | string(date-time) |           |
| &emsp;&emsp;status    | 读取状态,可用值:READ,UNREAD                               |      | false | string            |           |
| &emsp;&emsp;title     | 标题                                                 |      | false | string            |           |
| &emsp;&emsp;type      | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE |      | false | string            |           |
| &emsp;&emsp;userId    | 用户id                                               |      | false | string            |           |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## getMessageOne

**接口地址**:`/bbt-api/api/org/user/message/getMessageOne`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| id   | id   | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                         |
|-----|----|--------------------------------| 
| 200 | OK | Result«OrgUserMessageDetailAO» |

**响应参数**:

| 参数名称                    | 参数说明                                               | 类型                     | schema                 |
|-------------------------|----------------------------------------------------|------------------------|------------------------| 
| code                    |                                                    | integer(int32)         | integer(int32)         |
| data                    |                                                    | OrgUserMessageDetailAO | OrgUserMessageDetailAO |
| &emsp;&emsp;content     | 内容                                                 | string                 |                        |
| &emsp;&emsp;creator     | 创建人                                                | string                 |                        |
| &emsp;&emsp;creatorName |                                                    | string                 |                        |
| &emsp;&emsp;deptCode    | 部门Code                                             | string                 |                        |
| &emsp;&emsp;id          | 主键                                                 | string                 |                        |
| &emsp;&emsp;images      | 系统公告图片                                             | object                 |                        |
| &emsp;&emsp;messageId   | 消息id                                               | string                 |                        |
| &emsp;&emsp;sendDate    | 发布日期                                               | string(date-time)      |                        |
| &emsp;&emsp;status      | 读取状态,可用值:READ,UNREAD                               | string                 |                        |
| &emsp;&emsp;title       | 标题                                                 | string                 |                        |
| &emsp;&emsp;type        | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE | string                 |                        |
| &emsp;&emsp;userId      | 用户id                                               | string                 |                        |
| message                 |                                                    | string                 |                        |
| stack                   |                                                    | string                 |                        |
| success                 |                                                    | boolean                |                        |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"content": "",
		"creator": "",
		"creatorName": "",
		"deptCode": "",
		"id": "",
		"images": {},
		"messageId": "",
		"sendDate": "",
		"status": "",
		"title": "",
		"type": "",
		"userId": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

# 用户菜单收藏表

## 删除用户菜单收藏表

**接口地址**:`/bbt-api/org/user/menu/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 判断是否收藏用户菜单收藏表

**接口地址**:`/bbt-api/org/user/menu/favorite`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "menuId": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称               | 参数说明        | 请求类型 | 是否必须  | 数据类型        | schema      |
|--------------------|-------------|------|-------|-------------|-------------|
| 用户菜单收藏表新增参数        | 用户菜单收藏表新增参数 | body | true  | 用户菜单收藏表新增参数 | 用户菜单收藏表新增参数 |
| &emsp;&emsp;menuId | 菜单id        |      | false | string      |             |
| &emsp;&emsp;userId | 用户id        |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 点击用户菜单收藏表

**接口地址**:`/bbt-api/org/user/menu/click`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "menuId": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称               | 参数说明        | 请求类型 | 是否必须  | 数据类型        | schema      |
|--------------------|-------------|------|-------|-------------|-------------|
| 用户菜单收藏表新增参数        | 用户菜单收藏表新增参数 | body | true  | 用户菜单收藏表新增参数 | 用户菜单收藏表新增参数 |
| &emsp;&emsp;menuId | 菜单id        |      | false | string      |             |
| &emsp;&emsp;userId | 用户id        |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 新增用户菜单收藏表

**接口地址**:`/bbt-api/org/user/menu/add`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "menuId": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称               | 参数说明        | 请求类型 | 是否必须  | 数据类型        | schema      |
|--------------------|-------------|------|-------|-------------|-------------|
| 用户菜单收藏表新增参数        | 用户菜单收藏表新增参数 | body | true  | 用户菜单收藏表新增参数 | 用户菜单收藏表新增参数 |
| &emsp;&emsp;menuId | 菜单id        |      | false | string      |             |
| &emsp;&emsp;userId | 用户id        |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 用户菜单收藏表详情

**接口地址**:`/bbt-api/org/user/menu/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema        |
|-----|----|---------------| 
| 200 | OK | OrgUserMenuVO |

**响应参数**:

| 参数名称   | 参数说明                   | 类型     | schema |
|--------|------------------------|--------|--------| 
| id     | 主键                     | string |        |
| menuId | 菜单id                   | string |        |
| status | 状态,可用值:MARKED,UNMARKED | string |        |
| userId | 用户id                   | string |        |

**响应示例**:

```javascript
{
	"id": "",
	"menuId": "",
	"status": "",
	"userId": ""
}
```

## 更新用户菜单收藏表

**接口地址**:`/bbt-api/org/user/menu/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "menuId": "",
  "userId": ""
}
```

**请求参数**:

| 参数名称               | 参数说明        | 请求类型 | 是否必须  | 数据类型        | schema      |
|--------------------|-------------|------|-------|-------------|-------------|
| 用户菜单收藏表修改参数        | 用户菜单收藏表修改参数 | body | true  | 用户菜单收藏表修改参数 | 用户菜单收藏表修改参数 |
| &emsp;&emsp;id     | 主键          |      | false | string      |             |
| &emsp;&emsp;menuId | 菜单id        |      | false | string      |             |
| &emsp;&emsp;userId | 用户id        |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 用户菜单收藏表分页列表

**接口地址**:`/bbt-api/org/user/menu/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明      | 请求类型  | 是否必须  | 数据类型              | schema |
|---------|-----------|-------|-------|-------------------|--------|
| current | 页码,默认为1   | query | false | integer(int32)    |        |
| end     | 结束时间      | query | false | string(date-time) |        |
| menuId  | 菜单id      | query | false | string            |        |
| size    | 页大小,默认为10 | query | false | integer(int32)    |        |
| start   | 开始时间      | query | false | string(date-time) |        |
| userId  | 用户id      | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema                |
|-----|----|-----------------------| 
| 200 | OK | 分页结果对象«OrgUserMenuVO» |

**响应参数**:

| 参数名称               | 参数说明                   | 类型             | schema         |
|--------------------|------------------------|----------------|----------------| 
| current            | 当前页码                   | integer(int32) | integer(int32) |
| pages              | 总页数                    | integer(int32) | integer(int32) |
| records            | 数据列表                   | array          | OrgUserMenuVO  |
| &emsp;&emsp;id     | 主键                     | string         |                |
| &emsp;&emsp;menuId | 菜单id                   | string         |                |
| &emsp;&emsp;status | 状态,可用值:MARKED,UNMARKED | string         |                |
| &emsp;&emsp;userId | 用户id                   | string         |                |
| size               | 页大小                    | integer(int32) | integer(int32) |
| total              | 总行数                    | integer(int32) | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"id": "",
			"menuId": "",
			"status": "",
			"userId": ""
		}
	],
	"size": 0,
	"total": 0
}
```

# 用户菜单收藏表Feign接口

## getMenus

**接口地址**:`/bbt-api/api/org/user/menu`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«List«MenuVO»» |

**响应参数**:

| 参数名称                  | 参数说明            | 类型             | schema         |
|-----------------------|-----------------|----------------|----------------| 
| code                  |                 | integer(int32) | integer(int32) |
| data                  |                 | array          | MenuVO         |
| &emsp;&emsp;children  | 子集              | array          | MenuVO         |
| &emsp;&emsp;component | 目标组件            | string         |                |
| &emsp;&emsp;hidden    | 显示和隐藏，0：显示，1：隐藏 | boolean        |                |
| &emsp;&emsp;icon      | 前端图标            | string         |                |
| &emsp;&emsp;id        | ID              | string         |                |
| &emsp;&emsp;level     | 菜单级数            | integer(int32) |                |
| &emsp;&emsp;meta      | 菜单配置            | object         |                |
| &emsp;&emsp;name      | 前端名称            | string         |                |
| &emsp;&emsp;parentId  | 父级ID            | integer(int64) |                |
| &emsp;&emsp;path      | 前端路由            | string         |                |
| &emsp;&emsp;roles     |                 | array          | string         |
| &emsp;&emsp;state     | 菜单状态，0：禁用，1：启用  | boolean        |                |
| &emsp;&emsp;title     | controller名称    | string         |                |
| message               |                 | string         |                |
| stack                 |                 | string         |                |
| success               |                 | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"children": [
				{
					"children": [
						{}
					],
					"component": "",
					"hidden": false,
					"icon": "",
					"id": "",
					"level": 0,
					"meta": {},
					"name": "",
					"parentId": 0,
					"path": "",
					"roles": [],
					"state": false,
					"title": ""
				}
			],
			"component": "",
			"hidden": false,
			"icon": "",
			"id": "",
			"level": 0,
			"meta": {},
			"name": "",
			"parentId": 0,
			"path": "",
			"roles": [],
			"state": false,
			"title": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

# 用户角色Feign接口

## getUserRole

**接口地址**:`/bbt-api/api/org/user/role/getUserRole`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须 | 数据类型   | schema |
|---------|---------|-------|------|--------|--------|
| userIds | userIds | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                   |
|-----|----|--------------------------| 
| 200 | OK | Result«List«UserRoleAO»» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | array          | UserRoleAO     |
| &emsp;&emsp;id       |      | string         |                |
| &emsp;&emsp;roleCode |      | string         |                |
| &emsp;&emsp;roleId   |      | string         |                |
| &emsp;&emsp;roleName |      | string         |                |
| &emsp;&emsp;userId   |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"id": "",
			"roleCode": "",
			"roleId": "",
			"roleName": "",
			"userId": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取角色列表

**接口地址**:`/bbt-api/api/org/role/getRoleList`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«List«RoleAO»» |

**响应参数**:

| 参数名称                                           | 参数说明   | 类型                | schema         |
|------------------------------------------------|--------|-------------------|----------------| 
| code                                           |        | integer(int32)    | integer(int32) |
| data                                           |        | array             | RoleAO         |
| &emsp;&emsp;code                               |        | string            |                |
| &emsp;&emsp;createBy                           |        | string            |                |
| &emsp;&emsp;createTime                         |        | string(date-time) |                |
| &emsp;&emsp;description                        |        | string            |                |
| &emsp;&emsp;id                                 |        | string            |                |
| &emsp;&emsp;menuIds                            |        | array             | string         |
| &emsp;&emsp;name                               |        | string            |                |
| &emsp;&emsp;permissionList                     |        | array             | FormPermission |
| &emsp;&emsp;&emsp;&emsp;operates               | 操作集合   | array             | string         |
| &emsp;&emsp;&emsp;&emsp;table                  | 表单列表   | FormTable         | FormTable      |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id         | 主键ID   | string            |                |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tableLabel | 数据表中文名 | string            |                |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tableName  | 数据表名   | string            |                |
| &emsp;&emsp;permissions                        |        | array             | PermissionAO   |
| &emsp;&emsp;&emsp;&emsp;operateIds             |        | array             | string         |
| &emsp;&emsp;&emsp;&emsp;tableId                |        | string            |                |
| &emsp;&emsp;state                              |        | string            |                |
| &emsp;&emsp;updateBy                           |        | string            |                |
| &emsp;&emsp;updateTime                         |        | string(date-time) |                |
| &emsp;&emsp;userIds                            |        | array             | string         |
| &emsp;&emsp;userList                           |        | array             | UserInfo       |
| &emsp;&emsp;&emsp;&emsp;id                     |        | Serializable      | Serializable   |
| &emsp;&emsp;&emsp;&emsp;name                   |        | string            |                |
| &emsp;&emsp;&emsp;&emsp;nickName               |        | string            |                |
| &emsp;&emsp;&emsp;&emsp;realName               |        | string            |                |
| &emsp;&emsp;version                            |        | integer(int64)    |                |
| message                                        |        | string            |                |
| stack                                          |        | string            |                |
| success                                        |        | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"code": "",
			"createBy": "",
			"createTime": "",
			"description": "",
			"id": "",
			"menuIds": [],
			"name": "",
			"permissionList": [
				{
					"operates": [],
					"table": {
						"id": "",
						"tableLabel": "",
						"tableName": ""
					}
				}
			],
			"permissions": [
				{
					"operateIds": [],
					"tableId": ""
				}
			],
			"state": "",
			"updateBy": "",
			"updateTime": "",
			"userIds": [],
			"userList": [
				{
					"id": {},
					"name": "",
					"nickName": "",
					"realName": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取角色详情

**接口地址**:`/bbt-api/api/org/role/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | Result«RoleAO» |

**响应参数**:

| 参数名称                                           | 参数说明   | 类型                | schema         |
|------------------------------------------------|--------|-------------------|----------------| 
| code                                           |        | integer(int32)    | integer(int32) |
| data                                           |        | RoleAO            | RoleAO         |
| &emsp;&emsp;code                               |        | string            |                |
| &emsp;&emsp;createBy                           |        | string            |                |
| &emsp;&emsp;createTime                         |        | string(date-time) |                |
| &emsp;&emsp;description                        |        | string            |                |
| &emsp;&emsp;id                                 |        | string            |                |
| &emsp;&emsp;menuIds                            |        | array             | string         |
| &emsp;&emsp;name                               |        | string            |                |
| &emsp;&emsp;permissionList                     |        | array             | FormPermission |
| &emsp;&emsp;&emsp;&emsp;operates               | 操作集合   | array             | string         |
| &emsp;&emsp;&emsp;&emsp;table                  | 表单列表   | FormTable         | FormTable      |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id         | 主键ID   | string            |                |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tableLabel | 数据表中文名 | string            |                |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tableName  | 数据表名   | string            |                |
| &emsp;&emsp;permissions                        |        | array             | PermissionAO   |
| &emsp;&emsp;&emsp;&emsp;operateIds             |        | array             | string         |
| &emsp;&emsp;&emsp;&emsp;tableId                |        | string            |                |
| &emsp;&emsp;state                              |        | string            |                |
| &emsp;&emsp;updateBy                           |        | string            |                |
| &emsp;&emsp;updateTime                         |        | string(date-time) |                |
| &emsp;&emsp;userIds                            |        | array             | string         |
| &emsp;&emsp;userList                           |        | array             | UserInfo       |
| &emsp;&emsp;&emsp;&emsp;id                     |        | Serializable      | Serializable   |
| &emsp;&emsp;&emsp;&emsp;name                   |        | string            |                |
| &emsp;&emsp;&emsp;&emsp;nickName               |        | string            |                |
| &emsp;&emsp;&emsp;&emsp;realName               |        | string            |                |
| &emsp;&emsp;version                            |        | integer(int64)    |                |
| message                                        |        | string            |                |
| stack                                          |        | string            |                |
| success                                        |        | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"code": "",
		"createBy": "",
		"createTime": "",
		"description": "",
		"id": "",
		"menuIds": [],
		"name": "",
		"permissionList": [
			{
				"operates": [],
				"table": {
					"id": "",
					"tableLabel": "",
					"tableName": ""
				}
			}
		],
		"permissions": [
			{
				"operateIds": [],
				"tableId": ""
			}
		],
		"state": "",
		"updateBy": "",
		"updateTime": "",
		"userIds": [],
		"userList": [
			{
				"id": {},
				"name": "",
				"nickName": "",
				"realName": ""
			}
		],
		"version": 0
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取所有的角色列表

**接口地址**:`/bbt-api/api/org/role/getUserIdByRoleId`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须 | 数据类型   | schema |
|---------|---------|-------|------|--------|--------|
| roleIds | roleIds | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«Set«Serializable»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          | Serializable   |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取所有用户角色对应信息

**接口地址**:`/bbt-api/api/org/user/role/allUserRole`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                   |
|-----|----|--------------------------| 
| 200 | OK | Result«List«UserRoleAO»» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | array          | UserRoleAO     |
| &emsp;&emsp;id       |      | string         |                |
| &emsp;&emsp;roleCode |      | string         |                |
| &emsp;&emsp;roleId   |      | string         |                |
| &emsp;&emsp;roleName |      | string         |                |
| &emsp;&emsp;userId   |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"id": "",
			"roleCode": "",
			"roleId": "",
			"roleName": "",
			"userId": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

# 移动端登录接口

## 登录

**接口地址**:`/bbt-api/org/app/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:<p>账号密码登录</p>

**请求示例**:

```javascript
{
  "deptCodes": [],
  "roleIds": [
    {}
  ],
  "roleNames": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明 | 请求类型 | 是否必须  | 数据类型  | schema       |
|-----------------------|------|------|-------|-------|--------------|
| 登录参数                  | 登录参数 | body | true  | 登录参数  | 登录参数         |
| &emsp;&emsp;deptCodes |      |      | false | array | string       |
| &emsp;&emsp;roleIds   |      |      | false | array | Serializable |
| &emsp;&emsp;roleNames |      |      | false | array | Serializable |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | LoginSuccessVO |

**响应参数**:

| 参数名称       | 参数说明    | 类型      | schema |
|------------|---------|---------|--------| 
| firstLogin | 是否为首次登录 | boolean |        |
| name       | 请求头名称   | string  |        |
| token      | 访问令牌    | string  |        |

**响应示例**:

```javascript
{
	"firstLogin": false,
	"name": "",
	"token": ""
}
```

## 获取当前登录用户信息

**接口地址**:`/bbt-api/org/app/user`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>用户信息包括用户菜单, 基本信息</p>

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | AppUserSessionVO |

**响应参数**:

| 参数名称                   | 参数说明                                             | 类型                | schema            |
|------------------------|--------------------------------------------------|-------------------|-------------------| 
| account                | 用户名                                              | string            |                   |
| address                | 详细地址                                             | string            |                   |
| admin                  | 是否是机构管理员                                         | boolean           |                   |
| createBy               | 创建者                                              | string            |                   |
| createTime             | 创建时间                                             | string(date-time) | string(date-time) |
| department             | 当前部门信息                                           | AppDepartmentVO   | AppDepartmentVO   |
| &emsp;&emsp;address    | 详细地址                                             | string            |                   |
| &emsp;&emsp;adminId    | 管理员                                              | string            |                   |
| &emsp;&emsp;createBy   | 创建者                                              | string            |                   |
| &emsp;&emsp;createTime | 创建时间                                             | string(date-time) |                   |
| &emsp;&emsp;deptCode   | 部门编号                                             | string            |                   |
| &emsp;&emsp;deptName   | 部门名称                                             | string            |                   |
| &emsp;&emsp;id         | 主键                                               | string            |                   |
| &emsp;&emsp;level      | 部门层级                                             | integer(int32)    |                   |
| &emsp;&emsp;tel        | 联系方式                                             | string            |                   |
| &emsp;&emsp;updateBy   | 修改者                                              | string            |                   |
| &emsp;&emsp;updateTime | 修改时间                                             | string(date-time) |                   |
| departmentName         | 部门名称                                             | string            |                   |
| departments            | 所属部门信息                                           | array             | AppDepartmentVO   |
| &emsp;&emsp;address    | 详细地址                                             | string            |                   |
| &emsp;&emsp;adminId    | 管理员                                              | string            |                   |
| &emsp;&emsp;createBy   | 创建者                                              | string            |                   |
| &emsp;&emsp;createTime | 创建时间                                             | string(date-time) |                   |
| &emsp;&emsp;deptCode   | 部门编号                                             | string            |                   |
| &emsp;&emsp;deptName   | 部门名称                                             | string            |                   |
| &emsp;&emsp;id         | 主键                                               | string            |                   |
| &emsp;&emsp;level      | 部门层级                                             | integer(int32)    |                   |
| &emsp;&emsp;tel        | 联系方式                                             | string            |                   |
| &emsp;&emsp;updateBy   | 修改者                                              | string            |                   |
| &emsp;&emsp;updateTime | 修改时间                                             | string(date-time) |                   |
| description            | 介绍                                               | string            |                   |
| email                  | 邮箱                                               | string            |                   |
| gender                 | 性别，0：保密, 1：男，2：女，默认0,可用值:FEMALE,MALE,UNKNOWN     | string            |                   |
| head                   | 头像ID                                             | string            |                   |
| id                     | 主键                                               | string            |                   |
| lastLogin              | 最后登录时间                                           | string(date-time) | string(date-time) |
| loginCount             | 登录次数                                             | integer(int64)    | integer(int64)    |
| menus                  | 用户菜单                                             | array             | UserMenuAO        |
| &emsp;&emsp;children   |                                                  | array             | UserMenuAO        |
| &emsp;&emsp;component  |                                                  | string            |                   |
| &emsp;&emsp;hidden     |                                                  | boolean           |                   |
| &emsp;&emsp;icon       |                                                  | string            |                   |
| &emsp;&emsp;id         |                                                  | string            |                   |
| &emsp;&emsp;meta       |                                                  | object            |                   |
| &emsp;&emsp;name       |                                                  | string            |                   |
| &emsp;&emsp;path       |                                                  | string            |                   |
| &emsp;&emsp;roles      |                                                  | array             | string            |
| &emsp;&emsp;sort       |                                                  | integer(int32)    |                   |
| &emsp;&emsp;state      |                                                  | boolean           |                   |
| phone                  | 手机号码                                             | string            |                   |
| realname               | 真实姓名                                             | string            |                   |
| roleIds                | 角色id列表                                           | array             |                   |
| roleName               | 角色名称                                             | string            |                   |
| roles                  | 角色信息                                             | array             | Role              |
| &emsp;&emsp;roleCode   |                                                  | string            |                   |
| &emsp;&emsp;roleId     |                                                  | string            |                   |
| &emsp;&emsp;roleName   |                                                  | string            |                   |
| state                  | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                   |
| updateBy               | 修改者                                              | string            |                   |
| updateTime             | 修改时间                                             | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"account": "",
	"address": "",
	"admin": false,
	"createBy": "",
	"createTime": "",
	"department": {
		"address": "",
		"adminId": "",
		"createBy": "",
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"id": "",
		"level": 0,
		"tel": "",
		"updateBy": "",
		"updateTime": ""
	},
	"departmentName": "",
	"departments": [
		{
			"address": "",
			"adminId": "",
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"id": "",
			"level": 0,
			"tel": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"description": "",
	"email": "",
	"gender": "",
	"head": "",
	"id": "",
	"lastLogin": "",
	"loginCount": 0,
	"menus": [
		{
			"children": [
				{
					"children": [
						{}
					],
					"component": "",
					"hidden": true,
					"icon": "",
					"id": "",
					"meta": {},
					"name": "",
					"path": "",
					"roles": [],
					"sort": 0,
					"state": true
				}
			],
			"component": "",
			"hidden": true,
			"icon": "",
			"id": "",
			"meta": {},
			"name": "",
			"path": "",
			"roles": [],
			"sort": 0,
			"state": true
		}
	],
	"phone": "",
	"realname": "",
	"roleIds": [],
	"roleName": "",
	"roles": [
		{
			"roleCode": "",
			"roleId": "",
			"roleName": ""
		}
	],
	"state": "",
	"updateBy": "",
	"updateTime": ""
}
```

# 系统-日志表

## 删除系统-日志表

**接口地址**:`/bbt-api/sys/log/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 新增系统-日志表

**接口地址**:`/bbt-api/sys/log`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "browser": "",
  "exception": "",
  "ip": "",
  "layer": 0,
  "message": "",
  "operator": "",
  "operatorAccount": "",
  "operatorId": "",
  "remark": "",
  "url": ""
}
```

**请求参数**:

| 参数名称                        | 参数说明       | 请求类型 | 是否必须  | 数据类型           | schema     |
|-----------------------------|------------|------|-------|----------------|------------|
| 系统-日志表新增参数                  | 系统-日志表新增参数 | body | true  | 系统-日志表新增参数     | 系统-日志表新增参数 |
| &emsp;&emsp;browser         | 浏览器        |      | false | string         |            |
| &emsp;&emsp;exception       | 错误         |      | false | string         |            |
| &emsp;&emsp;ip              | IP         |      | false | string         |            |
| &emsp;&emsp;layer           | 级别         |      | false | integer(int64) |            |
| &emsp;&emsp;message         | 信息         |      | false | string         |            |
| &emsp;&emsp;operator        | 操作人        |      | false | string         |            |
| &emsp;&emsp;operatorAccount | 操作账户       |      | false | string         |            |
| &emsp;&emsp;operatorId      | 操作人id      |      | false | string         |            |
| &emsp;&emsp;remark          | 备注         |      | false | string         |            |
| &emsp;&emsp;url             | 地址         |      | false | string         |            |

**响应状态**:

| 状态码 | 说明 | schema          |
|-----|----|-----------------| 
| 200 | OK | Result«boolean» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | boolean        |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": true,
	"message": "",
	"stack": "",
	"success": true
}
```

## 更新系统-日志表

**接口地址**:`/bbt-api/sys/log/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "browser": "",
  "exception": "",
  "id": "",
  "ip": "",
  "layer": 0,
  "message": "",
  "operator": "",
  "operatorAccount": "",
  "operatorId": "",
  "remark": "",
  "url": ""
}
```

**请求参数**:

| 参数名称                        | 参数说明       | 请求类型 | 是否必须  | 数据类型           | schema     |
|-----------------------------|------------|------|-------|----------------|------------|
| 系统-日志表修改参数                  | 系统-日志表修改参数 | body | true  | 系统-日志表修改参数     | 系统-日志表修改参数 |
| &emsp;&emsp;browser         | 浏览器        |      | false | string         |            |
| &emsp;&emsp;exception       | 错误         |      | false | string         |            |
| &emsp;&emsp;id              | 主键ID       |      | false | string         |            |
| &emsp;&emsp;ip              | IP         |      | false | string         |            |
| &emsp;&emsp;layer           | 级别         |      | false | integer(int64) |            |
| &emsp;&emsp;message         | 信息         |      | false | string         |            |
| &emsp;&emsp;operator        | 操作人        |      | false | string         |            |
| &emsp;&emsp;operatorAccount | 操作账户       |      | false | string         |            |
| &emsp;&emsp;operatorId      | 操作人id      |      | false | string         |            |
| &emsp;&emsp;remark          | 备注         |      | false | string         |            |
| &emsp;&emsp;url             | 地址         |      | false | string         |            |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 系统-日志表导出日志

**接口地址**:`/bbt-api/sys/log/export`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 系统-日志表详情

**接口地址**:`/bbt-api/sys/log/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema   |
|-----|----|----------| 
| 200 | OK | SysLogVO |

**响应参数**:

| 参数名称            | 参数说明  | 类型                | schema            |
|-----------------|-------|-------------------|-------------------| 
| browser         | 浏览器   | string            |                   |
| createTime      | 创建时间  | string(date-time) | string(date-time) |
| exception       | 错误    | string            |                   |
| id              | 主键ID  | string            |                   |
| ip              | IP    | string            |                   |
| layer           | 级别    | integer(int64)    | integer(int64)    |
| message         | 信息    | string            |                   |
| operator        | 操作人   | string            |                   |
| operatorAccount | 操作账户  | string            |                   |
| operatorId      | 操作人id | string            |                   |
| remark          | 备注    | string            |                   |
| url             | 地址    | string            |                   |

**响应示例**:

```javascript
{
	"browser": "",
	"createTime": "",
	"exception": "",
	"id": "",
	"ip": "",
	"layer": 0,
	"message": "",
	"operator": "",
	"operatorAccount": "",
	"operatorId": "",
	"remark": "",
	"url": ""
}
```

## 系统-日志表分页列表

**接口地址**:`/bbt-api/sys/log/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称            | 参数说明      | 请求类型  | 是否必须  | 数据类型              | schema |
|-----------------|-----------|-------|-------|-------------------|--------|
| browser         | 浏览器       | query | false | string            |        |
| current         | 页码,默认为1   | query | false | integer(int32)    |        |
| end             | 结束时间      | query | false | string(date-time) |        |
| exception       | 错误        | query | false | string            |        |
| ip              | IP        | query | false | string            |        |
| layer           | 级别        | query | false | integer(int64)    |        |
| message         | 信息        | query | false | string            |        |
| operator        | 操作人       | query | false | string            |        |
| operatorAccount | 操作账户      | query | false | string            |        |
| operatorId      | 操作人id     | query | false | string            |        |
| remark          | 备注        | query | false | string            |        |
| size            | 页大小,默认为10 | query | false | integer(int32)    |        |
| start           | 开始时间      | query | false | string(date-time) |        |
| url             | 地址        | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | 分页结果对象«SysLogVO» |

**响应参数**:

| 参数名称                        | 参数说明  | 类型                | schema         |
|-----------------------------|-------|-------------------|----------------| 
| current                     | 当前页码  | integer(int32)    | integer(int32) |
| pages                       | 总页数   | integer(int32)    | integer(int32) |
| records                     | 数据列表  | array             | SysLogVO       |
| &emsp;&emsp;browser         | 浏览器   | string            |                |
| &emsp;&emsp;createTime      | 创建时间  | string(date-time) |                |
| &emsp;&emsp;exception       | 错误    | string            |                |
| &emsp;&emsp;id              | 主键ID  | string            |                |
| &emsp;&emsp;ip              | IP    | string            |                |
| &emsp;&emsp;layer           | 级别    | integer(int64)    |                |
| &emsp;&emsp;message         | 信息    | string            |                |
| &emsp;&emsp;operator        | 操作人   | string            |                |
| &emsp;&emsp;operatorAccount | 操作账户  | string            |                |
| &emsp;&emsp;operatorId      | 操作人id | string            |                |
| &emsp;&emsp;remark          | 备注    | string            |                |
| &emsp;&emsp;url             | 地址    | string            |                |
| size                        | 页大小   | integer(int32)    | integer(int32) |
| total                       | 总行数   | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"browser": "",
			"createTime": "",
			"exception": "",
			"id": "",
			"ip": "",
			"layer": 0,
			"message": "",
			"operator": "",
			"operatorAccount": "",
			"operatorId": "",
			"remark": "",
			"url": ""
		}
	],
	"size": 0,
	"total": 0
}
```

# 系统-通知公告

## 系统-通知公告分页列表

**接口地址**:`/bbt-api/sys/news/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明      | 请求类型  | 是否必须  | 数据类型              | schema |
|-------------|-----------|-------|-------|-------------------|--------|
| content     | 内容        | query | false | string            |        |
| current     | 页码,默认为1   | query | false | integer(int32)    |        |
| end         | 结束时间      | query | false | string(date-time) |        |
| introducing | 简介        | query | false | string            |        |
| sendDate    | 发送日期      | query | false | string(date-time) |        |
| size        | 页大小,默认为10 | query | false | integer(int32)    |        |
| source      | 来源        | query | false | string            |        |
| start       | 开始时间      | query | false | string(date-time) |        |
| title       | 标题        | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema            |
|-----|----|-------------------| 
| 200 | OK | 分页结果对象«SysNewsVO» |

**响应参数**:

| 参数名称                         | 参数说明                                               | 类型                | schema         |
|------------------------------|----------------------------------------------------|-------------------|----------------| 
| current                      | 当前页码                                               | integer(int32)    | integer(int32) |
| pages                        | 总页数                                                | integer(int32)    | integer(int32) |
| records                      | 数据列表                                               | array             | SysNewsVO      |
| &emsp;&emsp;content          | 内容                                                 | string            |                |
| &emsp;&emsp;createBy         | 创建者                                                | string            |                |
| &emsp;&emsp;createTime       | 创建时间                                               | string(date-time) |                |
| &emsp;&emsp;enabled          |                                                    | boolean           |                |
| &emsp;&emsp;id               | 主键                                                 | string            |                |
| &emsp;&emsp;imageUrlList     | 图像url                                              | array             | string         |
| &emsp;&emsp;images           | 标题图片                                               | array             | ImageVO        |
| &emsp;&emsp;&emsp;&emsp;id   |                                                    | string            |                |
| &emsp;&emsp;&emsp;&emsp;name |                                                    | string            |                |
| &emsp;&emsp;&emsp;&emsp;size |                                                    | number            |                |
| &emsp;&emsp;&emsp;&emsp;url  |                                                    | string            |                |
| &emsp;&emsp;introducing      | 简介                                                 | string            |                |
| &emsp;&emsp;isRead           | 是否已读                                               | boolean           |                |
| &emsp;&emsp;sendDate         | 发送日期                                               | string(date-time) |                |
| &emsp;&emsp;source           | 来源                                                 | string            |                |
| &emsp;&emsp;tenantId         | 租户id                                               | string            |                |
| &emsp;&emsp;title            | 标题                                                 | string            |                |
| &emsp;&emsp;type             | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE | string            |                |
| &emsp;&emsp;updateBy         | 修改者                                                | string            |                |
| &emsp;&emsp;updateTime       | 修改时间                                               | string(date-time) |                |
| size                         | 页大小                                                | integer(int32)    | integer(int32) |
| total                        | 总行数                                                | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"content": "",
			"createBy": "",
			"createTime": "",
			"enabled": false,
			"id": "",
			"imageUrlList": [],
			"images": [
				{
					"id": "",
					"name": "",
					"size": 0,
					"url": ""
				}
			],
			"introducing": "",
			"isRead": false,
			"sendDate": "",
			"source": "",
			"tenantId": "",
			"title": "",
			"type": "",
			"updateBy": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 系统-通知公告详情

**接口地址**:`/bbt-api/sys/news/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema    |
|-----|----|-----------| 
| 200 | OK | SysNewsVO |

**响应参数**:

| 参数名称             | 参数说明                                               | 类型                | schema            |
|------------------|----------------------------------------------------|-------------------|-------------------| 
| content          | 内容                                                 | string            |                   |
| createBy         | 创建者                                                | string            |                   |
| createTime       | 创建时间                                               | string(date-time) | string(date-time) |
| enabled          |                                                    | boolean           |                   |
| id               | 主键                                                 | string            |                   |
| imageUrlList     | 图像url                                              | array             |                   |
| images           | 标题图片                                               | array             | ImageVO           |
| &emsp;&emsp;id   |                                                    | string            |                   |
| &emsp;&emsp;name |                                                    | string            |                   |
| &emsp;&emsp;size |                                                    | number(float)     |                   |
| &emsp;&emsp;url  |                                                    | string            |                   |
| introducing      | 简介                                                 | string            |                   |
| isRead           | 是否已读                                               | boolean           |                   |
| sendDate         | 发送日期                                               | string(date-time) | string(date-time) |
| source           | 来源                                                 | string            |                   |
| tenantId         | 租户id                                               | string            |                   |
| title            | 标题                                                 | string            |                   |
| type             | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE | string            |                   |
| updateBy         | 修改者                                                | string            |                   |
| updateTime       | 修改时间                                               | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"content": "",
	"createBy": "",
	"createTime": "",
	"enabled": false,
	"id": "",
	"imageUrlList": [],
	"images": [
		{
			"id": "",
			"name": "",
			"size": 0,
			"url": ""
		}
	],
	"introducing": "",
	"isRead": false,
	"sendDate": "",
	"source": "",
	"tenantId": "",
	"title": "",
	"type": "",
	"updateBy": "",
	"updateTime": ""
}
```

## 更新系统-通知公告

**接口地址**:`/bbt-api/sys/news/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "content": "",
  "enabled": false,
  "id": "",
  "images": [
    {
      "id": "",
      "name": "",
      "size": 0,
      "url": ""
    }
  ],
  "introducing": "",
  "sendDate": "",
  "source": "",
  "tenantId": "",
  "title": ""
}
```

**请求参数**:

| 参数名称                         | 参数说明        | 请求类型 | 是否必须  | 数据类型        | schema      |
|------------------------------|-------------|------|-------|-------------|-------------|
| 系统-通知公告修改参数                  | 系统-通知公告修改参数 | body | true  | 系统-通知公告修改参数 | 系统-通知公告修改参数 |
| &emsp;&emsp;content          | 内容          |      | false | string      |             |
| &emsp;&emsp;enabled          |             |      | false | boolean     |             |
| &emsp;&emsp;id               | 主键          |      | false | string      |             |
| &emsp;&emsp;images           | 标题图片        |      | false | array       | ImageDTO    |
| &emsp;&emsp;&emsp;&emsp;id   | 文件id        |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;name | 文件名         |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;size | 文件大小        |      | false | number      |             |
| &emsp;&emsp;&emsp;&emsp;url  | 文件url       |      | false | string      |             |
| &emsp;&emsp;introducing      | 简介          |      | false | string      |             |
| &emsp;&emsp;sendDate         | 发送日期        |      | false | string      |             |
| &emsp;&emsp;source           | 来源          |      | false | string      |             |
| &emsp;&emsp;tenantId         | 租户id        |      | false | string      |             |
| &emsp;&emsp;title            | 标题          |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除系统-通知公告

**接口地址**:`/bbt-api/sys/news/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 新增系统-通知公告

**接口地址**:`/bbt-api/sys/news`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "content": "",
  "enabled": false,
  "images": [
    {
      "id": "",
      "name": "",
      "size": 0,
      "url": ""
    }
  ],
  "introducing": "",
  "sendDate": "",
  "source": "",
  "title": "",
  "type": ""
}
```

**请求参数**:

| 参数名称                         | 参数说明                                               | 请求类型 | 是否必须  | 数据类型        | schema      |
|------------------------------|----------------------------------------------------|------|-------|-------------|-------------|
| 系统-通知公告新增参数                  | 系统-通知公告新增参数                                        | body | true  | 系统-通知公告新增参数 | 系统-通知公告新增参数 |
| &emsp;&emsp;content          | 内容                                                 |      | false | string      |             |
| &emsp;&emsp;enabled          |                                                    |      | false | boolean     |             |
| &emsp;&emsp;images           | 标题图片                                               |      | false | array       | ImageDTO    |
| &emsp;&emsp;&emsp;&emsp;id   | 文件id                                               |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;name | 文件名                                                |      | false | string      |             |
| &emsp;&emsp;&emsp;&emsp;size | 文件大小                                               |      | false | number      |             |
| &emsp;&emsp;&emsp;&emsp;url  | 文件url                                              |      | false | string      |             |
| &emsp;&emsp;introducing      | 简介                                                 |      | false | string      |             |
| &emsp;&emsp;sendDate         | 发送日期                                               |      | false | string      |             |
| &emsp;&emsp;source           | 来源                                                 |      | false | string      |             |
| &emsp;&emsp;title            | 标题                                                 |      | false | string      |             |
| &emsp;&emsp;type             | 消息类型,可用值:APPLY,CHECK,COLLECTION,COMPLAINTS,MESSAGE |      | false | string      |             |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 系统字典类型

## 新增字典

**接口地址**:`/bbt-api/sys/dict`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "description": "",
  "dictCode": "",
  "dictName": "",
  "items": [
    {
      "description": "",
      "itemCode": "",
      "itemName": "",
      "sort": 0
    }
  ]
}
```

**请求参数**:

| 参数名称                                | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema   |
|-------------------------------------|----------|------|-------|----------|----------|
| 系统字典新增参数                            | 系统字典新增参数 | body | true  | 系统字典新增参数 | 系统字典新增参数 |
| &emsp;&emsp;description             | 描述       |      | false | string   |          |
| &emsp;&emsp;dictCode                | 字典编码     |      | false | string   |          |
| &emsp;&emsp;dictName                | 字典名称     |      | false | string   |          |
| &emsp;&emsp;items                   | 字典选项     |      | false | array    | Item     |
| &emsp;&emsp;&emsp;&emsp;description | 描述       |      | false | string   |          |
| &emsp;&emsp;&emsp;&emsp;itemCode    | 字典项key   |      | false | string   |          |
| &emsp;&emsp;&emsp;&emsp;itemName    | 字典项值     |      | false | string   |          |
| &emsp;&emsp;&emsp;&emsp;sort        | 排序       |      | false | integer  |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更新字典

**接口地址**:`/bbt-api/sys/dict/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "description": "",
  "dictName": "",
  "id": "",
  "items": [
    {
      "description": "",
      "itemCode": "",
      "itemName": "",
      "sort": 0
    }
  ]
}
```

**请求参数**:

| 参数名称                                | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema   |
|-------------------------------------|----------|------|-------|----------|----------|
| 系统字典修改参数                            | 系统字典修改参数 | body | true  | 系统字典修改参数 | 系统字典修改参数 |
| &emsp;&emsp;description             | 描述       |      | false | string   |          |
| &emsp;&emsp;dictName                | 字典名称     |      | false | string   |          |
| &emsp;&emsp;id                      | 主键       |      | false | string   |          |
| &emsp;&emsp;items                   | 字典选项     |      | false | array    | Item     |
| &emsp;&emsp;&emsp;&emsp;description | 描述       |      | false | string   |          |
| &emsp;&emsp;&emsp;&emsp;itemCode    | 字典项key   |      | false | string   |          |
| &emsp;&emsp;&emsp;&emsp;itemName    | 字典项值     |      | false | string   |          |
| &emsp;&emsp;&emsp;&emsp;sort        | 排序       |      | false | integer  |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 使用txt文本初始化

**接口地址**:`/bbt-api/sys/dict/initWithTxt`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| path | path | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除字典

**接口地址**:`/bbt-api/sys/dict/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 强制刷新字典

**接口地址**:`/bbt-api/sys/dict/refresh`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 字典下拉列表

**接口地址**:`/bbt-api/sys/dict/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>支持按名称过滤</p>

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| name | name | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | DictListVO |

**响应参数**:

| 参数名称                 | 参数说明   | 类型     | schema   |
|----------------------|--------|--------|----------| 
| description          | 字典描述   | string |          |
| dictCode             | 字典code | string |          |
| dictName             | 字典名称   | string |          |
| id                   | 主键     | string |          |
| items                |        | array  | DictItem |
| &emsp;&emsp;id       |        | string |          |
| &emsp;&emsp;itemCode |        | string |          |
| &emsp;&emsp;itemName |        | string |          |

**响应示例**:

```javascript
[
	{
		"description": "",
		"dictCode": "",
		"dictName": "",
		"id": "",
		"items": [
			{
				"id": "",
				"itemCode": "",
				"itemName": ""
			}
		]
	}
]
```

## 字典分页列表

**接口地址**:`/bbt-api/sys/dict/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明                             | 请求类型  | 是否必须  | 数据类型              | schema |
|-------------|----------------------------------|-------|-------|-------------------|--------|
| current     | 页码,默认为1                          | query | false | integer(int32)    |        |
| description | 描述                               | query | false | string            |        |
| dictName    | 字典名称                             | query | false | string            |        |
| end         | 结束时间                             | query | false | string(date-time) |        |
| size        | 页大小,默认为10                        | query | false | integer(int32)    |        |
| start       | 开始时间                             | query | false | string(date-time) |        |
| type        | 字典类型 数字类型 字符类型,可用值:NUMBER,STRING | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema        |
|-----|----|---------------| 
| 200 | OK | IPage«DictVO» |

**响应参数**:

| 参数名称                                | 参数说明                             | 类型                | schema         |
|-------------------------------------|----------------------------------|-------------------|----------------| 
| current                             |                                  | integer(int64)    | integer(int64) |
| pages                               |                                  | integer(int64)    | integer(int64) |
| records                             |                                  | array             | DictVO         |
| &emsp;&emsp;createTime              | 创建时间                             | string(date-time) |                |
| &emsp;&emsp;description             | 描述                               | string            |                |
| &emsp;&emsp;dictCode                | 字典编码                             | string            |                |
| &emsp;&emsp;dictName                | 字典名称                             | string            |                |
| &emsp;&emsp;id                      | 主键                               | string            |                |
| &emsp;&emsp;items                   | 字典选项                             | array             | DictItemVO     |
| &emsp;&emsp;&emsp;&emsp;createTime  | 创建时间                             | string            |                |
| &emsp;&emsp;&emsp;&emsp;description | 描述                               | string            |                |
| &emsp;&emsp;&emsp;&emsp;dictId      | 字典id                             | string            |                |
| &emsp;&emsp;&emsp;&emsp;enable      | 状态（1启用 0不启用）                     | boolean           |                |
| &emsp;&emsp;&emsp;&emsp;id          | 主键                               | string            |                |
| &emsp;&emsp;&emsp;&emsp;itemCode    | 字典项key                           | string            |                |
| &emsp;&emsp;&emsp;&emsp;itemName    | 字典项值                             | string            |                |
| &emsp;&emsp;&emsp;&emsp;sort        | 排序                               | integer           |                |
| &emsp;&emsp;&emsp;&emsp;updateTime  | 更新时间                             | string            |                |
| &emsp;&emsp;type                    | 字典类型 数字类型 字符类型,可用值:NUMBER,STRING | string            |                |
| &emsp;&emsp;updateTime              | 更新时间                             | string(date-time) |                |
| size                                |                                  | integer(int64)    | integer(int64) |
| total                               |                                  | integer(int64)    | integer(int64) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"createTime": "",
			"description": "",
			"dictCode": "",
			"dictName": "",
			"id": "",
			"items": [
				{
					"createTime": "",
					"description": "",
					"dictId": "",
					"enable": false,
					"id": "",
					"itemCode": "",
					"itemName": "",
					"sort": 0,
					"updateTime": ""
				}
			],
			"type": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 字典下拉列表 + 选项列表

**接口地址**:`/bbt-api/sys/dict/full/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>支持按名称过滤</p>

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| name | name | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | DictListVO |

**响应参数**:

| 参数名称                 | 参数说明   | 类型     | schema   |
|----------------------|--------|--------|----------| 
| description          | 字典描述   | string |          |
| dictCode             | 字典code | string |          |
| dictName             | 字典名称   | string |          |
| id                   | 主键     | string |          |
| items                |        | array  | DictItem |
| &emsp;&emsp;id       |        | string |          |
| &emsp;&emsp;itemCode |        | string |          |
| &emsp;&emsp;itemName |        | string |          |

**响应示例**:

```javascript
[
	{
		"description": "",
		"dictCode": "",
		"dictName": "",
		"id": "",
		"items": [
			{
				"id": "",
				"itemCode": "",
				"itemName": ""
			}
		]
	}
]
```

## 字典详情

**接口地址**:`/bbt-api/sys/dict/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK | DictVO |

**响应参数**:

| 参数名称                    | 参数说明                             | 类型                | schema            |
|-------------------------|----------------------------------|-------------------|-------------------| 
| createTime              | 创建时间                             | string(date-time) | string(date-time) |
| description             | 描述                               | string            |                   |
| dictCode                | 字典编码                             | string            |                   |
| dictName                | 字典名称                             | string            |                   |
| id                      | 主键                               | string            |                   |
| items                   | 字典选项                             | array             | DictItemVO        |
| &emsp;&emsp;createTime  | 创建时间                             | string(date-time) |                   |
| &emsp;&emsp;description | 描述                               | string            |                   |
| &emsp;&emsp;dictId      | 字典id                             | string            |                   |
| &emsp;&emsp;enable      | 状态（1启用 0不启用）                     | boolean           |                   |
| &emsp;&emsp;id          | 主键                               | string            |                   |
| &emsp;&emsp;itemCode    | 字典项key                           | string            |                   |
| &emsp;&emsp;itemName    | 字典项值                             | string            |                   |
| &emsp;&emsp;sort        | 排序                               | integer(int32)    |                   |
| &emsp;&emsp;updateTime  | 更新时间                             | string(date-time) |                   |
| type                    | 字典类型 数字类型 字符类型,可用值:NUMBER,STRING | string            |                   |
| updateTime              | 更新时间                             | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"createTime": "",
	"description": "",
	"dictCode": "",
	"dictName": "",
	"id": "",
	"items": [
		{
			"createTime": "",
			"description": "",
			"dictId": "",
			"enable": false,
			"id": "",
			"itemCode": "",
			"itemName": "",
			"sort": 0,
			"updateTime": ""
		}
	],
	"type": "",
	"updateTime": ""
}
```

# 系统字典选项

## 字典选项下拉列表

**接口地址**:`/bbt-api/sys/dict/item/select`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| code | code | query | false | string |        |
| name | name | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | DictItemListVO |

**响应参数**:

| 参数名称        | 参数说明   | 类型     | schema |
|-------------|--------|--------|--------| 
| code        | 字典项key | string |        |
| description | 描述     | string |        |
| id          | 主键     | string |        |
| name        | 字典项值   | string |        |

**响应示例**:

```javascript
[
	{
		"code": "",
		"description": "",
		"id": "",
		"name": ""
	}
]
```

## 删除字典选项

**接口地址**:`/bbt-api/sys/dict/item/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 修改字典选项

**接口地址**:`/bbt-api/sys/dict/item/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "description": "",
  "enable": false,
  "id": "",
  "itemName": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明       | 请求类型 | 是否必须  | 数据类型       | schema     |
|-------------------------|------------|------|-------|------------|------------|
| 系统字典详情修改参数              | 系统字典详情修改参数 | body | true  | 系统字典详情修改参数 | 系统字典详情修改参数 |
| &emsp;&emsp;description | 描述         |      | false | string     |            |
| &emsp;&emsp;enable      | 是否启用       |      | false | boolean    |            |
| &emsp;&emsp;id          | 主键         |      | false | string     |            |
| &emsp;&emsp;itemName    | 字典项值       |      | false | string     |            |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 字典选项列表

**接口地址**:`/bbt-api/sys/dict/item/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称   | 参数说明   | 请求类型  | 是否必须  | 数据类型           | schema |
|--------|--------|-------|-------|----------------|--------|
| dictId | dictId | query | false | integer(int64) |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | DictItemVO |

**响应参数**:

| 参数名称        | 参数说明         | 类型                | schema            |
|-------------|--------------|-------------------|-------------------| 
| createTime  | 创建时间         | string(date-time) | string(date-time) |
| description | 描述           | string            |                   |
| dictId      | 字典id         | string            |                   |
| enable      | 状态（1启用 0不启用） | boolean           |                   |
| id          | 主键           | string            |                   |
| itemCode    | 字典项key       | string            |                   |
| itemName    | 字典项值         | string            |                   |
| sort        | 排序           | integer(int32)    | integer(int32)    |
| updateTime  | 更新时间         | string(date-time) | string(date-time) |

**响应示例**:

```javascript
[
	{
		"createTime": "",
		"description": "",
		"dictId": "",
		"enable": false,
		"id": "",
		"itemCode": "",
		"itemName": "",
		"sort": 0,
		"updateTime": ""
	}
]
```

## 新增字典选项

**接口地址**:`/bbt-api/sys/dict/item`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "description": "",
  "dictId": "",
  "itemCode": "",
  "itemName": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明       | 请求类型 | 是否必须  | 数据类型       | schema     |
|-------------------------|------------|------|-------|------------|------------|
| 系统字典详情新增参数              | 系统字典详情新增参数 | body | true  | 系统字典详情新增参数 | 系统字典详情新增参数 |
| &emsp;&emsp;description | 描述         |      | false | string     |            |
| &emsp;&emsp;dictId      | 字典id       |      | false | string     |            |
| &emsp;&emsp;itemCode    | 字典项key     |      | false | string     |            |
| &emsp;&emsp;itemName    | 字典项值       |      | false | string     |            |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 字典选项详情

**接口地址**:`/bbt-api/sys/dict/item/info/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | DictItemVO |

**响应参数**:

| 参数名称        | 参数说明         | 类型                | schema            |
|-------------|--------------|-------------------|-------------------| 
| createTime  | 创建时间         | string(date-time) | string(date-time) |
| description | 描述           | string            |                   |
| dictId      | 字典id         | string            |                   |
| enable      | 状态（1启用 0不启用） | boolean           |                   |
| id          | 主键           | string            |                   |
| itemCode    | 字典项key       | string            |                   |
| itemName    | 字典项值         | string            |                   |
| sort        | 排序           | integer(int32)    | integer(int32)    |
| updateTime  | 更新时间         | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"createTime": "",
	"description": "",
	"dictId": "",
	"enable": false,
	"id": "",
	"itemCode": "",
	"itemName": "",
	"sort": 0,
	"updateTime": ""
}
```

## 字典选项分页列表

**接口地址**:`/bbt-api/sys/dict/item/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明      | 请求类型  | 是否必须  | 数据类型              | schema |
|-------------|-----------|-------|-------|-------------------|--------|
| current     | 页码,默认为1   | query | false | integer(int32)    |        |
| description | 描述        | query | false | string            |        |
| dictId      | 字典id      | query | false | integer(int64)    |        |
| end         | 结束时间      | query | false | string(date-time) |        |
| itemName    | 字典项值      | query | false | string            |        |
| size        | 页大小,默认为10 | query | false | integer(int32)    |        |
| start       | 开始时间      | query | false | string(date-time) |        |

**响应状态**:

| 状态码 | 说明 | schema            |
|-----|----|-------------------| 
| 200 | OK | IPage«DictItemVO» |

**响应参数**:

| 参数名称                    | 参数说明         | 类型                | schema         |
|-------------------------|--------------|-------------------|----------------| 
| current                 |              | integer(int64)    | integer(int64) |
| pages                   |              | integer(int64)    | integer(int64) |
| records                 |              | array             | DictItemVO     |
| &emsp;&emsp;createTime  | 创建时间         | string(date-time) |                |
| &emsp;&emsp;description | 描述           | string            |                |
| &emsp;&emsp;dictId      | 字典id         | string            |                |
| &emsp;&emsp;enable      | 状态（1启用 0不启用） | boolean           |                |
| &emsp;&emsp;id          | 主键           | string            |                |
| &emsp;&emsp;itemCode    | 字典项key       | string            |                |
| &emsp;&emsp;itemName    | 字典项值         | string            |                |
| &emsp;&emsp;sort        | 排序           | integer(int32)    |                |
| &emsp;&emsp;updateTime  | 更新时间         | string(date-time) |                |
| size                    |              | integer(int64)    | integer(int64) |
| total                   |              | integer(int64)    | integer(int64) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"createTime": "",
			"description": "",
			"dictId": "",
			"enable": false,
			"id": "",
			"itemCode": "",
			"itemName": "",
			"sort": 0,
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

# 系统用户Feign接口

## allUser

**接口地址**:`/bbt-api/api/org/user/allUser`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«UserDetailAO»» |

**响应参数**:

| 参数名称                      | 参数说明                           | 类型             | schema         |
|---------------------------|--------------------------------|----------------|----------------| 
| code                      |                                | integer(int32) | integer(int32) |
| data                      |                                | array          | UserDetailAO   |
| &emsp;&emsp;account       |                                | string         |                |
| &emsp;&emsp;address       |                                | string         |                |
| &emsp;&emsp;departmentIds |                                | array          | string         |
| &emsp;&emsp;description   |                                | string         |                |
| &emsp;&emsp;email         |                                | string         |                |
| &emsp;&emsp;gender        | 可用值:FEMALE,MALE,UNKNOWN        | string         |                |
| &emsp;&emsp;head          |                                | string         |                |
| &emsp;&emsp;id            |                                | string         |                |
| &emsp;&emsp;identify      |                                | string         |                |
| &emsp;&emsp;phone         |                                | string         |                |
| &emsp;&emsp;realname      |                                | string         |                |
| &emsp;&emsp;regionCode    |                                | string         |                |
| &emsp;&emsp;state         | 可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                |
| message                   |                                | string         |                |
| stack                     |                                | string         |                |
| success                   |                                | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"account": "",
			"address": "",
			"departmentIds": [],
			"description": "",
			"email": "",
			"gender": "",
			"head": "",
			"id": "",
			"identify": "",
			"phone": "",
			"realname": "",
			"regionCode": "",
			"state": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取部门code下所属用户

**接口地址**:`/bbt-api/api/org/user/listByDeptCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明     | 请求类型  | 是否必须 | 数据类型   | schema |
|----------|----------|-------|------|--------|--------|
| deptCode | deptCode | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                 |
|-----|----|------------------------| 
| 200 | OK | Result«List«UserInfo»» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | array          | UserInfo       |
| &emsp;&emsp;id       |      | Serializable   | Serializable   |
| &emsp;&emsp;name     |      | string         |                |
| &emsp;&emsp;nickName |      | string         |                |
| &emsp;&emsp;realName |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"id": {},
			"name": "",
			"nickName": "",
			"realName": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前部门及子部门id

**接口地址**:`/bbt-api/api/org/user/scope/currentDeptAllId`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称         | 参数说明         | 请求类型  | 是否必须 | 数据类型   | schema |
|--------------|--------------|-------|------|--------|--------|
| departmentId | departmentId | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«Set«Serializable»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          | Serializable   |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 部门所有用户

**接口地址**:`/bbt-api/api/org/department/user/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                         |
|-----|----|--------------------------------| 
| 200 | OK | Result«List«DepartmentUserAO»» |

**响应参数**:

| 参数名称                 | 参数说明                                             | 类型             | schema           |
|----------------------|--------------------------------------------------|----------------|------------------| 
| code                 |                                                  | integer(int32) | integer(int32)   |
| data                 |                                                  | array          | DepartmentUserAO |
| &emsp;&emsp;account  | 用户名                                              | string         |                  |
| &emsp;&emsp;admin    | 管理员                                              | boolean        |                  |
| &emsp;&emsp;id       | 主键                                               | string         |                  |
| &emsp;&emsp;phone    | 手机号码                                             | string         |                  |
| &emsp;&emsp;realname | 真实姓名                                             | string         |                  |
| &emsp;&emsp;state    | 状态，0：禁用，1：启用，2：锁定,可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                  |
| &emsp;&emsp;userId   | 用户id                                             | string         |                  |
| message              |                                                  | string         |                  |
| stack                |                                                  | string         |                  |
| success              |                                                  | boolean        |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"account": "",
			"admin": false,
			"id": "",
			"phone": "",
			"realname": "",
			"state": "",
			"userId": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 当前用户部门同级部门

**接口地址**:`/bbt-api/api/org/department/current/sameLeve`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明     | 请求类型  | 是否必须 | 数据类型    | schema |
|----------|----------|-------|------|---------|--------|
| userFlag | userFlag | query | true | boolean |        |

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 当前用户部门树形列表

**接口地址**:`/bbt-api/api/org/department/tree`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称         | 参数说明         | 请求类型  | 是否必须 | 数据类型    | schema |
|--------------|--------------|-------|------|---------|--------|
| departmentId | departmentId | query | true | string  |        |
| userFlag     | userFlag     | query | true | boolean |        |

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## deptCode下级部门code

**接口地址**:`/bbt-api/api/org/department/all/subDept/deptCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明     | 请求类型  | 是否必须 | 数据类型   | schema |
|----------|----------|-------|------|--------|--------|
| deptCode | deptCode | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«Set«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前登录用户信息

**接口地址**:`/bbt-api/api/org/user/currentUser`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«UserLoginAO» |

**响应参数**:

| 参数名称                                | 参数说明                           | 类型             | schema         |
|-------------------------------------|--------------------------------|----------------|----------------| 
| code                                |                                | integer(int32) | integer(int32) |
| data                                |                                | UserLoginAO    | UserLoginAO    |
| &emsp;&emsp;account                 |                                | string         |                |
| &emsp;&emsp;address                 |                                | string         |                |
| &emsp;&emsp;admin                   |                                | boolean        |                |
| &emsp;&emsp;department              |                                | Department     | Department     |
| &emsp;&emsp;&emsp;&emsp;address     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;adminId     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createTime  |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptCode    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptName    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;description |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;id          |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;level       |                                | integer        |                |
| &emsp;&emsp;&emsp;&emsp;parentId    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateTime  |                                | string         |                |
| &emsp;&emsp;departmentIds           |                                | array          | string         |
| &emsp;&emsp;departments             |                                | array          | Department     |
| &emsp;&emsp;&emsp;&emsp;address     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;adminId     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createTime  |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptCode    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptName    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;description |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;id          |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;level       |                                | integer        |                |
| &emsp;&emsp;&emsp;&emsp;parentId    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateTime  |                                | string         |                |
| &emsp;&emsp;description             |                                | string         |                |
| &emsp;&emsp;email                   |                                | string         |                |
| &emsp;&emsp;gender                  | 可用值:FEMALE,MALE,UNKNOWN        | string         |                |
| &emsp;&emsp;head                    |                                | string         |                |
| &emsp;&emsp;id                      |                                | string         |                |
| &emsp;&emsp;identify                |                                | string         |                |
| &emsp;&emsp;menus                   |                                | array          | UserMenuDTO    |
| &emsp;&emsp;&emsp;&emsp;children    |                                | array          | UserMenuDTO    |
| &emsp;&emsp;&emsp;&emsp;component   |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;hidden      |                                | boolean        |                |
| &emsp;&emsp;&emsp;&emsp;icon        |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;id          |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;meta        |                                | object         |                |
| &emsp;&emsp;&emsp;&emsp;name        |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;path        |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;roles       |                                | array          | string         |
| &emsp;&emsp;&emsp;&emsp;sort        |                                | integer        |                |
| &emsp;&emsp;&emsp;&emsp;state       |                                | boolean        |                |
| &emsp;&emsp;phone                   |                                | string         |                |
| &emsp;&emsp;realname                |                                | string         |                |
| &emsp;&emsp;regionCode              |                                | string         |                |
| &emsp;&emsp;roleIds                 |                                | array          | string         |
| &emsp;&emsp;roleName                |                                | string         |                |
| &emsp;&emsp;roles                   |                                | array          | Role           |
| &emsp;&emsp;&emsp;&emsp;roleCode    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;roleId      |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;roleName    |                                | string         |                |
| &emsp;&emsp;state                   | 可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                |
| message                             |                                | string         |                |
| stack                               |                                | string         |                |
| success                             |                                | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"account": "",
		"address": "",
		"admin": true,
		"department": {
			"address": "",
			"adminId": "",
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentId": "",
			"updateBy": "",
			"updateTime": ""
		},
		"departmentIds": [],
		"departments": [
			{
				"address": "",
				"adminId": "",
				"createBy": "",
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"description": "",
				"id": "",
				"level": 0,
				"parentId": "",
				"updateBy": "",
				"updateTime": ""
			}
		],
		"description": "",
		"email": "",
		"gender": "",
		"head": "",
		"id": "",
		"identify": "",
		"menus": [
			{
				"children": [
					{
						"children": [
							{}
						],
						"component": "",
						"hidden": true,
						"icon": "",
						"id": "",
						"meta": {},
						"name": "",
						"path": "",
						"roles": [],
						"sort": 0,
						"state": true
					}
				],
				"component": "",
				"hidden": true,
				"icon": "",
				"id": "",
				"meta": {},
				"name": "",
				"path": "",
				"roles": [],
				"sort": 0,
				"state": true
			}
		],
		"phone": "",
		"realname": "",
		"regionCode": "",
		"roleIds": [],
		"roleName": "",
		"roles": [
			{
				"roleCode": "",
				"roleId": "",
				"roleName": ""
			}
		],
		"state": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前部门及子部门所有用户id

**接口地址**:`/bbt-api/api/org/user/scope/currentDeptAll`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称         | 参数说明         | 请求类型  | 是否必须 | 数据类型   | schema |
|--------------|--------------|-------|------|--------|--------|
| departmentId | departmentId | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«Set«Serializable»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          | Serializable   |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 通过ids获取用户信息

**接口地址**:`/bbt-api/api/org/user/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| ids  | ids  | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                 |
|-----|----|------------------------| 
| 200 | OK | Result«List«UserInfo»» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | array          | UserInfo       |
| &emsp;&emsp;id       |      | Serializable   | Serializable   |
| &emsp;&emsp;name     |      | string         |                |
| &emsp;&emsp;nickName |      | string         |                |
| &emsp;&emsp;realName |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"id": {},
			"name": "",
			"nickName": "",
			"realName": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前部门所有用户id

**接口地址**:`/bbt-api/api/org/user/scope/currentDept`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«Set«Serializable»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          | Serializable   |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前登录用户id

**接口地址**:`/bbt-api/api/org/user/currentUserId`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | Result«string» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | string         |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": "",
	"message": "",
	"stack": "",
	"success": true
}
```

## 上级部门

**接口地址**:`/bbt-api/api/org/department/parentDept/{deptCode}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明     | 请求类型 | 是否必须 | 数据类型   | schema |
|----------|----------|------|------|--------|--------|
| deptCode | deptCode | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«DepartmentAO» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | DepartmentAO      | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"address": "",
		"adminId": "",
		"children": [
			{
				"address": "",
				"adminId": "",
				"children": [
					{}
				],
				"createBy": "",
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"description": "",
				"id": "",
				"level": 0,
				"parentDeptName": "",
				"parentId": "",
				"regionCode": "",
				"sort": 0,
				"state": "",
				"tel": "",
				"updateBy": "",
				"updateTime": "",
				"users": [
					{
						"account": "",
						"admin": true,
						"departmentId": "",
						"id": "",
						"phone": "",
						"realname": "",
						"state": "",
						"userId": ""
					}
				],
				"version": 0
			}
		],
		"createBy": "",
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"description": "",
		"id": "",
		"level": 0,
		"parentDeptName": "",
		"parentId": "",
		"regionCode": "",
		"sort": 0,
		"state": "",
		"tel": "",
		"updateBy": "",
		"updateTime": "",
		"users": [
			{
				"account": "",
				"admin": true,
				"departmentId": "",
				"id": "",
				"phone": "",
				"realname": "",
				"state": "",
				"userId": ""
			}
		],
		"version": 0
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 预览部门名称

**接口地址**:`/bbt-api/api/org/department/previewDepartmentName`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型           | schema |
|------|------|-------|------|----------------|--------|
| id   | id   | query | true | string         |        |
| i    | i    | query | true | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«List«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 通过id获取用户信息

**接口地址**:`/bbt-api/api/org/user/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema           |
|-----|----|------------------| 
| 200 | OK | Result«UserInfo» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | UserInfo       | UserInfo       |
| &emsp;&emsp;id       |      | Serializable   | Serializable   |
| &emsp;&emsp;name     |      | string         |                |
| &emsp;&emsp;nickName |      | string         |                |
| &emsp;&emsp;realName |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"id": {},
		"name": "",
		"nickName": "",
		"realName": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前角色所有用户id

**接口地址**:`/bbt-api/api/org/user/scope/currentRole`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«Set«Serializable»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          | Serializable   |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 递归获取下级部门id列表

**接口地址**:`/bbt-api/api/org/department/listRecurIds`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| root | root | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«Set«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## getUserDetailById

**接口地址**:`/bbt-api/api/org/user/detail/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«UserDetailAO» |

**响应参数**:

| 参数名称                      | 参数说明                           | 类型             | schema         |
|---------------------------|--------------------------------|----------------|----------------| 
| code                      |                                | integer(int32) | integer(int32) |
| data                      |                                | UserDetailAO   | UserDetailAO   |
| &emsp;&emsp;account       |                                | string         |                |
| &emsp;&emsp;address       |                                | string         |                |
| &emsp;&emsp;departmentIds |                                | array          | string         |
| &emsp;&emsp;description   |                                | string         |                |
| &emsp;&emsp;email         |                                | string         |                |
| &emsp;&emsp;gender        | 可用值:FEMALE,MALE,UNKNOWN        | string         |                |
| &emsp;&emsp;head          |                                | string         |                |
| &emsp;&emsp;id            |                                | string         |                |
| &emsp;&emsp;identify      |                                | string         |                |
| &emsp;&emsp;phone         |                                | string         |                |
| &emsp;&emsp;realname      |                                | string         |                |
| &emsp;&emsp;regionCode    |                                | string         |                |
| &emsp;&emsp;state         | 可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                |
| message                   |                                | string         |                |
| stack                     |                                | string         |                |
| success                   |                                | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"account": "",
		"address": "",
		"departmentIds": [],
		"description": "",
		"email": "",
		"gender": "",
		"head": "",
		"id": "",
		"identify": "",
		"phone": "",
		"realname": "",
		"regionCode": "",
		"state": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## getUserByDeptAndRole

**接口地址**:`/bbt-api/api/org/user/getUserByDeptAndRole`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "deptCodes": [],
  "roleIds": [
    {}
  ],
  "roleNames": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明 | 请求类型 | 是否必须  | 数据类型  | schema       |
|-----------------------|------|------|-------|-------|--------------|
| 登录参数                  | 登录参数 | body | true  | 登录参数  | 登录参数         |
| &emsp;&emsp;deptCodes |      |      | false | array | string       |
| &emsp;&emsp;roleIds   |      |      | false | array | Serializable |
| &emsp;&emsp;roleNames |      |      | false | array | Serializable |

**响应状态**:

| 状态码 | 说明 | schema                       |
|-----|----|------------------------------| 
| 200 | OK | Result«List«UserDeptRoleAO»» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | array          | UserDeptRoleAO |
| &emsp;&emsp;deptCode |      | string         |                |
| &emsp;&emsp;deptId   |      | string         |                |
| &emsp;&emsp;deptName |      | string         |                |
| &emsp;&emsp;id       |      | string         |                |
| &emsp;&emsp;phone    |      | string         |                |
| &emsp;&emsp;roleCode |      | string         |                |
| &emsp;&emsp;roleId   |      | string         |                |
| &emsp;&emsp;roleName |      | string         |                |
| &emsp;&emsp;userId   |      | string         |                |
| &emsp;&emsp;userName |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"deptCode": "",
			"deptId": "",
			"deptName": "",
			"id": "",
			"phone": "",
			"roleCode": "",
			"roleId": "",
			"roleName": "",
			"userId": "",
			"userName": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据部门Id获取用户Id

**接口地址**:`/bbt-api/api/org/department/getUserIdByDeptId`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| ids  | ids  | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«Set«Serializable»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          | Serializable   |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据ids获取

**接口地址**:`/bbt-api/api/org/department/listByIds`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| ids  | ids  | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前登录用户所在部门code

**接口地址**:`/bbt-api/api/org/user/currentDepartmentCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | Result«string» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | string         |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": "",
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据部门ID获取一段时间内部门用户活跃度列表

**接口地址**:`/bbt-api/api/org/department/getAactivityRatio`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称      | 参数说明      | 请求类型  | 是否必须 | 数据类型   | schema |
|-----------|-----------|-------|------|--------|--------|
| startTime | startTime | query | true | string |        |
| endTime   | endTime   | query | true | string |        |
| deptCode  | deptCode  | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                       |
|-----|----|------------------------------| 
| 200 | OK | Result«List«CommonCustomAO»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前登录用户所在区域code

**接口地址**:`/bbt-api/api/org/user/currentRegionCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | Result«string» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | string         |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": "",
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取当前用户区域及子区域code

**接口地址**:`/bbt-api/api/org/user/scope/currentRegionAllCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«Set«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 下级部门code

**接口地址**:`/bbt-api/api/org/department/all/subDept/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须 | 数据类型    | schema |
|---------|---------|-------|------|---------|--------|
| id      | id      | path  | true | string  |        |
| addSelf | addSelf | query | true | boolean |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«Set«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取所有部门

**接口地址**:`/bbt-api/api/org/department/getDepartmentList`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据用户ID获取部门列表

**接口地址**:`/bbt-api/api/org/department/getDepyByUserId`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称   | 参数说明   | 请求类型  | 是否必须 | 数据类型   | schema |
|--------|--------|-------|------|--------|--------|
| userId | userId | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据id获取子部门集合

**接口地址**:`/bbt-api/api/org/department/getSubDepartment`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| id   | id   | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## regionCode下级部门code

**接口地址**:`/bbt-api/api/org/department/all/subDept/regionCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明       | 请求类型  | 是否必须 | 数据类型   | schema |
|------------|------------|-------|------|--------|--------|
| regionCode | regionCode | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«Set«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据code获取部门

**接口地址**:`/bbt-api/api/org/department/getDepartmentByCode`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| code | code | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«DepartmentAO» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | DepartmentAO      | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"address": "",
		"adminId": "",
		"children": [
			{
				"address": "",
				"adminId": "",
				"children": [
					{}
				],
				"createBy": "",
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"description": "",
				"id": "",
				"level": 0,
				"parentDeptName": "",
				"parentId": "",
				"regionCode": "",
				"sort": 0,
				"state": "",
				"tel": "",
				"updateBy": "",
				"updateTime": "",
				"users": [
					{
						"account": "",
						"admin": true,
						"departmentId": "",
						"id": "",
						"phone": "",
						"realname": "",
						"state": "",
						"userId": ""
					}
				],
				"version": 0
			}
		],
		"createBy": "",
		"createTime": "",
		"deptCode": "",
		"deptName": "",
		"description": "",
		"id": "",
		"level": 0,
		"parentDeptName": "",
		"parentId": "",
		"regionCode": "",
		"sort": 0,
		"state": "",
		"tel": "",
		"updateBy": "",
		"updateTime": "",
		"users": [
			{
				"account": "",
				"admin": true,
				"departmentId": "",
				"id": "",
				"phone": "",
				"realname": "",
				"state": "",
				"userId": ""
			}
		],
		"version": 0
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据部门id获取上级部门名称

**接口地址**:`/bbt-api/api/org/department/getDepartmentName`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称         | 参数说明         | 请求类型  | 是否必须 | 数据类型           | schema |
|--------------|--------------|-------|------|----------------|--------|
| departmentId | departmentId | query | true | string         |        |
| limit        | limit        | query | true | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema               |
|-----|----|----------------------| 
| 200 | OK | Result«List«string»» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | array          |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [],
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取部门信息

**接口地址**:`/bbt-api/api/org/department/info`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称         | 参数说明         | 请求类型  | 是否必须 | 数据类型           | schema |
|--------------|--------------|-------|------|----------------|--------|
| departmentId | departmentId | query | true | string         |        |
| limit        | limit        | query | true | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema                 |
|-----|----|------------------------| 
| 200 | OK | Result«DepartmentInfo» |

**响应参数**:

| 参数名称                 | 参数说明 | 类型             | schema         |
|----------------------|------|----------------|----------------| 
| code                 |      | integer(int32) | integer(int32) |
| data                 |      | DepartmentInfo | DepartmentInfo |
| &emsp;&emsp;code     |      | string         |                |
| &emsp;&emsp;fullName |      | array          | string         |
| &emsp;&emsp;id       |      | Serializable   | Serializable   |
| &emsp;&emsp;name     |      | string         |                |
| message              |      | string         |                |
| stack                |      | string         |                |
| success              |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"code": "",
		"fullName": [],
		"id": {},
		"name": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## userMonthlyActive

**接口地址**:`/bbt-api/api/org/user/userMonthlyActive`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | Result«int» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | integer(int32) | integer(int32) |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": 0,
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取所有部门数量

**接口地址**:`/bbt-api/api/org/department/deptCountStatistic`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | Result«int» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | integer(int32) | integer(int32) |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": 0,
	"message": "",
	"stack": "",
	"success": true
}
```

## 获取所有部门

**接口地址**:`/bbt-api/api/org/department/listAll`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                     |
|-----|----|----------------------------| 
| 200 | OK | Result«List«DepartmentAO»» |

**响应参数**:

| 参数名称                                 | 参数说明                           | 类型                | schema           |
|--------------------------------------|--------------------------------|-------------------|------------------| 
| code                                 |                                | integer(int32)    | integer(int32)   |
| data                                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;address                  |                                | string            |                  |
| &emsp;&emsp;adminId                  |                                | string            |                  |
| &emsp;&emsp;children                 |                                | array             | DepartmentAO     |
| &emsp;&emsp;createBy                 |                                | string            |                  |
| &emsp;&emsp;createTime               |                                | string(date-time) |                  |
| &emsp;&emsp;deptCode                 |                                | string            |                  |
| &emsp;&emsp;deptName                 |                                | string            |                  |
| &emsp;&emsp;description              |                                | string            |                  |
| &emsp;&emsp;id                       |                                | string            |                  |
| &emsp;&emsp;level                    |                                | integer(int32)    |                  |
| &emsp;&emsp;parentDeptName           |                                | string            |                  |
| &emsp;&emsp;parentId                 |                                | string            |                  |
| &emsp;&emsp;regionCode               |                                | string            |                  |
| &emsp;&emsp;sort                     |                                | integer(int32)    |                  |
| &emsp;&emsp;state                    |                                | string            |                  |
| &emsp;&emsp;tel                      |                                | string            |                  |
| &emsp;&emsp;updateBy                 |                                | string            |                  |
| &emsp;&emsp;updateTime               |                                | string(date-time) |                  |
| &emsp;&emsp;users                    |                                | array             | UserDepartmentAO |
| &emsp;&emsp;&emsp;&emsp;account      |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;admin        |                                | boolean           |                  |
| &emsp;&emsp;&emsp;&emsp;departmentId |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;id           |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;phone        |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;realname     |                                | string            |                  |
| &emsp;&emsp;&emsp;&emsp;state        | 可用值:DELETE,DISABLE,ENABLE,LOCK | string            |                  |
| &emsp;&emsp;&emsp;&emsp;userId       |                                | string            |                  |
| &emsp;&emsp;version                  |                                | integer(int64)    |                  |
| message                              |                                | string            |                  |
| stack                                |                                | string            |                  |
| success                              |                                | boolean           |                  |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"adminId": "",
			"children": [
				{
					"address": "",
					"adminId": "",
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"deptCode": "",
					"deptName": "",
					"description": "",
					"id": "",
					"level": 0,
					"parentDeptName": "",
					"parentId": "",
					"regionCode": "",
					"sort": 0,
					"state": "",
					"tel": "",
					"updateBy": "",
					"updateTime": "",
					"users": [
						{
							"account": "",
							"admin": true,
							"departmentId": "",
							"id": "",
							"phone": "",
							"realname": "",
							"state": "",
							"userId": ""
						}
					],
					"version": 0
				}
			],
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentDeptName": "",
			"parentId": "",
			"regionCode": "",
			"sort": 0,
			"state": "",
			"tel": "",
			"updateBy": "",
			"updateTime": "",
			"users": [
				{
					"account": "",
					"admin": true,
					"departmentId": "",
					"id": "",
					"phone": "",
					"realname": "",
					"state": "",
					"userId": ""
				}
			],
			"version": 0
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 白名单接口获取当前登录用户信息

**接口地址**:`/bbt-api/api/org/user/currentUserByWhitList`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«UserLoginAO» |

**响应参数**:

| 参数名称                                | 参数说明                           | 类型             | schema         |
|-------------------------------------|--------------------------------|----------------|----------------| 
| code                                |                                | integer(int32) | integer(int32) |
| data                                |                                | UserLoginAO    | UserLoginAO    |
| &emsp;&emsp;account                 |                                | string         |                |
| &emsp;&emsp;address                 |                                | string         |                |
| &emsp;&emsp;admin                   |                                | boolean        |                |
| &emsp;&emsp;department              |                                | Department     | Department     |
| &emsp;&emsp;&emsp;&emsp;address     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;adminId     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createTime  |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptCode    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptName    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;description |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;id          |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;level       |                                | integer        |                |
| &emsp;&emsp;&emsp;&emsp;parentId    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateTime  |                                | string         |                |
| &emsp;&emsp;departmentIds           |                                | array          | string         |
| &emsp;&emsp;departments             |                                | array          | Department     |
| &emsp;&emsp;&emsp;&emsp;address     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;adminId     |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;createTime  |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptCode    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;deptName    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;description |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;id          |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;level       |                                | integer        |                |
| &emsp;&emsp;&emsp;&emsp;parentId    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateBy    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;updateTime  |                                | string         |                |
| &emsp;&emsp;description             |                                | string         |                |
| &emsp;&emsp;email                   |                                | string         |                |
| &emsp;&emsp;gender                  | 可用值:FEMALE,MALE,UNKNOWN        | string         |                |
| &emsp;&emsp;head                    |                                | string         |                |
| &emsp;&emsp;id                      |                                | string         |                |
| &emsp;&emsp;identify                |                                | string         |                |
| &emsp;&emsp;menus                   |                                | array          | UserMenuDTO    |
| &emsp;&emsp;&emsp;&emsp;children    |                                | array          | UserMenuDTO    |
| &emsp;&emsp;&emsp;&emsp;component   |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;hidden      |                                | boolean        |                |
| &emsp;&emsp;&emsp;&emsp;icon        |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;id          |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;meta        |                                | object         |                |
| &emsp;&emsp;&emsp;&emsp;name        |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;path        |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;roles       |                                | array          | string         |
| &emsp;&emsp;&emsp;&emsp;sort        |                                | integer        |                |
| &emsp;&emsp;&emsp;&emsp;state       |                                | boolean        |                |
| &emsp;&emsp;phone                   |                                | string         |                |
| &emsp;&emsp;realname                |                                | string         |                |
| &emsp;&emsp;regionCode              |                                | string         |                |
| &emsp;&emsp;roleIds                 |                                | array          | string         |
| &emsp;&emsp;roleName                |                                | string         |                |
| &emsp;&emsp;roles                   |                                | array          | Role           |
| &emsp;&emsp;&emsp;&emsp;roleCode    |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;roleId      |                                | string         |                |
| &emsp;&emsp;&emsp;&emsp;roleName    |                                | string         |                |
| &emsp;&emsp;state                   | 可用值:DELETE,DISABLE,ENABLE,LOCK | string         |                |
| message                             |                                | string         |                |
| stack                               |                                | string         |                |
| success                             |                                | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"account": "",
		"address": "",
		"admin": true,
		"department": {
			"address": "",
			"adminId": "",
			"createBy": "",
			"createTime": "",
			"deptCode": "",
			"deptName": "",
			"description": "",
			"id": "",
			"level": 0,
			"parentId": "",
			"updateBy": "",
			"updateTime": ""
		},
		"departmentIds": [],
		"departments": [
			{
				"address": "",
				"adminId": "",
				"createBy": "",
				"createTime": "",
				"deptCode": "",
				"deptName": "",
				"description": "",
				"id": "",
				"level": 0,
				"parentId": "",
				"updateBy": "",
				"updateTime": ""
			}
		],
		"description": "",
		"email": "",
		"gender": "",
		"head": "",
		"id": "",
		"identify": "",
		"menus": [
			{
				"children": [
					{
						"children": [
							{}
						],
						"component": "",
						"hidden": true,
						"icon": "",
						"id": "",
						"meta": {},
						"name": "",
						"path": "",
						"roles": [],
						"sort": 0,
						"state": true
					}
				],
				"component": "",
				"hidden": true,
				"icon": "",
				"id": "",
				"meta": {},
				"name": "",
				"path": "",
				"roles": [],
				"sort": 0,
				"state": true
			}
		],
		"phone": "",
		"realname": "",
		"regionCode": "",
		"roleIds": [],
		"roleName": "",
		"roles": [
			{
				"roleCode": "",
				"roleId": "",
				"roleName": ""
			}
		],
		"state": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

# 系统登录Feign接口

## 系统用户登录

**接口地址**:`/bbt-api/api/org/login`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "deptCodes": [],
  "roleIds": [
    {}
  ],
  "roleNames": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明 | 请求类型 | 是否必须  | 数据类型  | schema       |
|-----------------------|------|------|-------|-------|--------------|
| 登录参数                  | 登录参数 | body | true  | 登录参数  | 登录参数         |
| &emsp;&emsp;deptCodes |      |      | false | array | string       |
| &emsp;&emsp;roleIds   |      |      | false | array | Serializable |
| &emsp;&emsp;roleNames |      |      | false | array | Serializable |

**响应状态**:

| 状态码 | 说明 | schema                 |
|-----|----|------------------------| 
| 200 | OK | Result«LoginSuccessAO» |

**响应参数**:

| 参数名称                   | 参数说明    | 类型             | schema         |
|------------------------|---------|----------------|----------------| 
| code                   |         | integer(int32) | integer(int32) |
| data                   |         | LoginSuccessAO | LoginSuccessAO |
| &emsp;&emsp;firstLogin | 是否为首次登录 | boolean        |                |
| &emsp;&emsp;name       | 请求头名称   | string         |                |
| &emsp;&emsp;token      | 访问令牌    | string         |                |
| message                |         | string         |                |
| stack                  |         | string         |                |
| success                |         | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"firstLogin": false,
		"name": "",
		"token": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

# 系统菜单

## 菜单列表

**接口地址**:`/bbt-api/sys/menu/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>逐级请求</p>

**请求参数**:

| 参数名称     | 参数说明                   | 请求类型  | 是否必须  | 数据类型   | schema |
|----------|------------------------|-------|-------|--------|--------|
| parentId | parentId               | query | false | string |        |
| menuType | menuType,可用值:MOBILE,PC | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK | MenuVO |

**响应参数**:

| 参数名称                  | 参数说明            | 类型             | schema         |
|-----------------------|-----------------|----------------|----------------| 
| children              | 子集              | array          | MenuVO         |
| &emsp;&emsp;children  | 子集              | array          | MenuVO         |
| &emsp;&emsp;component | 目标组件            | string         |                |
| &emsp;&emsp;hidden    | 显示和隐藏，0：显示，1：隐藏 | boolean        |                |
| &emsp;&emsp;icon      | 前端图标            | string         |                |
| &emsp;&emsp;id        | ID              | string         |                |
| &emsp;&emsp;level     | 菜单级数            | integer(int32) |                |
| &emsp;&emsp;meta      | 菜单配置            | object         |                |
| &emsp;&emsp;name      | 前端名称            | string         |                |
| &emsp;&emsp;parentId  | 父级ID            | integer(int64) |                |
| &emsp;&emsp;path      | 前端路由            | string         |                |
| &emsp;&emsp;roles     |                 | array          | string         |
| &emsp;&emsp;state     | 菜单状态，0：禁用，1：启用  | boolean        |                |
| &emsp;&emsp;title     | controller名称    | string         |                |
| component             | 目标组件            | string         |                |
| hidden                | 显示和隐藏，0：显示，1：隐藏 | boolean        |                |
| icon                  | 前端图标            | string         |                |
| id                    | ID              | string         |                |
| level                 | 菜单级数            | integer(int32) | integer(int32) |
| meta                  | 菜单配置            | object         |                |
| name                  | 前端名称            | string         |                |
| parentId              | 父级ID            | integer(int64) | integer(int64) |
| path                  | 前端路由            | string         |                |
| roles                 |                 | array          |                |
| state                 | 菜单状态，0：禁用，1：启用  | boolean        |                |
| title                 | controller名称    | string         |                |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [
					{
						"children": [
							{}
						],
						"component": "",
						"hidden": false,
						"icon": "",
						"id": "",
						"level": 0,
						"meta": {},
						"name": "",
						"parentId": 0,
						"path": "",
						"roles": [],
						"state": false,
						"title": ""
					}
				],
				"component": "",
				"hidden": false,
				"icon": "",
				"id": "",
				"level": 0,
				"meta": {},
				"name": "",
				"parentId": 0,
				"path": "",
				"roles": [],
				"state": false,
				"title": ""
			}
		],
		"component": "",
		"hidden": false,
		"icon": "",
		"id": "",
		"level": 0,
		"meta": {},
		"name": "",
		"parentId": 0,
		"path": "",
		"roles": [],
		"state": false,
		"title": ""
	}
]
```

## 更新系统菜单

**接口地址**:`/bbt-api/sys/menu/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "component": "",
  "hidden": false,
  "icon": "",
  "id": "",
  "meta": "",
  "name": "",
  "parentId": "",
  "path": "",
  "remark": "",
  "sort": 0,
  "state": "",
  "title": "",
  "url": ""
}
```

**请求参数**:

| 参数名称                  | 参数说明            | 请求类型 | 是否必须  | 数据类型           | schema   |
|-----------------------|-----------------|------|-------|----------------|----------|
| 系统菜单修改参数              | 系统菜单修改参数        | body | true  | 系统菜单修改参数       | 系统菜单修改参数 |
| &emsp;&emsp;component | 目标组件            |      | false | string         |          |
| &emsp;&emsp;hidden    | 显示和隐藏，0：显示，1：隐藏 |      | false | boolean        |          |
| &emsp;&emsp;icon      | 前端图标            |      | false | string         |          |
| &emsp;&emsp;id        | 主键              |      | false | string         |          |
| &emsp;&emsp;meta      | 菜单配置            |      | false | string         |          |
| &emsp;&emsp;name      | 前端名称            |      | false | string         |          |
| &emsp;&emsp;parentId  | 父级ID            |      | false | string         |          |
| &emsp;&emsp;path      | 前端路由            |      | false | string         |          |
| &emsp;&emsp;remark    |                 |      | false | string         |          |
| &emsp;&emsp;sort      | 菜单排序            |      | false | integer(int32) |          |
| &emsp;&emsp;state     | 状态              |      | false | string         |          |
| &emsp;&emsp;title     | controller名称    |      | false | string         |          |
| &emsp;&emsp;url       | 请求地址            |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 菜单树形列表

**接口地址**:`/bbt-api/sys/menu/tree/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>菜单下拉列表，仅包含基本信息</p>

**请求参数**:

| 参数名称     | 参数说明                   | 请求类型  | 是否必须  | 数据类型   | schema |
|----------|------------------------|-------|-------|--------|--------|
| parentId | parentId               | query | false | string |        |
| title    | title                  | query | false | string |        |
| menuType | menuType,可用值:MOBILE,PC | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema          |
|-----|----|-----------------| 
| 200 | OK | MenuRecursionVO |

**响应参数**:

| 参数名称                     | 参数说明                                     | 类型                | schema            |
|--------------------------|------------------------------------------|-------------------|-------------------| 
| children                 | 子菜单信息                                    | array             | MenuRecursionVO   |
| &emsp;&emsp;children     | 子菜单信息                                    | array             | MenuRecursionVO   |
| &emsp;&emsp;component    | 目标组件                                     | string            |                   |
| &emsp;&emsp;createTime   | 创建时间                                     | string(date-time) |                   |
| &emsp;&emsp;haveChildren | 是否有子菜单                                   | boolean           |                   |
| &emsp;&emsp;hidden       | 显示和隐藏，0：显示，1：隐藏                          | boolean           |                   |
| &emsp;&emsp;icon         | 前端图标                                     | string            |                   |
| &emsp;&emsp;id           | 主键                                       | string            |                   |
| &emsp;&emsp;level        | 菜单级数                                     | integer(int32)    |                   |
| &emsp;&emsp;menuType     | 菜单类型，PC：pc端菜单，MOBILE：移动端菜单,可用值:MOBILE,PC | string            |                   |
| &emsp;&emsp;meta         | 菜单配置                                     | string            |                   |
| &emsp;&emsp;name         | 前端名称                                     | string            |                   |
| &emsp;&emsp;parentId     | 父级ID                                     | string            |                   |
| &emsp;&emsp;path         | 前端路由                                     | string            |                   |
| &emsp;&emsp;remark       | 备注                                       | string            |                   |
| &emsp;&emsp;sort         | 菜单排序                                     | integer(int32)    |                   |
| &emsp;&emsp;state        | 状态                                       | boolean           |                   |
| &emsp;&emsp;title        | controller名称                             | string            |                   |
| &emsp;&emsp;updateTime   | 修改时间                                     | string(date-time) |                   |
| &emsp;&emsp;url          | 请求地址                                     | string            |                   |
| component                | 目标组件                                     | string            |                   |
| createTime               | 创建时间                                     | string(date-time) | string(date-time) |
| haveChildren             | 是否有子菜单                                   | boolean           |                   |
| hidden                   | 显示和隐藏，0：显示，1：隐藏                          | boolean           |                   |
| icon                     | 前端图标                                     | string            |                   |
| id                       | 主键                                       | string            |                   |
| level                    | 菜单级数                                     | integer(int32)    | integer(int32)    |
| menuType                 | 菜单类型，PC：pc端菜单，MOBILE：移动端菜单,可用值:MOBILE,PC | string            |                   |
| meta                     | 菜单配置                                     | string            |                   |
| name                     | 前端名称                                     | string            |                   |
| parentId                 | 父级ID                                     | string            |                   |
| path                     | 前端路由                                     | string            |                   |
| remark                   | 备注                                       | string            |                   |
| sort                     | 菜单排序                                     | integer(int32)    | integer(int32)    |
| state                    | 状态                                       | boolean           |                   |
| title                    | controller名称                             | string            |                   |
| updateTime               | 修改时间                                     | string(date-time) | string(date-time) |
| url                      | 请求地址                                     | string            |                   |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"component": "",
				"createTime": "",
				"haveChildren": false,
				"hidden": false,
				"icon": "",
				"id": "",
				"level": 0,
				"menuType": "",
				"meta": "",
				"name": "",
				"parentId": "",
				"path": "",
				"remark": "",
				"sort": 0,
				"state": false,
				"title": "",
				"updateTime": "",
				"url": ""
			}
		],
		"component": "",
		"createTime": "",
		"haveChildren": false,
		"hidden": false,
		"icon": "",
		"id": "",
		"level": 0,
		"menuType": "",
		"meta": "",
		"name": "",
		"parentId": "",
		"path": "",
		"remark": "",
		"sort": 0,
		"state": false,
		"title": "",
		"updateTime": "",
		"url": ""
	}
]
```

## 菜单树形下拉列表

**接口地址**:`/bbt-api/sys/menu/tree/select`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:<p>菜单树形列表，包含详情信息</p>

**请求参数**:

| 参数名称     | 参数说明                   | 请求类型  | 是否必须  | 数据类型   | schema |
|----------|------------------------|-------|-------|--------|--------|
| title    | title                  | query | false | string |        |
| menuType | menuType,可用值:MOBILE,PC | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | MenuTreeVO |

**响应参数**:

| 参数名称                 | 参数说明 | 类型     | schema     |
|----------------------|------|--------|------------| 
| children             | 子菜单  | array  | MenuTreeVO |
| &emsp;&emsp;children | 子菜单  | array  | MenuTreeVO |
| &emsp;&emsp;id       | 菜单id | string |            |
| &emsp;&emsp;name     | 菜单名称 | string |            |
| &emsp;&emsp;title    | 菜单标题 | string |            |
| id                   | 菜单id | string |            |
| name                 | 菜单名称 | string |            |
| title                | 菜单标题 | string |            |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"id": "",
				"name": "",
				"title": ""
			}
		],
		"id": "",
		"name": "",
		"title": ""
	}
]
```

## 新增系统菜单

**接口地址**:`/bbt-api/sys/menu`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "component": "",
  "hidden": false,
  "icon": "",
  "menuType": "",
  "meta": "",
  "name": "",
  "parentId": "",
  "path": "",
  "remark": "",
  "sort": 0,
  "state": false,
  "title": "",
  "url": ""
}
```

**请求参数**:

| 参数名称                  | 参数说明                                     | 请求类型 | 是否必须  | 数据类型           | schema   |
|-----------------------|------------------------------------------|------|-------|----------------|----------|
| 系统菜单新增参数              | 系统菜单新增参数                                 | body | true  | 系统菜单新增参数       | 系统菜单新增参数 |
| &emsp;&emsp;component | 目标组件                                     |      | false | string         |          |
| &emsp;&emsp;hidden    | 显示和隐藏，0：显示，1：隐藏                          |      | false | boolean        |          |
| &emsp;&emsp;icon      | 前端图标                                     |      | false | string         |          |
| &emsp;&emsp;menuType  | 菜单类型，PC：pc端菜单，MOBILE：移动端菜单,可用值:MOBILE,PC |      | false | string         |          |
| &emsp;&emsp;meta      | 菜单配置                                     |      | false | string         |          |
| &emsp;&emsp;name      | 前端名称                                     |      | false | string         |          |
| &emsp;&emsp;parentId  | 父级ID                                     |      | false | string         |          |
| &emsp;&emsp;path      | 视图地址                                     |      | false | string         |          |
| &emsp;&emsp;remark    | 备注                                       |      | false | string         |          |
| &emsp;&emsp;sort      | 菜单排序                                     |      | false | integer(int32) |          |
| &emsp;&emsp;state     | 状态                                       |      | false | boolean        |          |
| &emsp;&emsp;title     | 标题                                       |      | false | string         |          |
| &emsp;&emsp;url       | 请求地址                                     |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除系统菜单

**接口地址**:`/bbt-api/sys/menu/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 系统菜单详情

**接口地址**:`/bbt-api/sys/menu/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK | MenuVO |

**响应参数**:

| 参数名称                  | 参数说明            | 类型             | schema         |
|-----------------------|-----------------|----------------|----------------| 
| children              | 子集              | array          | MenuVO         |
| &emsp;&emsp;children  | 子集              | array          | MenuVO         |
| &emsp;&emsp;component | 目标组件            | string         |                |
| &emsp;&emsp;hidden    | 显示和隐藏，0：显示，1：隐藏 | boolean        |                |
| &emsp;&emsp;icon      | 前端图标            | string         |                |
| &emsp;&emsp;id        | ID              | string         |                |
| &emsp;&emsp;level     | 菜单级数            | integer(int32) |                |
| &emsp;&emsp;meta      | 菜单配置            | object         |                |
| &emsp;&emsp;name      | 前端名称            | string         |                |
| &emsp;&emsp;parentId  | 父级ID            | integer(int64) |                |
| &emsp;&emsp;path      | 前端路由            | string         |                |
| &emsp;&emsp;roles     |                 | array          | string         |
| &emsp;&emsp;state     | 菜单状态，0：禁用，1：启用  | boolean        |                |
| &emsp;&emsp;title     | controller名称    | string         |                |
| component             | 目标组件            | string         |                |
| hidden                | 显示和隐藏，0：显示，1：隐藏 | boolean        |                |
| icon                  | 前端图标            | string         |                |
| id                    | ID              | string         |                |
| level                 | 菜单级数            | integer(int32) | integer(int32) |
| meta                  | 菜单配置            | object         |                |
| name                  | 前端名称            | string         |                |
| parentId              | 父级ID            | integer(int64) | integer(int64) |
| path                  | 前端路由            | string         |                |
| roles                 |                 | array          |                |
| state                 | 菜单状态，0：禁用，1：启用  | boolean        |                |
| title                 | controller名称    | string         |                |

**响应示例**:

```javascript
{
	"children": [
		{
			"children": [
				{
					"children": [
						{}
					],
					"component": "",
					"hidden": false,
					"icon": "",
					"id": "",
					"level": 0,
					"meta": {},
					"name": "",
					"parentId": 0,
					"path": "",
					"roles": [],
					"state": false,
					"title": ""
				}
			],
			"component": "",
			"hidden": false,
			"icon": "",
			"id": "",
			"level": 0,
			"meta": {},
			"name": "",
			"parentId": 0,
			"path": "",
			"roles": [],
			"state": false,
			"title": ""
		}
	],
	"component": "",
	"hidden": false,
	"icon": "",
	"id": "",
	"level": 0,
	"meta": {},
	"name": "",
	"parentId": 0,
	"path": "",
	"roles": [],
	"state": false,
	"title": ""
}
```

# 系统资源

## 删除系统资源

**接口地址**:`/bbt-api/sys/resource/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 系统资源详情

**接口地址**:`/bbt-api/sys/resource/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema     |
|-----|----|------------| 
| 200 | OK | ResourceVO |

**响应参数**:

| 参数名称         | 参数说明            | 类型                | schema            |
|--------------|-----------------|-------------------|-------------------| 
| categoryId   | 分类id            | integer(int64)    | integer(int64)    |
| categoryName | 菜单名称            | string            |                   |
| createTime   | 创建时间            | string(date-time) | string(date-time) |
| description  | 资源描述            | string            |                   |
| hidden       | 显示和隐藏，0：显示，1：隐藏 | boolean           |                   |
| icon         | 图标              | string            |                   |
| id           | 主键              | string            |                   |
| method       | 请求方式            | string            |                   |
| name         | 资源名称            | string            |                   |
| path         | 接口路径            | string            |                   |
| state        | 状态，0：禁用，1：启用    | string            |                   |
| type         | 资源类型，按钮         | string            |                   |
| updateTime   | 修改时间            | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"categoryId": 0,
	"categoryName": "",
	"createTime": "",
	"description": "",
	"hidden": false,
	"icon": "",
	"id": "",
	"method": "",
	"name": "",
	"path": "",
	"state": "",
	"type": "",
	"updateTime": ""
}
```

## 获取资源分组列表

**接口地址**:`/bbt-api/sys/resource/group`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>资源分组列表</p>

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema          |
|-----|----|-----------------| 
| 200 | OK | ResourceGroupVO |

**响应参数**:

| 参数名称             | 参数说明 | 类型     | schema         |
|------------------|------|--------|----------------| 
| name             | 菜单分类 | string |                |
| resources        | 资源列表 | array  | ResourceBaseVO |
| &emsp;&emsp;id   | 主键   | string |                |
| &emsp;&emsp;name | 资源名称 | string |                |
| &emsp;&emsp;path | 接口路径 | string |                |

**响应示例**:

```javascript
[
	{
		"name": "",
		"resources": [
			{
				"id": "",
				"name": "",
				"path": ""
			}
		]
	}
]
```

## 系统资源列表

**接口地址**:`/bbt-api/sys/resource/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称        | 参数说明            | 请求类型  | 是否必须  | 数据类型              | schema |
|-------------|-----------------|-------|-------|-------------------|--------|
| categoryId  | 分类id            | query | false | integer(int64)    |        |
| current     | 页码,默认为1         | query | false | integer(int32)    |        |
| description | 资源描述            | query | false | string            |        |
| end         | 结束时间            | query | false | string(date-time) |        |
| hidden      | 显示和隐藏，0：显示，1：隐藏 | query | false | boolean           |        |
| icon        | 图标              | query | false | string            |        |
| method      | 请求方式            | query | false | string            |        |
| name        | 资源名称            | query | false | string            |        |
| path        | 接口路径            | query | false | string            |        |
| size        | 页大小,默认为10       | query | false | integer(int32)    |        |
| start       | 开始时间            | query | false | string(date-time) |        |
| state       | 状态，0：禁用，1：启用    | query | false | string            |        |
| type        | 资源类型，按钮         | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | 分页结果对象«ResourceVO» |

**响应参数**:

| 参数名称                     | 参数说明            | 类型                | schema         |
|--------------------------|-----------------|-------------------|----------------| 
| current                  | 当前页码            | integer(int32)    | integer(int32) |
| pages                    | 总页数             | integer(int32)    | integer(int32) |
| records                  | 数据列表            | array             | ResourceVO     |
| &emsp;&emsp;categoryId   | 分类id            | integer(int64)    |                |
| &emsp;&emsp;categoryName | 菜单名称            | string            |                |
| &emsp;&emsp;createTime   | 创建时间            | string(date-time) |                |
| &emsp;&emsp;description  | 资源描述            | string            |                |
| &emsp;&emsp;hidden       | 显示和隐藏，0：显示，1：隐藏 | boolean           |                |
| &emsp;&emsp;icon         | 图标              | string            |                |
| &emsp;&emsp;id           | 主键              | string            |                |
| &emsp;&emsp;method       | 请求方式            | string            |                |
| &emsp;&emsp;name         | 资源名称            | string            |                |
| &emsp;&emsp;path         | 接口路径            | string            |                |
| &emsp;&emsp;state        | 状态，0：禁用，1：启用    | string            |                |
| &emsp;&emsp;type         | 资源类型，按钮         | string            |                |
| &emsp;&emsp;updateTime   | 修改时间            | string(date-time) |                |
| size                     | 页大小             | integer(int32)    | integer(int32) |
| total                    | 总行数             | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"categoryId": 0,
			"categoryName": "",
			"createTime": "",
			"description": "",
			"hidden": false,
			"icon": "",
			"id": "",
			"method": "",
			"name": "",
			"path": "",
			"state": "",
			"type": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 新增系统资源

**接口地址**:`/bbt-api/sys/resource`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "categoryId": 0,
  "description": "",
  "hidden": false,
  "icon": "",
  "method": "",
  "name": "",
  "path": "",
  "pathPrefix": "",
  "state": "",
  "type": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明                                                  | 请求类型 | 是否必须  | 数据类型           | schema   |
|-------------------------|-------------------------------------------------------|------|-------|----------------|----------|
| 系统资源新增参数                | 系统资源新增参数                                              | body | true  | 系统资源新增参数       | 系统资源新增参数 |
| &emsp;&emsp;categoryId  | 分类id                                                  |      | false | integer(int64) |          |
| &emsp;&emsp;description | 资源描述                                                  |      | false | string         |          |
| &emsp;&emsp;hidden      | 显示和隐藏，0：显示，1：隐藏                                       |      | false | boolean        |          |
| &emsp;&emsp;icon        | 图标                                                    |      | false | string         |          |
| &emsp;&emsp;method      | 请求方式,可用值:DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT,TRACE |      | false | string         |          |
| &emsp;&emsp;name        | 资源名称                                                  |      | false | string         |          |
| &emsp;&emsp;path        | 接口路径                                                  |      | false | string         |          |
| &emsp;&emsp;pathPrefix  | 接口路径前缀                                                |      | false | string         |          |
| &emsp;&emsp;state       | 状态，0：禁用，1：启用                                          |      | false | string         |          |
| &emsp;&emsp;type        | 资源类型，按钮                                               |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 资源Tree下拉列表

**接口地址**:`/bbt-api/sys/resource/tree`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>菜单资源树形列表</p>

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | ResourceTreeVO |

**响应参数**:

| 参数名称                         | 参数说明 | 类型     | schema         |
|------------------------------|------|--------|----------------| 
| children                     | 子菜单  | array  | ResourceTreeVO |
| &emsp;&emsp;children         | 子菜单  | array  | ResourceTreeVO |
| &emsp;&emsp;id               | 菜单id | string |                |
| &emsp;&emsp;name             | 菜单名称 | string |                |
| &emsp;&emsp;path             | 接口地址 | string |                |
| &emsp;&emsp;resources        | 资源列表 | array  | ResourceBaseVO |
| &emsp;&emsp;&emsp;&emsp;id   | 主键   | string |                |
| &emsp;&emsp;&emsp;&emsp;name | 资源名称 | string |                |
| &emsp;&emsp;&emsp;&emsp;path | 接口路径 | string |                |
| &emsp;&emsp;title            | 菜单标题 | string |                |
| id                           | 菜单id | string |                |
| name                         | 菜单名称 | string |                |
| path                         | 接口地址 | string |                |
| resources                    | 资源列表 | array  | ResourceBaseVO |
| &emsp;&emsp;id               | 主键   | string |                |
| &emsp;&emsp;name             | 资源名称 | string |                |
| &emsp;&emsp;path             | 接口路径 | string |                |
| title                        | 菜单标题 | string |                |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"id": "",
				"name": "",
				"path": "",
				"resources": [],
				"title": ""
			}
		],
		"id": "",
		"name": "",
		"path": "",
		"resources": [
			{
				"id": "",
				"name": "",
				"path": ""
			}
		],
		"title": ""
	}
]
```

## 更新系统资源

**接口地址**:`/bbt-api/sys/resource/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "categoryId": 0,
  "description": "",
  "hidden": false,
  "icon": "",
  "id": "",
  "method": "",
  "name": "",
  "path": "",
  "pathPrefix": "",
  "state": "",
  "type": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明                                                  | 请求类型 | 是否必须  | 数据类型           | schema   |
|-------------------------|-------------------------------------------------------|------|-------|----------------|----------|
| 系统资源修改参数                | 系统资源修改参数                                              | body | true  | 系统资源修改参数       | 系统资源修改参数 |
| &emsp;&emsp;categoryId  | 分类id                                                  |      | false | integer(int64) |          |
| &emsp;&emsp;description | 资源描述                                                  |      | false | string         |          |
| &emsp;&emsp;hidden      | 显示和隐藏，0：显示，1：隐藏                                       |      | false | boolean        |          |
| &emsp;&emsp;icon        | 图标                                                    |      | false | string         |          |
| &emsp;&emsp;id          | 主键                                                    |      | false | string         |          |
| &emsp;&emsp;method      | 请求方式,可用值:DELETE,GET,HEAD,OPTIONS,PATCH,POST,PUT,TRACE |      | false | string         |          |
| &emsp;&emsp;name        | 资源名称                                                  |      | false | string         |          |
| &emsp;&emsp;path        | 接口路径                                                  |      | false | string         |          |
| &emsp;&emsp;pathPrefix  | 接口路径前缀                                                |      | false | string         |          |
| &emsp;&emsp;state       | 状态，0：禁用，1：启用                                          |      | false | string         |          |
| &emsp;&emsp;type        | 资源类型，按钮                                               |      | false | string         |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 系统配置

## 删除系统配置

**接口地址**:`/bbt-api/sys/config/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 获取全部配置

**接口地址**:`/bbt-api/sys/config/all`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 根据code获取配置

**接口地址**:`/bbt-api/api/sys/config/code`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| code | code | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | Result«SysConfigAO» |

**响应参数**:

| 参数名称                    | 参数说明 | 类型             | schema         |
|-------------------------|------|----------------|----------------| 
| code                    |      | integer(int32) | integer(int32) |
| data                    |      | SysConfigAO    | SysConfigAO    |
| &emsp;&emsp;configCode  | 参数编码 | string         |                |
| &emsp;&emsp;configName  | 参数名称 | string         |                |
| &emsp;&emsp;configValue | 参数键值 | string         |                |
| &emsp;&emsp;id          | 参数Id | string         |                |
| &emsp;&emsp;valueType   | 值类型  | string         |                |
| message                 |      | string         |                |
| stack                   |      | string         |                |
| success                 |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {
		"configCode": "",
		"configName": "",
		"configValue": "",
		"id": "",
		"valueType": ""
	},
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据code获取配置

**接口地址**:`/bbt-api/sys/config/code`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|------|------|-------|-------|--------|--------|
| code | code | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | SysConfigVO |

**响应参数**:

| 参数名称        | 参数说明 | 类型      | schema |
|-------------|------|---------|--------| 
| configCode  | 参数编码 | string  |        |
| configName  | 参数名称 | string  |        |
| configValue | 参数键值 | string  |        |
| id          | 参数Id | string  |        |
| remark      | 备注   | string  |        |
| state       | 状态   | boolean |        |
| valueType   | 值类型  | string  |        |

**响应示例**:

```javascript
{
	"configCode": "",
	"configName": "",
	"configValue": "",
	"id": "",
	"remark": "",
	"state": false,
	"valueType": ""
}
```

## 系统配置分页列表

**接口地址**:`/bbt-api/sys/config/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明      | 请求类型  | 是否必须  | 数据类型           | schema |
|------------|-----------|-------|-------|----------------|--------|
| configName | 参数名称      | query | false | string         |        |
| current    | 页码,默认为1   | query | false | integer(int32) |        |
| size       | 页大小,默认为10 | query | false | integer(int32) |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | 分页结果对象«SysConfigVO» |

**响应参数**:

| 参数名称                    | 参数说明 | 类型             | schema         |
|-------------------------|------|----------------|----------------| 
| current                 | 当前页码 | integer(int32) | integer(int32) |
| pages                   | 总页数  | integer(int32) | integer(int32) |
| records                 | 数据列表 | array          | SysConfigVO    |
| &emsp;&emsp;configCode  | 参数编码 | string         |                |
| &emsp;&emsp;configName  | 参数名称 | string         |                |
| &emsp;&emsp;configValue | 参数键值 | string         |                |
| &emsp;&emsp;id          | 参数Id | string         |                |
| &emsp;&emsp;remark      | 备注   | string         |                |
| &emsp;&emsp;state       | 状态   | boolean        |                |
| &emsp;&emsp;valueType   | 值类型  | string         |                |
| size                    | 页大小  | integer(int32) | integer(int32) |
| total                   | 总行数  | integer(int32) | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"configCode": "",
			"configName": "",
			"configValue": "",
			"id": "",
			"remark": "",
			"state": false,
			"valueType": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 系统配置详情

**接口地址**:`/bbt-api/sys/config/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | SysConfigVO |

**响应参数**:

| 参数名称        | 参数说明 | 类型      | schema |
|-------------|------|---------|--------| 
| configCode  | 参数编码 | string  |        |
| configName  | 参数名称 | string  |        |
| configValue | 参数键值 | string  |        |
| id          | 参数Id | string  |        |
| remark      | 备注   | string  |        |
| state       | 状态   | boolean |        |
| valueType   | 值类型  | string  |        |

**响应示例**:

```javascript
{
	"configCode": "",
	"configName": "",
	"configValue": "",
	"id": "",
	"remark": "",
	"state": false,
	"valueType": ""
}
```

## 新增系统配置

**接口地址**:`/bbt-api/sys/config`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "configCode": "",
  "configName": "",
  "configValue": "",
  "state": false,
  "valueType": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema   |
|-------------------------|----------|------|-------|----------|----------|
| 系统配置新增参数                | 系统配置新增参数 | body | true  | 系统配置新增参数 | 系统配置新增参数 |
| &emsp;&emsp;configCode  | 参数编码     |      | false | string   |          |
| &emsp;&emsp;configName  | 参数名称     |      | false | string   |          |
| &emsp;&emsp;configValue | 参数键值     |      | false | string   |          |
| &emsp;&emsp;state       | 状态       |      | false | boolean  |          |
| &emsp;&emsp;valueType   | 值类型      |      | false | string   |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 更新系统配置

**接口地址**:`/bbt-api/sys/config/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "configName": "",
  "configValue": "",
  "id": "",
  "remark": "",
  "state": false,
  "valueType": ""
}
```

**请求参数**:

| 参数名称                    | 参数说明     | 请求类型 | 是否必须  | 数据类型     | schema   |
|-------------------------|----------|------|-------|----------|----------|
| 系统配置修改参数                | 系统配置修改参数 | body | true  | 系统配置修改参数 | 系统配置修改参数 |
| &emsp;&emsp;configName  | 参数名称     |      | false | string   |          |
| &emsp;&emsp;configValue | 参数键值     |      | false | string   |          |
| &emsp;&emsp;id          | 参数Id     |      | false | string   |          |
| &emsp;&emsp;remark      | 备注       |      | false | string   |          |
| &emsp;&emsp;state       | 状态       |      | false | boolean  |          |
| &emsp;&emsp;valueType   | 值类型      |      | false | string   |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 菜单操作

## 菜单操作详情

**接口地址**:`/bbt-api/sys/menu/operate/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema        |
|-----|----|---------------| 
| 200 | OK | MenuOperateVO |

**响应参数**:

| 参数名称          | 参数说明            | 类型                | schema            |
|---------------|-----------------|-------------------|-------------------| 
| createTime    | 创建时间            | string(date-time) | string(date-time) |
| description   | 资源描述            | string            |                   |
| hidden        | 显示和隐藏，0：显示，1：隐藏 | boolean           |                   |
| icon          | 图标              | string            |                   |
| id            | 主键              | string            |                   |
| menuId        | 菜单id            | string            |                   |
| operateCode   | 资源编码            | string            |                   |
| operateMethod | 请求方式            | string            |                   |
| operateName   | 资源名称            | string            |                   |
| operateType   | 资源类型，按钮         | string            |                   |
| path          | 接口路径            | string            |                   |
| state         | 状态，0：禁用，1：启用    | string            |                   |
| updateTime    | 修改时间            | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"createTime": "",
	"description": "",
	"hidden": false,
	"icon": "",
	"id": "",
	"menuId": "",
	"operateCode": "",
	"operateMethod": "",
	"operateName": "",
	"operateType": "",
	"path": "",
	"state": "",
	"updateTime": ""
}
```

## 菜单操作分页列表

**接口地址**:`/bbt-api/sys/menu/operate/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求参数**:

| 参数名称          | 参数说明            | 请求类型  | 是否必须  | 数据类型              | schema |
|---------------|-----------------|-------|-------|-------------------|--------|
| current       | 页码,默认为1         | query | false | integer(int32)    |        |
| description   | 资源描述            | query | false | string            |        |
| end           | 结束时间            | query | false | string(date-time) |        |
| hidden        | 显示和隐藏，0：显示，1：隐藏 | query | false | boolean           |        |
| icon          | 图标              | query | false | string            |        |
| operateCode   | 资源编码            | query | false | string            |        |
| operateMethod | 请求方式            | query | false | string            |        |
| operateName   | 资源名称            | query | false | string            |        |
| operateType   | 资源类型，按钮         | query | false | string            |        |
| path          | 接口路径            | query | false | string            |        |
| size          | 页大小,默认为10       | query | false | integer(int32)    |        |
| start         | 开始时间            | query | false | string(date-time) |        |
| state         | 状态，0：禁用，1：启用    | query | false | string            |        |

**响应状态**:

| 状态码 | 说明 | schema                |
|-----|----|-----------------------| 
| 200 | OK | 分页结果对象«MenuOperateVO» |

**响应参数**:

| 参数名称                      | 参数说明            | 类型                | schema         |
|---------------------------|-----------------|-------------------|----------------| 
| current                   | 当前页码            | integer(int32)    | integer(int32) |
| pages                     | 总页数             | integer(int32)    | integer(int32) |
| records                   | 数据列表            | array             | MenuOperateVO  |
| &emsp;&emsp;createTime    | 创建时间            | string(date-time) |                |
| &emsp;&emsp;description   | 资源描述            | string            |                |
| &emsp;&emsp;hidden        | 显示和隐藏，0：显示，1：隐藏 | boolean           |                |
| &emsp;&emsp;icon          | 图标              | string            |                |
| &emsp;&emsp;id            | 主键              | string            |                |
| &emsp;&emsp;menuId        | 菜单id            | string            |                |
| &emsp;&emsp;operateCode   | 资源编码            | string            |                |
| &emsp;&emsp;operateMethod | 请求方式            | string            |                |
| &emsp;&emsp;operateName   | 资源名称            | string            |                |
| &emsp;&emsp;operateType   | 资源类型，按钮         | string            |                |
| &emsp;&emsp;path          | 接口路径            | string            |                |
| &emsp;&emsp;state         | 状态，0：禁用，1：启用    | string            |                |
| &emsp;&emsp;updateTime    | 修改时间            | string(date-time) |                |
| size                      | 页大小             | integer(int32)    | integer(int32) |
| total                     | 总行数             | integer(int32)    | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"createTime": "",
			"description": "",
			"hidden": false,
			"icon": "",
			"id": "",
			"menuId": "",
			"operateCode": "",
			"operateMethod": "",
			"operateName": "",
			"operateType": "",
			"path": "",
			"state": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 新增菜单操作

**接口地址**:`/bbt-api/sys/menu/operate/add`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "description": "",
  "hidden": false,
  "icon": "",
  "operateCode": "",
  "operateMethod": "",
  "operateName": "",
  "operateType": "",
  "path": "",
  "state": ""
}
```

**请求参数**:

| 参数名称                      | 参数说明            | 请求类型 | 是否必须  | 数据类型     | schema   |
|---------------------------|-----------------|------|-------|----------|----------|
| 菜单操作新增参数                  | 菜单操作新增参数        | body | true  | 菜单操作新增参数 | 菜单操作新增参数 |
| &emsp;&emsp;description   | 资源描述            |      | false | string   |          |
| &emsp;&emsp;hidden        | 显示和隐藏，0：显示，1：隐藏 |      | false | boolean  |          |
| &emsp;&emsp;icon          | 图标              |      | false | string   |          |
| &emsp;&emsp;operateCode   | 资源编码            |      | false | string   |          |
| &emsp;&emsp;operateMethod | 请求方式            |      | false | string   |          |
| &emsp;&emsp;operateName   | 资源名称            |      | false | string   |          |
| &emsp;&emsp;operateType   | 资源类型，按钮         |      | false | string   |          |
| &emsp;&emsp;path          | 接口路径            |      | false | string   |          |
| &emsp;&emsp;state         | 状态，0：禁用，1：启用    |      | false | string   |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 菜单操作查询列表

**接口地址**:`/bbt-api/sys/menu/operate/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Yangle

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明 | 请求类型  | 是否必须  | 数据类型   | schema |
|---------|------|-------|-------|--------|--------|
| keyword | 关键字  | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema        |
|-----|----|---------------| 
| 200 | OK | MenuOperateVO |

**响应参数**:

| 参数名称          | 参数说明            | 类型                | schema            |
|---------------|-----------------|-------------------|-------------------| 
| createTime    | 创建时间            | string(date-time) | string(date-time) |
| description   | 资源描述            | string            |                   |
| hidden        | 显示和隐藏，0：显示，1：隐藏 | boolean           |                   |
| icon          | 图标              | string            |                   |
| id            | 主键              | string            |                   |
| menuId        | 菜单id            | string            |                   |
| operateCode   | 资源编码            | string            |                   |
| operateMethod | 请求方式            | string            |                   |
| operateName   | 资源名称            | string            |                   |
| operateType   | 资源类型，按钮         | string            |                   |
| path          | 接口路径            | string            |                   |
| state         | 状态，0：禁用，1：启用    | string            |                   |
| updateTime    | 修改时间            | string(date-time) | string(date-time) |

**响应示例**:

```javascript
[
	{
		"createTime": "",
		"description": "",
		"hidden": false,
		"icon": "",
		"id": "",
		"menuId": "",
		"operateCode": "",
		"operateMethod": "",
		"operateName": "",
		"operateType": "",
		"path": "",
		"state": "",
		"updateTime": ""
	}
]
```

## 更新菜单操作

**接口地址**:`/bbt-api/sys/menu/operate/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "description": "",
  "hidden": false,
  "icon": "",
  "id": "",
  "operateCode": "",
  "operateMethod": "",
  "operateName": "",
  "operateType": "",
  "path": "",
  "state": ""
}
```

**请求参数**:

| 参数名称                      | 参数说明            | 请求类型 | 是否必须  | 数据类型     | schema   |
|---------------------------|-----------------|------|-------|----------|----------|
| 菜单操作修改参数                  | 菜单操作修改参数        | body | true  | 菜单操作修改参数 | 菜单操作修改参数 |
| &emsp;&emsp;description   | 资源描述            |      | false | string   |          |
| &emsp;&emsp;hidden        | 显示和隐藏，0：显示，1：隐藏 |      | false | boolean  |          |
| &emsp;&emsp;icon          | 图标              |      | false | string   |          |
| &emsp;&emsp;id            | 主键              |      | false | string   |          |
| &emsp;&emsp;operateCode   | 资源编码            |      | false | string   |          |
| &emsp;&emsp;operateMethod | 请求方式            |      | false | string   |          |
| &emsp;&emsp;operateName   | 资源名称            |      | false | string   |          |
| &emsp;&emsp;operateType   | 资源类型，按钮         |      | false | string   |          |
| &emsp;&emsp;path          | 接口路径            |      | false | string   |          |
| &emsp;&emsp;state         | 状态，0：禁用，1：启用    |      | false | string   |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 删除菜单操作

**接口地址**:`/bbt-api/sys/menu/operate/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**开发者**:yunjin

**接口描述**:

**请求示例**:

```javascript
{
  "ids": []
}
```

**请求参数**:

| 参数名称            | 参数说明            | 请求类型 | 是否必须  | 数据类型            | schema          |
|-----------------|-----------------|------|-------|-----------------|-----------------|
| set集合参数«string» | Set集合参数«string» | body | true  | Set集合参数«string» | Set集合参数«string» |
| &emsp;&emsp;ids |                 |      | false | array           | string          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 菜单操作Feign接口

## 根据ids获取菜单操作列表

**接口地址**:`/bbt-api/api/sys/menu/operate/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型   | schema |
|------|------|-------|------|--------|--------|
| ids  | ids  | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                      |
|-----|----|-----------------------------| 
| 200 | OK | Result«List«MenuOperateAO»» |

**响应参数**:

| 参数名称                      | 参数说明 | 类型                | schema         |
|---------------------------|------|-------------------|----------------| 
| code                      |      | integer(int32)    | integer(int32) |
| data                      |      | array             | MenuOperateAO  |
| &emsp;&emsp;createTime    |      | string(date-time) |                |
| &emsp;&emsp;description   |      | string            |                |
| &emsp;&emsp;hidden        |      | boolean           |                |
| &emsp;&emsp;icon          |      | string            |                |
| &emsp;&emsp;id            |      | string            |                |
| &emsp;&emsp;operateCode   |      | string            |                |
| &emsp;&emsp;operateMethod |      | string            |                |
| &emsp;&emsp;operateName   |      | string            |                |
| &emsp;&emsp;operateType   |      | string            |                |
| &emsp;&emsp;path          |      | string            |                |
| &emsp;&emsp;state         |      | string            |                |
| &emsp;&emsp;updateTime    |      | string(date-time) |                |
| message                   |      | string            |                |
| stack                     |      | string            |                |
| success                   |      | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"createTime": "",
			"description": "",
			"hidden": true,
			"icon": "",
			"id": "",
			"operateCode": "",
			"operateMethod": "",
			"operateName": "",
			"operateType": "",
			"path": "",
			"state": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 根据menuId获取菜单操作列表

**接口地址**:`/bbt-api/api/sys/menu/operate/api/menu/operate/list/menuId`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称   | 参数说明   | 请求类型  | 是否必须 | 数据类型   | schema |
|--------|--------|-------|------|--------|--------|
| menuId | menuId | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                      |
|-----|----|-----------------------------| 
| 200 | OK | Result«List«MenuOperateAO»» |

**响应参数**:

| 参数名称                      | 参数说明 | 类型                | schema         |
|---------------------------|------|-------------------|----------------| 
| code                      |      | integer(int32)    | integer(int32) |
| data                      |      | array             | MenuOperateAO  |
| &emsp;&emsp;createTime    |      | string(date-time) |                |
| &emsp;&emsp;description   |      | string            |                |
| &emsp;&emsp;hidden        |      | boolean           |                |
| &emsp;&emsp;icon          |      | string            |                |
| &emsp;&emsp;id            |      | string            |                |
| &emsp;&emsp;operateCode   |      | string            |                |
| &emsp;&emsp;operateMethod |      | string            |                |
| &emsp;&emsp;operateName   |      | string            |                |
| &emsp;&emsp;operateType   |      | string            |                |
| &emsp;&emsp;path          |      | string            |                |
| &emsp;&emsp;state         |      | string            |                |
| &emsp;&emsp;updateTime    |      | string(date-time) |                |
| message                   |      | string            |                |
| stack                     |      | string            |                |
| success                   |      | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"createTime": "",
			"description": "",
			"hidden": true,
			"icon": "",
			"id": "",
			"operateCode": "",
			"operateMethod": "",
			"operateName": "",
			"operateType": "",
			"path": "",
			"state": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## listByMenuIds

**接口地址**:`/bbt-api/api/sys/menu/operate/list/menuIds`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称    | 参数说明    | 请求类型  | 是否必须 | 数据类型   | schema |
|---------|---------|-------|------|--------|--------|
| menuIds | menuIds | query | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema                      |
|-----|----|-----------------------------| 
| 200 | OK | Result«List«MenuOperateAO»» |

**响应参数**:

| 参数名称                      | 参数说明 | 类型                | schema         |
|---------------------------|------|-------------------|----------------| 
| code                      |      | integer(int32)    | integer(int32) |
| data                      |      | array             | MenuOperateAO  |
| &emsp;&emsp;createTime    |      | string(date-time) |                |
| &emsp;&emsp;description   |      | string            |                |
| &emsp;&emsp;hidden        |      | boolean           |                |
| &emsp;&emsp;icon          |      | string            |                |
| &emsp;&emsp;id            |      | string            |                |
| &emsp;&emsp;operateCode   |      | string            |                |
| &emsp;&emsp;operateMethod |      | string            |                |
| &emsp;&emsp;operateName   |      | string            |                |
| &emsp;&emsp;operateType   |      | string            |                |
| &emsp;&emsp;path          |      | string            |                |
| &emsp;&emsp;state         |      | string            |                |
| &emsp;&emsp;updateTime    |      | string(date-time) |                |
| message                   |      | string            |                |
| stack                     |      | string            |                |
| success                   |      | boolean           |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"createTime": "",
			"description": "",
			"hidden": true,
			"icon": "",
			"id": "",
			"operateCode": "",
			"operateMethod": "",
			"operateName": "",
			"operateType": "",
			"path": "",
			"state": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

# 行政区域划分

## 删除行政区域划分

**接口地址**:`/bbt-api/sys/region/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 导出模板

**接口地址**:`/bbt-api/sys/region/export/template`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 行政区域树形列表

**接口地址**:`/bbt-api/sys/region/tree/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:<p>一次性全部加载</p>

**请求参数**:

| 参数名称       | 参数说明               | 请求类型  | 是否必须  | 数据类型    | schema |
|------------|--------------------|-------|-------|---------|--------|
| codeTop    | 是否以传入的code作为顶级树的开始 | query | false | boolean |        |
| parentId   | parentId           | query | false | string  |        |
| regionCode | 当前区域code           | query | false | string  |        |
| regionName | 当前区域名称             | query | false | string  |        |

**响应状态**:

| 状态码 | 说明 | schema                    |
|-----|----|---------------------------| 
| 200 | OK | Result«List«SysRegionVO»» |

**响应参数**:

| 参数名称                           | 参数说明                                                           | 类型                 | schema         |
|--------------------------------|----------------------------------------------------------------|--------------------|----------------| 
| code                           |                                                                | integer(int32)     | integer(int32) |
| data                           |                                                                | array              | SysRegionVO    |
| &emsp;&emsp;boundBottom        | 行政区下边界纬度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;boundLeft          | 行政区左边界经度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;boundRight         | 行政区右边界经度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;boundTop           | 行政区上边界纬度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;children           | 子区域信息                                                          | array              | SysRegionVO    |
| &emsp;&emsp;createBy           | 创建人                                                            | string             |                |
| &emsp;&emsp;createTime         | 创建时间                                                           | string(date-time)  |                |
| &emsp;&emsp;extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | integer(int64)     |                |
| &emsp;&emsp;gisOid             | gis标绘信息，大于0整数代表已标绘                                             | integer(int64)     |                |
| &emsp;&emsp;id                 | 自增ID                                                           | string             |                |
| &emsp;&emsp;latitude           | 纬度                                                             | number(bigdecimal) |                |
| &emsp;&emsp;longitude          | 经度                                                             | number(bigdecimal) |                |
| &emsp;&emsp;parentCode         | 上级行政区域编码                                                       | string             |                |
| &emsp;&emsp;parents            | 父级code集合 ,分隔                                                   | string             |                |
| &emsp;&emsp;regionArea         | 区域面积(KM2)                                                      | number(bigdecimal) |                |
| &emsp;&emsp;regionCode         | 行政区域编码                                                         | string             |                |
| &emsp;&emsp;regionLeader       | 区域负责人                                                          | string             |                |
| &emsp;&emsp;regionLevel        | 区域级别                                                           | integer(int32)     |                |
| &emsp;&emsp;regionName         | 行政区域名称                                                         | string             |                |
| &emsp;&emsp;regionPeopleNumber | 区域人口                                                           | integer(int64)     |                |
| &emsp;&emsp;regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | string             |                |
| &emsp;&emsp;remark             | 备注                                                             | string             |                |
| &emsp;&emsp;subNum             | 子级数目                                                           | integer(int64)     |                |
| &emsp;&emsp;updateBy           | 更新人                                                            | string             |                |
| &emsp;&emsp;updateTime         | 更新时间                                                           | string(date-time)  |                |
| message                        |                                                                | string             |                |
| stack                          |                                                                | string             |                |
| success                        |                                                                | boolean            |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": [
		{
			"boundBottom": 0,
			"boundLeft": 0,
			"boundRight": 0,
			"boundTop": 0,
			"children": [
				{
					"boundBottom": 0,
					"boundLeft": 0,
					"boundRight": 0,
					"boundTop": 0,
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"extend": 0,
					"gisOid": 0,
					"id": "",
					"latitude": 0,
					"longitude": 0,
					"parentCode": "",
					"parents": "",
					"regionArea": 0,
					"regionCode": "",
					"regionLeader": "",
					"regionLevel": 0,
					"regionName": "",
					"regionPeopleNumber": 0,
					"regionType": "",
					"remark": "",
					"subNum": 0,
					"updateBy": "",
					"updateTime": ""
				}
			],
			"createBy": "",
			"createTime": "",
			"extend": 0,
			"gisOid": 0,
			"id": "",
			"latitude": 0,
			"longitude": 0,
			"parentCode": "",
			"parents": "",
			"regionArea": 0,
			"regionCode": "",
			"regionLeader": "",
			"regionLevel": 0,
			"regionName": "",
			"regionPeopleNumber": 0,
			"regionType": "",
			"remark": "",
			"subNum": 0,
			"updateBy": "",
			"updateTime": ""
		}
	],
	"message": "",
	"stack": "",
	"success": true
}
```

## 逐级加载行政区域

**接口地址**:`/bbt-api/sys/region/loadStep`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称       | 参数说明     | 请求类型  | 是否必须  | 数据类型   | schema |
|------------|----------|-------|-------|--------|--------|
| regionCode | 当前区域code | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | SysRegionVO |

**响应参数**:

| 参数名称                           | 参数说明                                                           | 类型                 | schema             |
|--------------------------------|----------------------------------------------------------------|--------------------|--------------------| 
| boundBottom                    | 行政区下边界纬度                                                       | number(bigdecimal) | number(bigdecimal) |
| boundLeft                      | 行政区左边界经度                                                       | number(bigdecimal) | number(bigdecimal) |
| boundRight                     | 行政区右边界经度                                                       | number(bigdecimal) | number(bigdecimal) |
| boundTop                       | 行政区上边界纬度                                                       | number(bigdecimal) | number(bigdecimal) |
| children                       | 子区域信息                                                          | array              | SysRegionVO        |
| &emsp;&emsp;boundBottom        | 行政区下边界纬度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;boundLeft          | 行政区左边界经度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;boundRight         | 行政区右边界经度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;boundTop           | 行政区上边界纬度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;children           | 子区域信息                                                          | array              | SysRegionVO        |
| &emsp;&emsp;createBy           | 创建人                                                            | string             |                    |
| &emsp;&emsp;createTime         | 创建时间                                                           | string(date-time)  |                    |
| &emsp;&emsp;extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | integer(int64)     |                    |
| &emsp;&emsp;gisOid             | gis标绘信息，大于0整数代表已标绘                                             | integer(int64)     |                    |
| &emsp;&emsp;id                 | 自增ID                                                           | string             |                    |
| &emsp;&emsp;latitude           | 纬度                                                             | number(bigdecimal) |                    |
| &emsp;&emsp;longitude          | 经度                                                             | number(bigdecimal) |                    |
| &emsp;&emsp;parentCode         | 上级行政区域编码                                                       | string             |                    |
| &emsp;&emsp;parents            | 父级code集合 ,分隔                                                   | string             |                    |
| &emsp;&emsp;regionArea         | 区域面积(KM2)                                                      | number(bigdecimal) |                    |
| &emsp;&emsp;regionCode         | 行政区域编码                                                         | string             |                    |
| &emsp;&emsp;regionLeader       | 区域负责人                                                          | string             |                    |
| &emsp;&emsp;regionLevel        | 区域级别                                                           | integer(int32)     |                    |
| &emsp;&emsp;regionName         | 行政区域名称                                                         | string             |                    |
| &emsp;&emsp;regionPeopleNumber | 区域人口                                                           | integer(int64)     |                    |
| &emsp;&emsp;regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | string             |                    |
| &emsp;&emsp;remark             | 备注                                                             | string             |                    |
| &emsp;&emsp;subNum             | 子级数目                                                           | integer(int64)     |                    |
| &emsp;&emsp;updateBy           | 更新人                                                            | string             |                    |
| &emsp;&emsp;updateTime         | 更新时间                                                           | string(date-time)  |                    |
| createBy                       | 创建人                                                            | string             |                    |
| createTime                     | 创建时间                                                           | string(date-time)  | string(date-time)  |
| extend                         | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | integer(int64)     | integer(int64)     |
| gisOid                         | gis标绘信息，大于0整数代表已标绘                                             | integer(int64)     | integer(int64)     |
| id                             | 自增ID                                                           | string             |                    |
| latitude                       | 纬度                                                             | number(bigdecimal) | number(bigdecimal) |
| longitude                      | 经度                                                             | number(bigdecimal) | number(bigdecimal) |
| parentCode                     | 上级行政区域编码                                                       | string             |                    |
| parents                        | 父级code集合 ,分隔                                                   | string             |                    |
| regionArea                     | 区域面积(KM2)                                                      | number(bigdecimal) | number(bigdecimal) |
| regionCode                     | 行政区域编码                                                         | string             |                    |
| regionLeader                   | 区域负责人                                                          | string             |                    |
| regionLevel                    | 区域级别                                                           | integer(int32)     | integer(int32)     |
| regionName                     | 行政区域名称                                                         | string             |                    |
| regionPeopleNumber             | 区域人口                                                           | integer(int64)     | integer(int64)     |
| regionType                     | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | string             |                    |
| remark                         | 备注                                                             | string             |                    |
| subNum                         | 子级数目                                                           | integer(int64)     | integer(int64)     |
| updateBy                       | 更新人                                                            | string             |                    |
| updateTime                     | 更新时间                                                           | string(date-time)  | string(date-time)  |

**响应示例**:

```javascript
[
	{
		"boundBottom": 0,
		"boundLeft": 0,
		"boundRight": 0,
		"boundTop": 0,
		"children": [
			{
				"boundBottom": 0,
				"boundLeft": 0,
				"boundRight": 0,
				"boundTop": 0,
				"children": [
					{
						"boundBottom": 0,
						"boundLeft": 0,
						"boundRight": 0,
						"boundTop": 0,
						"children": [
							{}
						],
						"createBy": "",
						"createTime": "",
						"extend": 0,
						"gisOid": 0,
						"id": "",
						"latitude": 0,
						"longitude": 0,
						"parentCode": "",
						"parents": "",
						"regionArea": 0,
						"regionCode": "",
						"regionLeader": "",
						"regionLevel": 0,
						"regionName": "",
						"regionPeopleNumber": 0,
						"regionType": "",
						"remark": "",
						"subNum": 0,
						"updateBy": "",
						"updateTime": ""
					}
				],
				"createBy": "",
				"createTime": "",
				"extend": 0,
				"gisOid": 0,
				"id": "",
				"latitude": 0,
				"longitude": 0,
				"parentCode": "",
				"parents": "",
				"regionArea": 0,
				"regionCode": "",
				"regionLeader": "",
				"regionLevel": 0,
				"regionName": "",
				"regionPeopleNumber": 0,
				"regionType": "",
				"remark": "",
				"subNum": 0,
				"updateBy": "",
				"updateTime": ""
			}
		],
		"createBy": "",
		"createTime": "",
		"extend": 0,
		"gisOid": 0,
		"id": "",
		"latitude": 0,
		"longitude": 0,
		"parentCode": "",
		"parents": "",
		"regionArea": 0,
		"regionCode": "",
		"regionLeader": "",
		"regionLevel": 0,
		"regionName": "",
		"regionPeopleNumber": 0,
		"regionType": "",
		"remark": "",
		"subNum": 0,
		"updateBy": "",
		"updateTime": ""
	}
]
```

## 导入区域

**接口地址**:`/bbt-api/sys/region/import`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,multipart/form-data`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型  | 是否必须 | 数据类型 | schema |
|------|------|-------|------|------|--------|
| file | file | query | true | file |        |

**响应状态**:

| 状态码 | 说明 | schema         |
|-----|----|----------------| 
| 200 | OK | Result«object» |

**响应参数**:

| 参数名称    | 参数说明 | 类型             | schema         |
|---------|------|----------------|----------------| 
| code    |      | integer(int32) | integer(int32) |
| data    |      | object         |                |
| message |      | string         |                |
| stack   |      | string         |                |
| success |      | boolean        |                |

**响应示例**:

```javascript
{
	"code": 0,
	"data": {},
	"message": "",
	"stack": "",
	"success": true
}
```

## 更新行政区域划分

**接口地址**:`/bbt-api/sys/region/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "boundBottom": 0,
  "boundLeft": 0,
  "boundRight": 0,
  "boundTop": 0,
  "extend": 0,
  "gisOid": 0,
  "id": "",
  "latitude": 0,
  "longitude": 0,
  "parentCode": "",
  "parents": "",
  "regionArea": 0,
  "regionCode": "",
  "regionLeader": "",
  "regionLevel": 0,
  "regionName": "",
  "regionPeopleNumber": 0,
  "regionType": "",
  "remark": ""
}
```

**请求参数**:

| 参数名称                           | 参数说明                                                           | 请求类型 | 是否必须  | 数据类型               | schema     |
|--------------------------------|----------------------------------------------------------------|------|-------|--------------------|------------|
| 行政区域划分修改参数                     | 行政区域划分修改参数                                                     | body | true  | 行政区域划分修改参数         | 行政区域划分修改参数 |
| &emsp;&emsp;boundBottom        | 行政区下边界纬度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;boundLeft          | 行政区左边界经度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;boundRight         | 行政区右边界经度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;boundTop           | 行政区上边界纬度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） |      | false | integer(int64)     |            |
| &emsp;&emsp;gisOid             | gis标绘信息，大于0整数代表已标绘                                             |      | false | integer(int64)     |            |
| &emsp;&emsp;id                 | 自增ID                                                           |      | false | string             |            |
| &emsp;&emsp;latitude           | 纬度                                                             |      | false | number(bigdecimal) |            |
| &emsp;&emsp;longitude          | 经度                                                             |      | false | number(bigdecimal) |            |
| &emsp;&emsp;parentCode         | 上级行政区域编码                                                       |      | false | string             |            |
| &emsp;&emsp;parents            | 父级code集合 ,分隔                                                   |      | false | string             |            |
| &emsp;&emsp;regionArea         | 区域面积(KM2)                                                      |      | false | number(bigdecimal) |            |
| &emsp;&emsp;regionCode         | 行政区域编码                                                         |      | false | string             |            |
| &emsp;&emsp;regionLeader       | 区域负责人                                                          |      | false | string             |            |
| &emsp;&emsp;regionLevel        | 区域级别                                                           |      | false | integer(int32)     |            |
| &emsp;&emsp;regionName         | 行政区域名称                                                         |      | false | string             |            |
| &emsp;&emsp;regionPeopleNumber | 区域人口                                                           |      | false | integer(int64)     |            |
| &emsp;&emsp;regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   |      | false | string             |            |
| &emsp;&emsp;remark             | 备注                                                             |      | false | string             |            |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 行政区域划分分页列表

**接口地址**:`/bbt-api/sys/region/page`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称               | 参数说明                                                           | 请求类型  | 是否必须  | 数据类型               | schema |
|--------------------|----------------------------------------------------------------|-------|-------|--------------------|--------|
| boundBottom        | 行政区下边界纬度                                                       | query | false | number(bigdecimal) |        |
| boundLeft          | 行政区左边界经度                                                       | query | false | number(bigdecimal) |        |
| boundRight         | 行政区右边界经度                                                       | query | false | number(bigdecimal) |        |
| boundTop           | 行政区上边界纬度                                                       | query | false | number(bigdecimal) |        |
| current            | 页码,默认为1                                                        | query | false | integer(int32)     |        |
| end                | 结束时间                                                           | query | false | string(date-time)  |        |
| extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | query | false | integer(int64)     |        |
| gisOid             | gis标绘信息，大于0整数代表已标绘                                             | query | false | integer(int64)     |        |
| latitude           | 纬度                                                             | query | false | number(bigdecimal) |        |
| longitude          | 经度                                                             | query | false | number(bigdecimal) |        |
| parentCode         | 上级行政区域编码                                                       | query | false | string             |        |
| parents            | 父级code集合 ,分隔                                                   | query | false | string             |        |
| regionArea         | 区域面积(KM2)                                                      | query | false | number(bigdecimal) |        |
| regionCode         | 行政区域编码                                                         | query | false | string             |        |
| regionLeader       | 区域负责人                                                          | query | false | string             |        |
| regionLevel        | 区域级别                                                           | query | false | integer(int32)     |        |
| regionName         | 行政区域名称                                                         | query | false | string             |        |
| regionPeopleNumber | 区域人口                                                           | query | false | integer(int64)     |        |
| regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | query | false | string             |        |
| remark             | 备注                                                             | query | false | string             |        |
| size               | 页大小,默认为10                                                      | query | false | integer(int32)     |        |
| start              | 开始时间                                                           | query | false | string(date-time)  |        |

**响应状态**:

| 状态码 | 说明 | schema              |
|-----|----|---------------------| 
| 200 | OK | 分页结果对象«SysRegionVO» |

**响应参数**:

| 参数名称                           | 参数说明                                                           | 类型                 | schema         |
|--------------------------------|----------------------------------------------------------------|--------------------|----------------| 
| current                        | 当前页码                                                           | integer(int32)     | integer(int32) |
| pages                          | 总页数                                                            | integer(int32)     | integer(int32) |
| records                        | 数据列表                                                           | array              | SysRegionVO    |
| &emsp;&emsp;boundBottom        | 行政区下边界纬度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;boundLeft          | 行政区左边界经度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;boundRight         | 行政区右边界经度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;boundTop           | 行政区上边界纬度                                                       | number(bigdecimal) |                |
| &emsp;&emsp;children           | 子区域信息                                                          | array              | SysRegionVO    |
| &emsp;&emsp;createBy           | 创建人                                                            | string             |                |
| &emsp;&emsp;createTime         | 创建时间                                                           | string(date-time)  |                |
| &emsp;&emsp;extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | integer(int64)     |                |
| &emsp;&emsp;gisOid             | gis标绘信息，大于0整数代表已标绘                                             | integer(int64)     |                |
| &emsp;&emsp;id                 | 自增ID                                                           | string             |                |
| &emsp;&emsp;latitude           | 纬度                                                             | number(bigdecimal) |                |
| &emsp;&emsp;longitude          | 经度                                                             | number(bigdecimal) |                |
| &emsp;&emsp;parentCode         | 上级行政区域编码                                                       | string             |                |
| &emsp;&emsp;parents            | 父级code集合 ,分隔                                                   | string             |                |
| &emsp;&emsp;regionArea         | 区域面积(KM2)                                                      | number(bigdecimal) |                |
| &emsp;&emsp;regionCode         | 行政区域编码                                                         | string             |                |
| &emsp;&emsp;regionLeader       | 区域负责人                                                          | string             |                |
| &emsp;&emsp;regionLevel        | 区域级别                                                           | integer(int32)     |                |
| &emsp;&emsp;regionName         | 行政区域名称                                                         | string             |                |
| &emsp;&emsp;regionPeopleNumber | 区域人口                                                           | integer(int64)     |                |
| &emsp;&emsp;regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | string             |                |
| &emsp;&emsp;remark             | 备注                                                             | string             |                |
| &emsp;&emsp;subNum             | 子级数目                                                           | integer(int64)     |                |
| &emsp;&emsp;updateBy           | 更新人                                                            | string             |                |
| &emsp;&emsp;updateTime         | 更新时间                                                           | string(date-time)  |                |
| size                           | 页大小                                                            | integer(int32)     | integer(int32) |
| total                          | 总行数                                                            | integer(int32)     | integer(int32) |

**响应示例**:

```javascript
{
	"current": 0,
	"pages": 0,
	"records": [
		{
			"boundBottom": 0,
			"boundLeft": 0,
			"boundRight": 0,
			"boundTop": 0,
			"children": [
				{
					"boundBottom": 0,
					"boundLeft": 0,
					"boundRight": 0,
					"boundTop": 0,
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"extend": 0,
					"gisOid": 0,
					"id": "",
					"latitude": 0,
					"longitude": 0,
					"parentCode": "",
					"parents": "",
					"regionArea": 0,
					"regionCode": "",
					"regionLeader": "",
					"regionLevel": 0,
					"regionName": "",
					"regionPeopleNumber": 0,
					"regionType": "",
					"remark": "",
					"subNum": 0,
					"updateBy": "",
					"updateTime": ""
				}
			],
			"createBy": "",
			"createTime": "",
			"extend": 0,
			"gisOid": 0,
			"id": "",
			"latitude": 0,
			"longitude": 0,
			"parentCode": "",
			"parents": "",
			"regionArea": 0,
			"regionCode": "",
			"regionLeader": "",
			"regionLevel": 0,
			"regionName": "",
			"regionPeopleNumber": 0,
			"regionType": "",
			"remark": "",
			"subNum": 0,
			"updateBy": "",
			"updateTime": ""
		}
	],
	"size": 0,
	"total": 0
}
```

## 新增行政区域划分

**接口地址**:`/bbt-api/sys/region`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "boundBottom": 0,
  "boundLeft": 0,
  "boundRight": 0,
  "boundTop": 0,
  "extend": 0,
  "gisOid": 0,
  "latitude": 0,
  "longitude": 0,
  "parentCode": "",
  "parents": "",
  "regionArea": 0,
  "regionCode": "",
  "regionLeader": "",
  "regionLevel": 0,
  "regionName": "",
  "regionPeopleNumber": 0,
  "regionType": "",
  "remark": ""
}
```

**请求参数**:

| 参数名称                           | 参数说明                                                           | 请求类型 | 是否必须  | 数据类型               | schema     |
|--------------------------------|----------------------------------------------------------------|------|-------|--------------------|------------|
| 行政区域划分新增参数                     | 行政区域划分新增参数                                                     | body | true  | 行政区域划分新增参数         | 行政区域划分新增参数 |
| &emsp;&emsp;boundBottom        | 行政区下边界纬度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;boundLeft          | 行政区左边界经度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;boundRight         | 行政区右边界经度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;boundTop           | 行政区上边界纬度                                                       |      | false | number(bigdecimal) |            |
| &emsp;&emsp;extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） |      | false | integer(int64)     |            |
| &emsp;&emsp;gisOid             | gis标绘信息，大于0整数代表已标绘                                             |      | false | integer(int64)     |            |
| &emsp;&emsp;latitude           | 纬度                                                             |      | false | number(bigdecimal) |            |
| &emsp;&emsp;longitude          | 经度                                                             |      | false | number(bigdecimal) |            |
| &emsp;&emsp;parentCode         | 上级行政区域编码                                                       |      | false | string             |            |
| &emsp;&emsp;parents            | 父级code集合 ,分隔                                                   |      | false | string             |            |
| &emsp;&emsp;regionArea         | 区域面积(KM2)                                                      |      | false | number(bigdecimal) |            |
| &emsp;&emsp;regionCode         | 行政区域编码                                                         |      | false | string             |            |
| &emsp;&emsp;regionLeader       | 区域负责人                                                          |      | false | string             |            |
| &emsp;&emsp;regionLevel        | 区域级别                                                           |      | false | integer(int32)     |            |
| &emsp;&emsp;regionName         | 行政区域名称                                                         |      | false | string             |            |
| &emsp;&emsp;regionPeopleNumber | 区域人口                                                           |      | false | integer(int64)     |            |
| &emsp;&emsp;regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   |      | false | string             |            |
| &emsp;&emsp;remark             | 备注                                                             |      | false | string             |            |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 行政区域划分详情

**接口地址**:`/bbt-api/sys/region/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema      |
|-----|----|-------------| 
| 200 | OK | SysRegionVO |

**响应参数**:

| 参数名称                           | 参数说明                                                           | 类型                 | schema             |
|--------------------------------|----------------------------------------------------------------|--------------------|--------------------| 
| boundBottom                    | 行政区下边界纬度                                                       | number(bigdecimal) | number(bigdecimal) |
| boundLeft                      | 行政区左边界经度                                                       | number(bigdecimal) | number(bigdecimal) |
| boundRight                     | 行政区右边界经度                                                       | number(bigdecimal) | number(bigdecimal) |
| boundTop                       | 行政区上边界纬度                                                       | number(bigdecimal) | number(bigdecimal) |
| children                       | 子区域信息                                                          | array              | SysRegionVO        |
| &emsp;&emsp;boundBottom        | 行政区下边界纬度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;boundLeft          | 行政区左边界经度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;boundRight         | 行政区右边界经度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;boundTop           | 行政区上边界纬度                                                       | number(bigdecimal) |                    |
| &emsp;&emsp;children           | 子区域信息                                                          | array              | SysRegionVO        |
| &emsp;&emsp;createBy           | 创建人                                                            | string             |                    |
| &emsp;&emsp;createTime         | 创建时间                                                           | string(date-time)  |                    |
| &emsp;&emsp;extend             | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | integer(int64)     |                    |
| &emsp;&emsp;gisOid             | gis标绘信息，大于0整数代表已标绘                                             | integer(int64)     |                    |
| &emsp;&emsp;id                 | 自增ID                                                           | string             |                    |
| &emsp;&emsp;latitude           | 纬度                                                             | number(bigdecimal) |                    |
| &emsp;&emsp;longitude          | 经度                                                             | number(bigdecimal) |                    |
| &emsp;&emsp;parentCode         | 上级行政区域编码                                                       | string             |                    |
| &emsp;&emsp;parents            | 父级code集合 ,分隔                                                   | string             |                    |
| &emsp;&emsp;regionArea         | 区域面积(KM2)                                                      | number(bigdecimal) |                    |
| &emsp;&emsp;regionCode         | 行政区域编码                                                         | string             |                    |
| &emsp;&emsp;regionLeader       | 区域负责人                                                          | string             |                    |
| &emsp;&emsp;regionLevel        | 区域级别                                                           | integer(int32)     |                    |
| &emsp;&emsp;regionName         | 行政区域名称                                                         | string             |                    |
| &emsp;&emsp;regionPeopleNumber | 区域人口                                                           | integer(int64)     |                    |
| &emsp;&emsp;regionType         | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | string             |                    |
| &emsp;&emsp;remark             | 备注                                                             | string             |                    |
| &emsp;&emsp;subNum             | 子级数目                                                           | integer(int64)     |                    |
| &emsp;&emsp;updateBy           | 更新人                                                            | string             |                    |
| &emsp;&emsp;updateTime         | 更新时间                                                           | string(date-time)  |                    |
| createBy                       | 创建人                                                            | string             |                    |
| createTime                     | 创建时间                                                           | string(date-time)  | string(date-time)  |
| extend                         | 扩展字段值（网格：1、综合网格；2、专属网格；3、镇街网格；4村社区网格；5、基础网格）（社区：1、村社区；2、镇街社区；） | integer(int64)     | integer(int64)     |
| gisOid                         | gis标绘信息，大于0整数代表已标绘                                             | integer(int64)     | integer(int64)     |
| id                             | 自增ID                                                           | string             |                    |
| latitude                       | 纬度                                                             | number(bigdecimal) | number(bigdecimal) |
| longitude                      | 经度                                                             | number(bigdecimal) | number(bigdecimal) |
| parentCode                     | 上级行政区域编码                                                       | string             |                    |
| parents                        | 父级code集合 ,分隔                                                   | string             |                    |
| regionArea                     | 区域面积(KM2)                                                      | number(bigdecimal) | number(bigdecimal) |
| regionCode                     | 行政区域编码                                                         | string             |                    |
| regionLeader                   | 区域负责人                                                          | string             |                    |
| regionLevel                    | 区域级别                                                           | integer(int32)     | integer(int32)     |
| regionName                     | 行政区域名称                                                         | string             |                    |
| regionPeopleNumber             | 区域人口                                                           | integer(int64)     | integer(int64)     |
| regionType                     | 区域类型(0：村下级，1：社区/村，2：街道/乡镇，3：区/县，4：市，5：省/直辖市)                   | string             |                    |
| remark                         | 备注                                                             | string             |                    |
| subNum                         | 子级数目                                                           | integer(int64)     | integer(int64)     |
| updateBy                       | 更新人                                                            | string             |                    |
| updateTime                     | 更新时间                                                           | string(date-time)  | string(date-time)  |

**响应示例**:

```javascript
{
	"boundBottom": 0,
	"boundLeft": 0,
	"boundRight": 0,
	"boundTop": 0,
	"children": [
		{
			"boundBottom": 0,
			"boundLeft": 0,
			"boundRight": 0,
			"boundTop": 0,
			"children": [
				{
					"boundBottom": 0,
					"boundLeft": 0,
					"boundRight": 0,
					"boundTop": 0,
					"children": [
						{}
					],
					"createBy": "",
					"createTime": "",
					"extend": 0,
					"gisOid": 0,
					"id": "",
					"latitude": 0,
					"longitude": 0,
					"parentCode": "",
					"parents": "",
					"regionArea": 0,
					"regionCode": "",
					"regionLeader": "",
					"regionLevel": 0,
					"regionName": "",
					"regionPeopleNumber": 0,
					"regionType": "",
					"remark": "",
					"subNum": 0,
					"updateBy": "",
					"updateTime": ""
				}
			],
			"createBy": "",
			"createTime": "",
			"extend": 0,
			"gisOid": 0,
			"id": "",
			"latitude": 0,
			"longitude": 0,
			"parentCode": "",
			"parents": "",
			"regionArea": 0,
			"regionCode": "",
			"regionLeader": "",
			"regionLevel": 0,
			"regionName": "",
			"regionPeopleNumber": 0,
			"regionType": "",
			"remark": "",
			"subNum": 0,
			"updateBy": "",
			"updateTime": ""
		}
	],
	"createBy": "",
	"createTime": "",
	"extend": 0,
	"gisOid": 0,
	"id": "",
	"latitude": 0,
	"longitude": 0,
	"parentCode": "",
	"parents": "",
	"regionArea": 0,
	"regionCode": "",
	"regionLeader": "",
	"regionLevel": 0,
	"regionName": "",
	"regionPeopleNumber": 0,
	"regionType": "",
	"remark": "",
	"subNum": 0,
	"updateBy": "",
	"updateTime": ""
}
```

## 初始化区域缓存

**接口地址**:`/bbt-api/sys/region/initCache`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 导出数据

**接口地址**:`/bbt-api/sys/region/export`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**开发者**:Zhujun

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

# 资源分类

## 新增资源分类

**接口地址**:`/bbt-api/resource/category`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "name": "",
  "parentId": 0
}
```

**请求参数**:

| 参数名称                 | 参数说明     | 请求类型 | 是否必须  | 数据类型           | schema   |
|----------------------|----------|------|-------|----------------|----------|
| 资源分类新增参数             | 资源分类新增参数 | body | true  | 资源分类新增参数       | 资源分类新增参数 |
| &emsp;&emsp;name     | 类型名称     |      | false | string         |          |
| &emsp;&emsp;parentId | 父级ID     |      | false | integer(int64) |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 资源分类下拉选项

**接口地址**:`/bbt-api/resource/category/tree/select`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                      |
|-----|----|-----------------------------| 
| 200 | OK | ResourceCategoryRecursionVO |

**响应参数**:

| 参数名称                     | 参数说明   | 类型                | schema                      |
|--------------------------|--------|-------------------|-----------------------------| 
| children                 | 子类型    | array             | ResourceCategoryRecursionVO |
| &emsp;&emsp;children     | 子类型    | array             | ResourceCategoryRecursionVO |
| &emsp;&emsp;createTime   | 创建时间   | string(date-time) |                             |
| &emsp;&emsp;haveChildren | 是否有子类型 | boolean           |                             |
| &emsp;&emsp;id           | 主键     | string            |                             |
| &emsp;&emsp;name         | 类型名称   | string            |                             |
| &emsp;&emsp;parentId     | 父级ID   | integer(int64)    |                             |
| &emsp;&emsp;updateTime   | 修改时间   | string(date-time) |                             |
| createTime               | 创建时间   | string(date-time) | string(date-time)           |
| haveChildren             | 是否有子类型 | boolean           |                             |
| id                       | 主键     | string            |                             |
| name                     | 类型名称   | string            |                             |
| parentId                 | 父级ID   | integer(int64)    | integer(int64)              |
| updateTime               | 修改时间   | string(date-time) | string(date-time)           |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"haveChildren": false,
				"id": "",
				"name": "",
				"parentId": 0,
				"updateTime": ""
			}
		],
		"createTime": "",
		"haveChildren": false,
		"id": "",
		"name": "",
		"parentId": 0,
		"updateTime": ""
	}
]
```

## 删除资源分类

**接口地址**:`/bbt-api/resource/category/delete`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "ids": [
    {}
  ]
}
```

**请求参数**:

| 参数名称                  | 参数说明                  | 请求类型 | 是否必须  | 数据类型                  | schema                |
|-----------------------|-----------------------|------|-------|-----------------------|-----------------------|
| set集合参数«Serializable» | Set集合参数«Serializable» | body | true  | Set集合参数«Serializable» | Set集合参数«Serializable» |
| &emsp;&emsp;ids       |                       |      | false | array                 | Serializable          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 资源分类列表

**接口地址**:`/bbt-api/resource/category/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称     | 参数说明     | 请求类型  | 是否必须  | 数据类型   | schema |
|----------|----------|-------|-------|--------|--------|
| parentId | parentId | query | false | string |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | ResourceCategoryVO |

**响应参数**:

| 参数名称         | 参数说明   | 类型                | schema            |
|--------------|--------|-------------------|-------------------| 
| createTime   | 创建时间   | string(date-time) | string(date-time) |
| haveChildren | 是否有子类型 | boolean           |                   |
| id           | 主键     | string            |                   |
| name         | 类型名称   | string            |                   |
| parentId     | 父级ID   | integer(int64)    | integer(int64)    |
| updateTime   | 修改时间   | string(date-time) | string(date-time) |

**响应示例**:

```javascript
[
	{
		"createTime": "",
		"haveChildren": false,
		"id": "",
		"name": "",
		"parentId": 0,
		"updateTime": ""
	}
]
```

## 更新资源分类

**接口地址**:`/bbt-api/resource/category/update`

**请求方式**:`POST`

**请求数据类型**:`application/x-www-form-urlencoded,application/json`

**响应数据类型**:`*/*`

**接口描述**:

**请求示例**:

```javascript
{
  "id": "",
  "name": "",
  "parentId": 0
}
```

**请求参数**:

| 参数名称                 | 参数说明     | 请求类型 | 是否必须  | 数据类型           | schema   |
|----------------------|----------|------|-------|----------------|----------|
| 资源分类修改参数             | 资源分类修改参数 | body | true  | 资源分类修改参数       | 资源分类修改参数 |
| &emsp;&emsp;id       | 主键       |      | false | string         |          |
| &emsp;&emsp;name     | 类型名称     |      | false | string         |          |
| &emsp;&emsp;parentId | 父级ID     |      | false | integer(int64) |          |

**响应状态**:

| 状态码 | 说明 | schema |
|-----|----|--------| 
| 200 | OK |        |

**响应参数**:

暂无

**响应示例**:

```javascript

```

## 资源分类树形列表

**接口地址**:`/bbt-api/resource/category/tree/list`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

暂无

**响应状态**:

| 状态码 | 说明 | schema                      |
|-----|----|-----------------------------| 
| 200 | OK | ResourceCategoryRecursionVO |

**响应参数**:

| 参数名称                     | 参数说明   | 类型                | schema                      |
|--------------------------|--------|-------------------|-----------------------------| 
| children                 | 子类型    | array             | ResourceCategoryRecursionVO |
| &emsp;&emsp;children     | 子类型    | array             | ResourceCategoryRecursionVO |
| &emsp;&emsp;createTime   | 创建时间   | string(date-time) |                             |
| &emsp;&emsp;haveChildren | 是否有子类型 | boolean           |                             |
| &emsp;&emsp;id           | 主键     | string            |                             |
| &emsp;&emsp;name         | 类型名称   | string            |                             |
| &emsp;&emsp;parentId     | 父级ID   | integer(int64)    |                             |
| &emsp;&emsp;updateTime   | 修改时间   | string(date-time) |                             |
| createTime               | 创建时间   | string(date-time) | string(date-time)           |
| haveChildren             | 是否有子类型 | boolean           |                             |
| id                       | 主键     | string            |                             |
| name                     | 类型名称   | string            |                             |
| parentId                 | 父级ID   | integer(int64)    | integer(int64)              |
| updateTime               | 修改时间   | string(date-time) | string(date-time)           |

**响应示例**:

```javascript
[
	{
		"children": [
			{
				"children": [],
				"createTime": "",
				"haveChildren": false,
				"id": "",
				"name": "",
				"parentId": 0,
				"updateTime": ""
			}
		],
		"createTime": "",
		"haveChildren": false,
		"id": "",
		"name": "",
		"parentId": 0,
		"updateTime": ""
	}
]
```

## 资源分类详情

**接口地址**:`/bbt-api/resource/category/{id}`

**请求方式**:`GET`

**请求数据类型**:`application/x-www-form-urlencoded`

**响应数据类型**:`*/*`

**接口描述**:

**请求参数**:

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型   | schema |
|------|------|------|------|--------|--------|
| id   | id   | path | true | string |        |

**响应状态**:

| 状态码 | 说明 | schema             |
|-----|----|--------------------| 
| 200 | OK | ResourceCategoryVO |

**响应参数**:

| 参数名称         | 参数说明   | 类型                | schema            |
|--------------|--------|-------------------|-------------------| 
| createTime   | 创建时间   | string(date-time) | string(date-time) |
| haveChildren | 是否有子类型 | boolean           |                   |
| id           | 主键     | string            |                   |
| name         | 类型名称   | string            |                   |
| parentId     | 父级ID   | integer(int64)    | integer(int64)    |
| updateTime   | 修改时间   | string(date-time) | string(date-time) |

**响应示例**:

```javascript
{
	"createTime": "",
	"haveChildren": false,
	"id": "",
	"name": "",
	"parentId": 0,
	"updateTime": ""
}
```