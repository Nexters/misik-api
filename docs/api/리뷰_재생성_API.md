# 리뷰 재생성

리뷰를 재생성합니다.

## Request

### HTTP METHOD : `POST`

### url : `https://api.misik.me/reviews/{id}/re-create`
### Http Headers
- device-id: 식별할 수 있는 값   
  `미래에도 변하지 않아야함 앱을 삭제했다 다시 깔아도 안변하는 값으로 줄 수 있는지`

### Response

#### `Response Status 200 OK`

```json
{
  "id": "123456789",
  "review": "가득합니다....."
}
```
