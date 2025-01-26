# 리뷰 생성

리뷰를 생성합니다.

## Request

### HTTP METHOD : `GET`

### url : `https://api.misik.me/reviews/{id}`
### Http Headers
- device-id: 식별할 수 있는 값   
  `미래에도 변하지 않아야함 앱을 삭제했다 다시 깔아도 안변하는 값으로 줄 수 있는지`

### Response

#### `Response Status 200 OK`

```json
{
  "isSuccess": true, // 리뷰가 생성되지 않았으면 false, 전체가 성공되면 true
  "id": "123456789",
  "review": "카야토스는 숨겨져 있는 카야잼과 버터가 확실히 가득합니다. 또한, 가게의 분위기는 아늑하고 편안하고 바깥쪽에 있고 사랑하는 시간을 보낼 수 있는 공간입니다. 무엇보다 가격에 비해 음식의 품질이 정말 훌륭해서, 마음에 들었습니다."
}
```
